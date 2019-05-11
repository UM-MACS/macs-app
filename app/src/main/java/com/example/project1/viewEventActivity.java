package com.example.project1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class viewEventActivity extends AppCompatActivity {
private LinearLayout parentLinearLayout;
private LinearLayout datePickerLayout;
private LinearLayout clickableView;
private FrameLayout frameLayout;
private DatabaseHelper db;
private TextView mTextMessage;
private Button b1;
private int yy, mm, dd;
private DatePicker datePicker;
private Calendar cal;
private AlarmManager alarmManager;
public String appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        Intent i1 = new Intent(viewEventActivity.this,MainActivity.class);
                        startActivity(i1);
                        break;
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(viewEventActivity.this,emotionActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(viewEventActivity.this,scheduleActivity.class);
                        startActivity(i3);
                        break;
                }
                return true;
            }
        });


        parentLinearLayout=(LinearLayout)findViewById(R.id.parent_linear_layout);
        frameLayout = (FrameLayout)findViewById(R.id.foreground_menu);
        frameLayout.getForeground().setAlpha(0);
        datePickerLayout = (LinearLayout)findViewById(R.id.container_date_picker);
        clickableView = (LinearLayout)findViewById(R.id.clickable_view);



    }
    public void onAddField(View v){
        //add row of view
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field,parentLinearLayout,false);
        parentLinearLayout.addView(rowView,parentLinearLayout.getChildCount()-1);
        //alarmService
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        final PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        cal = Calendar.getInstance();
        db = new DatabaseHelper(this);
        LayoutInflater inflater1 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater1.inflate(R.layout.activity_schedule,null);
        float density=viewEventActivity.this.getResources().getDisplayMetrics().density;
        // create a focusable PopupWindow with the given layout and correct size
        final PopupWindow pw = new PopupWindow(view, (int)density*500, (int)density*500, true);
        frameLayout.getForeground().setAlpha(220);
//        datePicker = (DatePicker)((View) v.getParent()).view.findViewById(R.id.datePicker);
        datePicker = (DatePicker)view.findViewById(R.id.datePicker);
        datePicker.setCalendarViewShown(false); //hide the calendar view
        //Button to close the pop-up
        ((Button) view.findViewById(R.id.submitButton)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pw.dismiss();
                frameLayout.getForeground().setAlpha(0);
                yy = datePicker.getYear();
                mm=datePicker.getMonth()+1;
                dd = datePicker.getDayOfMonth();
                appointment = yy+"/"+mm+"/"+dd;
                Log.e("tag", ""+appointment);
//                Patient.getInstance().setPatientAppointment(appointment);
//                boolean set = db.setAppointment(User.getInstance().getUserType(),User.getInstance().getEmail());
//                if(set){
//                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
//                }
                cal.set(Calendar.YEAR,yy);
                cal.set(Calendar.MONTH,mm);
                cal.set(Calendar.DAY_OF_MONTH,dd);
                cal.set(Calendar.HOUR_OF_DAY,12);
                cal.set(Calendar.MINUTE,59);
                cal.set(Calendar.SECOND,0);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
            }
        });
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });
        pw.setOutsideTouchable(true);
        // display the pop-up in the center
        pw.showAtLocation(view, Gravity.CENTER, 0, 0);

    }
    public void onDelete(View v){
        parentLinearLayout.removeView((View)v.getParent());
    }

    public void onEdit(View v) {
//        //alarmService
//        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
//        notificationIntent.addCategory("android.intent.category.DEFAULT");
//        final PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        cal = Calendar.getInstance();
//        db = new DatabaseHelper(this);
//        LayoutInflater inflater1 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View view = inflater1.inflate(R.layout.activity_schedule,null);
//        float density=viewEventActivity.this.getResources().getDisplayMetrics().density;
//        // create a focusable PopupWindow with the given layout and correct size
//        final PopupWindow pw = new PopupWindow(view, (int)density*500, (int)density*500, true);
//        frameLayout.getForeground().setAlpha(220);
////        datePicker = (DatePicker)((View) v.getParent()).view.findViewById(R.id.datePicker);
//        datePicker = (DatePicker)view.findViewById(R.id.datePicker);
//        datePicker.setCalendarViewShown(false); //hide the calendar view
//        //Button to close the pop-up
//        ((Button) view.findViewById(R.id.submitButton)).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                pw.dismiss();
//                frameLayout.getForeground().setAlpha(0);
//                yy = datePicker.getYear();
//                mm=datePicker.getMonth()+1;
//                dd = datePicker.getDayOfMonth();
//                appointment = yy+"/"+mm+"/"+dd;
//                Log.e("tag", ""+appointment);
////                Patient.getInstance().setPatientAppointment(appointment);
////                boolean set = db.setAppointment(User.getInstance().getUserType(),User.getInstance().getEmail());
////                if(set){
////                    Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
////                }
////                else{
////                    Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_SHORT).show();
////                }
//                cal.set(Calendar.YEAR,yy);
//                cal.set(Calendar.MONTH,mm);
//                cal.set(Calendar.DAY_OF_MONTH,dd);
//                cal.set(Calendar.HOUR_OF_DAY,12);
//                cal.set(Calendar.MINUTE,59);
//                cal.set(Calendar.SECOND,0);
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
//            }
//        });
//        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        pw.setTouchInterceptor(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                    pw.dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
//        pw.setOutsideTouchable(true);
//        // display the pop-up in the center
//        pw.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

}
