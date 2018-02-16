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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class ShowInfoActivity extends AppCompatActivity {

    public String gender;
    public String name;
    public String location;
    public String user;
    public String password;
    public String fecha;
    public String picture;

    private Context context;
    private ImageButton deleteUserButton;
    private ImageButton editButton;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        deleteUserButton = findViewById(R.id.deleteUserButton);
        editButton = findViewById(R.id.editUserButton);
        this.context=this;

        ImageView imageView= findViewById(R.id.userImage);
        TextView userName = findViewById(R.id.userName);
        TextView userGender = findViewById(R.id.userGender);
        TextView userRegisterDate = findViewById(R.id.userRegisterDate);
        TextView userLocation = findViewById(R.id.userLocation);
        TextView userId = findViewById(R.id.userId);
        TextView userPassword = findViewById(R.id.userPassword);

        Bundle extras = getIntent().getExtras();
        final int pos= (int) extras.get("Posicion");


        picture = (String) extras.get("UserImage");
        name = (String) extras.get("UserName");
        gender = (String) extras.get("UserGender");
        fecha = (String) extras.get("UserRegisterDate");
        location = (String) extras.get("UserLocation");
        user = (String) extras.get("UserId");
        password = (String) extras.get("UserPassword");

        final User newUser = new User(gender,name,location,user,password,fecha,picture);

        new Adapter.DownloadImageFromInternet(imageView).execute(picture);
        userName.setText("  "+name);
        userGender.setText("  "+gender);
        userRegisterDate.setText("  "+fecha);
        userLocation.setText("  "+location);
        userId.setText("  "+user);
        userPassword.setText("  "+password);


        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            AppDataBase mDb;
                            mDb = Room.databaseBuilder(context, AppDataBase.class, "Sample.db")
                                    .addMigrations(MIGRATION_1_2)
                                    .build();
                            UserDAO mUserDao = mDb.userDao();

                            mUserDao.delete(newUser);

                            Intent intent = intent = new Intent(context, ListUsers.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage(), e);

                        }
                    }
                });
            }
        });



        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Intent intent = intent = new Intent(context, EditUserActivity.class);
                            intent.putExtra("Posicion", pos);
                            startActivity(intent);

                        } catch (Exception e) {
                            Log.e("ERROR", e.getMessage(), e);

                        }
                    }
                });
            }
        });




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


