package com.example.project1.userManagement;

import android.os.Bundle;

import com.example.project1.R;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.SessionManager;

public class UserDetailActivity extends BaseActivity {
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        //drawer
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



    }
}
