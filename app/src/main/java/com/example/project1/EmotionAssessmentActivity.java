package com.example.project1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class EmotionAssessmentActivity extends AppCompatActivity {

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

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        Log.d("debug", toolbar.toString());
        setSupportActionBar(toolbar);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) findViewById(R.id.navigation);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_emotion_tracking);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(EmotionAssessmentActivity.this,
                                EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(EmotionAssessmentActivity.this,
                                viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(EmotionAssessmentActivity.this,
                                eventActivity.class);
                        startActivity(i4);
                        break;
                    case R.id.navigation_chat:
                        Intent i=getPackageManager().getLaunchIntentForPackage("com.example.fypchat");
                        startActivity(i);
                        break;
                    case R.id.navigation_forum:
                        if(User.getInstance().getUserType().equalsIgnoreCase("Caregiver")){
                            Intent i6 = new Intent(EmotionAssessmentActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if(User.getInstance().getUserType().equalsIgnoreCase("Patient")){
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
        Fragment[] fragments = {
                new FragmentEmoji(),
                new FragmentText(),
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
            Intent i = new Intent(EmotionAssessmentActivity.this,MainActivity.class);
            i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setUserType("");
//            finish();
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(EmotionAssessmentActivity.this,ChangePassword.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(EmotionAssessmentActivity.this,UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(EmotionAssessmentActivity.this, FAQ.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
