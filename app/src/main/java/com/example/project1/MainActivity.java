package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    LinearLayout l1,l2,l3;
    private TextView mTextMessage;
    SessionManager sessionManager;
//    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(this);
        boolean login = sessionManager.isLogin();
        Log.e("TAG", "is login"+ login );
        if(login){
            HashMap<String,String> user = sessionManager.getUserDetail();
            String mName = user.get(sessionManager.NAME);
            String mEmail = user.get(sessionManager.EMAIL);
            String mType = user.get(sessionManager.TYPE);
            Log.e("TAG", "shared preference name is "+mName );
            User.getInstance().setEmail(mEmail);
            User.getInstance().setUserName(mName);
            User.getInstance().setUserType(mType);
            Intent i = new Intent(MainActivity.this, EmotionAssessmentActivity.class);
            i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            if(mType.equals("Admin")||mType.equals("Specialist")){
                Intent intent = new Intent(MainActivity.this,SpecialistForumActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
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
                Intent intent3 = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent3);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_admin_login){
            User.getInstance().setUserType("Admin");
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
