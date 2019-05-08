package com.example.project1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class emotionActivity extends AppCompatActivity {
//    FeedReaderDbHelper dbHelper;
//    SQLiteDatabase db;
    DatabaseHelper2 db;
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

//        dbHelper = new FeedReaderDbHelper(getApplicationContext());
//        db = dbHelper.getWritableDatabase();
//        values = new ContentValues();
//        patient = new Patient();
        Toast.makeText(getApplicationContext(),Patient.getInstance().getPatientEmail(),Toast.LENGTH_SHORT).show();
        b1 = (Button) findViewById(R.id.emotion1);
        b2 = (Button) findViewById(R.id.emotion2);
        b3 = (Button) findViewById(R.id.emotion3);
        counter1 = 0;
        counter2 = 0;
        counter3 =0;
        arrayList = new ArrayList<String>();
        db = new DatabaseHelper2(this);
        db.createCounter("Happy",0);
        db.createCounter("Moderate",0);
        db.createCounter("Sad",0);
        Cursor cursor = db.getCounter();
        if(cursor.getCount()!=0){
//            Toast.makeText(getApplicationContext(),"Getting Count",Toast.LENGTH_SHORT).show();
            while (cursor.moveToNext()){
                arrayList.add(cursor.getString(1));

            }
        }
        counter1 = Integer.parseInt(arrayList.get(0));
        counter2 = Integer.parseInt(arrayList.get(1));
        counter3 = Integer.parseInt(arrayList.get(2));
//        Toast.makeText(getApplicationContext(),"counter 1 is "+counter1,Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),"counter 2 is "+counter2,Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),"counter 3 is "+counter3,Toast.LENGTH_SHORT).show();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter1++;
//                Toast.makeText(getApplicationContext(),"new counter 1: "+counter1,Toast.LENGTH_SHORT).show();
                Boolean counter = db.setCounter("Happy",counter1);
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
                Boolean counter = db.setCounter("Moderate",counter2);
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
                Boolean counter = db.setCounter("Sad",counter3);
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
