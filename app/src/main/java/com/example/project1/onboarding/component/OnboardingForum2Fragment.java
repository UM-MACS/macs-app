package com.example.project1.onboarding.component;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project1.R;

public class OnboardingForum2Fragment extends Fragment {
    public OnboardingForum2Fragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View thisFragment = inflater.inflate(R.layout.fragment_startup_forum2, container, false);

        return thisFragment;
    }

}
