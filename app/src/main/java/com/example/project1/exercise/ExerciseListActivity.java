package com.example.project1.exercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project1.PublicComponent;
import com.example.project1.R;
import com.example.project1.changeLanguage.ChangeLanguageActivity;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.chat.ChatChannelListActivity;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.exercise.adapter.ExerciseListAdapter;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.caregiver.CaregiverForumActivity;
import com.example.project1.forum.specialist.SpecialistForumActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.SessionManager;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.onboarding.OnboardingBaseActivity;
import com.example.project1.questionnaire.QuestionnaireActivity;
import com.example.project1.userProfile.UserProfileActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ExerciseListActivity extends BaseActivity {

    private Button btnRandom, btnExerciseStart;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private LinearLayoutManager layoutManager;
    private Intent intentToExercise;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SessionManager sessionManager;

    //exercise related variables
    private String exerciseType;
    //default exercise list
    private ArrayList<String> exerciseList = new ArrayList<>();
    private ArrayList<String> exerciseTimeList = new ArrayList<>();
    //exercise level one
    private ArrayList<String> exerciseList1 = new ArrayList<>(Arrays.asList("Shoulder Shrug", "Seated Ladder Climb", "Seated Russian Twist", "Sit to Stand",
            "Seated Bent over Row", "Toe Lift", "Wall Push Up", "Oblique Squeeze"));
    private ArrayList<String> exerciseTimeList1 = new ArrayList<>(Arrays.asList("20s","20s","20s","20s","20s","20s","20s","20s"));
    //exercise level two
    private ArrayList<String> exerciseList2 = new ArrayList<>(Arrays.asList("Seated Bicycle Crunch","Seated Butterfly","Lateral Leg Raise", "Squat with Rotational Press",
            "Wood Cutter", "Empty the Can", "Standing Bicycle Crunch"));
    private ArrayList<String> exerciseTimeList2 = new ArrayList<>(Arrays.asList("30s","30s","30s","30s","30s","30s","30s"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

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
                        Intent i2 = new Intent(ExerciseListActivity.this, EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_exercise:
                        Intent i3 = new Intent(ExerciseListActivity.this, ExerciseDashboardActivity.class);
                        startActivity(i3);
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
                            Intent i6 = new Intent(ExerciseListActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if(CurrentUser.getInstance().getUserType().equalsIgnoreCase(PublicComponent.CAREGIVER)){
                            Intent i6 = new Intent(ExerciseListActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else {
                            Intent i6 = new Intent(ExerciseListActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        }
                    case R.id.navigation_chat:
                        Intent i7 = new Intent(ExerciseListActivity.this, ChatChannelListActivity.class);
                        startActivity(i7);
                        break;
                }
                return true;
            }
        });

        //define all instant variables
        intentToExercise = new Intent(ExerciseListActivity.this, ExerciseActivity.class);
        sharedPreferences = getSharedPreferences(PublicComponent.EXERCISE_ACCESS, PublicComponent.PRIVATE_MODE);
        editor = sharedPreferences.edit();
        exerciseType = sharedPreferences.getString(PublicComponent.EXERCISE_TYPE, null);
        sessionManager = new SessionManager(this);

        //define all elements
        btnRandom = findViewById(R.id.button_random);
        btnExerciseStart = findViewById(R.id.button_exercise_start);
        tvTitle = findViewById(R.id.textview_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_exercise_list);

        //set value to exercise list
        if(exerciseType.contentEquals("exercise-level-one")){
            exerciseList = exerciseList1;
            exerciseTimeList = exerciseTimeList1;
        }
        else{
            exerciseList = exerciseList2;
            exerciseTimeList = exerciseTimeList2;
            tvTitle.setText(R.string.exercise_level_2);
        }

        //adapter declare
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerViewAdapter = new ExerciseListAdapter(this, exerciseList, exerciseTimeList);
        recyclerView.setAdapter(recyclerViewAdapter);

        //overwrites methods elements
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                ArrayList<String> temp1 = new ArrayList<>();
                ArrayList<String> temp2 = new ArrayList<>();
                ArrayList<Integer> temp = new ArrayList<>();
                int i;

                while(true){
                    i = random.nextInt(exerciseList.size());
                    if(temp.size() == exerciseList.size()){
                        break;
                    }
                    if(!temp.contains(i)) {
                        temp1.add(exerciseList.get(i));
                        temp2.add(exerciseTimeList.get(i));
                        temp.add(i);
                    }
                }
                exerciseList.clear();
                exerciseList.addAll(temp1);
                exerciseTimeList.clear();
                exerciseTimeList.addAll(temp2);

                recyclerViewAdapter.notifyDataSetChanged();
            }
        });
        btnExerciseStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(PublicComponent.EXERCISE_LIST,exerciseList.toString());
//                editor.putString("timelist",exerciseTimeList.toString());
                editor.apply();
                startActivity(intentToExercise);
            }
        });

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
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            CurrentUser.getInstance().setUserName("");
            CurrentUser.getInstance().setNRIC("");
            CurrentUser.getInstance().setUserType("");
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(this, OnboardingBaseActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_questionnaire) {
            Intent intent = new Intent(this, QuestionnaireActivity.class);
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
