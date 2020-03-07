package com.example.project1.exercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.project1.R;

public class ExerciseDashboardActivity extends AppCompatActivity {

    private Button btnStartExercise1, btnStartExercise2;
    private Intent intentToExerciseList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private final static String EXERCISE_TYPE = "EXERCISE_TYPE";
    private final static int PRIVATE_MODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_dashboard);

        //define all instant variables
        intentToExerciseList = new Intent(ExerciseDashboardActivity.this, ExerciseListActivity.class);
        sharedPreferences = getSharedPreferences(EXERCISE_TYPE,PRIVATE_MODE);
        editor = sharedPreferences.edit();

        //define all elements
        btnStartExercise1 = findViewById(R.id.button_exercise_one);
        btnStartExercise2 = findViewById(R.id.button_exercise_two);

        //overwrites methods elements
        btnStartExercise1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("exercise-type","exercise-level-one");
                editor.apply();
                startActivity(intentToExerciseList);
            }
        });
        btnStartExercise2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("exercise-type","exercise-level-two");
                editor.apply();
                startActivity(intentToExerciseList);
            }
        });
    }

    //PUT DASHBOARD LOGIC
}
