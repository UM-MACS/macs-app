package com.example.project1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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
    RadioGroup radio2;
    RadioGroup radio3;
    RadioGroup radio4;
    RadioGroup radio5;
    RadioGroup radio6;
    RadioGroup radio7;

    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    RadioButton radioButton5;
    RadioButton radioButton6;
    RadioButton radioButton7;

    TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_assessment);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.nagivation_event_assessment);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
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
        radio2 = (RadioGroup) findViewById(R.id.radioGroup2);
        radio3 = (RadioGroup) findViewById(R.id.radioGroup3);
        radio4 = (RadioGroup) findViewById(R.id.radioGroup4);
        radio5 = (RadioGroup) findViewById(R.id.radioGroup5);
        radio6 = (RadioGroup) findViewById(R.id.radioGroup6);
        radio7 = (RadioGroup) findViewById(R.id.radioGroup7);
        editText = (EditText) findViewById(R.id.answerEditText);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //button 1
                int radioId = radio1.getCheckedRadioButtonId();
                radioButton1 = (RadioButton)findViewById(radioId);
                String text = radioButton1.getText().toString();
                //button 2
                int radioId2 = radio2.getCheckedRadioButtonId();
                radioButton2 = (RadioButton)findViewById(radioId2);
                String text2 = radioButton2.getText().toString();
                //button 3
                int radioId3 = radio3.getCheckedRadioButtonId();
                radioButton3 = (RadioButton)findViewById(radioId3);
                String text3 = radioButton3.getText().toString();
                //button 4
                int radioId4 = radio4.getCheckedRadioButtonId();
                radioButton4 = (RadioButton)findViewById(radioId4);
                String text4 = radioButton4.getText().toString();
                //button 5
                int radioId5 = radio5.getCheckedRadioButtonId();
                radioButton5 = (RadioButton)findViewById(radioId5);
                String text5 = radioButton5.getText().toString();
                //button 6
                int radioId6 = radio6.getCheckedRadioButtonId();
                radioButton6 = (RadioButton)findViewById(radioId6);
                String text6 = radioButton6.getText().toString();
                //button 7
                int radioId7 = radio7.getCheckedRadioButtonId();
                radioButton7 = (RadioButton)findViewById(radioId7);
                String text7 = radioButton7.getText().toString();
                //edit text
                String text8 = editText.getText().toString();

                Boolean ins =db.insertEventAssessment(User.getInstance().getEmail(),text,text2,text3,text4,text5,text6,text7,text8);

                if(ins){
                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                }
                Log.e("tag", ""+radioButton1.getText());


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
            Intent intent = new Intent(eventAssessment.this,MainActivity.class);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setPassword("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}


