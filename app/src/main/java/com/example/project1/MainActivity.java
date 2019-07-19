package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    LinearLayout l1,l2,l3;
    private TextView mTextMessage;
    SessionManager sessionManager;
//    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        boolean login = sessionManager.isLogin();
        Log.e("TAG", "is login"+ login );
        if(login){
            Intent myIntent = new Intent(MainActivity.this, emotionActivity.class);
            startActivity(myIntent);
        }

        l1 = (LinearLayout) findViewById(R.id.userPatient);
        l2 = (LinearLayout) findViewById(R.id.userCaregiver);
        l3 = (LinearLayout) findViewById(R.id.userSpecialist);
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getInstance().setUserType("Patient");
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getInstance().setUserType("Caregiver");
                Intent intent2 = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent2);
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getInstance().setUserType("Specialist");
                Intent intent3 = new Intent(MainActivity.this,AndroidDatabaseManager.class);
                startActivity(intent3);
            }
        });

    }

}
