package com.example.project1;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class viewEventActivity extends AppCompatActivity {
    private LinearLayout parentLinearLayout;
    private LinearLayout datePickerLayout;
    private LinearLayout clickableView;
    private FrameLayout frameLayout;
    private DatabaseHelper db;
    private TextView appointmentDateText,appointmentTimeText,remarkTextView;
    private Button b1;
    private String min, timeSelected;
    private int yy, mm, dd;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private TimePickerDialog picker;
    private TextView timeText;
    private Calendar cal;
    private AlarmManager alarmManager;
    private EditText editText;
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
                        Intent i3 = new Intent(viewEventActivity.this,viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(viewEventActivity.this, eventAssessment.class);
                        startActivity(i4);
                        break;
                    case R.id.navigation_faq:
                        Intent i5 = new Intent(viewEventActivity.this,FAQ.class);
                        startActivity(i5);
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
        db = new DatabaseHelper(this);
        ArrayList <String> arrayList = new ArrayList<>();
        ArrayList <String> arrayList2 = new ArrayList<>();
        ArrayList <String> arrayList3 = new ArrayList<>();
        Cursor cursor = db.getAppointment(User.getInstance().getUserType(),User.getInstance().getEmail());
        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                Log.e("tag", ""+cursor.getString(1) );
                arrayList.add(cursor.getString(3)); //date
                arrayList2.add(cursor.getString(2)); //remark
                arrayList3.add(cursor.getString(4)); //time
            }
        }
        for (int i=0; i<arrayList.size();i++){
            Log.e("tag", ""+arrayList.get(i));
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.field,parentLinearLayout,false);
            parentLinearLayout.addView(rowView,parentLinearLayout.getChildCount()-1);
            remarkTextView = (TextView)((View)rowView).findViewById(R.id.show_remark_text);
            remarkTextView.setText(arrayList2.get(i));
            appointmentDateText = (TextView)((View)rowView).findViewById(R.id.appointment_date_text);
            appointmentDateText.setText(arrayList.get(i));
            appointmentTimeText = (TextView)((View)rowView).findViewById(R.id.appointment_time_text);
            appointmentTimeText.setText(arrayList3.get(i));
//            //set alarm for recorded
//            cal = Calendar.getInstance();
//            String []dateRecorded = (arrayList.get(i).split("/"));
//            yy = Integer.parseInt(dateRecorded[0]);
//            mm = (Integer.parseInt(dateRecorded[1]))-1;
//            dd = Integer.parseInt(dateRecorded[2]);
//            Log.e("tag", ""+yy+" "+mm+" "+dd);
//            cal.set(Calendar.YEAR, yy);
//            cal.set(Calendar.MONTH, mm) ;
//            cal.set(Calendar.DATE, dd);
//            cal.set(Calendar.HOUR_OF_DAY, 20);
//            cal.set(Calendar.MINUTE, 42);
//            cal.set(Calendar.SECOND, 0);
//            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            final String action = "ConnectivityManager.CONNECTIVITY_ACTION";
//            IntentFilter intentFilter = new IntentFilter(action);
//            intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//            AlarmReceiver mReceiver = new AlarmReceiver();
//            getApplicationContext().registerReceiver(mReceiver,intentFilter);
//            Intent i2 = new Intent(action);
//            final PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0,i2,0);
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
        }


    }
    public void onAddField(View v){
        //add row of view
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field,parentLinearLayout,false);
        parentLinearLayout.addView(rowView,parentLinearLayout.getChildCount()-1);
        appointmentDateText = (TextView)((View)rowView).findViewById(R.id.appointment_date_text);
        appointmentTimeText = (TextView)((View)rowView).findViewById(R.id.appointment_time_text);
        remarkTextView = (TextView)((View)rowView).findViewById(R.id.show_remark_text);

        //alarmService
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final String action = "ConnectivityManager.CONNECTIVITY_ACTION";
        IntentFilter intentFilter = new IntentFilter(action);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        AlarmReceiver mReceiver = new AlarmReceiver();
        getApplicationContext().registerReceiver(mReceiver,intentFilter);
        Intent i2 = new Intent(action);
        final PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0,i2,0);


        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        final PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        cal = Calendar.getInstance();
        db = new DatabaseHelper(this);
        //Create pop up window
        LayoutInflater inflater1 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater1.inflate(R.layout.activity_schedule,null);
            // create a focusable PopupWindow with the given layout and correct size
        final PopupWindow pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            //dim background
        frameLayout.getForeground().setAlpha(220);
        //get and set calendarView and editText
        editText = (EditText)view.findViewById(R.id.remark);
        datePicker = (DatePicker)view.findViewById(R.id.datePicker);
        datePicker.setCalendarViewShown(false); //hide the calendar view
