 package com.example.project1.emotionAssessment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.PublicComponent;
import com.example.project1.changeLanguage.ChangeLanguageActivity;
import com.example.project1.chat.ChatChannelListActivity;
import com.example.project1.eventReminder.EventReminderActivity;
import com.example.project1.forum.caregiver.CaregiverForumActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.questionnaire.QuestionnaireActivity;
import com.example.project1.exercise.ExerciseDashboardActivity;
import com.example.project1.faq.FAQActivity;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.R;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.login.component.SessionManager;
import com.example.project1.userProfile.UserProfileActivity;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.specialist.SpecialistForumActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmotionButtonAssessmentActivity extends BaseActivity {
    //    DatabaseHelper db;
    Button b1,b2,b3,b4,b5,b6,submitButton;
    ArrayList<String> arrayList;
    EditText expression;
    FrameLayout frameLayout;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    Button button;
    MyTask task;
    private String localhost;
    private static String URL;
    private static String URL_API_SA;
    Date currentTime;
    private TextView mTextMessage;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(CurrentUser.getInstance().getUserType().equals("Specialist")){
//            Intent i = new Intent(EmotionButtonAssessmentActivity.this,DisplayAnalysisActivity.class);
//            startActivity(i);
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_button_assessment);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        localhost = getString(R.string.localhost);
        URL =localhost+"/emotion";
        URL_API_SA ="https://api.deepai.org/api/sentiment-analysis";
        HashMap<String,String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);
        String mEmail = user.get(sessionManager.NRIC);
        String mType = user.get(sessionManager.TYPE);
        Log.e("TAG", "shared preference name is "+mName );
        CurrentUser.getInstance().setNRIC(mEmail);
        CurrentUser.getInstance().setUserName(mName);
        CurrentUser.getInstance().setUserType(mType);
        task = new MyTask();


        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if(CurrentUser.getInstance().getUserType().equals("Admin")){
            bottomNavigationView.setVisibility(View.GONE);
        }
        if(CurrentUser.getInstance().getUserType().equals("Caregiver")||
                CurrentUser.getInstance().getUserType().equals("Specialist")){
            MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_exercise);
            item.setVisible(false);
        }
//        MenuItem itemForum = bottomNavigationView.getMenu().findItem(R.id.navigation_forum);
//        itemForum.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_assessment:
                        Intent i2 = new Intent(EmotionButtonAssessmentActivity.this, EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_exercise:
                        Intent i3 = new Intent(EmotionButtonAssessmentActivity.this, ExerciseDashboardActivity.class);
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
                            Intent i6 = new Intent(EmotionButtonAssessmentActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if(CurrentUser.getInstance().getUserType().equalsIgnoreCase(PublicComponent.CAREGIVER)){
                            Intent i6 = new Intent(EmotionButtonAssessmentActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else {
                            Intent i6 = new Intent(EmotionButtonAssessmentActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        }
                    case R.id.navigation_chat:
                        Intent i7 = new Intent(EmotionButtonAssessmentActivity.this, ChatChannelListActivity.class);
                        startActivity(i7);
                        break;
                }
                return true;
            }
        });


        //end
//        frameLayout = (FrameLayout) findViewById(R.id.emotion_page);
//        frameLayout.getForeground().setAlpha(0);
        b1 = (Button) findViewById(R.id.emotion1);
        b2 = (Button) findViewById(R.id.emotion2);
        b3 = (Button) findViewById(R.id.emotion3);
        b4 = (Button) findViewById(R.id.emotion4);
        b5 = (Button) findViewById(R.id.emotion5);
        b6 = (Button) findViewById(R.id.emotion6);
        arrayList = new ArrayList<String>();
        expression = (EditText) findViewById(R.id.expression);
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//        db = new DatabaseHelper(this);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                final String date = dateFormat.format(d);
//                sentimentAnalysis(CurrentUser.getInstance().getEmail(), CurrentUser.getInstance().getUserType(), date,
//                        "Very Happy(def)");
                insert(CurrentUser.getInstance().getNRIC(), CurrentUser.getInstance().getUserType(),
                        date,"Happy(def)","Positive");
                task.execute();

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                final String date = dateFormat.format(d);
//                sentimentAnalysis(CurrentUser.getInstance().getEmail(), CurrentUser.getInstance().getUserType(), date,
//                        "Happy(def)");
                insert(CurrentUser.getInstance().getNRIC(), CurrentUser.getInstance().getUserType(),
                        date,"Surprised(def)","Positive");
                task.execute();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                final String date = dateFormat.format(d);
//                sentimentAnalysis(CurrentUser.getInstance().getEmail(), CurrentUser.getInstance().getUserType(), date,
//                        "Smiling(def)");
                insert(CurrentUser.getInstance().getNRIC(), CurrentUser.getInstance().getUserType(),
                        date,"Disgusted(def)","Negative");
                task.execute();
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                final String date = dateFormat.format(d);
//                sentimentAnalysis(CurrentUser.getInstance().getEmail(), CurrentUser.getInstance().getUserType(), date,
//                        "Unhappy(def)");
                insert(CurrentUser.getInstance().getNRIC(), CurrentUser.getInstance().getUserType(),
                        date,"Fear(def)","Neutral");
                task.execute();
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                final String date = dateFormat.format(d);
//                sentimentAnalysis(CurrentUser.getInstance().getEmail(), CurrentUser.getInstance().getUserType(), date,
//                        "Angry(def)");
                insert(CurrentUser.getInstance().getNRIC(), CurrentUser.getInstance().getUserType(),
                        date,"Angry(def)","Negative");
                task.execute();
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                final String date = dateFormat.format(d);
//                sentimentAnalysis(CurrentUser.getInstance().getEmail(), CurrentUser.getInstance().getUserType(), date,
//                        "Sad(def)");
                insert(CurrentUser.getInstance().getNRIC(), CurrentUser.getInstance().getUserType(),
                        date,"Sad(def)","Negative");
                task.execute();
            }
        });

        //submit input in text box
        expression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expression.setCursorVisible(true);
            }
        });
        submitButton = (Button) findViewById(R.id.submitExpressionButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = expression.getText().toString();
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                final String date = dateFormat.format(d);
                if (text.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.enter_something,
                            Toast.LENGTH_LONG).show();
                } else {
                    task.execute();
                    final String input =  text.replaceAll("[^a-zA-Z ]", "").toLowerCase();
                    Pattern re = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)",
                            Pattern.MULTILINE | Pattern.COMMENTS);
                    Matcher reMatcher = re.matcher(input);
                    String splitText="";
                    while (reMatcher.find()) {
                        splitText = (reMatcher.group());
                    }
                    String[] sArray = splitText.split("\\s{2,}");
                    for (int i =0; i<sArray.length; i++){
                        sentimentAnalysis(CurrentUser.getInstance().getNRIC(), CurrentUser.getInstance().getUserType(), date,
                                sArray[i]);
                    }
                }
            }
        });
    }

    private void insert(final String email, final String type, final String date,
                        final String expressionInput,final String output) {
        Log.e("TAG", "expression input: "+expressionInput );
        Log.e("TAG", "date: "+date );
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            Log.e("TAG", "success: "+success );
                            if(success.equals("1")){
                                Log.e("TAG", "success" );
                                AlertDialog diaBox = AskOption();
                                diaBox.show();
                                task.cancel(true);

                            }
                            else if(success.equals("0")){
//                    Toast.makeText(getApplicationContext(),"Fail to submit",
//                            Toast.LENGTH_SHORT).show();
                                expression.setText("");
                                AlertDialog diaBox = alertError();
                                diaBox.show();
                                task.cancel(true);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),"Error, Please Try Again Later",
//                            Toast.LENGTH_SHORT).show();
                            AlertDialog diaBox = alertError();
                            diaBox.show();
                            task.cancel(true);
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),"Error, Please Try Again Later",
//                                Toast.LENGTH_SHORT).show();
                        AlertDialog diaBox = alertError();
                        diaBox.show();
                        task.cancel(true);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("type",type);
                params.put("date",date);
                params.put("expression",expressionInput);
                params.put("output",output);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void sentimentAnalysis(final String email, final String type, final String date,
                                   final String expressionInput) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_API_SA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("TAG", "object " );
                            JSONObject object = new JSONObject(response);
                            JSONArray output = object.getJSONArray("output");
                            Log.e("TAG", "output1 is "+output );
