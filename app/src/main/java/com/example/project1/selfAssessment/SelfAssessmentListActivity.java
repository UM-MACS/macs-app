package com.example.project1.selfAssessment;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.project1.exercise.ExerciseActivity;
import com.example.project1.exercise.ExerciseDashboardActivity;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.R;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.login.component.SessionManager;
import com.example.project1.login.component.User;
import com.example.project1.userProfile.UserProfileActivity;
import com.example.project1.selfAssessment.adapter.SelfAssessmentActivityAdapter;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.eventReminder.EventReminderActivity;
import com.example.project1.faq.FAQActivity;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.caregiver.CaregiverForumActivity;
import com.example.project1.forum.specialist.SpecialistForumActivity;

import java.util.ArrayList;

public class SelfAssessmentListActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SessionManager sessionManager;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_assessment_list);
        Log.d(TAG, "onCreate: star.");

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        initImageBitmaps();

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView =
                findViewById(R.id.navigation);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_chat);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_assessment:
                        Intent i2 = new Intent(SelfAssessmentListActivity.this,
                                EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_exercise:
                        Intent i3 = new Intent(SelfAssessmentListActivity.this,
                                ExerciseDashboardActivity.class);
                        startActivity(i3);
                        break;
//                    //                        Intent i4 = new Intent(SelfAssessmentListActivity.this,
//                                SelfAssessmentListActivity.class);
//                        startActivity(i4);
//                        break;
                    case R.id.navigation_chat:
                        Intent i=getPackageManager().getLaunchIntentForPackage("com.example.fypchat");
                        startActivity(i);
                        break;
                    case R.id.navigation_forum:
                        if(User.getInstance().getUserType().equalsIgnoreCase("Caregiver")){
                            Intent i6 = new Intent(SelfAssessmentListActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if(User.getInstance().getUserType().equalsIgnoreCase("Patient")){
                            Intent i6 = new Intent(SelfAssessmentListActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        } else{
                            Intent i6 = new Intent(SelfAssessmentListActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        }


                }
                return true;
            }
        });
    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mNames.add("Oral Cancer and its History.\nDate: 10/12/2019");

        mNames.add("How to Gain Emotional Stability?\nDate: 22/11/2019");

        mNames.add("Let Your Family Help You\nDate: 5/11/2019");

        mNames.add("Think Positive!\nDate: 27/10/2019");

        mNames.add("Oral Health Seminar\nDate: 30/9/2019");

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        SelfAssessmentActivityAdapter adapter2 = new SelfAssessmentActivityAdapter(this, mNames);
        recyclerView.setAdapter(adapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            Intent intent = new Intent(SelfAssessmentListActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setUserType("");
//            finish();
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(SelfAssessmentListActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(SelfAssessmentListActivity.this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(SelfAssessmentListActivity.this, FAQActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
