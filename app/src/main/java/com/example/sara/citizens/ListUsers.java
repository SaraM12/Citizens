package com.example.sara.citizens;

import android.app.Activity;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListUsers extends AppCompatActivity {

    private ListView lista;
    private ArrayList<User> usersList;
    private Context context;
    private ImageButton imageButton;



    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);


        imageButton = (ImageButton) findViewById(R.id.elemLocation);
        this.context=this;


        String orientation;
        Activity activity = ListUsers.this;
        int value = activity.getResources().getConfiguration().orientation;

        if (value == Configuration.ORIENTATION_PORTRAIT) {

            orientation = "Portrait";
            Log.d("Orientation", orientation);
        }

        if (value == Configuration.ORIENTATION_LANDSCAPE) {

            orientation = "Landscape";
            Log.d("Orientation", orientation);
        }

        //pb = (ProgressBar) findViewById(R.id.progressBar);
        lista = findViewById(R.id.listView);

        usersList = new ArrayList<User>();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    AppDataBase mDb;
                    mDb = Room.databaseBuilder(context, AppDataBase.class, "Sample.db")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                    UserDAO mUserDao = mDb.userDao();

                    //LA LISTA ESTÁ VACÍA
                    List<User> listAux= mUserDao.getAll();
                    User userAux;
                    String aux = Boolean.toString(listAux.isEmpty()) ;

                    int i = 0;
                    for(i=0; i<listAux.size(); i++){
                        userAux= listAux.get(i);
                        usersList.add(userAux);
                    }

                }

                catch(Exception e) {
                    Log.e("ERROR", e.getMessage(), e);

                }
            }
        });


        if (value == Configuration.ORIENTATION_PORTRAIT) {
            Adapter myAdapter = new Adapter(getApplicationContext(),usersList);
            lista.setAdapter(myAdapter);
        } else{

            AdapterLandscape myAdapter2 = new AdapterLandscape(getApplicationContext(),usersList);
            lista.setAdapter(myAdapter2);

        }



        /*imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("HOLA","HOla");
            }

        });*/


        /*imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"HAS PULSADO EL IMAGEBUTTON", Toast.LENGTH_LONG);
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q=%s”,location");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri)); context.startActivity(intent);
            }
        });*/



    }

}

