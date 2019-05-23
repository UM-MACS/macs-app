package com.example.project1;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class emotionActivity extends AppCompatActivity{
    DatabaseHelper db;
    Button b1,b2,b3,b4,b5,b6,submitButton;
    ArrayList<String> arrayList;
    EditText expression;
    FrameLayout frameLayout;
    Date currentTime;
//    ContentValues values;
    private TextView mTextMessage;
//    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_emotion_tracking);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(emotionActivity.this,emotionActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(emotionActivity.this,viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(emotionActivity.this, eventAssessment.class);
                        startActivity(i4);
                        break;
                    case R.id.navigation_faq:
                        Intent i5 = new Intent(emotionActivity.this,FAQ.class);
                        startActivity(i5);
                        break;
                }
                return true;
            }
        });

        //end
        frameLayout = (FrameLayout)findViewById(R.id.emotion_page);
        frameLayout.getForeground().setAlpha(0);
        b1 = (Button) findViewById(R.id.emotion1);
        b2 = (Button) findViewById(R.id.emotion2);
        b3 = (Button) findViewById(R.id.emotion3);
        b4 = (Button) findViewById(R.id.emotion4);
        b5 = (Button) findViewById(R.id.emotion5);
        b6 = (Button) findViewById(R.id.emotion6);
        arrayList = new ArrayList<String>();
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        db = new DatabaseHelper(this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                final String date = c.toString();
                Boolean counter = db.insertEmotion(User.getInstance().getUserType(),User.getInstance().getEmail(),date,"Very Happy(def)");
//                Boolean counter2 = db.setCounter("Moderate",0);
//                Boolean counter3 = db.setCounter("Sad",0);
                if(!counter){
                    Toast.makeText(getApplicationContext(),"Error, Please try again later",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Successfully Recorded",Toast.LENGTH_SHORT).show();
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                final String date = c.toString();
                Boolean counter = db.insertEmotion(User.getInstance().getUserType(),User.getInstance().getEmail(),date,"Happy(def)");
                if(!counter){
                    Toast.makeText(getApplicationContext(),"Error, Please try again later",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Successfully Recorded",Toast.LENGTH_SHORT).show();
                }

            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                final String date = c.toString();
                Boolean counter = db.insertEmotion(User.getInstance().getUserType(),User.getInstance().getEmail(),date,"Smiling(def)");
                if(!counter){
                    Toast.makeText(getApplicationContext(),"Error, Please try again later",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Successfully Recorded",Toast.LENGTH_SHORT).show();
                }

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                final String date = c.toString();
                Boolean counter = db.insertEmotion(User.getInstance().getUserType(),User.getInstance().getEmail(),date,"Unhappy(def)");
                if(!counter){
                    Toast.makeText(getApplicationContext(),"Error, Please try again later",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Successfully Recorded",Toast.LENGTH_SHORT).show();
                }

            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                final String date = c.toString();
                Boolean counter = db.insertEmotion(User.getInstance().getUserType(),User.getInstance().getEmail(),date,"Angry(def)");
                if(!counter){
                    Toast.makeText(getApplicationContext(),"Error, Please try again later",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Successfully Recorded",Toast.LENGTH_SHORT).show();
                }


            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                final String date = c.toString();
                Boolean counter = db.insertEmotion(User.getInstance().getUserType(),User.getInstance().getEmail(),date,"Sad(def)");
//                Boolean counter2 = db.setCounter("Moderate",0);
//                Boolean counter3 = db.setCounter("Sad",0);
                if(!counter){
                    Toast.makeText(getApplicationContext(),"Error, Please try again later",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Successfully Recorded",Toast.LENGTH_SHORT).show();
                }
            }
        });


        expression = (EditText)findViewById(R.id.expression);
        expression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expression.setCursorVisible(true);
            }
        });
        submitButton = (Button)findViewById(R.id.submitExpressionButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = expression.getText().toString();
                Date c = Calendar.getInstance().getTime();
                final String date = c.toString();
                Boolean ins = db.insertEmotion(User.getInstance().getUserType(), User.getInstance().getEmail(),date, text);
                if (ins) {
                    //Create pop up window
                    LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View view = inflater1.inflate(R.layout.pop_up_message, null);
                    // create a focusable PopupWindow with the given layout and correct size
                    final PopupWindow pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    //dim background
                    frameLayout.getForeground().setAlpha(220);
                    ((Button) view.findViewById(R.id.pop_up_button)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pw.dismiss();
                            frameLayout.getForeground().setAlpha(0);
                            expression.setText("");
                        }
                    });
                    pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    pw.setTouchInterceptor(new View.OnTouchListener() {
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                pw.dismiss();
                                return true;
                            }
                            return false;
                        }
                    });
                    pw.setOutsideTouchable(true);
                    // display the pop-up in the center
                    pw.showAtLocation(view, Gravity.CENTER, 0, 0);
                } else {
                    Toast.makeText(getApplicationContext(), "Error! Please Try Again Later", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            Intent intent = new Intent(emotionActivity.this,MainActivity.class);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setPassword("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
