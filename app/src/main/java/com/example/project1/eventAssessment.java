package com.example.project1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class eventAssessment extends AppCompatActivity {
        DatabaseHelper db;
        Button b1;
        RadioGroup radio1;
        RadioButton radioButton1;
        TextView textView;
        EditText editText;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_event_assessment);

            //Bottom Navigation Bar
            BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.navigation_home:
                            Intent i1 = new Intent(eventAssessment.this,MainActivity.class);
                            startActivity(i1);
                            break;
                        case R.id.navigation_emotion_tracking:
                            Intent i2 = new Intent(eventAssessment.this,emotionActivity.class);
                            startActivity(i2);
                            break;
                        case R.id.navigation_schedule_appointment:
                            Intent i3 = new Intent(eventAssessment.this,viewEventActivity.class);
                            startActivity(i3);
                            break;
                        case R.id.nagivation_event_assessment:
                            Intent i4 = new Intent(eventAssessment.this, eventAssessment.class);
                            startActivity(i4);
                            break;
                        case R.id.navigation_faq:
                            Intent i5 = new Intent(eventAssessment.this,FAQ.class);
                            startActivity(i5);
                            break;
                    }
                    return true;
                }
            });
            //finish

            db = new DatabaseHelper(this);
            b1 = (Button)findViewById(R.id.button_submit_assessment);
            radio1 = (RadioGroup) findViewById(R.id.radioGroup);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int radioId = radio1.getCheckedRadioButtonId();
                    radioButton1 = (RadioButton)findViewById(radioId);
                    String text = radioButton1.getText().toString();
                    Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
                    Boolean ins =db.insertEventAssessment(User.getInstance().getEmail(),text);
                    if(ins){
                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                    }
                    Log.e("tag", ""+radioButton1.getText());
                }
            });

        }


    }


