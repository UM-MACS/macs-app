package com.example.project1.exercise;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.PublicComponent;
import com.example.project1.R;
import com.example.project1.login.component.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseActivity extends AppCompatActivity {

    private Button btnStart, btnEnd, btnReset;
    private TextView tvStopwatchName, tvVideoName;
    private Handler handler;

    //time related variables
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    private int Seconds, Minutes, MilliSeconds;

    //record related variables
    private List<String> durationList = new ArrayList<>();
    private List<String> exerciseNameList = new ArrayList<>();
    private int sessionId;
    private String email = User.getInstance().getEmail();
    private String exerciseLevel, feeling, startTime, endTime, time;
    private boolean saveStatus = true;

    //exercise related variables
    private String[] currentExerciseList;
    private ArrayList<Integer> currentExerciseIdList = new ArrayList<>();
    private String[] oriExerciseList1 = {"Shoulder Shrug", "Seated Ladder Climb", "Seated Russian Twist", "Sit to Stand",
            "Seated Bent over Row", "Toe Lift", "Wall Push Up", "Oblique Squeeze"};
    private int[] oriExerciseIdList1 = {R.raw.shoulder_shrug, R.raw.seated_ladder_climb, R.raw.seated_russian_twist, R.raw.sit_to_stand,
            R.raw.seated_bend_over_row, R.raw.toe_lift, R.raw.wall_push_up, R.raw.oblique_squeeze};
    private String[] oriExerciseList2 = {"Seated Bicycle Crunch", "Seated Butterfly", "Lateral Leg Raise", "Squat with Rotational Press",
            "Wood Cutter", "Empty the Can", "Standing Bicycle Crunch"};
    private int[] oriExerciseIdList2 = {R.raw.seated_bicycle_crunch, R.raw.seated_butterfly, R.raw.lateral_leg_raise, R.raw.squat_with_rotational_press,
            R.raw.wood_cutter, R.raw.empty_the_can, R.raw.standing_bicycle_crunch};
    private int exerciseCounter = 0;
    private boolean pause_check;
    private String localhost;
    private static String SESSION_URL, DETAILS_URL;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        //define all instant variables
        localhost = getString(R.string.localhost);
        SESSION_URL = localhost+"/postExercise/";
        DETAILS_URL = localhost+"/postExercisedetails";

        sharedPreferences = getSharedPreferences(PublicComponent.EXERCISE_ACCESS,PublicComponent.PRIVATE_MODE);
        exerciseLevel = sharedPreferences.getString(PublicComponent.EXERCISE_TYPE, null);
        String s = sharedPreferences.getString(PublicComponent.EXERCISE_LIST, null);
        String str = s.substring(1, s.length() - 1);
        currentExerciseList = str.split(", ");

        //define current list with id
        if (exerciseLevel.contentEquals("exercise-level-one")) {
            for (int i = 0; i < currentExerciseList.length; i++) {
                for (int j = 0; j < oriExerciseList1.length; j++) {
                    if (currentExerciseList[i].contentEquals(oriExerciseList1[j])) {
                        currentExerciseIdList.add(oriExerciseIdList1[j]);
                        System.out.println("true");
                    }
                }
            }
        } else {
            for (int i = 0; i < currentExerciseList.length; i++) {
                for (int j = 0; j < oriExerciseList2.length; j++) {
                    if (currentExerciseList[i].contentEquals(oriExerciseList2[j])) {
                        currentExerciseIdList.add(oriExerciseIdList2[j]);
                        System.out.println("false");
                    }
                }
            }
        }

        //define all elements
        handler = new Handler();
        btnStart = (Button) findViewById(R.id.button_start);
        btnEnd = (Button) findViewById(R.id.button_end);
        btnReset = (Button) findViewById(R.id.button_reset);
        tvStopwatchName = (TextView) findViewById(R.id.stopwatch_name);
        tvVideoName = (TextView) findViewById(R.id.video_name);

        final VideoView view = (VideoView) findViewById(R.id.video_view);
        MediaController mc = new MediaController(this);
        view.setMediaController(mc);
        playVideo(view);

        //define start time
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime = df.format(c);

        tvVideoName.setText(currentExerciseList[exerciseCounter]);
        btnEnd.setEnabled(false);
        pause_check = false;

        //overwrites methods elements
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pause_check) {
                    btnStart.setText("Start");
                    TimeBuff += MillisecondTime;
                    pause_check = false;
                    btnReset.setEnabled(true);
                    handler.removeCallbacks(runnable);
                } else {
                    StartTime = SystemClock.uptimeMillis();
                    btnStart.setText("Pause");
                    pause_check = true;
                    btnEnd.setEnabled(true);
                    btnReset.setEnabled(false);
                    handler.postDelayed(runnable, 0);
                }
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Map<String,String> temp = new HashMap<>();
                if (exerciseLevel.contentEquals("exercise level one")) {
                    if (exerciseCounter == 7) {
                        exerciseNameList.add(tvVideoName.getText().toString());
                        durationList.add(time);
                        handler.removeCallbacks(runnable);
                        btnReset.setEnabled(true);
                        btnEnd.setEnabled(false);
                        btnStart.setEnabled(false);
                        //define end time
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        endTime = df.format(c);
                        saveDialog();
                    } else {
                        exerciseCounter++;
                        if (exerciseCounter == 7)
                            btnEnd.setText("END");
                        exerciseNameList.add(tvVideoName.getText().toString());
                        durationList.add(time);
                        tvVideoName.setText(currentExerciseList[exerciseCounter]);
                        playVideo(view);
                        btnNextOnClick();
                    }
                } else {
                    if (exerciseCounter == 6) {
                        exerciseNameList.add(tvVideoName.getText().toString());
                        durationList.add(time);
                        handler.removeCallbacks(runnable);
                        btnReset.setEnabled(true);
                        btnEnd.setEnabled(false);
                        btnStart.setEnabled(false);
                        //define end time
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        endTime = df.format(c);
                        saveDialog();
                    } else {
                        exerciseCounter++;
                        if (exerciseCounter == 6)
                            btnEnd.setText("END");
                        exerciseNameList.add(tvVideoName.getText().toString());
                        durationList.add(time);
                        tvVideoName.setText(currentExerciseList[exerciseCounter]);
                        playVideo(view);
                        btnNextOnClick();
                    }
                }

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MillisecondTime = 0L;
                StartTime = 0L;
                TimeBuff = 0L;
                UpdateTime = 0L;
                Seconds = 0;
                Minutes = 0;
                MilliSeconds = 0;
                tvStopwatchName.setTextColor(getResources().getColor(android.R.color.black));
                tvStopwatchName.setText("00:00:00");
                //
                btnStart.setEnabled(true);
            }
        });
    }

    public Runnable runnable = new Runnable() {
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000) / 10;
            time = ("" + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%02d", MilliSeconds));
            tvStopwatchName.setText(time);
            handler.postDelayed(this, 0);
        }

    };

    //btn next
    public void btnNextOnClick(){
        handler.removeCallbacks(runnable);
        btnReset.setEnabled(true);
        btnEnd.setEnabled(false);
        btnStart.setEnabled(false);

        MillisecondTime = 0L;
        StartTime = 0L;
        TimeBuff = 0L;
        UpdateTime = 0L;
        Seconds = 0;
        Minutes = 0;
        MilliSeconds = 0;
        tvStopwatchName.setTextColor(getResources().getColor(android.R.color.black));
        tvStopwatchName.setText("00:00:00");
        //
        StartTime = SystemClock.uptimeMillis();
        btnStart.setText("Pause");
        pause_check = true;
        btnEnd.setEnabled(true);
        btnReset.setEnabled(false);
        handler.postDelayed(runnable, 0);
        btnStart.setEnabled(true);
    }

    //play video
    public void playVideo(VideoView view) {
        System.out.println(exerciseCounter);
        System.out.println(currentExerciseIdList.size());
        String path = "android.resource://" + getPackageName() + "/" + currentExerciseIdList.get(exerciseCounter);
        view.setVideoURI(Uri.parse(path));
        view.start();
    }

    //ask wish to save
    public void saveDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to save this exercise?");
        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#228B22'>Yes</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        feelingDialog();
                    }
                });
        alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#DC143C'>No</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //ask feeling
    public void feelingDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("What is your feeling after doing this exercise?");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#228B22'>Save</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        feeling = input.getText().toString().trim();
                        saveExercise();
                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#DC143C'>Cancel</font>"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //save exercise session
    public void saveExercise() {
        //SAVE TO DATABASE LOGIC HERE
            StringRequest stringRequest = new StringRequest(Request.Method.POST, SESSION_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                sessionId = jsonObject.getInt("sessionId");
                                if (success.equals("1")) {
                                    saveExerciseDetails();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Save exercise fail!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Save exercise fail!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Save exercise fail!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("exerciseLevel", exerciseLevel);
                    params.put("startTime", startTime);
                    params.put("endTime", endTime);
                    params.put("feeling", feeling);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
    }

    //save exercise details of session
    public void saveExerciseDetails(){
        //SAVE EXERCISE DETAILS TO DATABASE HERE
        for(int i = 0; i < exerciseNameList.size(); i++){
            final int j = i;
            if(!saveStatus){
                break;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DETAILS_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {
                                    saveStatus = true;
                                } else {
                                    saveStatus = false;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                saveStatus = false;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            saveStatus = false;
                        }
                    }) {
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("sessionId", Integer.toString(sessionId));
                    params.put("exerciseName", exerciseNameList.get(j));
                    params.put("duration", durationList.get(j));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        if(saveStatus){
            Toast.makeText(getApplicationContext(), "Save exercise success!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Save exercise fail!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}
