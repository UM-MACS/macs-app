package com.example.project1.exercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.project1.R;

public class ExerciseListActivity extends AppCompatActivity {

    private Button btnRandom, btnExerciseStart;
    private Intent intentToExercise;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String exerciseType;

    private final static String EXERCISE_TYPE = "EXERCISE_TYPE";
    private final static int PRIVATE_MODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        //define all instant variables
        intentToExercise = new Intent(ExerciseListActivity.this, ExerciseActivity.class);
        sharedPreferences = getSharedPreferences(EXERCISE_TYPE,PRIVATE_MODE);
        editor = sharedPreferences.edit();
        exerciseType = sharedPreferences.getString("exercise-type", null);

        //define all elements
        btnRandom = findViewById(R.id.button_random);
        btnExerciseStart = findViewById(R.id.button_exercise_start);

        //overwrites methods elements
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnExerciseStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
