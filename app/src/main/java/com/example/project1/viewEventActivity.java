package com.example.project1;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
    private TextView appointmentDateText, appointmentTimeText, remarkTextView;
    private Button b1;
    private String timeSelected, dateSelected;
    private int yy, MM, dd;
    private String HH, mm;
    private DatePicker datePicker;
    private TimePickerDialog picker;
    private DatePickerDialog picker2;
    private TextView timeText, dateText;
    private Calendar cal;
    private AlarmManager alarmManager;
    private EditText editText;
    private int id;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

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
                        Intent i2 = new Intent(viewEventActivity.this, emotionActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(viewEventActivity.this, viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(viewEventActivity.this, eventAssessment.class);
                        startActivity(i4);
                        break;
                    case R.id.navigation_faq:
                        Intent i5 = new Intent(viewEventActivity.this, FAQ.class);
                        startActivity(i5);
                        break;
                }
                return true;
            }
        });

        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        frameLayout = (FrameLayout) findViewById(R.id.foreground_menu);
        frameLayout.getForeground().setAlpha(0);
        datePickerLayout = (LinearLayout) findViewById(R.id.container_date_picker);
        clickableView = (LinearLayout) findViewById(R.id.clickable_view);
        db = new DatabaseHelper(this);
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> arrayList2 = new ArrayList<>();
        ArrayList<String> arrayList3 = new ArrayList<>();
        ArrayList<String> arrayList4 = new ArrayList<>();
        Cursor cursor = db.getAppointment(User.getInstance().getUserType(), User.getInstance().getEmail());
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                arrayList.add(cursor.getString(4)); //date
                arrayList2.add(cursor.getString(3)); //remark
                arrayList3.add(cursor.getString(5)); //time
            }
        }
        Cursor cursor2 = db.getLatest();
        int max =0;
        if(cursor2.getCount()!=0) {
            while (cursor2.moveToNext()) {
                arrayList4.add(cursor2.getString(0));
            }
            for (int i = 0; i < arrayList4.size(); i++) {
                if (Integer.parseInt(arrayList4.get(i)) > max) {
                    max = Integer.parseInt(arrayList4.get(i));
                }
                id = max;
                Log.e("tag", "max id" + id);
            }
        }
        else {
            id = 1;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.field, parentLinearLayout, false);
            parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
            remarkTextView = (TextView) ((View) rowView).findViewById(R.id.show_remark_text);
            remarkTextView.setText(arrayList2.get(i));
            appointmentDateText = (TextView) ((View) rowView).findViewById(R.id.appointment_date_text);
            appointmentDateText.setText(arrayList.get(i));
            appointmentTimeText = (TextView) ((View) rowView).findViewById(R.id.appointment_time_text);
            appointmentTimeText.setText(arrayList3.get(i));
            String oldDate[] = arrayList.get(i).split("/");
            String oldTime[] = arrayList3.get(i).split(":");
            String newDateString = ""+oldDate[0]+""+(oldDate[1])+""+oldDate[2]+""+oldTime[0]+oldTime[1];
            long newDate = Long.parseLong(newDateString);
            Log.e("tag", "new date "+newDate );
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
            long currentDate = Long.parseLong(df.format(c));
            Log.e("tag", "current date "+currentDate);
            if(currentDate>newDate){
                Log.e("tag", "enter delete");
                parentLinearLayout.removeView((View)rowView);
            }

        }


    }

    public void onAddField(View v) {
        //add row of view
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, parentLinearLayout, false);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
        appointmentDateText = (TextView) ((View) rowView).findViewById(R.id.appointment_date_text);
        appointmentTimeText = (TextView) ((View) rowView).findViewById(R.id.appointment_time_text);
        remarkTextView = (TextView) ((View) rowView).findViewById(R.id.show_remark_text);

        //alarmService
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final String action = "ConnectivityManager.CONNECTIVITY_ACTION";
        IntentFilter intentFilter = new IntentFilter(action);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        AlarmReceiver mReceiver = new AlarmReceiver();
        getApplicationContext().registerReceiver(mReceiver, intentFilter);
        Intent i2 = new Intent(action);
        final PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i2, 0);

        //Notification for api less than 26
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        final PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        cal = Calendar.getInstance();
        db = new DatabaseHelper(this);
        //Create pop up window
        LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater1.inflate(R.layout.activity_schedule, null);
        final PopupWindow pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        frameLayout.getForeground().setAlpha(220);

        editText = (EditText) view.findViewById(R.id.remark);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setCursorVisible(true);
            }
        });

        //When close Button is clicked
        ((Button) view.findViewById(R.id.close_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                frameLayout.getForeground().setAlpha(0);
                parentLinearLayout.removeView((View) rowView);
            }
        });

        //When set date is pressed
        dateText = (TextView)view.findViewById(R.id.select_date);
        dateText.setInputType(InputType.TYPE_NULL);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                //date picker dialog
                picker2 = new DatePickerDialog(viewEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedMonth = ""+(month+1);
                        String selectedDay=""+dayOfMonth;
                        if((month+1)<10){
                            selectedMonth = "0"+(month+1);
                        }
                        if(dayOfMonth<10){
                            selectedDay = "0" + dayOfMonth;
                        }
                        dateSelected = (year+"/"+selectedMonth+"/"+selectedDay);
                        yy = year;
                        MM = month+1;
                        dd = dayOfMonth;
                        dateText.setText(dateSelected);
                    }
                },year,month,day);
                picker2.show();
            }
        });



        //When set time is pressed
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
                        String hourSet="", minuteSet="";
                        if(sMinute==0){
                            minuteSet = "00";
                        }
                        else if (sMinute<10){
                            minuteSet = "0"+sMinute;
                        }
                        else{
                            minuteSet = ""+sMinute;
                        }
                        if( sHour==0){
                            hourSet = "00";
                        }
                        else if(sHour<10){
                            hourSet = "0"+sHour;
                        }
                        else{
                            hourSet = ""+sHour;
                        }
                        timeSelected = (hourSet + ":" + minuteSet);
                        HH = hourSet;
                        mm = minuteSet;
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
                //check if all field are not empty
                if (HH == null || mm == null) {
                    Toast.makeText(getApplicationContext(), "Error!! Please Select a Time", Toast.LENGTH_SHORT).show();
                    parentLinearLayout.removeView((View) rowView);
                } else {
                    //get remark text
                    String remarkText = editText.getText().toString();
                    //get date
                    Log.e("tag", "time selected is " + timeSelected);
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy");
                    SimpleDateFormat df2 = new SimpleDateFormat("MM");
                    SimpleDateFormat df3 = new SimpleDateFormat("dd");
                    SimpleDateFormat df4 = new SimpleDateFormat("mm");
                    SimpleDateFormat df5 = new SimpleDateFormat("HH");
                    SimpleDateFormat df6 = new SimpleDateFormat("yyyyMMddHHmm");
                    int year = Integer.parseInt(df.format(c));
                    int month = (Integer.parseInt(df2.format(c))) - 1;
                    int day = Integer.parseInt(df3.format(c));
                    int minute = Integer.parseInt(df4.format(c));
                    int hour = Integer.parseInt(df5.format(c));
                    long s1, s2;
                    s2 = Long.parseLong(df6.format(c));
                    Log.e("tag", "s2 is " + s2);
                    String yearSet = "" + yy, monthSet = "" + MM, daySet = "" + dd;
                    if (MM < 10) {
                        monthSet = "0" + MM;
                    }
                    if (dd < 10) {
                        daySet = "0" + dd;
                    }
                    s1 = Long.parseLong(yearSet + "" + monthSet + "" + daySet + "" + HH + "" + mm);
                    Log.e("tag", "s1 is " + s1);
                    if (s2 >= s1) {
                        Toast.makeText(getApplicationContext(), "Error!! Please enter a valid Date and Time", Toast.LENGTH_SHORT).show();
                        parentLinearLayout.removeView((View) rowView);
                    } else {
                        remarkTextView.setText(remarkText);
                        appointmentDateText.setText(dateSelected);
                        appointmentTimeText.setText(timeSelected);
                        User.getInstance().setAppointment(dateSelected);
                        Log.e("tag", "date selected is " + dateSelected);
                        boolean set = db.setAppointment("" + (id + 1), User.getInstance().getUserType(), User.getInstance().getEmail(), User.getInstance().getAppointment(), timeSelected, remarkText);
                        if (set) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                        }
                        cal.set(yy, (MM - 1), dd, hour, (minute + 1), 0);
                        Log.e("tag", "" + yy + MM + dd + hour + minute);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
                        Log.e("tag", "done setting");
                    }
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
        appointmentDateText = (TextView)((View)v.getParent()).findViewById(R.id.appointment_date_text);
        final String text = (String) appointmentDateText.getText();
        String box [] = text.split("/");
        yy = Integer.parseInt(box[0]);
        MM = Integer.parseInt(box[1]);
        dd = Integer.parseInt(box[2]);
        appointmentTimeText = (TextView)((View)v.getParent()).findViewById(R.id.appointment_time_text);
        final String text3 = (String) appointmentTimeText.getText();
        String box2 [] = text3.split(":");
        HH = box2[0];
        mm = box2[1];
        remarkTextView= (TextView)((View)v.getParent()).findViewById(R.id.show_remark_text);
        final String text2 = (String) remarkTextView.getText();
        remarkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarkTextView.setCursorVisible(true);
            }
        });
        Cursor cursor = db.getId(User.getInstance().getEmail(),User.getInstance().getUserType(),text,text3,text2);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Log.e("tag", ""+cursor.getString(0));
                ID = cursor.getString(0);
            }
        }


        //alarmService
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final String action = "ConnectivityManager.CONNECTIVITY_ACTION";
        IntentFilter intentFilter = new IntentFilter(action);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        AlarmReceiver mReceiver = new AlarmReceiver();
        getApplicationContext().registerReceiver(mReceiver,intentFilter);
        Intent i2 = new Intent(action);
        final PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0,i2,0);
        //Notification for api less than 26
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        final PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        cal = Calendar.getInstance();
        db = new DatabaseHelper(this);
        //Create pop up window
        LayoutInflater inflater1 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater1.inflate(R.layout.activity_schedule,null);
        final PopupWindow pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        frameLayout.getForeground().setAlpha(220);

        //get and set calendarView and editText
        editText = (EditText)view.findViewById(R.id.remark);
        timeText = (TextView)view.findViewById(R.id.select_time);
        dateText = (TextView)view.findViewById(R.id.select_date);
        timeText.setText(text3);
        timeSelected = timeText.getText().toString();
        editText.setText(text2);
        dateText.setText(text);
        dateSelected = dateText.getText().toString();
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setCursorVisible(true);
            }
        });

        //When close Button is clicked
        ((Button)view.findViewById(R.id.close_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                frameLayout.getForeground().setAlpha(0);
                Intent i = new Intent(viewEventActivity.this,viewEventActivity.class);
                startActivity(i);
            }
        });

        //When set date is pressed
        dateText = (TextView)view.findViewById(R.id.select_date);
        dateText.setInputType(InputType.TYPE_NULL);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                //date picker dialog
                picker2 = new DatePickerDialog(viewEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedMonth = ""+(month+1);
                        String selectedDay=""+dayOfMonth;
                        if((month+1)<10){
                            selectedMonth = "0"+(month+1);
                        }
                        if(dayOfMonth<10){
                            selectedDay = "0" + dayOfMonth;
                        }
                        dateSelected = (year+"/"+selectedMonth+"/"+selectedDay);
                        yy = year;
                        MM = month+1;
                        dd = dayOfMonth;
                        dateText.setText(dateSelected);
                    }
                },year,month,day);
                picker2.show();
            }
        });

        //When set time is pressed
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
                        String hourSet="", minuteSet="";
                        if(sMinute==0){
                            minuteSet = "00";
                        }
                        else if (sMinute<10){
                            minuteSet = "0"+sMinute;
                        }
                        else{
                            minuteSet = ""+sMinute;
                        }
                        if( sHour==0){
                            hourSet = "00";
                        }
                        else if(sHour<10){
                            hourSet = "0"+sHour;
                        }
                        else{
                            hourSet = ""+sHour;
                        }
                        timeSelected = (hourSet + ":" + minuteSet);
                        HH = hourSet;
                        mm = minuteSet;
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
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy");
                SimpleDateFormat df2 = new SimpleDateFormat("MM");
                SimpleDateFormat df3 = new SimpleDateFormat("dd");
                SimpleDateFormat df4 = new SimpleDateFormat("mm");
                SimpleDateFormat df5 = new SimpleDateFormat("HH");
                SimpleDateFormat df6 = new SimpleDateFormat("yyyyMMddHHmm");
                String year = df.format(c);
                int month = (Integer.parseInt(df2.format(c)))-1;
                int day = Integer.parseInt(df3.format(c));
                int minute = Integer.parseInt(df4.format(c));
                int hour = Integer.parseInt(df5.format(c));
                long s1,s2;
                s2 = Long.parseLong(df6.format(c));
                Log.e("tag", "s2 is "+s2);
                String yearSet=""+yy, monthSet=""+MM, daySet=""+dd;
                if(yy<1000){
                    yearSet = yy+"0";
                }
                if(MM<10){
                    monthSet = "0"+MM;
                }
                if(dd<10){
                    daySet = "0"+dd;
                }
                s1 = Long.parseLong(yearSet + "" + monthSet + "" + daySet + "" + HH + "" + mm);
                Log.e("tag", "s1 is "+s1);
                if(s2>=s1){
                    Toast.makeText(getApplicationContext(), "Error!! Please enter a valid Date and Time", Toast.LENGTH_SHORT).show();
                }
                else {
                    remarkTextView.setText(remarkText);
                    appointmentDateText.setText(dateSelected);
                    appointmentTimeText.setText(timeSelected);
                    User.getInstance().setAppointment(dateSelected);
                    if(timeSelected==null){
                        timeSelected = "";
                    }
                    boolean set = db.updateAppointment(ID,User.getInstance().getEmail(),User.getInstance().getUserType(),User.getInstance().getAppointment(),timeSelected,remarkText);
//                    boolean set = db.setAppointment(User.getInstance().getUserType(), User.getInstance().getEmail(), User.getInstance().getAppointment(),timeSelected, remarkText);
//                    boolean set2 = db.deleteAppointment(User.getInstance().getUserType(),User.getInstance().getEmail(),text,text3,text2);
                    if (set) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                    }
                    cal.set(yy,(MM-1),dd,hour,(minute+1),0);
                    Log.e("tag", "" + yy+MM+dd+hour+minute);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
                    Log.e("tag", "done setting");
                }
                Intent i = new Intent(viewEventActivity.this,viewEventActivity.class);
                startActivity(i);
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
            Intent intent = new Intent(viewEventActivity.this,MainActivity.class);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setPassword("");
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(viewEventActivity.this,ChangePassword.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
