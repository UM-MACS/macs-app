package com.example.project1.userProfile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.project1.PublicComponent;
import com.example.project1.changeLanguage.ChangeLanguageActivity;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.chat.ChatChannelListActivity;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.eventReminder.EventReminderActivity;
import com.example.project1.exercise.ExerciseDashboardActivity;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.caregiver.CaregiverEditDeletePostActivity;
import com.example.project1.forum.caregiver.CaregiverForumActivity;
import com.example.project1.forum.caregiver.CaregiverViewForumFavouriteListActivity;
import com.example.project1.forum.EditDeleteForumPostActivity;
import com.example.project1.R;
import com.example.project1.forum.ViewForumFavouriteListActivity;
import com.example.project1.forum.specialist.SpecialistEditDeleteForumPostActivity;
import com.example.project1.forum.specialist.SpecialistForumActivity;
import com.example.project1.forum.specialist.SpecialistViewForumFavouriteActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.login.component.SessionManager;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.onboarding.OnboardingBaseActivity;
import com.example.project1.questionnaire.QuestionnaireActivity;

public class UserProfileActivity extends BaseActivity {
    private Button viewMyPosts, viewMyEmotions, viewMyFavourite;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        sessionManager = new SessionManager(this);

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
//        MenuItem itemForum = bottomNavigationView.getMenu().findItem(R.id.navigation_forum);
//        itemForum.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_assessment:
                        Intent i2 = new Intent(UserProfileActivity.this, EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_exercise:
                        Intent i3 = new Intent(UserProfileActivity.this, ExerciseDashboardActivity.class);
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
                            Intent i6 = new Intent(UserProfileActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if(CurrentUser.getInstance().getUserType().equalsIgnoreCase(PublicComponent.CAREGIVER)){
                            Intent i6 = new Intent(UserProfileActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else {
                            Intent i6 = new Intent(UserProfileActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        }

                    case R.id.navigation_chat:
                        Intent i7 = new Intent(UserProfileActivity.this, ChatChannelListActivity.class);
                        startActivity(i7);
                        break;

                }
                return true;
            }
        });

        if(CurrentUser.getInstance().getUserType().equals("Admin")){
            Button mydetailsButton = (Button) findViewById(R.id.my_details);
            mydetailsButton.setVisibility(View.GONE);
        }

        viewMyPosts = (Button)findViewById(R.id.view_my_posts_button);
        viewMyFavourite = (Button)findViewById(R.id.view_my_favourite_button);

        viewMyPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentUser.getInstance().getUserType().equalsIgnoreCase("Caregiver")) {
                    Intent i = new Intent(UserProfileActivity.this, CaregiverEditDeletePostActivity.class);
                    startActivity(i);
                } else if (CurrentUser.getInstance().getUserType().equalsIgnoreCase("Patient")){
                    Intent i = new Intent(UserProfileActivity.this, EditDeleteForumPostActivity.class);
                    startActivity(i);
                } else {
                    //specialist and admin
                    Intent i = new Intent(UserProfileActivity.this, SpecialistEditDeleteForumPostActivity.class);
                    startActivity(i);
                }
            }
        });

        viewMyFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentUser.getInstance().getUserType().equalsIgnoreCase("Caregiver")) {
                    Intent i = new Intent(UserProfileActivity.this, CaregiverViewForumFavouriteListActivity.class);
                    startActivity(i);
                } else if (CurrentUser.getInstance().getUserType().equalsIgnoreCase("Patient")){
                    Intent i = new Intent(UserProfileActivity.this, ViewForumFavouriteListActivity.class);
                    startActivity(i);
                } else {
                    //specialist and admin
                    Intent i = new Intent(UserProfileActivity.this, SpecialistViewForumFavouriteActivity.class);
                    startActivity(i);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(sessionManager.isLogin()) {
            if (CurrentUser.getInstance().getUserType().equals(PublicComponent.PATIENT)) {
                getMenuInflater().inflate(R.menu.nav, menu);
                return true;
            } else if (CurrentUser.getInstance().getUserType().equals(PublicComponent.ADMIN)){
                getMenuInflater().inflate(R.menu.admin_nav, menu);
                return true;
            } else {
                getMenuInflater().inflate(R.menu.other_users_nav, menu);
                return true;
            }
        } return true;
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
            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            CurrentUser.getInstance().setUserName("");
            CurrentUser.getInstance().setNRIC("");
            CurrentUser.getInstance().setUserType("");
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(UserProfileActivity.this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(UserProfileActivity.this, OnboardingBaseActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_questionnaire){
            Intent intent = new Intent(UserProfileActivity.this, QuestionnaireActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_event_reminder){
            Intent intent = new Intent(UserProfileActivity.this, EventReminderActivity.class);
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

    public void onNavMyDetails(View view) {
        Intent i = new Intent (this, UserDetailsActivity.class);
        startActivity(i);
    }
}
