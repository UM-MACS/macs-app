package com.example.project1.eventReminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.onboarding.OnboardingBaseActivity;
import com.example.project1.questionnaire.QuestionnaireActivity;
import com.example.project1.exercise.ExerciseDashboardActivity;
import com.example.project1.faq.FAQActivity;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.R;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.eventReminder.component.AlarmReceiver;
import com.example.project1.login.component.SessionManager;
import com.example.project1.login.component.User;
import com.example.project1.userProfile.UserProfileActivity;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.caregiver.CaregiverForumActivity;
import com.example.project1.forum.specialist.SpecialistForumActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EventReminderActivity extends BaseActivity {
    private LinearLayout parentLinearLayout;
    private LinearLayout datePickerLayout;
    private LinearLayout clickableView;
    private FrameLayout frameLayout;
//    private DatabaseHelper db;
    private TextView appointmentDateText, appointmentTimeText, remarkTextView, idTextView;
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
    private String localhost;
    private String URL;
    private String URL2;
    private String URL3;
    private String URL4;
    private SessionManager sessionManager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_reminder);

        Log.e("TAG", "onCreate: view appointment name: "+ User.getInstance().getUserName() );

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
//        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_exercise);
//        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_assessment:
                        Intent i2 = new Intent(EventReminderActivity.this, EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_exercise:
                        Intent i3 = new Intent(EventReminderActivity.this, ExerciseDashboardActivity.class);
                        startActivity(i3);
                        break;
//                    //                        Intent i4 = new Intent(EventReminderActivity.this, QuestionnaireListActivity.class);
//                        startActivity(i4);
//                        break;
//                    case R.id.navigation_faq:
//                        Intent i5 = new Intent(EventReminderActivity.this, FAQActivity.class);
//                        startActivity(i5);
//                        break;
                    case R.id.navigation_forum:
                        if(User.getInstance().getUserType().equalsIgnoreCase("Caregiver")){
                            Intent i6 = new Intent(EventReminderActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if(User.getInstance().getUserType().equalsIgnoreCase("Patient")){
                            Intent i6 = new Intent(EventReminderActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        } else{
                            Intent i6 = new Intent(EventReminderActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        }
                    case R.id.navigation_chat:
                        break;
                }
                return true;
            }
        });

        localhost = getString(R.string.localhost);
        URL = localhost+"/getAppointment";
        URL2 = localhost+"/setAppointment";
        URL3 = localhost+"/deleteAppointment";
        URL4 = localhost+"/updateAppointment";
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        frameLayout = (FrameLayout) findViewById(R.id.foreground_menu);
        frameLayout.getForeground().setAlpha(0);
        datePickerLayout = (LinearLayout) findViewById(R.id.container_date_picker);
        clickableView = (LinearLayout) findViewById(R.id.clickable_view);
        sessionManager = new SessionManager(this);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        getAppointmentData(User.getInstance().getEmail());




    }

    private AlertDialog AskOption(final View v, final String text) {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Delete Appointment?")

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        delAppointment(v,text);
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    private void getAppointmentData(final String email) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            Log.e("TAG", "success: "+ success);
                            ArrayList <String> remarkList = new ArrayList<>();
                            ArrayList <String> dateList = new ArrayList<>();
                            ArrayList <String> timeList = new ArrayList<>();
                            ArrayList <String> idList = new ArrayList<>();
                            if(success.equals("1")){
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    remarkList.add(object.getString("remark"));
                                    dateList.add(object.getString("appointmentDate"));
                                    timeList.add(object.getString("appointmentTime"));
                                    idList.add(object.getString("id"));
                                }

                                for (int i = 0; i < dateList.size(); i++) {
                                    Log.e("TAG", " dates: "+dateList.get(i) );
                                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View rowView = inflater.inflate(R.layout.field_event_reminder, parentLinearLayout, false);
                                    parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
                                    remarkTextView = (TextView) ((View) rowView).findViewById(R.id.show_remark_text);
                                    appointmentDateText = (TextView) ((View) rowView).findViewById(R.id.appointment_date_text);
                                    appointmentTimeText = (TextView) ((View) rowView).findViewById(R.id.appointment_time_text);
                                    idTextView = (TextView)((View)rowView).findViewById(R.id.appointment_id);

                                    remarkTextView.setText(remarkList.get(i));
                                    appointmentDateText.setText(dateList.get(i));
                                    appointmentTimeText.setText(timeList.get(i));
                                    idTextView.setText(idList.get(i));

                                    String oldDate[] = dateList.get(i).split("/");
                                    String oldTime[] = timeList.get(i).split(":");
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
                                progressBar.setVisibility(View.GONE);
                            } else{
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void onAddField(View v) {
        //add row of view
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field_event_reminder, parentLinearLayout, false);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
        appointmentDateText = (TextView) ((View) rowView).findViewById(R.id.appointment_date_text);
        appointmentTimeText = (TextView) ((View) rowView).findViewById(R.id.appointment_time_text);
        remarkTextView = (TextView) ((View) rowView).findViewById(R.id.show_remark_text);

        //Create pop up window
        LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater1.inflate(R.layout.activity_schedule_event, null);
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
                picker2 = new DatePickerDialog(EventReminderActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                picker = new TimePickerDialog(EventReminderActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        new TimePickerDialog.OnTimeSetListener() {
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
                //check if all field_event_reminder are not empty
                if (HH == null || mm == null) {
                    Toast.makeText(getApplicationContext(), "Please Select a Time", Toast.LENGTH_SHORT).show();
                    parentLinearLayout.removeView((View) rowView);
                } else if(dateText.equals("")){
                    Toast.makeText(getApplicationContext(), "Please Select a Date", Toast.LENGTH_SHORT).show();
                    parentLinearLayout.removeView((View) rowView);
                }
                else {
                    //get remark text
                    String remarkText = editText.getText().toString();
                    //get date
                    Log.e("tag", "time selected is " + timeSelected);
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df6 = new SimpleDateFormat("yyyyMMddHHmm");
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
                        setAppointment(User.getInstance().getEmail(), User.getInstance().getUserType(), remarkText, dateSelected, timeSelected);
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

    private void setAppointment(final String email, final String type, final String remark,
                                final String date, final String time) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            Log.e("TAG", "success insert: "+success );
                            if(success.equals("1")){
                                setReminder(date,time);
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                            else{
                             Toast.makeText(getApplicationContext(),"Fail to Insert",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Fail to Insert",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Fail to Insert",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email", email);
                params.put("type", type);
                params.put("remark",remark);
                params.put("date",date);
                params.put("time",time);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void updateAppointment(final String id, final String remark,
                                   final String date, final String time) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            Log.e("TAG", "success insert: "+success );
                            if(success.equals("1")){
                                Log.e("TAG", "update success" );
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Fail to Insert",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Fail to Insert",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Fail to Insert",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", id);
                params.put("remark",remark);
                params.put("date",date);
                params.put("time",time);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    private void delAppointment(final View v, final String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            Log.e("TAG", "success insert: "+success );
                            if(success.equals("1")){
                                parentLinearLayout.removeView((View)v.getParent());
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Fail to Insert",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Fail to Insert",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Fail to Insert",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void setReminder(String date, String time) {
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
//        db = new DatabaseHelper(this);
        int Year = Integer.parseInt(date.substring(0,4));
        int Month = Integer.parseInt(date.substring(5,7));
        int Day = Integer.parseInt(date.substring(8,10));
        int Hour = Integer.parseInt(time.substring(0,2));
        int Minute = Integer.parseInt(time.substring(3,5));
        cal.set(Year, (Month - 1), Day, Hour, Minute, 0);
        long eventTime=cal.getTimeInMillis();//Returns Time in milliseconds
        long oneDay=AlarmManager.INTERVAL_DAY;//Converts 24 Hrs(1 Day) to milliseconds
        int noOfDays=1;
        long reminderTime =eventTime-(noOfDays*oneDay);

        Log.e("tag", "" + Year+ (Month - 1)+ Day+ Hour+ Minute);
        Log.e("tag", "reminder time is "+reminderTime);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, pi);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, broadcast);
        Log.e("tag", "done setting");
    }

    public void onDelete(View v){
        idTextView = (TextView)((View)v.getParent()).findViewById(R.id.appointment_id);
        final String text = (String) idTextView.getText();
        AlertDialog diaBox = AskOption(v,text);
        diaBox.show();
    }

    public void onEdit(View v) {
        idTextView = (TextView)((View)v.getParent()).findViewById(R.id.appointment_id);
        final String id = idTextView.getText().toString();
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
        Log.e("TAG", "text 2 is recorded"+ text2);
        remarkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarkTextView.setCursorVisible(true);
            }
        });

        LayoutInflater inflater1 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater1.inflate(R.layout.activity_schedule_event,null);
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
                Intent i = new Intent(EventReminderActivity.this, EventReminderActivity.class);
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
                picker2 = new DatePickerDialog(EventReminderActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                picker = new TimePickerDialog(EventReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
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


                    Log.e("tag", "remark is"+text2+"date: "+text+"time: "+text3+
                            "remarktext"+remarkText +"dateSelected: "+dateSelected +
                            "timeselected: "+ timeSelected);
                    updateAppointment(id,remarkText,dateSelected,timeSelected);

                    setReminder(dateSelected,timeSelected);
                }
                Intent i = new Intent(EventReminderActivity.this, EventReminderActivity.class);
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
            sessionManager.logout();
            Intent intent = new Intent(EventReminderActivity.this, MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setPassword("");
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(EventReminderActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(EventReminderActivity.this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(EventReminderActivity.this, OnboardingBaseActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_questionnaire){
            Intent intent = new Intent(EventReminderActivity.this, QuestionnaireActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_event_reminder){
            Intent intent = new Intent(EventReminderActivity.this, EventReminderActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
