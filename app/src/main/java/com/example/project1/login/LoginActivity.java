package com.example.project1.login;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.PublicComponent;
import com.example.project1.chat.service.DisplayNotificationJobService;
import com.example.project1.chat.service.NotificationService;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.R;
import com.example.project1.login.component.SessionManager;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.forum.specialist.SpecialistForumActivity;
import com.example.project1.onboarding.OnboardingBaseActivity;
import com.example.project1.register.RegisterCaregiverActivity;
import com.example.project1.register.RegisterPatientActivity;
import com.example.project1.register.RegisterSpecialistActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {
//    DatabaseHelper db;
    EditText e1, e2;
    Button b1,b2,b3;
    TextView t1;
    private TextView mTextMessage;
    private ProgressBar progressBar;
    private String localhost;
    private String URL_LOGIN;
    private String URL_LOGIN2;
    private String URL_LOGIN3;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        Intent i1 = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i1);
                        break;
                }
                return true;
            }
        });
        //finish

        localhost = getString(R.string.localhost);
        URL_LOGIN = PublicComponent.URL_LOGIN_PATIENT;
        URL_LOGIN2 = PublicComponent.URL_LOGIN_CAREGIVER;
        URL_LOGIN3 = PublicComponent.URL_LOGIN_SPECIALIST;
