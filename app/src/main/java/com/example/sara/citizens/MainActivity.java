package com.example.sara.citizens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Intent intent1;
    private Intent intent2;
    private Button btnCreate;
    private Button btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent1= new Intent (this, CreateUsers.class);

        intent2= new Intent (this, ListUsers.class);

        btnCreate = (Button) findViewById(R.id.insertUserButton);
        btnList = (Button) findViewById(R.id.listUsersButton);

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent2);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent1);
            }
        });

    }


}

