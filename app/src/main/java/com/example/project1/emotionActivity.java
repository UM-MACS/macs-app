package com.example.project1;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class emotionActivity extends AppCompatActivity {
    DatabaseHelper db;
    Button b1,b2,b3,b4,b5,submitButton;
    ArrayList<String> arrayList;
    EditText expression;
    int counter1, counter2, counter3, counter4, counter5;
//    ContentValues values;
    private TextView mTextMessage;
//    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        Intent i1 = new Intent(emotionActivity.this,MainActivity.class);
                        startActivity(i1);
                        break;
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(emotionActivity.this,emotionActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(emotionActivity.this,viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(emotionActivity.this, eventAssessment.class);
                        startActivity(i4);
                        break;
                    case R.id.navigation_faq:
                        Intent i5 = new Intent(emotionActivity.this,FAQ.class);
                        startActivity(i5);
                        break;
                }
                return true;
            }
        });
        //end

        b1 = (Button) findViewById(R.id.emotion1);
        b2 = (Button) findViewById(R.id.emotion2);
        b3 = (Button) findViewById(R.id.emotion3);
        b4 = (Button) findViewById(R.id.emotion4);
        b5 = (Button) findViewById(R.id.emotion5);
        counter1 = 0;
        counter2 = 0;
        counter3 =0;
        counter4 =0;
        counter5 =0;
        arrayList = new ArrayList<String>();
        db = new DatabaseHelper(this);
        Boolean check = db.checkMail(User.getInstance().getEmail());
        Boolean check2 = db.checkMail2(User.getInstance().getEmail());
        Log.e("tag", ""+User.getInstance().getUserType());
        if(check || check2) {
            db.createCounter(User.getInstance().getUserType(),User.getInstance().getEmail(), 0, 0, 0,0,0);
        }
        Cursor cursor = db.getHappyCounter(User.getInstance().getUserType(),User.getInstance().getEmail());
        Cursor cursor2 = db.getSmileyCounter(User.getInstance().getUserType(),User.getInstance().getEmail());
        Cursor cursor3 = db.getUnhappyCounter(User.getInstance().getUserType(),User.getInstance().getEmail());
        Cursor cursor4 = db.getAngryCounter(User.getInstance().getUserType(),User.getInstance().getEmail());
        Cursor cursor5 = db.getSadCounter(User.getInstance().getUserType(),User.getInstance().getEmail());
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                counter1 = cursor.getInt(cursor.getColumnIndex("Happy"));
            }
        }
        Log.e("tag", "Happy: "+counter1);
        if(cursor2.getCount()!=0){
            while (cursor2.moveToNext()){
                counter2 = cursor2.getInt(cursor2.getColumnIndex("Smiley"));
            }
        }
        Log.e("tag", "Smiley: "+counter2);
        if(cursor3.getCount()!=0){
            while (cursor3.moveToNext()){
                counter3 = cursor3.getInt(cursor3.getColumnIndex("Unhappy"));
            }
        }
        Log.e("tag", "Unhappy: "+counter3);
        if(cursor4.getCount()!=0){
            while (cursor4.moveToNext()){
                counter3 = cursor4.getInt(cursor4.getColumnIndex("Angry"));
            }
        }
        Log.e("tag", "Angry: "+counter3);
        if(cursor5.getCount()!=0){
            while (cursor5.moveToNext()){
                counter3 = cursor5.getInt(cursor5.getColumnIndex("Sad"));
            }
        }
        Log.e("tag", "Sad: "+counter3);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter1++;
//                Toast.makeText(getApplicationContext(),"new counter 1: "+counter1,Toast.LENGTH_SHORT).show();
                Boolean counter = db.setHappyCounter(User.getInstance().getUserType(),User.getInstance().getEmail(),counter1);
//                Boolean counter2 = db.setCounter("Moderate",0);
//                Boolean counter3 = db.setCounter("Sad",0);
                if(!counter){
                    Toast.makeText(getApplicationContext(),"Error, Please try again later",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter2++;
//                Toast.makeText(getApplicationContext(),"new counter 2: "+counter2,Toast.LENGTH_SHORT).show();
                Boolean counter = db.setSmileyCounter(User.getInstance().getUserType(),User.getInstance().getEmail(),counter2);
                if(!counter){
                    Toast.makeText(getApplicationContext(),"Error, Please try again later",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                }

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter3++;
//                Toast.makeText(getApplicationContext(),"new counter 2: "+counter2,Toast.LENGTH_SHORT).show();
                Boolean counter = db.setUnhappyCounter(User.getInstance().getUserType(),User.getInstance().getEmail(),counter3);
                if(!counter){
                    Toast.makeText(getApplicationContext(),"Error, Please try again later",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                }

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter4++;
//                Toast.makeText(getApplicationContext(),"new counter 2: "+counter2,Toast.LENGTH_SHORT).show();
                Boolean counter = db.setAngryCounter(User.getInstance().getUserType(),User.getInstance().getEmail(),counter4);
                if(!counter){
                    Toast.makeText(getApplicationContext(),"Error, Please try again later",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                }

            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter5++;
//                Toast.makeText(getApplicationContext(),"new counter 3: "+counter3,Toast.LENGTH_SHORT).show();
                Boolean counter = db.setSadCounter(User.getInstance().getUserType(),User.getInstance().getEmail(),counter5);
                if(!counter){
                    Toast.makeText(getApplicationContext(),"Error, Please try again later",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                }


            }
        });


        expression = (EditText)findViewById(R.id.expression);
        submitButton = (Button)findViewById(R.id.submitExpressionButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = expression.getText().toString();
                db.setExpressionText(User.getInstance().getUserType(), User.getInstance().getEmail(),text);
            }
        });

    }

}
