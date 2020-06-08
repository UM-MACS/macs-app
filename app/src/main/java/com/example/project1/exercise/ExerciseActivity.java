package com.example.project1.exercise;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
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
import com.example.project1.changeLanguage.ChangeLanguageActivity;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.specialist.SpecialistForumActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.SessionManager;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.onboarding.OnboardingBaseActivity;
import com.example.project1.questionnaire.QuestionnaireActivity;
import com.example.project1.userProfile.UserProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseActivity extends BaseActivity implements TextureView.SurfaceTextureListener, MediaPlayer.OnVideoSizeChangedListener{

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
    private String email = CurrentUser.getInstance().getNRIC();
    private String exerciseLevel, feeling, startTime, endTime, time;
    private boolean saveStatus = true;
    private SessionManager sessionManager;

    //exercise related variables
    private String[] currentExerciseList;
//    private ArrayList<Integer> currentExerciseIdList = new ArrayList<>();
    private ArrayList<String> currentExerciseIdList = new ArrayList<>();
    private String[] oriExerciseList1 = {"Shoulder Shrug", "Seated Ladder Climb", "Seated Russian Twist", "Sit to Stand",
            "Seated Bent over Row", "Toe Lift", "Wall Push Up"};
//    private int[] oriExerciseIdList1 = {R.raw.shoulder_shrug, R.raw.seated_ladder_climb, R.raw.seated_russian_twist, R.raw.sit_to_stand,
//            R.raw.seated_bend_over_row, R.raw.toe_lift, R.raw.wall_push_up, R.raw.oblique_squeeze};
//    private String[] oriExerciseList2 = {"Seated Bicycle Crunch", "Seated Butterfly", "Lateral Leg Raise", "Squat with Rotational Press",
//            "Wood Cutter", "Empty the Can", "Standing Bicycle Crunch"};
//    private int[] oriExerciseIdList2 = {R.raw.seated_bicycle_crunch, R.raw.seated_butterfly, R.raw.lateral_leg_raise, R.raw.squat_with_rotational_press,
//            R.raw.wood_cutter, R.raw.empty_the_can, R.raw.standing_bicycle_crunch};

    private String[] oriExerciseList = {"seated_bicycle_crunch.mp4", "seated_butterfly.mp4",
            "lateral_leg_raise.mp4", "squat_with_rotational_press.mp4","wood_cutter.mp4", "empty_the_can.mp4", "standing_bicycle_crunch.mp4"};


    private int exerciseCounter = 0;
    private boolean pause_check;
    private String localhost;
    private static String SESSION_URL, DETAILS_URL;

    private SharedPreferences sharedPreferences;

    TextureView textureView;
    private MediaPlayer mediaPlayer;
    AssetFileDescriptor fileDescriptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if(CurrentUser.getInstance().getUserType().equals("Admin")){
            bottomNavigationView.setVisibility(View.GONE);
        }
        if(CurrentUser.getInstance().getUserType().equals("Caregiver")||
                CurrentUser.getInstance().getUserType().equals("Specialist")){
            MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_exercise);
            item.setVisible(false);
        }
        MenuItem itemForum = bottomNavigationView.getMenu().findItem(R.id.navigation_exercise);
        itemForum.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_assessment:
                        Intent i2 = new Intent(ExerciseActivity.this, EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_exercise:
                        Intent i3 = new Intent(ExerciseActivity.this, ExerciseDashboardActivity.class);
                        startActivity(i3);
                        break;
//                    Intent i4 = new Intent(ForumActivity.this, QuestionnaireListActivity.class);
//                        startActivity(i4);
//                        break;
//                    case R.id.navigation_faq:
//                        Intent i5 = new Intent(ForumActivity.this, FAQActivity.class);
//                        startActivity(i5);
//                        break;
                    case R.id.navigation_forum:
                        if(CurrentUser.getInstance().getUserType().equalsIgnoreCase("Specialist")
                                || CurrentUser.getInstance().getUserType().equalsIgnoreCase("Admin")){
                            Intent i6 = new Intent(ExerciseActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        } else {
                            Intent i6 = new Intent(ExerciseActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        }
                    case R.id.navigation_chat:
//                         startActivity(i);
                }
                return true;
            }
        });

        //define all instant variables
        localhost = getString(R.string.localhost);
        SESSION_URL = localhost+"/postExercise/";
        DETAILS_URL = localhost+"/postExercisedetails";

        sharedPreferences = getSharedPreferences(PublicComponent.EXERCISE_ACCESS,PublicComponent.PRIVATE_MODE);
        exerciseLevel = sharedPreferences.getString(PublicComponent.EXERCISE_TYPE, null);
        String s = sharedPreferences.getString(PublicComponent.EXERCISE_LIST, null);
        String str = s.substring(1, s.length() - 1);
        currentExerciseList = str.split(", ");
        sessionManager = new SessionManager(this);

        //define current list with id
        if (exerciseLevel.contentEquals("exercise-level-one")) {
            for (int i = 0; i < currentExerciseList.length; i++) {
                for (int j = 0; j < oriExerciseList1.length; j++) {
                    if (currentExerciseList[i].contentEquals(oriExerciseList1[j])) {
                        currentExerciseIdList.add(oriExerciseList1[j]);
                        System.out.println("true");
                    }
                }
            }
        } else {
            for (int i = 0; i < currentExerciseList.length; i++) {
                for (int j = 0; j < oriExerciseList.length; j++) {
                    if (currentExerciseList[i].contentEquals(oriExerciseList[j])) {
                        currentExerciseIdList.add(oriExerciseList[j]);
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

//        final VideoView view = (VideoView) findViewById(R.id.video_view);
//        MediaController mc = new MediaController(this);
//        view.setMediaController(mc);
//        playVideo(view);


        textureView = findViewById(R.id.video_view);
        textureView.setSurfaceTextureListener(this);
        mediaPlayer = new MediaPlayer();
        try {
            String path = "standing_bicycle_crunch.mp4";
            Log.d("TAG", "playVideo: path is "+path);
            fileDescriptor = getApplicationContext().getAssets().openFd("standing_bicycle_crunch.mp4");
        } catch (IOException e) {
            Log.e("TAG", "playVideo: "+ e);
        }




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
                    btnStart.setText(R.string.start);
                    TimeBuff += MillisecondTime;
                    pause_check = false;
                    btnReset.setEnabled(true);
                    handler.removeCallbacks(runnable);
                } else {
                    StartTime = SystemClock.uptimeMillis();
                    btnStart.setText(R.string.pause);
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
                            btnEnd.setText(R.string.end);
                        exerciseNameList.add(tvVideoName.getText().toString());
                        durationList.add(time);
                        tvVideoName.setText(currentExerciseList[exerciseCounter]);
                        playVideo();
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
                            btnEnd.setText(R.string.end);
                        exerciseNameList.add(tvVideoName.getText().toString());
                        durationList.add(time);
                        tvVideoName.setText(currentExerciseList[exerciseCounter]);
                        playVideo();
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

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);
        try {
            mediaPlayer.setSurface(surface);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaPlayer.setDataSource(this,Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.standing_bicycle_crunch));
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }
    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    }





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
        btnStart.setText(R.string.pause);
        pause_check = true;
        btnEnd.setEnabled(true);
        btnReset.setEnabled(false);
        handler.postDelayed(runnable, 0);
        btnStart.setEnabled(true);
    }

    //play video
    public void playVideo() {
        System.out.println(exerciseCounter);
        System.out.println(currentExerciseIdList.size());
        try {
            String path = "android.resource://" + getPackageName() + "/" + currentExerciseIdList.get(exerciseCounter);
//            String path = currentExerciseIdList.get(exerciseCounter);
//            String path = "standing_bicycle_crunch.mp4";
            Log.d("TAG", "playVideo: path is "+path);
            fileDescriptor = getAssets().openFd(path);
        } catch (IOException e) {
            Log.e("TAG", "playVideo: "+ e);
        }
    }

    //ask wish to save
    public void saveDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.save_exercise);
        alertDialogBuilder.setMessage(R.string.save_exercise_confirm);
        String yes = "<font color='#228B22'>"+getString(R.string.yes)+"</font>";
        String no = "<font color='#228B22'>"+getString(R.string.no)+"</font>";
        alertDialogBuilder.setPositiveButton(Html.fromHtml(yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        feelingDialog();
                    }
                });
        alertDialogBuilder.setNegativeButton(Html.fromHtml(no),
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
//        alertDialogBuilder.setTitle("Your Feeling");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_feeling, null);

        final ToggleButton b1,b2,b3,b4,b5,b6;
        b1 = dialogView.findViewById(R.id.emotion1);
//        b2 = dialogView.findViewById(R.id.emotion2);
        b3 = dialogView.findViewById(R.id.emotion3);
        b4 = dialogView.findViewById(R.id.emotion4);
//        b5 = dialogView.findViewById(R.id.emotion5);
        b6 = dialogView.findViewById(R.id.emotion6);
//        final ToggleButton[] tbArray = new ToggleButton[]{b1,b2,b3,b4,b5,b6};
        final ToggleButton[] tbArray = new ToggleButton[]{b1,b3,b4,b6};

        final LinearLayout layout1,layout2,layout3,layout4,layout5,layout6;
        layout1 = dialogView.findViewById(R.id.layout1);
//        layout2 = dialogView.findViewById(R.id.layout2);
        layout3 = dialogView.findViewById(R.id.layout3);
        layout4 = dialogView.findViewById(R.id.layout4);
//        layout5 = dialogView.findViewById(R.id.layout5);
        layout6 = dialogView.findViewById(R.id.layout6);


        b1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckedChange(layout1, isChecked);
            }
        });
