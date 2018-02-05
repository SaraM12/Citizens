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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListUsers extends AppCompatActivity {

    ListView lista;
    ArrayList<User> usersList;
    Context context;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
// Since we didn't alter the table, there's nothing else to do here.
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);


        //COMPROBAR ORIENTACIÓN
        String orientation;
        Activity activity = ListUsers.this;
        int value = activity.getResources().getConfiguration().orientation;

        if (value == Configuration.ORIENTATION_PORTRAIT) {

            orientation = "Portrait";
        }

        if (value == Configuration.ORIENTATION_LANDSCAPE) {

            orientation = "Landscape";
        }

        this.context=this;


        lista = (ListView) findViewById(R.id.listView);
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

                    Log.i("USUARIO",aux);
                    int i = 0;
                    for(i=0; i<listAux.size(); i++){
                        Log.d("HOLA","aquí");
                        userAux= listAux.get(i);
                        usersList.add(userAux);
                        //Log.d("USUARIO", userAux.toString());
                    }

                }

                catch(Exception e) {
                    Log.e("ERROR", e.getMessage(), e);

                }
            }
        });


        Adapter myAdapter = new Adapter(getApplicationContext(),usersList);
        lista.setAdapter(myAdapter);

    }
}

