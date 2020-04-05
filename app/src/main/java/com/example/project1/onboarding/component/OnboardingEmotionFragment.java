package com.example.project1.onboarding.component;

<<<<<<< HEAD
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.project1.R;
import com.example.project1.emotionAssessment.component.EmotionFragment;
import com.example.project1.login.component.SessionManager;
import com.example.project1.login.component.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class OnboardingEmotionFragment extends Fragment {
    public OnboardingEmotionFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View thisFragment = inflater.inflate(R.layout.fragment_startup_emotion, container, false);

        return thisFragment;
    }
}
