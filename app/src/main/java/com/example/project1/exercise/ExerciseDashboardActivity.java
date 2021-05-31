package com.example.project1.exercise;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.PublicComponent;
import com.example.project1.changeLanguage.ChangeLanguageActivity;
import com.example.project1.chat.ChatChannelListActivity;
import com.example.project1.eventReminder.EventReminderActivity;
import com.example.project1.forum.caregiver.CaregiverForumActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.onboarding.OnboardingBaseActivity;
import com.example.project1.questionnaire.QuestionnaireActivity;
import com.example.project1.R;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.specialist.SpecialistForumActivity;
import com.example.project1.login.component.SessionManager;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.userProfile.UserProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ExerciseDashboardActivity extends BaseActivity {

    private SessionManager sessionManager;
    private Button btnStartExercise1, btnStartExercise2;
    private TextView tvCustomize, tvExerciseDay;
    private Intent intentToExerciseList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    //by default
    private int desiredExerciseDay;
    private int desiredToBeReminded;
    //database
    private String localhost;
    private static String GET_SESSION_URL;
    private int completedExerciseDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_dashboard);

        //define all instant variables
        localhost = getString(R.string.localhost);
        GET_SESSION_URL = localhost+"/getExercise/";

        //call database
        completedExerciseDay = 0;
        getExerciseRecord();

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if(CurrentUser.getInstance().getUserType().equals("Admin")){
            bottomNavigationView.setVisibility(View.GONE);
        }
        if(CurrentUser.getInstance().getUserType().equals("Caregiver")||
                CurrentUser.getInstance().getUserType().equals("Specialist")){
            MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_exercise);
            item.setVisible(false);
        }
        MenuItem itemForum = bottomNavigationView.getMenu().findItem(R.id.navigation_exercise);
        itemForum.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_assessment:
                        Intent i2 = new Intent(ExerciseDashboardActivity.this, EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_exercise:
//                        Intent i3 = new Intent(ExerciseDashboardActivity.this, ExerciseDashboardActivity.class);
//                        startActivity(i3);
                        break;
//                    Intent i4 = new Intent(ForumActivity.this, QuestionnaireListActivity.class);
//                        startActivity(i4);
//                        break;
//                    case R.id.navigation_faq:
//                        Intent i5 = new Intent(ForumActivity.this, FAQActivity.class);
//                        startActivity(i5);
//                        break;
                    case R.id.navigation_forum:
                        if(CurrentUser.getInstance().getUserType().equalsIgnoreCase("Specialist")
                                || CurrentUser.getInstance().getUserType().equalsIgnoreCase("Admin")){
                            Intent i6 = new Intent(ExerciseDashboardActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if(CurrentUser.getInstance().getUserType().equalsIgnoreCase(PublicComponent.CAREGIVER)){
                            Intent i6 = new Intent(ExerciseDashboardActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else {
                            Intent i6 = new Intent(ExerciseDashboardActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        }
                    case R.id.navigation_chat:
                        Intent i7 = new Intent(ExerciseDashboardActivity.this, ChatChannelListActivity.class);
                        startActivity(i7);
                        break;
                }
                return true;
            }
        });

        //define all instant variables
        sessionManager = new SessionManager(this);
        intentToExerciseList = new Intent(ExerciseDashboardActivity.this, ExerciseListActivity.class);
        sharedPreferences = getSharedPreferences(PublicComponent.EXERCISE_ACCESS,PublicComponent.PRIVATE_MODE);
        editor = sharedPreferences.edit();
        desiredExerciseDay = sharedPreferences.getInt(PublicComponent.DESIRE_EXERCISE_DAY, 0);
        desiredToBeReminded = sharedPreferences.getInt(PublicComponent.DESIRE_TO_BE_REMIND, 0);

        //define all elements
        btnStartExercise1 = findViewById(R.id.button_exercise_one);
        btnStartExercise2 = findViewById(R.id.button_exercise_two);
        tvCustomize = findViewById(R.id.tv_customize);
        tvExerciseDay = findViewById(R.id.tv_exercise_day);

        //overwrites methods elements
        btnStartExercise1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(PublicComponent.EXERCISE_TYPE,"exercise-level-one");
                editor.apply();
                startActivity(intentToExerciseList);
            }
        });
        btnStartExercise2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(PublicComponent.EXERCISE_TYPE,"exercise-level-two");
                editor.apply();
                startActivity(intentToExerciseList);
            }
        });
        tvCustomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        if(desiredExerciseDay > 0){
            String text = getResources().getString(R.string.exercise_1_day) + " "
                    + completedExerciseDay + "/" + desiredExerciseDay + " "
                    + getResources().getString(R.string.exercise_2_day);
            tvExerciseDay.setText(text);
        }

    }

    private void getExerciseRecord() {
        //implement database logic here
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_SESSION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            ArrayList<String> datetime = new ArrayList<>();
                            if (success.equals("1")) {
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    datetime.add(object.getString("startTime"));
                                }
                                Calendar cal = Calendar.getInstance();
                                cal.add( Calendar.DAY_OF_WEEK, -(Calendar.SUNDAY - cal.get( Calendar.DAY_OF_WEEK )) );
                                Date sunday = cal.getTime();
                                for(int i = 0; i < datetime.size(); i++){
                                    Date currentDate =  new SimpleDateFormat("yyyy-MM-dd").parse(datetime.get(i).substring(0,10));
                                    long diffInMillies = Math.abs(sunday.getTime() - currentDate.getTime());
                                    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                                    int compare = Integer.parseInt(Long.toString(diff));
                                    Log.e("TAG", "compare: " + compare);
                                    if(compare < 7){
                                        completedExerciseDay++;
                                        Log.e("TAG", "completed: " + completedExerciseDay);
                                    }
                                }
                                if(desiredExerciseDay > 0){
                                    String text = getResources().getString(R.string.exercise_1_day) + " "
                                            + completedExerciseDay + "/" + desiredExerciseDay + " "
                                            + getResources().getString(R.string.exercise_2_day);
                                    tvExerciseDay.setText(text);                                }
                            } else {
//                                Toast.makeText(getApplicationContext(), "Error",
//                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), "Error",
//                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", sessionManager.getUserDetail().get(sessionManager.NRIC));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExerciseDashboardActivity.this);
        alertDialogBuilder.setTitle(R.string.customize_exercise_plan);
//        alertDialogBuilder.setMessage("How many days do you want to exercise per week?");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
        final Spinner spinner = (Spinner)dialogView.findViewById(R.id.spinner_dialog);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ExerciseDashboardActivity.this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.exercise_day));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final CheckBox checkBox = (CheckBox) dialogView.findViewById(R.id.checkbox_dialog);
        String save = "<font color='#228B22'>"+getString(R.string.ok)+"</font>";
        String cancel = "<font color='#DC143C'>"+getString(R.string.cancel)+"</font>";

        alertDialogBuilder.setPositiveButton(Html.fromHtml(save),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        desiredExerciseDay = Integer.parseInt(spinner.getSelectedItem().toString());
                        if(desiredExerciseDay > 0){
                            String text = getResources().getString(R.string.exercise_1_day) + " "
                                    + completedExerciseDay + "/" + desiredExerciseDay + " "
                                    + getResources().getString(R.string.exercise_2_day);
                            tvExerciseDay.setText(text);                        }
                        editor.putInt(PublicComponent.DESIRE_EXERCISE_DAY, Integer.parseInt(spinner.getSelectedItem().toString()));
                        if(checkBox.isChecked()){
                            desiredToBeReminded = 1;
                            System.out.println("err " + desiredToBeReminded);
                            setReminder();
                            editor.putInt(PublicComponent.DESIRE_TO_BE_REMIND, 1);
                        }
                        editor.apply();
                        arg0.dismiss();
                    }
                });
        alertDialogBuilder.setNegativeButton(Html.fromHtml(cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                    }
                });

        alertDialogBuilder.setView(dialogView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void setReminder(){

        //alarmService
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final String action = "ConnectivityManager.CONNECTIVITY_ACTION";
        IntentFilter intentFilter = new IntentFilter(action);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        NotificationReceiver mReceiver = new NotificationReceiver();
        getApplicationContext().registerReceiver(mReceiver, intentFilter);
        Intent i2 = new Intent(action);
        final PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i2, 0);

        //Notification for api less than 26
        Intent notificationIntent = new Intent("android.media.action.exercise.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.exercise.DEFAULT");
        final PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        Log.e("tag", "remind");
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pi);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, broadcast);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);
        Log.e("tag", "done setting");
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
            Intent intent = new Intent(ExerciseDashboardActivity.this, MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            CurrentUser.getInstance().setUserName("");
            CurrentUser.getInstance().setNRIC("");
            CurrentUser.getInstance().setUserType("");
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(ExerciseDashboardActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(ExerciseDashboardActivity.this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(ExerciseDashboardActivity.this, OnboardingBaseActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_questionnaire) {
            Intent intent = new Intent(ExerciseDashboardActivity.this, QuestionnaireActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_event_reminder){
            Intent intent = new Intent(ExerciseDashboardActivity.this, EventReminderActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_switch_language){
            Intent intent = new Intent(this, ChangeLanguageActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
