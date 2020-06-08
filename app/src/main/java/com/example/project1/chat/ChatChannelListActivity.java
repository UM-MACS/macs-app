package com.example.project1.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.project1.R;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.eventReminder.EventReminderActivity;
import com.example.project1.exercise.ExerciseDashboardActivity;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.imageFile.ImgLoader;
import com.example.project1.forum.specialist.SpecialistForumActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.SessionManager;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.onboarding.OnboardingBaseActivity;
import com.example.project1.questionnaire.QuestionnaireActivity;
import com.example.project1.userProfile.UserProfileActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatChannelListActivity extends BaseActivity {

    private SessionManager sessionManager;
    private Button btnSearchChat;
    private FloatingActionButton btnCreateNewChat;
    private LinearLayout linearLayoutChatList;
    private EditText etSearchChat;
    private ProgressBar progressBarChat;
    private TextView tvEmptyChat;
    private ArrayList<HashMap<String,String>> receiverList = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String tempPic = "";
    private final String RECEIVER_PIC = "receiverPic";
    private final String NRIC_TO = "NRICTo";
    private final String CHAT_CHANNEL_ID = "chatChannelId";
    private final String RECEIVER_NAME = "receiverName";
    private final String RECEIVER_TYPE = "receiverType";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_channel_list);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //bottom navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if(CurrentUser.getInstance().getUserType().equals(PublicComponent.CAREGIVER)||
                CurrentUser.getInstance().getUserType().equals(PublicComponent.SPECIALIST)){
            MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_exercise);
            item.setVisible(false);
        }
        MenuItem itemChat = bottomNavigationView.getMenu().findItem(R.id.navigation_chat);
        itemChat.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_assessment:
                        Intent i2 = new Intent(ChatChannelListActivity.this, EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_exercise:
                        Intent i3 = new Intent(ChatChannelListActivity.this, ExerciseDashboardActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.navigation_forum:
                        if(CurrentUser.getInstance().getUserType().equalsIgnoreCase(PublicComponent.SPECIALIST)
                                || CurrentUser.getInstance().getUserType().equalsIgnoreCase(PublicComponent.ADMIN)){
                            Intent i6 = new Intent(ChatChannelListActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        } else {
                            Intent i6 = new Intent(ChatChannelListActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        }
                    case R.id.navigation_chat:
//                         startActivity(i);
                }
                return true;
            }
        });

        //firebase database
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(PublicComponent.FIREBASE_CHAT_BASE);

        sessionManager = new SessionManager(this);

        linearLayoutChatList = findViewById(R.id.linear_layout_chat_list);
        etSearchChat = findViewById(R.id.et_search_chat);
        btnSearchChat = findViewById(R.id.btn_search_chat);
        btnCreateNewChat = findViewById(R.id.btn_create_new_chat);
        progressBarChat = findViewById(R.id.progress_bar_chat);
        tvEmptyChat = findViewById(R.id.tv_empty_chat);

        getChatChannelList();

        btnCreateNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewChat();
            }
        });
    }

    //Complete, may need enhance
    public void getChatChannelList(){
        progressBarChat.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PublicComponent.URL_GET_CHAT_CHANNEL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            final JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String apiStatus = jsonObject.getString(PublicComponent.API_CALL_STATUS);
                            Log.e("onResponse: ", jsonArray.toString());

                            if(apiStatus.equals("1")){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    final HashMap<String,String> tempMap = new HashMap<>();
                                    tempMap.put(NRIC_TO,object.getString(NRIC_TO));
                                    tempMap.put(CHAT_CHANNEL_ID,object.getString(CHAT_CHANNEL_ID));
                                    tempMap.put(RECEIVER_NAME,object.getString(RECEIVER_NAME));
                                    tempMap.put(RECEIVER_TYPE,object.getString(RECEIVER_TYPE));

                                    DatabaseReference tempRef = databaseReference;
                                    tempRef.child(tempMap.get(CHAT_CHANNEL_ID)).child(PublicComponent.FIREBASE_CHAT_CHANNEL_CHAT_HISTORY).orderByChild(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.exists()){
                                                for(DataSnapshot ds : dataSnapshot.getChildren()){

                                                    tempMap.put(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP,ds.child(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP).getValue(String.class));
                                                    tempMap.put(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE,ds.child(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE).getValue(String.class));
                                                    break;
                                                }
                                                receiverList.add(tempMap);

                                                Log.e("onResponse: ", "f" + receiverList.size());
                                                Log.e("onResponse: ", "l" + jsonArray.length());
                                                //if list is empty show sth else
                                                if (receiverList.size() > 0) {
                                                    for (int i = receiverList.size() - 1; i < receiverList.size(); i++) {
                                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                        final View rowView = inflater.inflate(R.layout.field_chat_channel, linearLayoutChatList, false);
                                                        linearLayoutChatList.addView(rowView);

                                                        final HashMap<String, String> tempMap = receiverList.get(i);

                                                        CircleImageView civReceiverProfilePic = (CircleImageView) ((View) rowView).findViewById(R.id.civ_receiver_profile_pic);
                                                        TextView tvReceiverName = (TextView) ((View) rowView).findViewById(R.id.tv_receiver_name);
                                                        TextView tvLastChatTime = (TextView) ((View) rowView).findViewById(R.id.tv_last_chat_time);
                                                        TextView tvLastChatMessage = (TextView) ((View) rowView).findViewById(R.id.tv_last_chat_message);
                                                        TextView tvChatChannelId = (TextView) ((View) rowView).findViewById(R.id.tv_chat_channel_id);
                                                        TextView tvNRICTo = (TextView) ((View) rowView).findViewById(R.id.tv_email_to);

                                                        final String temp = getPic(tempMap.get(NRIC_TO), tempMap.get(RECEIVER_TYPE), civReceiverProfilePic);
                                                        tvReceiverName.setText(tempMap.get(RECEIVER_NAME));
                                                        tvLastChatTime.setText(tempMap.get(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP));
                                                        tvLastChatMessage.setText(tempMap.get(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE));
                                                        tvChatChannelId.setText(tempMap.get(CHAT_CHANNEL_ID));
                                                        tvNRICTo.setText(tempMap.get(NRIC_TO));

                                                        rowView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent i = new Intent(ChatChannelListActivity.this, ChatPageActivity.class);
                                                                i.putExtra(NRIC_TO, tempMap.get(NRIC_TO));
                                                                i.putExtra(RECEIVER_NAME, tempMap.get(RECEIVER_NAME));
                                                                i.putExtra(RECEIVER_TYPE, tempMap.get(RECEIVER_TYPE));
                                                                i.putExtra(CHAT_CHANNEL_ID, tempMap.get(CHAT_CHANNEL_ID));
                                                                i.putExtra(RECEIVER_PIC,temp);
                                                                startActivity(i);
                                                            }
                                                        });
                                                    }
                                                }
                                                else{

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            throw databaseError.toException();
                                        }
                                    });
                                }

                                progressBarChat.setVisibility(View.GONE);
                            }
                            else{
                                progressBarChat.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Error1, Please Try Again Later",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e){
                            Log.e("Error", e.toString());
                            Toast.makeText(getApplicationContext(), "Error2, Please Try Again Later",
                                    Toast.LENGTH_SHORT).show();
                            progressBarChat.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBarChat.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Error3, Please Try Again Later",
                                Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("NRICFrom", sessionManager.getUserDetail().get("NRIC"));
                        return params;
                    }
                };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Get Pic function from Forum Activity
    public String getPic(final String email, final String type, final CircleImageView view) {
        String URL_GETPIC;
        if (type.equals(PublicComponent.SPECIALIST)) {
            URL_GETPIC = PublicComponent.URL_SPECIALIST_PIC;
        } else if (type.equals(PublicComponent.CAREGIVER)){
            URL_GETPIC = PublicComponent.URL_CAREGIVER_PIC;
        } else {
            URL_GETPIC = PublicComponent.URL_PATIENT_PIC;
        }
//        Log.e("TAG", "getPic: get pic url" + URL_GETPIC);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETPIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                String picture = jsonObject.getString("photo");
                                tempPic = picture;

                                //load picture example
                                int loader = R.drawable.ic_user;
                                ImgLoader imgLoader = new ImgLoader(getApplicationContext());
                                imgLoader.DisplayImage(picture, loader, view);
                                Log.e("TAG", "success loading photo");
                            }
                        } catch (JSONException e) {
                            Log.e("TAG", "fail to load photo");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "fail to load photo");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        return tempPic;
    }

    public void createNewChat(){
        Intent i = new Intent(ChatChannelListActivity.this, CreateChatChannelActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,ChatChannelListActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(CurrentUser.getInstance().getUserType().equals("Patient")){
            getMenuInflater().inflate(R.menu.nav, menu);
            return true;
        } else {
            getMenuInflater().inflate(R.menu.other_users_nav, menu);
            return true;
        }
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

        if(id == R.id.action_questionnaire){
            Intent intent = new Intent(this, QuestionnaireActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_event_reminder){
            Intent intent = new Intent(this, EventReminderActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}