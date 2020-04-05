package com.example.project1.onboarding.component;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.project1.R;


public class OnboardingEmotionFragment extends Fragment {
    public OnboardingEmotionFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View thisFragment = inflater.inflate(R.layout.fragment_startup_emotion, container, false);

        return thisFragment;
    }
}
