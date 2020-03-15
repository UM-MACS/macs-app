package com.example.project1.exercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

public class ExerciseDashboardActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private Button btnStartExercise1, btnStartExercise2;
    private Intent intentToExerciseList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final static String EXERCISE_TYPE = "EXERCISE_TYPE";
    private final static int PRIVATE_MODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_dashboard);

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
//                    ////                        Intent i4 = new Intent(EmotionAssessmentActivity.this,
////                                SelfAssessmentListActivity.class);
////                        startActivity(i4);
////                        break;
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

        sessionManager = new SessionManager(this);



        //define all instant variables
        intentToExerciseList = new Intent(ExerciseDashboardActivity.this, ExerciseListActivity.class);
        sharedPreferences = getSharedPreferences(EXERCISE_TYPE,PRIVATE_MODE);
        editor = sharedPreferences.edit();

        //define all elements
        btnStartExercise1 = findViewById(R.id.button_exercise_one);
        btnStartExercise2 = findViewById(R.id.button_exercise_two);

        //overwrites methods elements
        btnStartExercise1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("exercise-type","exercise-level-one");
                editor.apply();
                startActivity(intentToExerciseList);
            }
        });
        btnStartExercise2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("exercise-type","exercise-level-two");
                editor.apply();
                startActivity(intentToExerciseList);
            }
        });
    }

    //PUT DASHBOARD LOGIC


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

        return super.onOptionsItemSelected(item);
    }

}
