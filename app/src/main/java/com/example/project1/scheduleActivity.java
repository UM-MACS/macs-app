package com.example.project1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class scheduleActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView mTextMessage;
    private Button b1;
    private int yy, mm, dd;
    private DatePicker datePicker;
    private Calendar cal;
    private AlarmManager alarmManager;
    public String appointment;
//    Patient patient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        Intent i1 = new Intent(scheduleActivity.this,MainActivity.class);
                        startActivity(i1);
                        break;
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(scheduleActivity.this,emotionActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(scheduleActivity.this,scheduleActivity.class);
                        startActivity(i3);
                        break;
                }
                return true;
            }
        });
        //finish

        //alarmService
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        final PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        cal = Calendar.getInstance();

        datePicker = (DatePicker)findViewById(R.id.datePicker);
        datePicker.setCalendarViewShown(false); //hide the calendar view
        db = new DatabaseHelper(this);
//        patient = new Patient();
        b1 = (Button)findViewById(R.id.submitButton);
        Toast.makeText(getApplicationContext(),Patient.getInstance().getPatientEmail(),Toast.LENGTH_SHORT).show();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yy = datePicker.getYear();
                mm=datePicker.getMonth();
                dd = datePicker.getDayOfMonth();
                appointment = yy+"/"+mm+"/"+dd;
                Patient.getInstance().setPatientAppointment(appointment);
                boolean set = db.setAppointment(Patient.getInstance().getPatientEmail());
                if(set){
                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
                }
                cal.set(Calendar.YEAR,yy);
                cal.set(Calendar.MONTH,mm);
                cal.set(Calendar.DAY_OF_MONTH,dd);
                cal.set(Calendar.HOUR_OF_DAY,7);
                cal.set(Calendar.MINUTE,2);
                cal.set(Calendar.SECOND,0);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

            }
        });



//        cal.add(Calendar.SECOND, 5);
//        cal.set(yy,mm,dd,2,28,0);
//        cal.set(yy,mm,dd);


        mTextMessage = (TextView) findViewById(R.id.message);

    }

}
