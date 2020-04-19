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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.eventReminder.EventReminderActivity;
import com.example.project1.exercise.ExerciseDashboardActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.R;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.login.component.SessionManager;
import com.example.project1.login.component.User;
import com.example.project1.onboarding.OnboardingBaseActivity;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QuestionnaireActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private Button btnSubmit, btnPrev, btnNext;
    private RadioGroup radioGroup;
    private TextView tvQuestionNum, tvQuestionMin, tvQuestionMax;
    private int[] questionMinArr = {R.string.questionnaire_q1_min, R.string.questionnaire_q2_min, R.string.questionnaire_q3_min,
            R.string.questionnaire_q4_min, R.string.questionnaire_q5_min, R.string.questionnaire_q6_min, R.string.questionnaire_q7_min,
            R.string.questionnaire_q8_min};
    private int[] questionMaxArr = {R.string.questionnaire_q1_max, R.string.questionnaire_q2_max, R.string.questionnaire_q3_max,
            R.string.questionnaire_q4_max, R.string.questionnaire_q5_max, R.string.questionnaire_q6_max, R.string.questionnaire_q7_max,
            R.string.questionnaire_q8_max};
    private String[] answerArr = new String[8];
    private int[] answerIdArr = new int[8];
    private int currentPage = 1;

    private String localhost;
    private static String URL_POST, URL_GET;
    private SessionManager sessionManager;
    private String period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //TODO

        localhost = getString(R.string.localhost);
        URL_POST = localhost + "/postQuestionnaire";
        URL_GET = localhost + "/getQuestionnaire";
        sessionManager = new SessionManager(this);
        period = "0";
        getQuestionnaire(User.getInstance().getEmail());
        btnSubmit = (Button) findViewById(R.id.button_submit_assessment);
        btnSubmit.setVisibility(View.INVISIBLE);
        btnPrev = (Button) findViewById(R.id.button_prev_assessment);
        btnPrev.setVisibility(View.INVISIBLE);
        btnNext = (Button) findViewById(R.id.button_next_assessment);
        btnNext.setVisibility(View.VISIBLE);
        tvQuestionNum = (TextView) findViewById(R.id.tvQuestionNum);
        tvQuestionMin = (TextView) findViewById(R.id.tvQuestionMin);
        tvQuestionMax = (TextView) findViewById(R.id.tvQuestionMax);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please Make Sure The Question is Answered", Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton btn = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                    String answer = btn.getText().toString();
                    answerArr[currentPage - 1] = answer;
                    answerIdArr[currentPage - 1] = btn.getId();

                    currentPage++;
                    radioGroup.clearCheck();

                    if(answerIdArr[currentPage - 1] != 0 && answerIdArr[currentPage - 1] != -1){
                        RadioButton check = (RadioButton) findViewById(answerIdArr[currentPage - 1]);
                        check.setChecked(true);
                    }

                    tvQuestionNum.setText(Integer.toString(currentPage));
                    tvQuestionMin.setText(questionMinArr[currentPage-1]);
                    tvQuestionMax.setText(questionMaxArr[currentPage-1]);

                    if(currentPage < 8 && currentPage > 1){
                        btnNext.setVisibility(View.VISIBLE);
                        btnPrev.setVisibility(View.VISIBLE);
                        btnSubmit.setVisibility(View.INVISIBLE);
                    }
                    else if(currentPage == 8){
                        btnSubmit.setVisibility(View.VISIBLE);
                        btnPrev.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please Make Sure The Question is Answered", Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton btn = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                    String answer = btn.getText().toString();
                    answerArr[currentPage - 1] = answer;
                    answerIdArr[currentPage - 1] = btn.getId();

                    currentPage--;
                    radioGroup.clearCheck();

                    if(answerIdArr[currentPage - 1] != 0 && answerIdArr[currentPage - 1] != -1){
                        RadioButton check = (RadioButton) findViewById(answerIdArr[currentPage - 1]);
                        check.setChecked(true);
                    }

                    tvQuestionNum.setText(Integer.toString(currentPage));
                    tvQuestionMin.setText(questionMinArr[currentPage-1]);
                    tvQuestionMax.setText(questionMaxArr[currentPage-1]);

                    if(currentPage < 8 && currentPage > 1){
                        btnNext.setVisibility(View.VISIBLE);
                        btnPrev.setVisibility(View.VISIBLE);
                        btnSubmit.setVisibility(View.INVISIBLE);
                    }
                    else if(currentPage == 1){
                        btnSubmit.setVisibility(View.INVISIBLE);
                        btnPrev.setVisibility(View.INVISIBLE);
                        btnNext.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check all questions has answer
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please Make Sure The Question is Answered", Toast.LENGTH_SHORT).show();
                } else {
                    String answer = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                    answerArr[currentPage - 1] = answer;

                    Date d = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    final String date = dateFormat.format(d);

                    period = Integer.toString(Integer.parseInt(period) + 1);

                    insertAssessment(User.getInstance().getEmail(),date, period,answerArr.toString());

                }
            }
        });
    }


        private void insertAssessment ( final String email, final String date, final String period, final String answer){

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String success = jsonObject.getString("success");
                        Log.e("TAG", "success" + success);
                        if (success.equals("1")) {
                            Toast.makeText(getApplicationContext(), "Your Feedback is Recorded", Toast.LENGTH_SHORT).show();
                            Intent i2 = new Intent(QuestionnaireActivity.this, EmotionAssessmentActivity.class);
                            startActivity(i2);
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
                    params.put("answer", answer);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }

    private void getQuestionnaire (final String email){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String success = jsonObject.getString("success");
                    Log.e("TAG", "success" + success);
                    if (success.equals("1")) {
                        period = Integer.toString(jsonArray.length());
                    }
                    else if(success.equals("-1")){
                        period = "0";
                    }
                    else {
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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

        //DO NOT TOUCH
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
                Intent intent = new Intent(QuestionnaireActivity.this, OnboardingBaseActivity.class);
                startActivity(intent);
                return true;
            }

            if(id == R.id.action_questionnaire){
                Intent intent = new Intent(QuestionnaireActivity.this, QuestionnaireActivity.class);
                startActivity(intent);
                return true;
            }

            if(id == R.id.action_event_reminder){
                Intent intent = new Intent(QuestionnaireActivity.this, EventReminderActivity.class);
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