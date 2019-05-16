package com.example.project1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText e1, e2;
    Button b1,b2;
    TextView t1;
    private TextView mTextMessage;
//    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        Intent i1 = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i1);
                        break;
                }
                return true;
            }
        });
        //finish

        db = new DatabaseHelper(this);
        e1 = (EditText)findViewById(R.id.login_email);
        e2 = (EditText)findViewById(R.id.login_password);
        b1 = (Button)findViewById(R.id.login_button);
        b2 = (Button)findViewById(R.id.registerButton);
        t1 = (TextView)findViewById(R.id.textLogin);
        if(User.getInstance().getUserType().equals("Patient")){
            t1.setText("Patient Login");
        }
        else{
            t1.setText("Caregiver Login");
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                String password = e2.getText().toString();
                Boolean checkEmail = db.verifyAccount(email,password);
                Boolean checkEmail2 = db.verifyAccount2(email,password);
                if (User.getInstance().getUserType().equals("Patient")) {
                    if (checkEmail) {
                        User.getInstance().setEmail(email);
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(LoginActivity.this, emotionActivity.class);
                        startActivity(myIntent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (User.getInstance().getUserType().equals("Caregiver")) {
                    if (checkEmail2) {
                        User.getInstance().setEmail(email);
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(LoginActivity.this, emotionActivity.class);
                        startActivity(myIntent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                    }
                }
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );

            }


        });


        mTextMessage = (TextView) findViewById(R.id.message);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.getInstance().getUserType().equals("Patient")) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                else if (User.getInstance().getUserType().equals("Caregiver")) {
                    Intent intent = new Intent(LoginActivity.this, Register2Activity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
