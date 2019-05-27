package com.example.project1;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {
private EditText o1,c1,c2;
private Button button;
private String t1, t2, t3,pw1;
private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_schedule_appointment);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(ChangePassword.this, emotionActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(ChangePassword.this, viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(ChangePassword.this, eventAssessment.class);
                        startActivity(i4);
                        break;
                    case R.id.navigation_faq:
                        Intent i5 = new Intent(ChangePassword.this, FAQ.class);
                        startActivity(i5);
                        break;
                }
                return true;
            }
        });

        db = new DatabaseHelper(this);
        o1 = (EditText)findViewById(R.id.old_pw);
        c1 = (EditText)findViewById(R.id.new_pw_1);
        c2 = (EditText)findViewById(R.id.new_pw_2);
        button = (Button)findViewById(R.id.submit_change_password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1 = o1.getText().toString();
                t2 = c1.getText().toString();
                t3 = c2.getText().toString();
                if(t2.equals(t3)){
                    if(User.getInstance().getUserType().equals("Patient")) {
                        Cursor cursor = db.checkOldPasswordP(User.getInstance().getEmail());
                        if (cursor.getCount() != 0) {
                            while (cursor.moveToNext()) {
                                pw1 = cursor.getString(0);
                            }
                        }
                        if (t1.equals(pw1)) {
                            Boolean ins = db.updatePasswordP(User.getInstance().getEmail(), t2);
                            if (ins) {
                                Toast.makeText(getApplicationContext(), "Password is Successfully Changed!", Toast.LENGTH_LONG).show();
                                o1.setText("");
                                c1.setText("");
                                c2.setText("");
                            } else {
                                Toast.makeText(getApplicationContext(), "Error! Please Try Again Later", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Old Password is not correct!", Toast.LENGTH_LONG).show();
                            o1.setText("");
                            c1.setText("");
                            c2.setText("");
                        }

                    }
                    else{
                        Cursor cursor = db.checkOldPasswordC(User.getInstance().getEmail());
                        if (cursor.getCount() != 0) {
                            while (cursor.moveToNext()) {
                                pw1 = cursor.getString(0);
                            }
                        }
                        if (t1.equals(pw1)) {
                            Boolean ins = db.updatePasswordC(User.getInstance().getEmail(), t2);
                            if (ins) {
                                Toast.makeText(getApplicationContext(), "Password is Successfully Changed!", Toast.LENGTH_LONG).show();
                                o1.setText("");
                                c1.setText("");
                                c2.setText("");
                            } else {
                                Toast.makeText(getApplicationContext(), "Error! Please Try Again Later", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Old Password is not correct!", Toast.LENGTH_LONG).show();
                            o1.setText("");
                            c1.setText("");
                            c2.setText("");
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"New Passwords are not same!",Toast.LENGTH_LONG).show();
                    c1.setText("");
                    c2.setText("");
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent intent = new Intent(ChangePassword.this,MainActivity.class);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setPassword("");
            return true;
        }
        if (id == R.id.action_change_password){
            Intent intent = new Intent(ChangePassword.this,ChangePassword.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
