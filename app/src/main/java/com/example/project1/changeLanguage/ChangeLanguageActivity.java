package com.example.project1.changeLanguage;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.project1.PublicComponent;
import com.example.project1.R;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.chat.ChatChannelListActivity;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.eventReminder.EventReminderActivity;
import com.example.project1.exercise.ExerciseDashboardActivity;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.caregiver.CaregiverForumActivity;
import com.example.project1.forum.specialist.SpecialistForumActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.login.component.SessionManager;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.onboarding.OnboardingBaseActivity;
import com.example.project1.questionnaire.QuestionnaireActivity;
import com.example.project1.userProfile.UserProfileActivity;

import static android.content.pm.PackageManager.GET_META_DATA;


public class ChangeLanguageActivity extends BaseActivity {

    private CheckBox checkBoxEn, checkBoxMs, checkBoxCh;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);

        sessionManager = new SessionManager(this);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


            //Bottom Navigation
            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if(sessionManager.isLogin()) {
            if (CurrentUser.getInstance().getUserType().equals("Admin")) {
                bottomNavigationView.setVisibility(View.GONE);
            }
            if (CurrentUser.getInstance().getUserType().equals("Caregiver") ||
                    CurrentUser.getInstance().getUserType().equals("Specialist")) {
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
                            Intent i2 = new Intent(ChangeLanguageActivity.this, EmotionAssessmentActivity.class);
                            startActivity(i2);
                            break;
                        case R.id.navigation_exercise:
                            Intent i3 = new Intent(ChangeLanguageActivity.this, ExerciseDashboardActivity.class);
                            startActivity(i3);
                            break;
                        case R.id.navigation_chat:
                            Intent i7 = new Intent(ChangeLanguageActivity.this, ChatChannelListActivity.class);
                            startActivity(i7);
                            break;
//                    Intent i4 = new Intent(ForumActivity.this, QuestionnaireListActivity.class);
//                        startActivity(i4);
//                        break;
//                    case R.id.navigation_faq:
//                        Intent i5 = new Intent(ForumActivity.this, FAQActivity.class);
//                        startActivity(i5);
//                        break;
                        case R.id.navigation_forum:
                            if (CurrentUser.getInstance().getUserType().equalsIgnoreCase(PublicComponent.SPECIALIST)
                                    || CurrentUser.getInstance().getUserType().equalsIgnoreCase(PublicComponent.ADMIN)) {
                                Intent i6 = new Intent(ChangeLanguageActivity.this, SpecialistForumActivity.class);
                                startActivity(i6);
                                break;
                            } else if(CurrentUser.getInstance().getUserType().equalsIgnoreCase(PublicComponent.CAREGIVER)){
                                Intent i6 = new Intent(ChangeLanguageActivity.this, CaregiverForumActivity.class);
                                startActivity(i6);
                                break;
                            } else {
                                Intent i6 = new Intent(ChangeLanguageActivity.this, ForumActivity.class);
                                startActivity(i6);
                                break;
                            }
                    }
                    return true;
                }
            });
        } else{
            bottomNavigationView.setVisibility(View.GONE);
        }

        checkBoxEn = (CheckBox)findViewById(R.id.checkbox_en);
        checkBoxMs = (CheckBox)findViewById(R.id.checkbox_ms);
        checkBoxCh = (CheckBox)findViewById(R.id.checkbox_ch);
        String s = sessionManager.getLanguagePref(this);
        if(s.equals("en")){
            checkBoxEn.setChecked(true);
        } else if (s.equals("ms")){
            checkBoxMs.setChecked(true);
        } else{
            checkBoxCh.setChecked(true);
        }

        checkBoxEn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                compoundButton.setChecked(true);
                resetTitles();
                setNewLocale(ChangeLanguageActivity.this, SessionManager.ENGLISH);
            }
        });

        checkBoxMs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                compoundButton.setChecked(true);
                resetTitles();
                setNewLocale(ChangeLanguageActivity.this, SessionManager.MALAY);
            }
        });

        checkBoxCh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                compoundButton.setChecked(true);
                resetTitles();
                setNewLocale(ChangeLanguageActivity.this, SessionManager.CHINESE);
            }
        });

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(SessionManager.setLocale(base));
    }

    protected void resetTitles() {
        try {
            ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), GET_META_DATA);
            if (info.labelRes != 0) {
                setTitle(info.labelRes);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setNewLocale(AppCompatActivity mContext, @SessionManager.LocaleDef String language) {
        SessionManager.setNewLocale(this, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
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
    public void onBackPressed() {
            Intent i = new Intent(this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
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
            Intent intent = new Intent(ChangeLanguageActivity.this, MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            CurrentUser.getInstance().setUserName("");
            CurrentUser.getInstance().setNRIC("");
            CurrentUser.getInstance().setPassword("");
            return true;
        }
        if (id == R.id.action_change_password){
            Intent intent = new Intent(ChangeLanguageActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(ChangeLanguageActivity.this, OnboardingBaseActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(ChangeLanguageActivity.this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_questionnaire){
            Intent intent = new Intent(ChangeLanguageActivity.this, QuestionnaireActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_event_reminder){
            Intent intent = new Intent(ChangeLanguageActivity.this, EventReminderActivity.class);
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
