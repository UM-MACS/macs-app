package com.example.project1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class ChatActivity extends AppCompatActivity {
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sessionManager = new SessionManager(this);


        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) findViewById(R.id.navigation);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_chat);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(ChatActivity.this,
                                EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(ChatActivity.this,
                                viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(ChatActivity.this,
                                eventAssessment.class);
                        startActivity(i4);
                        break;
//                    case R.id.navigation_faq:
//                        Intent i5 = new Intent(ChatActivity.this, FAQ.class);
//                        startActivity(i5);
//                        break;
                    case R.id.navigation_forum:
                        if (User.getInstance().getUserType().equalsIgnoreCase("Caregiver")) {
                            Intent i6 = new Intent(ChatActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if (User.getInstance().getUserType().equalsIgnoreCase("Patient")) {
                            Intent i6 = new Intent(ChatActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        } else {
                            Intent i6 = new Intent(ChatActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        }
                    case R.id.navigation_chat:
                        Intent i=getPackageManager().getLaunchIntentForPackage("com.example.fypchat");
                        startActivity(i);
                        break;
                }
                return true;
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
            Intent intent = new Intent(ChatActivity.this, MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setUserType("");
            return true;
        }

        if (id == R.id.action_change_password) {
            Intent intent = new Intent(ChatActivity.this, ChangePassword.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_user_profile) {
            Intent intent = new Intent(ChatActivity.this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(ChatActivity.this, FAQ.class);
            startActivity(intent);
            return true;
        }

//        if (id == R.id.action_faq) {
//            Intent intent = new Intent(ChatActivity.this, FAQ.class);
//            startActivity(intent);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
