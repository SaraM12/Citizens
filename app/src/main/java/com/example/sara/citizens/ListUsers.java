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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListUsers extends AppCompatActivity {

    private ListView lista;
    private ArrayList<User> usersList;
    private Context context;
    private ImageButton imageButton;
    private TextView textView;



    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);


        imageButton = (ImageButton) findViewById(R.id.elemLocation);
        textView = findViewById(R.id.userListInfo);
        this.context=this;

        Activity activity = ListUsers.this;
        int value = activity.getResources().getConfiguration().orientation;


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

                    List<User> listAux = mUserDao.getAll();

                    if(listAux.isEmpty()){

                        textView.post(new Runnable() {
                            @Override
                            public void run() {

                                textView.setVisibility(View.VISIBLE);
                            }
                        });


                    }else{
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setVisibility(View.GONE);
                            }
                        });

                    }

                    User userAux;

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

    }

}