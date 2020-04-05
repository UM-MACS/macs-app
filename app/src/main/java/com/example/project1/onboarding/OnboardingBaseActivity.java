package com.example.project1.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.project1.PublicComponent;
import com.example.project1.R;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.forum.specialist.SpecialistForumActivity;
import com.example.project1.login.component.SessionManager;
import com.example.project1.onboarding.component.OnboardingEmotionFragment;
import com.example.project1.onboarding.component.OnboardingExercise2Fragment;
import com.example.project1.onboarding.component.OnboardingExerciseFragment;
import com.example.project1.onboarding.component.OnboardingForum2Fragment;
import com.example.project1.onboarding.component.OnboardingForumFragment;
import com.example.project1.onboarding.component.OnboardingGeneralFragment;
import com.example.project1.onboarding.component.OnboardingQuestionnaireFragment;

public class OnboardingBaseActivity extends AppCompatActivity {

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

        viewPager = findViewById(R.id.view_pager);

        if(sessionManager.getUserDetail().get(sessionManager.TYPE).equals(PublicComponent.PATIENT)){
            Fragment[] temp = {
                    new OnboardingGeneralFragment(),
                    new OnboardingEmotionFragment(),
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
                    new OnboardingForumFragment(),
                    new OnboardingForum2Fragment()
            };
            fragments = temp;
        }
        else if(sessionManager.getUserDetail().get(sessionManager.TYPE).equals(PublicComponent.SPECIALIST)){
            Fragment[] temp = {
                    new OnboardingGeneralFragment(),
                    new OnboardingEmotionFragment(),
                    new OnboardingForumFragment(),
                    new OnboardingForum2Fragment()
            };
            fragments = temp;
        }
        else{
            fragments = new Fragment[0];
        }

        pagerAdapter = new ScreenSlidePagerAdapter(
                getSupportFragmentManager(), fragments
        );
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(pageChanged);

        LinearLayout dotList = findViewById(R.id.pageDot);
        dots = new ImageView[fragments.length];

        for (int i = 0; i < fragments.length; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageResource(R.drawable.non_active_dot);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotList.addView(dots[i], params);
        }

        dots[0].setImageResource(R.drawable.active_dot);

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
                    i = new Intent(getApplicationContext(), EmotionAssessmentActivity.class);

                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("email",sessionManager.getUserDetail().get(sessionManager.EMAIL));
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
            if(position == dots.length - 1)
                btnDone.setVisibility(View.VISIBLE);
            else
                btnDone.setVisibility(View.GONE);

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

}
