package com.example.project1.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.project1.R;
import com.example.project1.login.component.SessionManager;
import com.example.project1.onboarding.component.OnboardingEmotionFragment;
import com.example.project1.onboarding.component.OnboardingExerciseFragment;
import com.example.project1.onboarding.component.OnboardingForumFragment;
import com.example.project1.onboarding.component.OnboardingGeneralFragment;
import com.example.project1.onboarding.component.OnboardingQuestionnaireFragment;

public class OnboardingBaseActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private ImageView[] dots;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_base);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        viewPager = findViewById(R.id.view_pager);
        Fragment[] fragments = {
                new OnboardingGeneralFragment(),
                new OnboardingEmotionFragment(),
                new OnboardingForumFragment(),
                new OnboardingExerciseFragment(),
                new OnboardingQuestionnaireFragment(),
        };
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
    }

    private ViewPager.OnPageChangeListener pageChanged = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageScrollStateChanged(int state) {}

        @Override
        public void onPageSelected(int position) {
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