//                            output = output.substring(2,output.length()-2);

                            for (int i =0; i<output.length();i++){
                                insert(email,type,date,expressionInput,output.getString(i));
                                Log.e("TAG", "output2 is "+output.getString(i) );
                            }
//                            updateAnalysis(id,output);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG", "exception e");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("text", expressionInput);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<>();
//                header.put("api-key","369aa822-9f19-4ae2-826f-df963534c2c9");
                header.put("api-key","d92743d6-c2d6-426c-81f1-034b93667aec");
//                header.put("Content-Type","text/plain");
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle(R.string.success)
                .setMessage(R.string.feedback_recorded)
                .setPositiveButton(R.string.return_to, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }

                })
                .create();
        return myQuittingDialogBox;

    }

    private AlertDialog alertError() {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle(R.string.fail)
                .setMessage(R.string.try_later)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }

                })
                .create();
        return myQuittingDialogBox;

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            button.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
//            button.setEnabled(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < 99; i++) {
                    Log.d("log", "Emulating some task.. Step " + i);
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(sessionManager.isLogin()) {
            if (CurrentUser.getInstance().getUserType().equals(PublicComponent.PATIENT)) {
                getMenuInflater().inflate(R.menu.nav, menu);
                return true;
            } else if (CurrentUser.getInstance().getUserType().equals(PublicComponent.ADMIN)){
                getMenuInflater().inflate(R.menu.admin_nav, menu);
                return true;
            } else {
                getMenuInflater().inflate(R.menu.other_users_nav, menu);
                return true;
            }
        } return true;
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
            Intent intent = new Intent(EmotionButtonAssessmentActivity.this, MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            CurrentUser.getInstance().setUserName("");
            CurrentUser.getInstance().setNRIC("");
            CurrentUser.getInstance().setUserType("");
//            finish();
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(EmotionButtonAssessmentActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(EmotionButtonAssessmentActivity.this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(EmotionButtonAssessmentActivity.this, FAQActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_questionnaire){
            Intent intent = new Intent(EmotionButtonAssessmentActivity.this, QuestionnaireActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_event_reminder){
            Intent intent = new Intent(EmotionButtonAssessmentActivity.this, EventReminderActivity.class);
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
