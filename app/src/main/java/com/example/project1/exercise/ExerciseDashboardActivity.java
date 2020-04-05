package com.example.project1.exercise;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.example.project1.PublicComponent;
import com.example.project1.questionnaire.QuestionnaireActivity;
import com.example.project1.R;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.faq.FAQActivity;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.caregiver.CaregiverForumActivity;
import com.example.project1.forum.specialist.SpecialistForumActivity;
import com.example.project1.login.component.SessionManager;
import com.example.project1.login.component.User;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.userProfile.UserProfileActivity;

import java.util.Calendar;

public class ExerciseDashboardActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private Button btnStartExercise1, btnStartExercise2;
    private TextView tvCustomize, tvExerciseDay;
    private Intent intentToExerciseList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    //by default
    private int desiredExerciseDay;
    private int desiredToBeReminded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_dashboard);

        //call database
        getExerciseRecord();

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) findViewById(R.id.navigation);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_exercise);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_assessment:
                        Intent i2 = new Intent(ExerciseDashboardActivity.this,
                                EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_exercise:
                        Intent i3 = new Intent(ExerciseDashboardActivity.this,
                                ExerciseDashboardActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.navigation_chat:
//                        Intent i=getPackageManager().getLaunchIntentForPackage("com.example.fypchat");
//                        startActivity(i);
                        break;
                    case R.id.navigation_forum:
                        if(User.getInstance().getUserType().equalsIgnoreCase("Caregiver")){
                            Intent i6 = new Intent(ExerciseDashboardActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if(User.getInstance().getUserType().equalsIgnoreCase("Patient")){
                            Intent i6 = new Intent(ExerciseDashboardActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        } else{
                            Intent i6 = new Intent(ExerciseDashboardActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        }
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
            tvExerciseDay.setText("You will exercise " + desiredExerciseDay + " days per week!");
        }

    }

    private void getExerciseRecord() {
        //implement database logic here
    }

    public void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExerciseDashboardActivity.this);
        alertDialogBuilder.setTitle("Customize My Exercise Plan");
//        alertDialogBuilder.setMessage("How many days do you want to exercise per week?");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
        final Spinner spinner = (Spinner)dialogView.findViewById(R.id.spinner_dialog);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ExerciseDashboardActivity.this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.exercise_day));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final CheckBox checkBox = (CheckBox) dialogView.findViewById(R.id.checkbox_dialog);

        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#228B22'>OK</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        desiredExerciseDay = Integer.parseInt(spinner.getSelectedItem().toString());
                        if(desiredExerciseDay > 0){
                            tvExerciseDay.setText("You will exercise " + desiredExerciseDay + " days per week!");
                        }
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
        alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#DC143C'>Cancel</font>"),
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
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY,22);
//        calendar.set(Calendar.MINUTE,48);
//        calendar.set(Calendar.SECOND,30);
//        Intent intent = new Intent(getApplicationContext(),NotificationReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),200,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

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
        calendar.set(Calendar.HOUR_OF_DAY,10);
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
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setUserType("");
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
            Intent intent = new Intent(ExerciseDashboardActivity.this, FAQActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_questionnaire) {
            Intent intent = new Intent(ExerciseDashboardActivity.this, QuestionnaireActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
