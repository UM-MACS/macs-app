package com.example.project1;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class eventActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SessionManager sessionManager;

    //vars
    private ArrayList<String> mNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event);
        Log.d(TAG, "onCreate: star.");

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        initImageBitmaps();

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) findViewById(R.id.navigation);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.nagivation_event_assessment);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(eventActivity.this,
                                emotionActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(eventActivity.this,
                                viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(eventActivity.this,
                                eventActivity.class);
                        startActivity(i4);
                        break;
                    case R.id.navigation_chat:
                        Intent i=getPackageManager().getLaunchIntentForPackage("com.example.fypchat");
                        startActivity(i);
                        break;
                    case R.id.navigation_forum:
                        if(User.getInstance().getUserType().equalsIgnoreCase("Caregiver")){
                            Intent i6 = new Intent(eventActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if(User.getInstance().getUserType().equalsIgnoreCase("Patient")){
                            Intent i6 = new Intent(eventActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        } else{
                            Intent i6 = new Intent(eventActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        }


                }
                return true;
            }
        });
    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mNames.add("Event 1");

        mNames.add("Event 2");

        mNames.add("Event 3");

        mNames.add("Event 4");

        mNames.add("Event 5");

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        RecyclerViewAdapter2 adapter2 = new RecyclerViewAdapter2(this, mNames);
        recyclerView.setAdapter(adapter2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
            Intent intent = new Intent(eventActivity.this,MainActivity.class);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setUserType("");
//            finish();
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(eventActivity.this,ChangePassword.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(eventActivity.this,UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(eventActivity.this, FAQ.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
