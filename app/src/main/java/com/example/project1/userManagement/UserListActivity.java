package com.example.project1.userManagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.PublicComponent;
import com.example.project1.R;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.specialist.SpecialistForumActivity;
import com.example.project1.userManagement.adapter.UserListAdapter;
import com.example.project1.userManagement.component.User;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserListActivity extends BaseActivity implements UserListAdapter.EventHandler {
    private SessionManager sessionManager;
    private Button btnPatient, btnCaregiver, btnDoctor;
    private RecyclerView recyclerView;
    private RelativeLayout progressBar;
    private ArrayList<User> patientList, caregiverList, doctorList;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnPatient = findViewById(R.id.btn_patient);
        btnCaregiver = findViewById(R.id.btn_caregiver);
        btnDoctor = findViewById(R.id.btn_doctor);
        recyclerView = findViewById(R.id.recycler_view_user_list);
        progressBar = findViewById(R.id.container);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getAllUser();

        btnPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPatient.setBackgroundColor(getResources().getColor(R.color.dodger_blue));
                btnCaregiver.setBackgroundColor(getResources().getColor(R.color.alice_blue));
                btnDoctor.setBackgroundColor(getResources().getColor(R.color.alice_blue));
                RecyclerView.Adapter adapter = new UserListAdapter(getApplicationContext(),patientList,UserListActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });

        btnCaregiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCaregiver.setBackgroundColor(getResources().getColor(R.color.dodger_blue));
                btnPatient.setBackgroundColor(getResources().getColor(R.color.alice_blue));
                btnDoctor.setBackgroundColor(getResources().getColor(R.color.alice_blue));
                RecyclerView.Adapter adapter = new UserListAdapter(getApplicationContext(),caregiverList,UserListActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });

        btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDoctor.setBackgroundColor(getResources().getColor(R.color.dodger_blue));
                btnCaregiver.setBackgroundColor(getResources().getColor(R.color.alice_blue));
                btnPatient.setBackgroundColor(getResources().getColor(R.color.alice_blue));
                RecyclerView.Adapter adapter = new UserListAdapter(getApplicationContext(),doctorList,UserListActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void getAllUser() {
        progressBar.setVisibility(View.VISIBLE);
        getUsers(PublicComponent.GET_ALL_PATIENT_URL,PublicComponent.PATIENT);
        getUsers(PublicComponent.GET_ALL_CAREGIVER_URL,PublicComponent.CAREGIVER);
        getUsers(PublicComponent.GET_ALL_SPECIALIST_URL,PublicComponent.DOCTOR);
    }

    private synchronized void getUsers(String url, final String type){
        final ArrayList<User> temp = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    User user = new User();
                                    user.setName(object.getString(PublicComponent.NAME));
                                    user.setEmail(object.getString(PublicComponent.EMAIL));
                                    user.setNric(object.getString(PublicComponent.NRIC));
                                    user.setContactNo(object.getString(PublicComponent.CONTACT_NO));
                                    user.setAge(object.getString(PublicComponent.AGE));
                                    user.setId(object.getString(PublicComponent.ID));
                                    user.setType(type);
                                    temp.add(user);
                                }
                                if(type.equals(PublicComponent.PATIENT)) {
                                    patientList = temp;
                                    RecyclerView.Adapter adapter = new UserListAdapter(getApplicationContext(),patientList,UserListActivity.this);
                                    recyclerView.setAdapter(adapter);
                                    progressBar.setVisibility(View.GONE);
                                }
                                else if(type.equals(PublicComponent.CAREGIVER)){
                                    caregiverList = temp;
                                }
                                else{
                                    doctorList = temp;
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), getString(R.string.retrieve_user_fail),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), getString(R.string.retrieve_user_fail),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), getString(R.string.retrieve_user_fail),
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UserListActivity.this, SpecialistForumActivity.class);
        startActivity(i);
    }

    @Override
    public void handle(boolean bool) {
        if(bool){
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            progressBar.setVisibility(View.GONE);
        }
    }
}