//        timePicker = (TimePicker)view.findViewById(R.id.timePicker);
//        timePicker.setIs24HourView(true);

        //When close Button is clicked
        ((Button)view.findViewById(R.id.close_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                frameLayout.getForeground().setAlpha(0);
                parentLinearLayout.removeView((View)rowView);
            }
        });

        //When set time edit text is pressed
        timeText = (TextView)view.findViewById(R.id.select_time);
        timeText.setInputType(InputType.TYPE_NULL);
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int Hour = cldr.get(Calendar.HOUR_OF_DAY);
                int Minutes = cldr.get(Calendar.MINUTE);
                //time picker dialog
                picker = new TimePickerDialog(viewEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        if(sMinute==0){
                            timeSelected=(sHour + ":00");
                        }
                        else{
                        timeSelected = (sHour + ":" + sMinute);
                        }
                        timeText.setText(timeSelected);
                    }
                }, Hour, Minutes, true);
                picker.show();
            }
                });

        //When Submit Button is clicked
        ((Button) view.findViewById(R.id.submitButton)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pw.dismiss();
                frameLayout.getForeground().setAlpha(0);
                //get remark text
                String remarkText = editText.getText().toString();
//                //get time
//                int hour, minute;
//                String am_pm;
//                if (Build.VERSION.SDK_INT >= 23 ){
//                    hour = timePicker.getHour();
//                    minute = timePicker.getMinute();
//                }
//                else{
//                    hour = timePicker.getCurrentHour();
//                    minute = timePicker.getCurrentMinute();
//                }
//                if(hour > 12) {
//                    am_pm = "PM";
//                    hour = hour - 12;
//                }
//                else
//                {
//                    am_pm="AM";
//                }
//                if(minute==0){
//                    min = "00";
//                    timeSelected = (hour +":"+ min+" "+am_pm);
//                }
//                else{
//                    timeSelected = (hour +":"+ minute+" "+am_pm);
//                }
//
//                Log.e("tag", "time is "+timeSelected);
                //get date
                yy = datePicker.getYear();
                mm = datePicker.getMonth();
                dd = datePicker.getDayOfMonth();
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy");
                SimpleDateFormat df2 = new SimpleDateFormat("MM");
                SimpleDateFormat df3 = new SimpleDateFormat("dd");
                int year = Integer.parseInt(df.format(c));
                int month = (Integer.parseInt(df2.format(c)))-1;
                int day = Integer.parseInt(df3.format(c));
                Log.e("tag", "year is " + year);
                //if date chosen has passed
                if (year > yy) {
                    Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_SHORT).show();
                    parentLinearLayout.removeView((View)rowView);
                }
                else if(year == yy && month>mm){
                        Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_SHORT).show();
                        parentLinearLayout.removeView((View)rowView);}

                else if (year == yy && month == mm && day>dd){
                            Toast.makeText(getApplicationContext(), "Error!!", Toast.LENGTH_SHORT).show();
                            parentLinearLayout.removeView((View)rowView);
                }
                else {
                    appointment = yy + "/" + (mm + 1) + "/" + dd;
                    remarkTextView.setText(remarkText);
                    appointmentDateText.setText(appointment);
                    appointmentTimeText.setText(timeSelected);
                    User.getInstance().setAppointment(appointment);
                    boolean set = db.setAppointment(User.getInstance().getUserType(), User.getInstance().getEmail(), User.getInstance().getAppointment(),timeSelected, remarkText);
                    if (set) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                    }
                    cal.set(Calendar.YEAR, yy);
                    cal.set(Calendar.MONTH, mm);
                    cal.set(Calendar.DAY_OF_MONTH, dd);
                    cal.set(Calendar.HOUR_OF_DAY, 20);
                    cal.set(Calendar.MINUTE, 00);
                    cal.set(Calendar.SECOND, 0);
                    Log.e("tag", "" + cal.getTime());
//                BroadcastReceiver br = new AlarmReceiver();
//                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//                filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//                registerReceiver(br,filter);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
                    Log.e("tag", "done setting");
                }
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
        appointmentDateText = (TextView)((View)v.getParent()).findViewById(R.id.appointment_date_text);
        final String text = (String) appointmentDateText.getText();
        appointmentTimeText = (TextView)((View)v.getParent()).findViewById(R.id.appointment_time_text);
        final String text3 = (String) appointmentTimeText.getText();
        remarkTextView= (TextView)((View)v.getParent()).findViewById(R.id.show_remark_text);
        final String text2 = (String) remarkTextView.getText();
        db = new DatabaseHelper(this);
        db.deleteAppointment(User.getInstance().getUserType(),User.getInstance().getEmail(),text,text3,text2);
        parentLinearLayout.removeView((View)v.getParent());
    }

    public void onEdit(View v) {
    }

}
