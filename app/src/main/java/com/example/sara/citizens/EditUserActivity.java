package com.example.sara.citizens;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class EditUserActivity extends AppCompatActivity {


    public String gender;
    public String name;
    public String location;
    public String user;
    public String password;
    public String fecha;
    public String picture;
    private Context context;

    String[] genderArray = {"Male", "Female"};

    private ImageButton saveButton;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.context=this;

        final EditText nameText = findViewById(R.id.userName);
        final Spinner genderText = findViewById(R.id.userGender);
        final EditText userText = findViewById(R.id.userId);
        final EditText locationText = findViewById(R.id.userLocation);
        final EditText passwordText = findViewById(R.id.userPassword);
        final ImageView image = findViewById(R.id.userImage);

        saveButton = findViewById(R.id.saveNewUserButton);

        final User[] userObj = new User[1];

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    AppDataBase mDb;
                    mDb = Room.databaseBuilder(context, AppDataBase.class, "Sample.db")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                    UserDAO mUserDao = mDb.userDao();

                    List<User> list = mUserDao.getAll();

                    userObj[0] = list.get((Integer) getIntent().getExtras().get("Posicion"));

                    picture = userObj[0].getPicture();
                    name = userObj[0].getName();
                    gender = userObj[0].getGender();
                    fecha = userObj[0].getFecha();
                    location = userObj[0].getLocation();
                    user = userObj[0].getUsername();
                    password = userObj[0].getPassword();

                    new DownloadImageFromInternet(image).execute(picture);
                    nameText.setHint(" "+ name);
                    userText.setHint(" "+ user);
                    locationText.setHint(" "+ location);
                    passwordText.setHint(" "+ password);

                    genderText.setAdapter(new MyCustomAdapter(EditUserActivity.this, R.layout.row, genderArray));

                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage(), e);

                }
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void run() {
                        try {

                            AppDataBase mDb;
                            mDb = Room.databaseBuilder(context, AppDataBase.class, "Sample.db")
                                    .addMigrations(MIGRATION_1_2)
                                    .build();
                            UserDAO mUserDao = mDb.userDao();

                            mUserDao.delete(userObj[0]);

                            String newName = String.valueOf(nameText.getText());
                            String newGender = genderText.getSelectedItem().toString();
                            String newUser = String.valueOf(userText.getText());
                            String newLocation = String.valueOf(locationText.getText());
                            String newPassword = String.valueOf(passwordText.getText());

                            if(!newName.equals("")){
                                userObj[0].setName(newName);
                            }
                            if(!newGender.equals("")){
                                userObj[0].setGender(newGender);
                            }
                            if(!newUser.equals("")){
                                userObj[0].setUsername(newUser);
                            }
                            if(!newLocation.equals("")){
                                userObj[0].setLocation(newLocation);
                            }
                            if(!newPassword.equals("")){
                                userObj[0].setPassword(newPassword);
                            }

                            mUserDao.insertAll(userObj[0]);

                            Intent intent = new Intent(context, ListUsers.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage(), e);

                        }
                    }
                });
            }
        });

    }


    public class MyCustomAdapter extends ArrayAdapter<String> {

        public MyCustomAdapter(Context context, int textViewResourceId,
                               String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.row, parent, false);
            TextView label=(TextView)row.findViewById(R.id.item);
            label.setText(genderArray[position]);

            return row;
        }
    }



    static class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