//        b2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                onCheckedChange(layout2, isChecked);
//            }
//        });
        b3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckedChange(layout3, isChecked);
            }
        });
        b4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckedChange(layout4, isChecked);
            }
        });
//        b5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                onCheckedChange(layout5, isChecked);
//            }
//        });
        b6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckedChange(layout6, isChecked);
            }
        });


        String save = "<font color='#228B22'>"+getString(R.string.save)+"</font>";
        String cancel = "<font color='#228B22'>"+getString(R.string.cancel)+"</font>";
        alertDialogBuilder.setPositiveButton(Html.fromHtml(save),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
//                        int counter = 0, feelingPosition = -1;
//                        for(int i = 0; i < tbArray.length; i++){
//                            if(tbArray[i].isChecked()){
//                                counter++;
//                                feelingPosition = i;
//                            }
//                        }
//                        feeling = getFeeling(feelingPosition);
//
//                        if(counter == 1) {
//                            saveExercise();
//                            finish();
//                        }
//                        else if(counter > 1){
//                            Toast.makeText(getApplicationContext(),"Please select one emotion only!",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        else{
//                            Toast.makeText(getApplicationContext(),"Please select at least one emotion!",
//                                    Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
        alertDialogBuilder.setNegativeButton(Html.fromHtml(cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                        finish();
                    }
                });

        alertDialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
