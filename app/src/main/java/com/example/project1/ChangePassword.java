package com.example.project1;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePassword extends AppCompatActivity {
private EditText o1,c1,c2;
private Button button;
private String t1, t2, t3;
private String localhost;
private static String URL ;
SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        sessionManager = new SessionManager(this);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_schedule_appointment);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(ChangePassword.this, emotionActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(ChangePassword.this, viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(ChangePassword.this, eventAssessment.class);
                        startActivity(i4);
                        break;
                    case R.id.navigation_faq:
                        Intent i5 = new Intent(ChangePassword.this, FAQ.class);
                        startActivity(i5);
                        break;
                }
                return true;
            }
        });

//        db = new DatabaseHelper(this);
        localhost = getString(R.string.localhost);
        URL = localhost+":3000/changepassword";
        o1 = (EditText)findViewById(R.id.old_pw);
        c1 = (EditText)findViewById(R.id.new_pw_1);
        c2 = (EditText)findViewById(R.id.new_pw_2);
        button = (Button)findViewById(R.id.submit_change_password);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "type: "+User.getInstance().getUserType() );
                t1 = o1.getText().toString(); //old pw
                t2 = c1.getText().toString(); //new pw
                t3 = c2.getText().toString();
                if (t2.equals(t3)) {
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST, URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String success = jsonObject.getString("success");
                                            if (success.equals("1")) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Update is Successful!",
                                                        Toast.LENGTH_SHORT).show();
                                            } else if (success.equals("0")) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Error, Please Try Again Later!",
                                                        Toast.LENGTH_SHORT).show();
                                            } else if (success.equals("-1")) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Old Password is wrong",
                                                        Toast.LENGTH_SHORT).show();
                                                o1.setText("");
                                                c1.setText("");
                                                c2.setText("");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(),
                                                "Error, Please Try Again Later!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("email", User.getInstance().getEmail());
                                params.put("type", User.getInstance().getUserType());
                                params.put("password", t1);
                                params.put("newPass", t2);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);
                    }
                else {
                    Toast.makeText(getApplicationContext(),"New Passwords are not same!",Toast.LENGTH_LONG).show();
                    c1.setText("");
                    c2.setText("");
                }
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
            Intent intent = new Intent(ChangePassword.this,MainActivity.class);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setPassword("");
            return true;
        }
        if (id == R.id.action_change_password){
            Intent intent = new Intent(ChangePassword.this,ChangePassword.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
