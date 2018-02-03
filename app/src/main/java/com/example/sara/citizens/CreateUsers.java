package com.example.sara.citizens;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.sara.citizens.R.id.dateShow;


public class CreateUsers extends AppCompatActivity {

    private Intent intent;
    private Button sendInfoButton;
    private Spinner citizenship;
    private Spinner genderSpinner;
    private int mYear,mMonth,mDay;
    private Context context;

    final String API_URL="https://randomuser.me/api/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=this;
        intent = new Intent(this, MainActivity.class);

        setContentView(R.layout.activity_create_users);

        sendInfoButton = (Button) findViewById(R.id.sendInfo_Button);

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        /*String country;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);*/
        //TODO AÑADIR INDICATIVOS DE CUAL ES EL PAÍS

        countries.add("AU");
        countries.add("BR");
        countries.add("CA");
        countries.add("CH");
        countries.add("DE");
        countries.add("DK");
        countries.add("ES");
        countries.add("FI");
        countries.add("FR");
        countries.add("GB");
        countries.add("IE");
        countries.add("IR");
        countries.add("NL");
        countries.add("NZ");
        countries.add("TR");
        countries.add("US");

        citizenship = (Spinner) findViewById(R.id.countrySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        citizenship.setAdapter(adapter);

        genderSpinner = (Spinner) findViewById(R.id.genderspinner);
        ArrayList<String> gender = new ArrayList<String>();
        gender.add("Male");
        gender.add("Female");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        genderSpinner.setAdapter(adapter1);

        final Button pickDate = (Button) findViewById(R.id.pick_date);
        final TextView textView = (TextView) findViewById(dateShow);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // myCalendar.add(Calendar.DATE, 0);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                textView.setText(sdf.format(myCalendar.getTime()));
                Log.d("HOLA", sdf.format(myCalendar.getTime()));

            }


        };

        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd;
                dpd = new DatePickerDialog(CreateUsers.this,new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                if (year < mYear)
                                    view.updateDate(mYear,mMonth,mDay);

                                if (monthOfYear < mMonth && year == mYear)
                                    view.updateDate(mYear,mMonth,mDay);

                                if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                                    view.updateDate(mYear,mMonth,mDay);

                                textView.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                //dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();

            }
        });

        sendInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final EditText num = (EditText) findViewById(R.id.numUsersEditText);
                final String numUsersText = num.getText().toString();
                final String countryValue = citizenship.getSelectedItem().toString();
                final String genderValue = genderSpinner.getSelectedItem().toString();
                final String registeredAux = textView.getText().toString();
                String aux[] = registeredAux.split("-");
                final String registered = aux[2] + "-" + aux[1] + "-" + aux[0];



                /*
                Toast.makeText(getApplicationContext(),message1, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),countryValue, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),genderValue, Toast.LENGTH_LONG).show();
                */





                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(API_URL + "?inc=name,registered,gender,location,picture,login&nat=" + countryValue.toLowerCase() + "&gender=" + genderValue.toLowerCase() + "&results=" + numUsersText + "&registered="+registered);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            try {
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                                StringBuilder stringBuilder = new StringBuilder();
                                String line;
                                while ((line = bufferedReader.readLine()) != null) {
                                    stringBuilder.append(line).append("\n");
                                }
                                bufferedReader.close();

                                Log.d("INFO", stringBuilder.toString());

                                //ArrayList<User> userList = new ArrayList<User>();
                                AppDataBase mDb = Room.inMemoryDatabaseBuilder(context, AppDataBase.class).build();
                                UserDAO mUserDao = mDb.userDao();
                                //List<User> userListAux = new ArrayList<User>();

                                JSONObject jsonObj = new JSONObject(stringBuilder.toString());

                                JSONArray results = jsonObj.getJSONArray("results");

                                for (int i = 0; i < results.length(); i++) {

                                    JSONObject c = results.getJSONObject(i);

                                    String gender = c.getString("gender");

                                    JSONObject nameObject = c.getJSONObject("name");
                                    String name = nameObject.getString("title") + " " + nameObject.getString("first") + " " + nameObject.getString("last");
                                    //String output = input.substring(0, 1).toUpperCase() + input.substring(1);

                                    JSONObject locationObject = c.getJSONObject("location");
                                    String location = locationObject.getString("street") + ", " + locationObject.getString("city") + ", " + locationObject.getString("state");

                                    JSONObject login = c.getJSONObject("login");
                                    String username = login.getString("username");
                                    String password = login.getString("password");

                                    String fecha = c.getString("registered");

                                    JSONObject pictureObject = c.getJSONObject("picture");
                                    String picture = pictureObject.getString("large");


                                    User auxUser = new User(gender,name,location,username,password,fecha,picture);

                                    //userList.add(auxUser);
                                    Log.i("INFO",auxUser.toString());


                                    mUserDao.insertAll(auxUser);

                                    //userListAux = mUserDao.getAll();


                                }

                                /*for(int i=0; i<userList.size(); i++){

                                    User user = userListAux.get(i);
                                    Log.d("INFOBASE", user.toString());

                                }*/

                            }

                            finally{
                                urlConnection.disconnect();
                            }
                        }
                        catch(Exception e) {
                            Log.e("ERROR", e.getMessage(), e);

                        }
                    }
                });

                startActivity(intent);

            }
        });


    }




}
