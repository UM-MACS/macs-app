package com.example.project1.login.component;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.project1.login.LoginActivity;
import com.example.project1.emotionAssessment.EmotionButtonAssessmentActivity;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String TYPE = "TYPE";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String name, String email, String type){

        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.putString(TYPE, type);
        editor.apply();

    }

    public boolean isLogin(){

        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if (!this.isLogin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((EmotionButtonAssessmentActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){

        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(TYPE, sharedPreferences.getString(TYPE, null));

        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }

    public int isFirstTimeUser(){
        SharedPreferences sp = context.getSharedPreferences("FIRST_TIME_USER", PRIVATE_MODE);
        SharedPreferences.Editor ed = sp.edit();

        // 0 is first time, 1 is not first time
        int n = sp.getInt("IS_FIRST_TIME_USER",0);

        if(n == 0){
            editor.putInt("IS_FIRST_TIME_USER", 1);
            editor.apply();
        }
        return n;
    }

    public int isFirstTimeCaregiver(){
        SharedPreferences sp = context.getSharedPreferences("FIRST_TIME_CAREGIVER", PRIVATE_MODE);
        SharedPreferences.Editor ed = sp.edit();

        // 0 is first time, 1 is not first time
        int n = sp.getInt("IS_FIRST_TIME_CAREGIVER",0);

        if(n == 0){
            editor.putInt("IS_FIRST_TIME_CAREGIVER", 1);
            editor.apply();
        }
        return n;
    }

    public int isFirstTimeSpecialist(){
        SharedPreferences sp = context.getSharedPreferences("FIRST_TIME_SPECIALIST", PRIVATE_MODE);
        SharedPreferences.Editor ed = sp.edit();

        // 0 is first time, 1 is not first time
        int n = sp.getInt("IS_FIRST_TIME_SPECIALIST",0);

        if(n == 0){
            editor.putInt("IS_FIRST_TIME_SPECIALIST", 1);
            editor.apply();
        }
        return n;
    }

}