//        db = new DatabaseHelper(this);
        progressBar = (ProgressBar) findViewById(R.id.login_loading);
        e1 = (EditText)findViewById(R.id.login_nric);
        e2 = (EditText)findViewById(R.id.login_password);
        b1 = (Button)findViewById(R.id.login_button);
        b2 = (Button)findViewById(R.id.registerButton);
        t1 = (TextView)findViewById(R.id.textLogin);
        b3 = (Button)findViewById(R.id.forgotpwButton);
        if(CurrentUser.getInstance().getUserType().equals("Patient")){
            t1.setText(R.string.patientLogin);
        }
        else if(CurrentUser.getInstance().getUserType().equals("Caregiver")){
            t1.setText(R.string.caregiverLogin);
        } else if(CurrentUser.getInstance().getUserType().equals("Specialist")){
            t1.setText(R.string.specialistLogin);
        } else{
            t1.setText(R.string.adminLogin);
            e1.setHint(R.string.username);
            b2.setVisibility(View.GONE);
            b3.setVisibility(View.GONE);
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nric = e1.getText().toString().trim();
                final String password = e2.getText().toString().trim();
                if (nric.equals("") || password.equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.empty_fields),Toast.LENGTH_SHORT).show();
                } else {
                    if (CurrentUser.getInstance().getUserType().equals("Patient")) {
                        /* mysql */
                        progressBar.setVisibility(View.VISIBLE);
                        b1.setVisibility(View.GONE);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
//                                      JSONObject jsonObject = new JSONObject(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String success = jsonObject.getString("success");
                                            Log.e("tag", "success: " + success);
                                            if (success.equals("-1")) {
                                                Toast.makeText(getApplicationContext(), getString(R.string.nric_not_exist), Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                b1.setVisibility(View.VISIBLE);
                                            } else if (success.equals("1")) {
//                                            JSONArray jsonArray = jsonObject.getJSONArray("login");
                                                for (int i = 0; i < jsonArray.length(); i++) {
//                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                    String jname = jsonObject.getString("name").trim();
                                                    String jnric = jsonObject.getString("nric").trim();
//                                                    Toast.makeText(getApplicationContext(), jname + " , success logging in " + jemail, Toast.LENGTH_SHORT).show();
                                                    sessionManager.createSession(jname, jnric, "Patient");
                                                }
                                                startService(new Intent(LoginActivity.this, NotificationService.class));
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                                    startForegroundService(new Intent(LoginActivity.this, NotificationService.class));
//                                                } else {
//                                                    startService(new Intent(LoginActivity.this, NotificationService.class));
//                                                }
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                    JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//                                                    JobInfo jobInfo = new JobInfo.Builder(333, new ComponentName(LoginActivity.this, DisplayNotificationJobService.class))
//                                                            .setOverrideDeadline(0)
//                                                            .build();
//                                                    jobScheduler.schedule(jobInfo);
//                                                } else {
//                                                    startService(new Intent(LoginActivity.this, NotificationService.class));
//                                                }
                                                Intent i;
                                                if(sessionManager.isFirstTimeUser() == 0){
                                                    i = new Intent(LoginActivity.this, OnboardingBaseActivity.class);
                                                    i.putExtra("isFirstTime","true");
                                                }
                                                else{
                                                    i = new Intent(LoginActivity.this, EmotionAssessmentActivity.class);
                                                }
//                                                Intent i = new Intent(LoginActivity.this, OnboardingBaseActivity.class);
                                                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                i.putExtra("nric",nric);
                                                startActivity(i);
                                            } else if(success.equals("2")){
                                                Intent i = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                                                i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                i.putExtra("nric",nric);
                                                startActivity(i);
                                            } else if (success.equals("0")) {
                                                Toast.makeText(getApplicationContext(), getString(R.string.incorrect_pw), Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                b1.setVisibility(View.VISIBLE);
                                            } else {
                                                Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                b1.setVisibility(View.VISIBLE);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            b1.setVisibility(View.VISIBLE);
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressBar.setVisibility(View.GONE);
                                        b1.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("nric", nric);
                                params.put("password", password);
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);

                        CurrentUser.getInstance().setNRIC(nric);

                    }
                    else if (CurrentUser.getInstance().getUserType().equals("Caregiver")) {
                        /* mysql */
                        progressBar.setVisibility(View.VISIBLE);
                        b1.setVisibility(View.GONE);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN2,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String success = jsonObject.getString("success");
                                            Log.e("tag", "success: " + success);
                                            if (success.equals("-1")) {
                                                Toast.makeText(getApplicationContext(), getString(R.string.nric_not_exist), Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                b1.setVisibility(View.VISIBLE);
                                            } else if (success.equals("1")) {
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    String jname = jsonObject.getString("name").trim();
                                                    String jnric = jsonObject.getString("nric").trim();
//                                                    Toast.makeText(getApplicationContext(), jname + " , success logging in " + jemail, Toast.LENGTH_SHORT).show();
                                                    sessionManager.createSession(jname, jnric, "Caregiver");
                                                    CurrentUser.getInstance().setNRIC(jnric); //email
                                                    CurrentUser.getInstance().setUserName(jname);

                                                }
                                                startService(new Intent(LoginActivity.this, NotificationService.class));
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                                    startForegroundService(new Intent(LoginActivity.this, NotificationService.class));
//                                                } else {
//                                                    startService(new Intent(LoginActivity.this, NotificationService.class));
//                                                }
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                    JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//                                                    JobInfo jobInfo = new JobInfo.Builder(333, new ComponentName(LoginActivity.this, DisplayNotificationJobService.class))
//                                                            .setOverrideDeadline(0)
//                                                            .build();
//                                                    jobScheduler.schedule(jobInfo);
//                                                } else {
//                                                    startService(new Intent(LoginActivity.this, NotificationService.class));
//                                                }
                                                Intent i;
                                                if(sessionManager.isFirstTimeCaregiver() == 0){
                                                    i = new Intent(LoginActivity.this, OnboardingBaseActivity.class);
                                                }
                                                else{
                                                    i = new Intent(LoginActivity.this, EmotionAssessmentActivity.class);
                                                }
//                                                Intent i = new Intent(LoginActivity.this, EmotionAssessmentActivity.class);
                                                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                i.putExtra("nric",nric);
                                                startActivity(i);
                                            } else if(success.equals("2")){
                                                Intent i = new Intent(LoginActivity.this,ResetPasswordActivity.class);
                                                i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                i.putExtra("nric",nric);
                                                startActivity(i);
                                            } else if (success.equals("0")) {
                                                Toast.makeText(getApplicationContext(), getString(R.string.incorrect_pw), Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                b1.setVisibility(View.VISIBLE);
                                            } else {
                                                Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                b1.setVisibility(View.VISIBLE);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            b1.setVisibility(View.VISIBLE);
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressBar.setVisibility(View.GONE);
                                        b1.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("nric", nric);
                                params.put("password", password);
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);

                        CurrentUser.getInstance().setNRIC(nric);
                    }
                    else if (CurrentUser.getInstance().getUserType().equals(PublicComponent.SPECIALIST)
                        && nric.equals("macsadmin")){
                        Toast.makeText(getApplicationContext(), getString(R.string.nric_not_exist), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        b1.setVisibility(View.VISIBLE);
                        }
                    else {
                        progressBar.setVisibility(View.VISIBLE);
                        b1.setVisibility(View.GONE);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN3,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
//                                      JSONObject jsonObject = new JSONObject(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String success = jsonObject.getString("success");
                                            Log.e("tag", "success: " + success);
                                            if (success.equals("-1")) {
                                                Toast.makeText(getApplicationContext(), getString(R.string.nric_not_exist), Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                b1.setVisibility(View.VISIBLE);
                                            } else if (success.equals("1")) {
//                                            JSONArray jsonArray = jsonObject.getJSONArray("login");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    if(nric.equals("macsadmin")){
                                                        Intent intent = new Intent(LoginActivity.this,SpecialistForumActivity.class);
                                                        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                        sessionManager.createSession("admin", "macsadmin", "Admin");
                                                    } else {
                                                        String jname = jsonObject.getString("name").trim();
                                                        String jnric = jsonObject.getString("nric").trim();
                                                        sessionManager.createSession(jname, jnric, "Specialist");
                                                        CurrentUser.getInstance().setNRIC(jnric); //email
                                                        CurrentUser.getInstance().setUserName(jname);
                                                    }
                                                }
                                                startService(new Intent(LoginActivity.this, NotificationService.class));
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                                    startForegroundService(new Intent(LoginActivity.this, NotificationService.class));
//                                                } else {
//                                                    startService(new Intent(LoginActivity.this, NotificationService.class));
//                                                }
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                                    JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//                                                    JobInfo jobInfo = new JobInfo.Builder(333, new ComponentName(LoginActivity.this, DisplayNotificationJobService.class))
//                                                            .setOverrideDeadline(0)
//                                                            .build();
//                                                    jobScheduler.schedule(jobInfo);
//                                                } else {
//                                                    startService(new Intent(LoginActivity.this, NotificationService.class));
//                                                }
                                                Intent i;
                                                if(sessionManager.isFirstTimeSpecialist() == 0){
                                                    i = new Intent(LoginActivity.this, OnboardingBaseActivity.class);
                                                }
                                                else{
                                                    i = new Intent(LoginActivity.this, SpecialistForumActivity.class);
                                                }
//                                                Intent i = new Intent(LoginActivity.this, SpecialistForumActivity.class);
                                                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                i.putExtra("nric",nric);
                                                startActivity(i);
                                            } else if (success.equals("0")) {
                                                Toast.makeText(getApplicationContext(), getString(R.string.incorrect_pw), Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                b1.setVisibility(View.VISIBLE);
                                            } else if(success.equals("2")){
                                                Intent i = new Intent(LoginActivity.this,ResetPasswordActivity.class);
                                                i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                i.putExtra("nric",nric);
                                                startActivity(i);
                                            }
                                            else {
                                                Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                b1.setVisibility(View.VISIBLE);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            b1.setVisibility(View.VISIBLE);
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressBar.setVisibility(View.GONE);
                                        b1.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("nric", nric);
                                params.put("password", password);
                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);

                        CurrentUser.getInstance().setNRIC(nric);
                    }

//                    else{
//                        progressBar.setVisibility(View.VISIBLE);
//                        b1.setVisibility(View.GONE);
//                        if(nric.equals("macsadmin")&&password.equals("abc123")){
//                            Intent i = new Intent(LoginActivity.this,SpecialistForumActivity.class);
//                            i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(i);
//                            sessionManager.createSession("admin", "macsadmin", "Admin");
//                        } else{
//                            Toast.makeText(getApplicationContext(), getString(R.string.wrong_nric_pw)
//                            ,Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                            b1.setVisibility(View.VISIBLE);
//                        }
//                    }
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );

                }

            }
        });

        mTextMessage = (TextView) findViewById(R.id.message);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrentUser.getInstance().getUserType().equals("Patient")) {
                    Intent intent = new Intent(LoginActivity.this, RegisterPatientActivity.class);
                    startActivity(intent);
                }
                else if (CurrentUser.getInstance().getUserType().equals("Caregiver")) {
                    Intent intent = new Intent(LoginActivity.this, RegisterCaregiverActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(LoginActivity.this, RegisterSpecialistActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void onNavForgotPassword(View v){
        Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(i);
    }
}
