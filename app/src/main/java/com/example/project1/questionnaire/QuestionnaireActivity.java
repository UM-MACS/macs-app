package com.example.project1.questionnaire;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.exercise.ExerciseDashboardActivity;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.R;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.login.component.SessionManager;
import com.example.project1.login.component.User;
import com.example.project1.userProfile.UserProfileActivity;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.faq.FAQActivity;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.caregiver.CaregiverForumActivity;
import com.example.project1.forum.specialist.SpecialistForumActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QuestionnaireActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button b1;
    RadioGroup radio1,radio2,radio3,radio4,radio5,radio6,radio7,radio8;

    private String localhost;
    private static String URL;
    private SessionManager sessionManager;
    private String period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

//        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
//        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
//        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
//        Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
//        Spinner spinner5 = (Spinner) findViewById(R.id.spinner5);
//        Spinner spinner6 = (Spinner) findViewById(R.id.spinner6);
//        Spinner spinner7 = (Spinner) findViewById(R.id.spinner7);
//
//
//        spinner1.setOnItemSelectedListener(this);
//        spinner2.setOnItemSelectedListener(this);
//        spinner3.setOnItemSelectedListener(this);
//        spinner4.setOnItemSelectedListener(this);
//        spinner5.setOnItemSelectedListener(this);
//        spinner6.setOnItemSelectedListener(this);
//        spinner7.setOnItemSelectedListener(this);
//
//        List<String> categories = new ArrayList<String>();
//        categories.add("Very Satisfied");
//        categories.add("Satisfied");
//        categories.add("Neutral");
//        categories.add("Unsatisfied");
//        categories.add("Very Unsatisfied");
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
//
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinner1.setAdapter(dataAdapter);
//        spinner2.setAdapter(dataAdapter);
//        spinner3.setAdapter(dataAdapter);
//        spinner4.setAdapter(dataAdapter);
//        spinner5.setAdapter(dataAdapter);
//        spinner6.setAdapter(dataAdapter);
//        spinner7.setAdapter(dataAdapter);


        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_chat);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_assessment:
                        Intent i2 = new Intent(QuestionnaireActivity.this, EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_exercise:
                        Intent i3 = new Intent(QuestionnaireActivity.this, ExerciseDashboardActivity.class);
                        startActivity(i3);
                        break;
//                    //                        Intent i4 = new Intent(QuestionnaireActivity.this, QuestionnaireListActivity.class);
//                        startActivity(i4);
//                        break;
//                    case R.id.navigation_faq:
//                        Intent i5 = new Intent(QuestionnaireActivity.this,FAQActivity.class);
//                        startActivity(i5);
//                        break;
                    case R.id.navigation_forum:
                        if (User.getInstance().getUserType().equalsIgnoreCase("Caregiver")) {
                            Intent i6 = new Intent(QuestionnaireActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if (User.getInstance().getUserType().equalsIgnoreCase("Patient")) {
                            Intent i6 = new Intent(QuestionnaireActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        } else {
                            Intent i6 = new Intent(QuestionnaireActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        }
                    case R.id.navigation_chat:
                        Intent i = getPackageManager().getLaunchIntentForPackage("com.example.fypchat");
                        startActivity(i);
                        break;
                }
                return true;
            }
        });

        //TODO

        localhost = getString(R.string.localhost);
        URL = localhost + "/postQuestionnaire";
        sessionManager = new SessionManager(this);
        b1 = (Button) findViewById(R.id.button_submit_assessment);
        radio1 = (RadioGroup) findViewById(R.id.radioGroup1);
        radio2 = (RadioGroup) findViewById(R.id.radioGroup2);
        radio3 = (RadioGroup) findViewById(R.id.radioGroup3);
        radio4 = (RadioGroup) findViewById(R.id.radioGroup4);
        radio5 = (RadioGroup) findViewById(R.id.radioGroup5);
        radio6 = (RadioGroup) findViewById(R.id.radioGroup6);
        radio7 = (RadioGroup) findViewById(R.id.radioGroup7);
        radio8 = (RadioGroup) findViewById(R.id.radioGroup8);

//        spinner1 = (Spinner) findViewById(R.id.spinner1);
//        spinner2 = (Spinner) findViewById(R.id.spinner2);
//        spinner3 = (Spinner) findViewById(R.id.spinner3);
//        spinner4 = (Spinner) findViewById(R.id.spinner4);
//        spinner5 = (Spinner) findViewById(R.id.spinner5);
//        spinner6 = (Spinner) findViewById(R.id.spinner6);
//        spinner7 = (Spinner) findViewById(R.id.spinner7);

//        spinner1.setAdapter(null);
//        spinner2.setAdapter(null);
//        spinner3.setAdapter(null);
//        spinner4.setAdapter(null);
//        spinner5.setAdapter(null);
//        spinner6.setAdapter(null);
//        spinner7.setAdapter(null);

        radio1.clearCheck();
        radio2.clearCheck();
        radio3.clearCheck();
        radio4.clearCheck();
        radio5.clearCheck();
        radio6.clearCheck();
        radio7.clearCheck();
        radio8.clearCheck();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check all questions has answer
                if (radio1.getCheckedRadioButtonId() == -1 ||
                        radio2.getCheckedRadioButtonId() == -1 ||
                        radio3.getCheckedRadioButtonId() == -1 ||
                        radio4.getCheckedRadioButtonId() == -1 ||
                        radio5.getCheckedRadioButtonId() == -1 ||
                        radio6.getCheckedRadioButtonId() == -1 ||
                        radio7.getCheckedRadioButtonId() == -1 ||
                        radio8.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please Make Sure Every Question is Answered", Toast.LENGTH_SHORT).show();
                } else {
                    String q1,q2,q3,q4,q5,q6,q7,q8;

                    q1 = ((RadioButton)findViewById(radio1.getCheckedRadioButtonId())).getText().toString();
                    q2 = ((RadioButton)findViewById(radio2.getCheckedRadioButtonId())).getText().toString();
                    q3 = ((RadioButton)findViewById(radio3.getCheckedRadioButtonId())).getText().toString();
                    q4 = ((RadioButton)findViewById(radio4.getCheckedRadioButtonId())).getText().toString();
                    q5 = ((RadioButton)findViewById(radio5.getCheckedRadioButtonId())).getText().toString();
                    q6 = ((RadioButton)findViewById(radio6.getCheckedRadioButtonId())).getText().toString();
                    q7 = ((RadioButton)findViewById(radio7.getCheckedRadioButtonId())).getText().toString();
                    q8 = ((RadioButton)findViewById(radio8.getCheckedRadioButtonId())).getText().toString();

                    Date d = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    final String date = dateFormat.format(d);

                    period = "1";

                    insertAssessment(User.getInstance().getEmail(),date, period,
                            q1,q2,q3,q4,q5,q6,q7,q8);

//                    spinner1.setAdapter(null);
//                    spinner2.setAdapter(null);
//                    spinner3.setAdapter(null);
//                    spinner4.setAdapter(null);
//                    spinner5.setAdapter(null);
//                    spinner6.setAdapter(null);
//                    spinner7.setAdapter(null);

                    radio1.clearCheck();
                    radio2.clearCheck();
                    radio3.clearCheck();
                    radio4.clearCheck();
                    radio5.clearCheck();
                    radio6.clearCheck();
                    radio7.clearCheck();
                    radio8.clearCheck();
                }
            }
        });
    }

        //TODO
        //API call

        private void insertAssessment ( final String email, final String date, final String period, final String text,
        final String text2, final String text3, final String text4, final String text5,
        final String text6, final String text7, final String text8){

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String success = jsonObject.getString("success");
                        Log.e("TAG", "success" + success);
                        if (success.equals("1")) {
                            Toast.makeText(getApplicationContext(), "Your Feedback is Recorded", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Error, Please Try Again Later", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error, Please Try Again Later", Toast.LENGTH_SHORT).show();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error, Please Try Again Later", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("date", date);
                    params.put("period", period);
                    params.put("q1", text);
                    params.put("q2", text2);
                    params.put("q3", text3);
                    params.put("q4", text4);
                    params.put("q5", text5);
                    params.put("q6", text6);
                    params.put("q7", text7);
                    params.put("q8", text8);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.nav, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_logout) {
                sessionManager.logout();
                Intent intent = new Intent(QuestionnaireActivity.this, MainActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                User.getInstance().setUserName("");
                User.getInstance().setEmail("");
                User.getInstance().setUserType("");
                return true;
            }

            if (id == R.id.action_change_password) {
                Intent intent = new Intent(QuestionnaireActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                return true;
            }

            if (id == R.id.action_user_profile) {
                Intent intent = new Intent(QuestionnaireActivity.this, UserProfileActivity.class);
                startActivity(intent);
                return true;
            }

            if (id == R.id.action_faq) {
                Intent intent = new Intent(QuestionnaireActivity.this, FAQActivity.class);
                startActivity(intent);
                return true;
            }

            if(id == R.id.action_questionnaire){
                Intent intent = new Intent(QuestionnaireActivity.this, QuestionnaireActivity.class);
                startActivity(intent);
                return true;
            }

            return super.onOptionsItemSelected(item);
        }


        @Override
        public void onItemSelected (AdapterView < ? > parent, View view,int position, long id){
            String item = parent.getItemAtPosition(position).toString();
//            String spinner1 = parent.getItemAtPosition(position).toString();

        }

        @Override
        public void onNothingSelected (AdapterView < ? > parent){

        }
    }