//        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(8);
//        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(8);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter = 0, feelingPosition = -1;
                for(int i = 0; i < tbArray.length; i++){
                    if(tbArray[i].isChecked()){
                        counter++;
                        feelingPosition = i;
                    }
                }
                feeling = getFeeling(feelingPosition);

                if(counter == 1) {
                    alertDialog.dismiss();
                    saveExercise();
                    finish();
                }
                else if(counter > 1){
                    Toast.makeText(getApplicationContext(), getString(R.string.one_emotion_only),
                                        Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), getString(R.string.at_least_one_emotion),
                                        Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                                    Toast.makeText(getApplicationContext(), getString(R.string.save_exercise_fail),
                                        Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), getString(R.string.save_exercise_fail),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), getString(R.string.save_exercise_fail),
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
            Toast.makeText(getApplicationContext(), getString(R.string.save_exercise_success), Toast.LENGTH_SHORT).show();
            //TODO
            //save latest exercise date in sharedpreferences
        }
        else{
            Toast.makeText(getApplicationContext(), getString(R.string.save_exercise_fail), Toast.LENGTH_SHORT).show();
        }
    }

    public void onCheckedChange(LinearLayout layout, Boolean isChecked){
        if(isChecked){
            layout.setBackgroundColor(getResources().getColor(R.color.almond));
        }
        else{
            layout.setBackgroundColor(Color.WHITE);
        }
    }

    public String getFeeling(int i){
        switch(i){
            case 0:
                return "Enjoyment";
            case 1:
                return "Satisfied/Relaxed";
            case 2:
                return "Normal";
            case 3:
                return "Dissatisfaction/Down";
//            case 4:
//                return "Angry";
//            case 5:
//                return "Sad";
            default:
                return "";
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            sessionManager.logout();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            CurrentUser.getInstance().setUserName("");
            CurrentUser.getInstance().setNRIC("");
            CurrentUser.getInstance().setUserType("");
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(this, OnboardingBaseActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_questionnaire) {
            Intent intent = new Intent(this, QuestionnaireActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_switch_language){
            Intent intent = new Intent(this, ChangeLanguageActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
