package com.example.project1.userManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.PublicComponent;
import com.example.project1.R;
import com.example.project1.forum.specialist.SpecialistForumActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewUserDetailActivity extends BaseActivity {
    private SessionManager sessionManager;
    private EditText nameET, emailET, ageET, phoneET, nricET;
    private String type, url;
    private RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_detail);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameET = (EditText)findViewById(R.id.name_et);
        emailET = (EditText)findViewById(R.id.email_et);
        ageET = (EditText)findViewById(R.id.age_et);
        phoneET = (EditText)findViewById(R.id.phone_et);
        nricET = (EditText)findViewById(R.id.nric_et);
        container = (RelativeLayout)findViewById(R.id.container);

        Bundle data = this.getIntent().getExtras();
        nameET.setText(data.getString(PublicComponent.NAME));
        emailET.setText(data.getString(PublicComponent.EMAIL));
        ageET.setText(data.getString(PublicComponent.AGE));
        phoneET.setText(data.getString(PublicComponent.CONTACT_NO));
        nricET.setText(data.getString(PublicComponent.NRIC));
        type = data.getString(PublicComponent.TYPE);

        if(type.equals(PublicComponent.PATIENT)) url = PublicComponent.UPDATE_PATIENT_URL;
        else if(type.equals(PublicComponent.CAREGIVER)) url = PublicComponent.UPDATE_CAREGIVER_URL;
        else url = PublicComponent.UPDATE_SPECIALIST_URL;
    }

    public void onUpdateDetails(View view) {
        final String name = nameET.getText().toString().trim();
        final String email = emailET.getText().toString().trim();
        final String phoneNo = phoneET.getText().toString().trim();
        final String age = ageET.getText().toString().trim();
        final String nric = nricET.getText().toString().trim();

        if(name.equals("")||phoneNo.equals("")||age.equals("")){
            Toast.makeText(getApplicationContext(), getString(R.string.empty_details),Toast.LENGTH_SHORT).show();
        } else if (!email.equals("") && !email.contains("@")){
            Toast.makeText(getApplicationContext(), getString(R.string.enter_valid_email),Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(age)>120){
            Toast.makeText(getApplicationContext(), getString(R.string.enter_valid_age),Toast.LENGTH_SHORT).show();
        }
        else {
            container.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                                    container.setVisibility(View.GONE);
                                    ageET.setCursorVisible(false);
                                    nameET.setCursorVisible(false);
                                    emailET.setCursorVisible(false);
                                    phoneET.setCursorVisible(false);
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_SHORT).show();
                                    container.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_SHORT).show();
                                container.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            container.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(PublicComponent.NRIC, nric);
                    params.put(PublicComponent.NAME, name);
                    params.put(PublicComponent.EMAIL, email);
                    params.put(PublicComponent.CONTACT_NO, phoneNo);
                    params.put(PublicComponent.AGE, age);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ViewUserDetailActivity.this, UserListActivity.class);
        startActivity(i);
    }
}
