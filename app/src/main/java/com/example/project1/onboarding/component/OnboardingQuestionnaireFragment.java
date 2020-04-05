package com.example.project1.onboarding.component;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.project1.R;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.login.LoginActivity;
import com.example.project1.login.component.SessionManager;

public class OnboardingQuestionnaireFragment extends Fragment {
    SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();

        View thisFragment = inflater.inflate(R.layout.fragment_onboarding_questionnaire, container, false);
        Button btnDone = (Button) thisFragment.findViewById(R.id.btn_done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EmotionAssessmentActivity.class);
                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("email",sessionManager.getUserDetail().get(sessionManager.EMAIL));
                startActivity(i);
            }
        });

        return thisFragment;
    }
}
