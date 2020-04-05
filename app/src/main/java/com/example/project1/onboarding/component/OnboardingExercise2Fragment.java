package com.example.project1.onboarding.component;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project1.R;
import com.example.project1.login.component.SessionManager;

public class OnboardingExercise2Fragment extends Fragment {
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();

        View thisFragment = inflater.inflate(R.layout.fragment_onboarding_exercise2, container, false);

        return thisFragment;
    }
}

