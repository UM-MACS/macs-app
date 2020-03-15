package com.example.project1.exercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project1.R;
import com.example.project1.exercise.adapter.ExerciseListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ExerciseListActivity extends AppCompatActivity {

    private Button btnRandom, btnExerciseStart;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private LinearLayoutManager layoutManager;
    private Intent intentToExercise;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    //exercise related variables
    private String exerciseType;
    //default exercise list
    private ArrayList<String> exerciseList = new ArrayList<>();
    private ArrayList<String> exerciseTimeList = new ArrayList<>();
    //exercise level one
    private ArrayList<String> exerciseList1 = new ArrayList<>(Arrays.asList("Shoulder Shrug", "Seated Ladder Climb", "Seated Russian Twist", "Sit to Stand",
            "Seated Bent over Row", "Toe Lift", "Wall Push Up", "Oblique Squeeze"));
    private ArrayList<String> exerciseTimeList1 = new ArrayList<>(Arrays.asList("20s","20s","20s","20s","20s","20s","20s","20s"));
    //exercise level two
    private ArrayList<String> exerciseList2 = new ArrayList<>(Arrays.asList("Seated Bicycle Crunch","Seated Butterfly","Lateral Leg Raise", "Squat with Rotational Press",
            "Wood Cutter", "Empty the Can", "Standing Bicycle Crunch"));
    private ArrayList<String> exerciseTimeList2 = new ArrayList<>(Arrays.asList("30s","30s","30s","30s","30s","30s","30s"));

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
        tvTitle = findViewById(R.id.textview_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_exercise_list);

        //set value to exercise list
        if(exerciseType.contentEquals("exercise-level-one")){
            exerciseList = exerciseList1;
            exerciseTimeList = exerciseTimeList1;
        }
        else{
            exerciseList = exerciseList2;
            exerciseTimeList = exerciseTimeList2;
            tvTitle.setText("Exercise Level 2");
        }

        //adapter declare
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerViewAdapter = new ExerciseListAdapter(this, exerciseList, exerciseTimeList);
        recyclerView.setAdapter(recyclerViewAdapter);



        //overwrites methods elements
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                ArrayList<String> temp1 = new ArrayList<>();
                ArrayList<String> temp2 = new ArrayList<>();
                ArrayList<Integer> temp = new ArrayList<>();
                int i;

                while(true){
                    i = random.nextInt(exerciseList.size());
                    if(temp.size() == exerciseList.size()){
                        break;
                    }
                    if(!temp.contains(i)) {
                        temp1.add(exerciseList.get(i));
                        temp2.add(exerciseTimeList.get(i));
                        temp.add(i);
                    }
                }
                exerciseList.clear();
                exerciseList.addAll(temp1);
                exerciseTimeList.clear();
                exerciseTimeList.addAll(temp2);

                recyclerViewAdapter.notifyDataSetChanged();
            }
        });
        btnExerciseStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("list",exerciseList.toString());
                editor.putString("timelist",exerciseTimeList.toString());
                editor.putString("videoType", exerciseType);
                editor.apply();
                startActivity(intentToExercise);
            }
        });

    }
}
