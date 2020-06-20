package com.example.project1.forum.specialist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.project1.PublicComponent;
import com.example.project1.changeLanguage.ChangeLanguageActivity;
import com.example.project1.chat.ChatChannelListActivity;
import com.example.project1.eventReminder.EventReminderActivity;
import com.example.project1.exercise.ExerciseDashboardActivity;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.caregiver.CaregiverForumActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.onboarding.OnboardingBaseActivity;
import com.example.project1.questionnaire.QuestionnaireActivity;
import com.example.project1.R;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.forum.EditDeleteForumPostActivity;
import com.example.project1.forum.caregiver.CaregiverEditDeletePostActivity;
import com.example.project1.login.component.SessionManager;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.userProfile.UserProfileActivity;

public class SpecialistEditDeleteForumPostActivity extends BaseActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialist_edit_delete_forum_post);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if(CurrentUser.getInstance().getUserType().equals("Admin")){
            bottomNavigationView.setVisibility(View.GONE);
        }
        if(CurrentUser.getInstance().getUserType().equals("Caregiver")||
                CurrentUser.getInstance().getUserType().equals("Specialist")){
            MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_exercise);
            item.setVisible(false);
        }
//        MenuItem itemForum = bottomNavigationView.getMenu().findItem(R.id.navigation_forum);
//        itemForum.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_assessment:
                        Intent i2 = new Intent(SpecialistEditDeleteForumPostActivity.this, EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_exercise:
                        Intent i3 = new Intent(SpecialistEditDeleteForumPostActivity.this, ExerciseDashboardActivity.class);
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
                            Intent i6 = new Intent(SpecialistEditDeleteForumPostActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if(CurrentUser.getInstance().getUserType().equalsIgnoreCase(PublicComponent.CAREGIVER)){
                            Intent i6 = new Intent(SpecialistEditDeleteForumPostActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else {
                            Intent i6 = new Intent(SpecialistEditDeleteForumPostActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        }
                    case R.id.navigation_chat:
                        Intent i7 = new Intent(SpecialistEditDeleteForumPostActivity.this, ChatChannelListActivity.class);
                        startActivity(i7);
                        break;
                }
                return true;
            }
        });

    }

    public void onNavMyPostsPatientForum(View v){
        Intent i = new Intent(SpecialistEditDeleteForumPostActivity.this, EditDeleteForumPostActivity.class);
        startActivity(i);
    }

    public void onNavMyPostsCaregiverForum(View v){
        Intent i = new Intent(SpecialistEditDeleteForumPostActivity.this, CaregiverEditDeletePostActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(CurrentUser.getInstance().getUserType().equals("Patient")){
            getMenuInflater().inflate(R.menu.nav, menu);
            return true;
        } else {
            getMenuInflater().inflate(R.menu.other_users_nav, menu);
            return true;
        }
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
            Intent i = new Intent(SpecialistEditDeleteForumPostActivity.this, MainActivity.class);
            i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            CurrentUser.getInstance().setUserName("");
            CurrentUser.getInstance().setNRIC("");
            CurrentUser.getInstance().setUserType("");
//            finish();
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(SpecialistEditDeleteForumPostActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(SpecialistEditDeleteForumPostActivity.this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(SpecialistEditDeleteForumPostActivity.this, OnboardingBaseActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_questionnaire){
            Intent intent = new Intent(SpecialistEditDeleteForumPostActivity.this, QuestionnaireActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_event_reminder){
            Intent intent = new Intent(SpecialistEditDeleteForumPostActivity.this, EventReminderActivity.class);
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
