package com.example.project1.onboarding;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.project1.PublicComponent;
import com.example.project1.R;
import com.example.project1.changeLanguage.ChangeLanguageActivity;
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
import com.example.project1.onboarding.component.OnboardingChatFragment;
import com.example.project1.onboarding.component.OnboardingEmotionFragment;
import com.example.project1.onboarding.component.OnboardingExercise2Fragment;
import com.example.project1.onboarding.component.OnboardingExerciseFragment;
import com.example.project1.onboarding.component.OnboardingForum2Fragment;
import com.example.project1.onboarding.component.OnboardingForumFragment;
import com.example.project1.onboarding.component.OnboardingGeneralFragment;
import com.example.project1.onboarding.component.OnboardingQuestionnaireFragment;
import com.example.project1.questionnaire.QuestionnaireActivity;
import com.example.project1.userProfile.UserProfileActivity;

public class OnboardingBaseActivity extends BaseActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ImageView[] dots;
    private SessionManager sessionManager;
    private Fragment[] fragments;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_base);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        /* if not first time, show nav menu*/
        String isFirstTime;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                isFirstTime= null;
            } else {
                isFirstTime= extras.getString("isFirstTime");
            }
        } else {
            isFirstTime = (String) savedInstanceState.getSerializable("isFirstTime");
        }

        if(isFirstTime == null){
            //drawer
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("FAQ");

            Button btnDone = (Button) findViewById(R.id.btn_done);
            btnDone.setVisibility(View.GONE);

            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
            if(CurrentUser.getInstance().getUserType().equals("Admin")){
                bottomNavigationView.setVisibility(View.GONE);
            }
            if(CurrentUser.getInstance().getUserType().equals("Caregiver")||
                    CurrentUser.getInstance().getUserType().equals("Specialist")){
                MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_exercise);
                item.setVisible(false);
            }
            MenuItem itemForum = bottomNavigationView.getMenu().findItem(R.id.navigation_emotion_assessment);
            itemForum.setChecked(true);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_emotion_assessment:
                            Intent i2 = new Intent(OnboardingBaseActivity.this, EmotionAssessmentActivity.class);
                            startActivity(i2);
                            break;
                        case R.id.navigation_exercise:
                            Intent i3 = new Intent(OnboardingBaseActivity.this, ExerciseDashboardActivity.class);
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
                                Intent i6 = new Intent(OnboardingBaseActivity.this, SpecialistForumActivity.class);
                                startActivity(i6);
                                break;
                            } else if(CurrentUser.getInstance().getUserType().equalsIgnoreCase(PublicComponent.CAREGIVER)){
                                Intent i6 = new Intent(OnboardingBaseActivity.this, CaregiverForumActivity.class);
                                startActivity(i6);
                                break;
                            } else {
                                Intent i6 = new Intent(OnboardingBaseActivity.this, ForumActivity.class);
                                startActivity(i6);
                                break;
                            }
                        case R.id.navigation_chat:
                            Intent i7 = new Intent(OnboardingBaseActivity.this, ChatChannelListActivity.class);
                            startActivity(i7);
                            break;
                    }
                    return true;
                }
            });
        }

        /* End of navigation*/

        viewPager = findViewById(R.id.view_pager);

        if(sessionManager.getUserDetail().get(sessionManager.TYPE).equals(PublicComponent.PATIENT)){
            Fragment[] temp = {
                    new OnboardingGeneralFragment(),
                    new OnboardingEmotionFragment(),
                    new OnboardingChatFragment(),
                    new OnboardingForumFragment(),
                    new OnboardingForum2Fragment(),
                    new OnboardingExerciseFragment(),
                    new OnboardingExercise2Fragment(),
                    new OnboardingQuestionnaireFragment(),
            };
            fragments = temp;
        }
        else if(sessionManager.getUserDetail().get(sessionManager.TYPE).equals(PublicComponent.CAREGIVER)){
            Fragment[] temp = {
                    new OnboardingGeneralFragment(),
                    new OnboardingEmotionFragment(),
                    new OnboardingChatFragment(),
                    new OnboardingForumFragment(),
                    new OnboardingForum2Fragment()
            };
            fragments = temp;
        }
        else if(sessionManager.getUserDetail().get(sessionManager.TYPE).equals(PublicComponent.SPECIALIST)){
            Fragment[] temp = {
                    new OnboardingGeneralFragment(),
                    new OnboardingEmotionFragment(),
                    new OnboardingChatFragment(),
                    new OnboardingForumFragment(),
                    new OnboardingForum2Fragment()
            };
            fragments = temp;
        }
        else{
            Fragment[] temp = {
                    new OnboardingGeneralFragment(),
                    new OnboardingForumFragment(),
                    new OnboardingForum2Fragment()
            };
            fragments = temp;
        }

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


        btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                if(sessionManager.getUserDetail().get(sessionManager.TYPE).equals(PublicComponent.PATIENT))
                    i = new Intent(getApplicationContext(), EmotionAssessmentActivity.class);
                else if(sessionManager.getUserDetail().get(sessionManager.TYPE).equals(PublicComponent.CAREGIVER))
                    i = new Intent(getApplicationContext(), EmotionAssessmentActivity.class);
                else if(sessionManager.getUserDetail().get(sessionManager.TYPE).equals(PublicComponent.SPECIALIST))
                    i = new Intent(getApplicationContext(), SpecialistForumActivity.class);
                else
                    i = new Intent(getApplicationContext(), SpecialistForumActivity.class);

                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("email",sessionManager.getUserDetail().get(sessionManager.NRIC));
                startActivity(i);
            }
        });
    }

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
            else if(position == dots.length - 1){
                btnDone.setVisibility(View.VISIBLE);
                buttonRight.setVisibility(View.GONE);
                buttonLeft.setVisibility(View.VISIBLE);
                }
            else {
                btnDone.setVisibility(View.GONE);
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
            Intent intent = new Intent(OnboardingBaseActivity.this, MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            CurrentUser.getInstance().setUserName("");
            CurrentUser.getInstance().setNRIC("");
            CurrentUser.getInstance().setUserType("");
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(OnboardingBaseActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(OnboardingBaseActivity.this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(OnboardingBaseActivity.this, OnboardingBaseActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_questionnaire){
            Intent intent = new Intent(OnboardingBaseActivity.this, QuestionnaireActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_event_reminder){
            Intent intent = new Intent(OnboardingBaseActivity.this, EventReminderActivity.class);
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
