package com.example.project1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText e1, e2;
    Button b1,b2;
    TextView t1;
    private TextView mTextMessage;
    private ProgressBar progressBar;
    private static String URL_LOGIN = "http://192.168.0.187:3000/login/";
    private static String URL_LOGIN2 = "http://192.168.0.187/jee/login2.php";
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
                        Intent i1 = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i1);
                        break;
                }
                return true;
            }
        });
        //finish

        db = new DatabaseHelper(this);
        progressBar = (ProgressBar) findViewById(R.id.login_loading);
        e1 = (EditText)findViewById(R.id.login_email);
        e2 = (EditText)findViewById(R.id.login_password);
        b1 = (Button)findViewById(R.id.login_button);
        b2 = (Button)findViewById(R.id.registerButton);
        t1 = (TextView)findViewById(R.id.textLogin);
        if(User.getInstance().getUserType().equals("Patient")){
            t1.setText("Patient Login");
        }
        else{
            t1.setText("Caregiver Login");
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = e1.getText().toString().trim();
                final String password = e2.getText().toString().trim();
                if (User.getInstance().getUserType().equals("Patient")) {
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
                                            Toast.makeText(getApplicationContext(), "Email does not exist", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            b1.setVisibility(View.VISIBLE);
                                        } else if (success.equals("1")) {
//                                            JSONArray jsonArray = jsonObject.getJSONArray("login");
                                            for (int i = 0; i < jsonArray.length(); i++) {
//                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                String jname = jsonObject.getString("name").trim();
                                                String jemail = jsonObject.getString("email").trim();
                                                Toast.makeText(getApplicationContext(), jname + " , success logging in " + jemail, Toast.LENGTH_SHORT).show();
                                                sessionManager.createSession(jname, jemail, "Patient");
                                            }
                                            Intent myIntent = new Intent(LoginActivity.this, emotionActivity.class);
                                            startActivity(myIntent);
                                        } else if (success.equals("0")) {
                                            Toast.makeText(getApplicationContext(), "Password is Incorrect", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            b1.setVisibility(View.VISIBLE);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error, Please Try Again Later", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            b1.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        b1.setVisibility(View.VISIBLE);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", email);
                            params.put("password", password);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                    User.getInstance().setEmail(email);

                }

                else if (User.getInstance().getUserType().equals("Caregiver")) {
                    /* mysql */
                    progressBar.setVisibility(View.VISIBLE);
                    b1.setVisibility(View.GONE);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN2,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String success = jsonObject.getString("success");
                                        String message = jsonObject.getString("message");
                                        Log.e("tag", "success: " + success);
                                        Log.e("tag", "message: " + message);
                                        if (success.equals("-1")) {
                                            Toast.makeText(getApplicationContext(), "Email does not exist", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            b1.setVisibility(View.VISIBLE);
                                        } else if (success.equals("1")) {
                                            JSONArray jsonArray = jsonObject.getJSONArray("login");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                String jname = jsonObject1.getString("name").trim();
                                                String jemail = jsonObject1.getString("email").trim();
                                                Toast.makeText(getApplicationContext(), jname + " , success logging in " + jemail, Toast.LENGTH_SHORT).show();
                                                sessionManager.createSession(jname, jemail, "Caregiver");
                                            }
                                            Intent myIntent = new Intent(LoginActivity.this, emotionActivity.class);
                                            startActivity(myIntent);
                                        } else if (success.equals("0")) {
                                            Toast.makeText(getApplicationContext(), "Password is Incorrect", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            b1.setVisibility(View.VISIBLE);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error, Please Try Again Later", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                            b1.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        b1.setVisibility(View.VISIBLE);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", email);
                            params.put("password", password);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                    User.getInstance().setEmail(email);
                }
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );

            }


        });


        mTextMessage = (TextView) findViewById(R.id.message);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.getInstance().getUserType().equals("Patient")) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                else if (User.getInstance().getUserType().equals("Caregiver")) {
                    Intent intent = new Intent(LoginActivity.this, Register2Activity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
