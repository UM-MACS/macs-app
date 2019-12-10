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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class FAQ extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SessionManager sessionManager;


    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        sessionManager = new SessionManager(this);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
//        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_faq);
//        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(FAQ.this,EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(FAQ.this,viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(FAQ.this, eventActivity.class);
                        startActivity(i4);
                        break;
//                    case R.id.navigation_faq:
//                        Intent i5 = new Intent(FAQ.this,FAQ.class);
//                        startActivity(i5);
//                        break;
                    case R.id.navigation_forum:
                        if(User.getInstance().getUserType().equalsIgnoreCase("Caregiver")){
                            Intent i6 = new Intent(FAQ.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if(User.getInstance().getUserType().equalsIgnoreCase("Patient")){
                            Intent i6 = new Intent(FAQ.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        } else{
                            Intent i6 = new Intent(FAQ.this, SpecialistForumActivity.class);
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
        //finish

        initImageBitmaps();
    }



    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://media.istockphoto.com/photos/letter-q-3d-metal-isolated-on-white-picture-id675661888?s=2048x2048");
        mNames.add("How do I schedule an appointment?");

        mImageUrls.add("https://media.istockphoto.com/photos/letter-q-3d-metal-isolated-on-white-picture-id675661888?s=2048x2048");
        mNames.add("How can I make changes to an appointment?");

        mImageUrls.add("https://media.istockphoto.com/photos/letter-q-3d-metal-isolated-on-white-picture-id675661888?s=2048x2048");
        mNames.add("Is my privacy secure when I am using this app?");

        mImageUrls.add("https://media.istockphoto.com/photos/letter-q-3d-metal-isolated-on-white-picture-id675661888?s=2048x2048");
        mNames.add("How will this app improve my emotional health?");

        mImageUrls.add("https://media.istockphoto.com/photos/letter-q-3d-metal-isolated-on-white-picture-id675661888?s=2048x2048");
        mNames.add("Question 5");

        mImageUrls.add("https://media.istockphoto.com/photos/letter-q-3d-metal-isolated-on-white-picture-id675661888?s=2048x2048");
        mNames.add("Question 6");

        mImageUrls.add("https://media.istockphoto.com/photos/letter-q-3d-metal-isolated-on-white-picture-id675661888?s=2048x2048");
        mNames.add("Question 7");

        mImageUrls.add("https://media.istockphoto.com/photos/letter-q-3d-metal-isolated-on-white-picture-id675661888?s=2048x2048");
        mNames.add("Question 8");



        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        com.example.project1.RecyclerViewAdapter adapter = new com.example.project1.RecyclerViewAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
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
            Intent intent = new Intent(FAQ.this,MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setUserType("");
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(FAQ.this,ChangePassword.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(FAQ.this,UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(FAQ.this, FAQ.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
