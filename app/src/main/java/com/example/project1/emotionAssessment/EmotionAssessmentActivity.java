package com.example.project1.emotionAssessment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.project1.changeLanguage.ChangeLanguageActivity;
import com.example.project1.eventReminder.EventReminderActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.onboarding.OnboardingBaseActivity;
import com.example.project1.questionnaire.QuestionnaireActivity;
import com.example.project1.exercise.ExerciseDashboardActivity;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.R;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.emotionAssessment.component.EmotionFragmentText;
import com.example.project1.login.component.SessionManager;
import com.example.project1.userProfile.UserProfileActivity;
import com.example.project1.emotionAssessment.component.EmotionFragment;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.caregiver.CaregiverForumActivity;
import com.example.project1.forum.specialist.SpecialistForumActivity;

public class EmotionAssessmentActivity extends BaseActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ImageView[] dots;
    private SessionManager sessionManager;

    private ViewPager.OnPageChangeListener pageChanged = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageScrollStateChanged(int state) {}

        @Override
        public void onPageSelected(int position) {
            Button buttonRight = (Button) findViewById(R.id.forward_button);
            Button buttonLeft = (Button) findViewById(R.id.backward_button);
            if(position==0){
                buttonLeft.setVisibility(View.GONE);
                buttonRight.setVisibility(View.VISIBLE);
            }
            if (position == dots.length-1){
                buttonRight.setVisibility(View.GONE);
                buttonLeft.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < dots.length; i++) {
                if (i == position)
                    dots[i].setImageResource(R.drawable.active_dot);
                else
                    dots[i].setImageResource(R.drawable.non_active_dot);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_assessment);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        sessionManager.getLanguagePref(this);


        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        Log.d("debug", toolbar.toString());
        setSupportActionBar(toolbar);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) findViewById(R.id.navigation);
        if(CurrentUser.getInstance().getUserType().equals("Caregiver")||
                CurrentUser.getInstance().getUserType().equals("Specialist")){
            MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_exercise);
            item.setVisible(false);
        }
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_emotion_assessment);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_assessment:
                        Intent i2 = new Intent(EmotionAssessmentActivity.this,
                                EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_exercise:
                        Intent i3 = new Intent(EmotionAssessmentActivity.this,
                                ExerciseDashboardActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.navigation_chat:
//                        Intent i=getPackageManager().getLaunchIntentForPackage("com.example.fypchat");
//                        startActivity(i);
                        break;
                    case R.id.navigation_forum:
                        if(CurrentUser.getInstance().getUserType().equalsIgnoreCase("Caregiver")){
                            Intent i6 = new Intent(EmotionAssessmentActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if(CurrentUser.getInstance().getUserType().equalsIgnoreCase("Patient")){
                            Intent i6 = new Intent(EmotionAssessmentActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        } else{
                            Intent i6 = new Intent(EmotionAssessmentActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        }


                }
                return true;
            }
        });


        viewPager = findViewById(R.id.view_pager);
        final Fragment[] fragments = {
                new EmotionFragment(),
                new EmotionFragmentText(),
        };
        pagerAdapter = new ScreenSlidePagerAdapter(
                getSupportFragmentManager(), fragments
        );
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(pageChanged);

        LinearLayout dotList = findViewById(R.id.pageDot);
        dots = new ImageView[fragments.length];

//        ImageView forwardArrow = new ImageView(this);
//        forwardArrow.setImageResource(R.drawable.left_arrow);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//        );
//        layoutParams.setMargins(15, 0, 15, 0);
//        dotList.addView(forwardArrow);

        for (int i = 0; i < fragments.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.non_active_dot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 8, 8, 0);
            dotList.addView(dots[i], params);
        }

        dots[0].setImageResource(R.drawable.active_dot);

//        ImageView backwardArrow = new ImageView(this);
//        backwardArrow.setImageResource(R.drawable.right_arrow);
//        layoutParams.setMargins(8, 0, 8, 0);
//        dotList.addView(backwardArrow);

        final Button buttonRight = (Button) findViewById(R.id.forward_button);
        final Button buttonLeft = (Button) findViewById(R.id.backward_button);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonLeft.setVisibility(View.VISIBLE);
                int i = viewPager.getCurrentItem()+1;
                viewPager.setCurrentItem(i);
                if(i == fragments.length-1){
                    buttonRight.setVisibility(View.GONE);
                }
            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonRight.setVisibility(View.VISIBLE);
                int i = viewPager.getCurrentItem() -1;
                viewPager.setCurrentItem(i);
                if(i == 0){
                    buttonLeft.setVisibility(View.GONE);
                }
            }
        });


    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private Fragment[] fragments;

        public ScreenSlidePagerAdapter(
                FragmentManager fragmentManager,
                Fragment[] fragments
        ) {
            super(fragmentManager);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
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
            Intent i = new Intent(EmotionAssessmentActivity.this, MainActivity.class);
            i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            CurrentUser.getInstance().setUserName("");
            CurrentUser.getInstance().setNRIC("");
            CurrentUser.getInstance().setUserType("");
//            finish();
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(EmotionAssessmentActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(EmotionAssessmentActivity.this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(EmotionAssessmentActivity.this, OnboardingBaseActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_questionnaire){
            Intent intent = new Intent(EmotionAssessmentActivity.this, QuestionnaireActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_event_reminder){
            Intent intent = new Intent(EmotionAssessmentActivity.this, EventReminderActivity.class);
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
