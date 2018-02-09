package com.example.sara.citizens;

import android.app.DatePickerDialog;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class CreateUsers extends AppCompatActivity{

    private Intent intent;
    private Button sendInfoButton;
    private Spinner citizenship;
    private Spinner genderSpinner;
    private int mYear,mMonth,mDay;
    private Context context;

    final String API_URL="https://randomuser.me/api/";

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
// Since we didn't alter the table, there's nothing else to do here.
        }
    };

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

        countries.add("Australia (AU)");
        countries.add("Brasil (BR)");
        countries.add("Canadá (CA)");
        countries.add("Suiza (CH)");
        countries.add("Alemania (DE)");
        countries.add("Dinamarca (DK)");
        countries.add("España (ES)");
        countries.add("Finlandia (FI)");
        countries.add("Francia (FR)");
        countries.add("Grna Bretaña (GB)");
        countries.add("Irlanda (IE)");
        countries.add("Irán (IR)");
        countries.add("Paises Bajos (NL)");
        countries.add("Nueva Zelanda (NZ)");
        countries.add("Turquía (TR)");
        countries.add("Estado Unidos (US)");

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
                //final String numUsersText = num.getText().toString();
                final String numUsersText = "0";
                final String countryValue = citizenship.getSelectedItem().toString();
                String countryAux[] = countryValue.split("\\(");
                String countryAux2[] = countryAux[1].split("\\)");
                final String nat = countryAux2[0];

                Log.i("Country", "Num " + nat);

                final String genderValue = genderSpinner.getSelectedItem().toString();
                final String registeredAux = textView.getText().toString();
                String aux2[] = new String[3];
                String rAux = null;

                Log.i("Hola", "Num " +numUsersText);


                if(registeredAux.equals("")){

                    rAux = "0001-01-01";
                    aux2[2] = "0001";
                    aux2[1] = "01";
                    aux2[0] = "01";


                } else {

                    Log.d("Hola","Hola");
                    aux2 = registeredAux.split("-");
                    rAux = aux2[2] + "-" + aux2[1] + "-" + aux2[0];

                }

                final String aux[] = aux2;
                final String registered = rAux;


                //TODO DA FALLO CUANDO NO SE INTRODUCE FECHA, HAY QUE ARREGLAR!!!!

                final int[] contador = {0};


                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(API_URL + "?inc=name,registered,gender,location,picture,login&nat=" + nat.toLowerCase() + "&gender=" + genderValue.toLowerCase() + "&results=" + numUsersText + "&registered="+registered);

                            Log.d("URL", url.toString());

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

                                AppDataBase mDb;
                                mDb = Room.databaseBuilder(context, AppDataBase.class, "Sample.db")
                                        .addMigrations(MIGRATION_1_2)
                                        .build();
                                //AppDataBase mDb = Room.inMemoryDatabaseBuilder(context, AppDataBase.class).build();
                                UserDAO mUserDao = mDb.userDao();
                                //List<User> userListAux = new ArrayList<User>();

                                JSONObject jsonObj = new JSONObject(stringBuilder.toString());

                                JSONArray results = jsonObj.getJSONArray("results");

                                for (int i = 0; i < results.length(); i++) {

                                    JSONObject c = results.getJSONObject(i);

                                    String gender = c.getString("gender");
                                    gender = gender.substring(0, 1).toUpperCase() + gender.substring(1);

                                    JSONObject nameObject = c.getJSONObject("name");
                                    String title = nameObject.getString("title");
                                    title = title.substring(0, 1).toUpperCase() + title.substring(1);
                                    String first = nameObject.getString("first");
                                    first = first.substring(0, 1).toUpperCase() + first.substring(1);
                                    String last = nameObject.getString("last");
                                    last = last.substring(0, 1).toUpperCase() + last.substring(1);
                                    String name = title + ". " + first + " " + last;


                                    JSONObject locationObject = c.getJSONObject("location");
                                    String location = locationObject.getString("street") + ", " + locationObject.getString("city") + ", " + locationObject.getString("state");

                                    JSONObject login = c.getJSONObject("login");
                                    String username = login.getString("username");
                                    String password = login.getString("password");

                                    String fecha = c.getString("registered");

                                    JSONObject pictureObject = c.getJSONObject("picture");
                                    String picture = pictureObject.getString("large");

                                    String a[] = fecha.split(" ");
                                    String b[] = a[0].split("-");

                                    boolean insert = false;

                                    if(Integer.parseInt(aux[2]) < Integer.parseInt(b[0])){
                                        insert = true;
                                    } else if(Integer.parseInt(aux[2]) == Integer.parseInt(b[0])){

                                        if(Integer.parseInt(aux[1]) < Integer.parseInt(b[1])){

                                            insert = true;

                                        } else if(Integer.parseInt(aux[1]) == Integer.parseInt(b[1])){
                                            if(Integer.parseInt(aux[0]) <= Integer.parseInt(b[2])){
                                                insert = true;
                                            }
                                        }
                                    }
                                    if(insert){

                                        User auxUser = new User(gender,name,location,username,password,fecha,picture);

                                        mUserDao.insertAll(auxUser);

                                        contador[0]++;

                                    } else{
                                        Log.e("Error", "No se pudo insertar usuario");
                                    }
                                }

                            }

                            finally{

                                urlConnection.disconnect();

                                Handler handler =  new Handler(context.getMainLooper());
                                handler.post( new Runnable(){
                                    public void run(){
                                        Log.d("Contador", Integer.toString(contador[0]));

                                        String string1 = getString(R.string.ToastInfo);
                                        String string2 = getString(R.string.ToastInfo2);
                                        Toast.makeText(context, string1 + " " +contador[0]+ " " +string2, Toast.LENGTH_LONG).show();
                                    }
                                });


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
