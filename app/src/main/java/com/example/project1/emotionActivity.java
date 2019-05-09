package com.example.project1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class emotionActivity extends AppCompatActivity {
//    FeedReaderDbHelper dbHelper;
//    SQLiteDatabase db;
    DatabaseHelper db;
    Button b1,b2,b3,b4;
    TextView t1;
    ArrayList<String> arrayList;
    int counter1, counter2, counter3, counter4=0;
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
                        Intent i3 = new Intent(emotionActivity.this,scheduleActivity.class);
                        startActivity(i3);
                        break;
                }
                return true;
            }
        });
        //finish

        Toast.makeText(getApplicationContext(),Patient.getInstance().getPatientEmail(),Toast.LENGTH_SHORT).show();
        b1 = (Button) findViewById(R.id.emotion1);
        b2 = (Button) findViewById(R.id.emotion2);
        b3 = (Button) findViewById(R.id.emotion3);
        counter1 = 0;
        counter2 = 0;
        counter3 =0;
        arrayList = new ArrayList<String>();
        db = new DatabaseHelper(this);
        Log.e("tag", "Testing 1");
        Boolean check = db.checkMail(Patient.getInstance().getPatientEmail());
        if(check) {
            db.createCounter(Patient.getInstance().getPatientEmail(), 0, 0, 0);
        }
        Cursor cursor = db.getHappyCounter(Patient.getInstance().getPatientEmail());
        Cursor cursor2 = db.getModerateCounter(Patient.getInstance().getPatientEmail());
        Cursor cursor3 = db.getSadCounter(Patient.getInstance().getPatientEmail());
        if(cursor.getCount()!=0){
            while (cursor.moveToNext()){
                counter1 = cursor.getInt(cursor.getColumnIndex("Happy"));
            }
        }
        Log.e("tag", "Happy: "+counter1);
        if(cursor2.getCount()!=0){
            while (cursor2.moveToNext()){
                counter2 = cursor2.getInt(cursor2.getColumnIndex("Moderate"));
            }
        }
        Log.e("tag", "Moderate: "+counter2);
        if(cursor3.getCount()!=0){
            while (cursor3.moveToNext()){
                counter3 = cursor3.getInt(cursor3.getColumnIndex("Sad"));
            }
        }
        Log.e("tag", "Sad: "+counter3);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter1++;
//                Toast.makeText(getApplicationContext(),"new counter 1: "+counter1,Toast.LENGTH_SHORT).show();
                Boolean counter = db.setHappyCounter(Patient.getInstance().getPatientEmail(),counter1);
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
                Boolean counter = db.setModerateCounter(Patient.getInstance().getPatientEmail(),counter2);
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
//                Toast.makeText(getApplicationContext(),"new counter 3: "+counter3,Toast.LENGTH_SHORT).show();
                Boolean counter = db.setSadCounter(Patient.getInstance().getPatientEmail(),counter3);
                if(!counter){
                    Toast.makeText(getApplicationContext(),"Error, Please try again later",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                }


            }
        });



        mTextMessage = (TextView) findViewById(R.id.message);

    }

}
