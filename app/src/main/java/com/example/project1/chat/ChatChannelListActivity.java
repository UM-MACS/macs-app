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
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.exercise.ExerciseDashboardActivity;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.imageFile.ImgLoader;
import com.example.project1.forum.specialist.SpecialistForumActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.SessionManager;
import com.example.project1.login.component.User;
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
    private final String EMAIL_FROM = "emailFrom";
    private final String EMAIL_TO = "emailTo";
    private final String CHAT_CHANNEL_ID = "chatChannelId";
    private final String RECEIVER_NAME = "receiverName";
    private final String RECEIVER_TYPE = "receiverType";
    //emailFrom, emailTo, chatChannelId

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_channel_list);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //bottom navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if(User.getInstance().getUserType().equals(PublicComponent.CAREGIVER)||
                User.getInstance().getUserType().equals(PublicComponent.SPECIALIST)){
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
                        if(User.getInstance().getUserType().equalsIgnoreCase(PublicComponent.SPECIALIST)
                                || User.getInstance().getUserType().equalsIgnoreCase(PublicComponent.ADMIN)){
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
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String apiStatus = jsonObject.getString(PublicComponent.API_CALL_STATUS);

                            if(apiStatus.equals("1")){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    final HashMap<String,String> tempMap = new HashMap<>();
                                    tempMap.put(EMAIL_TO,object.getString(EMAIL_TO));
                                    tempMap.put(CHAT_CHANNEL_ID,object.getString(CHAT_CHANNEL_ID));
                                    tempMap.put(RECEIVER_NAME,object.getString(RECEIVER_NAME));
                                    tempMap.put(RECEIVER_TYPE,object.getString(RECEIVER_TYPE));

                                    DatabaseReference tempRef = firebaseDatabase.getReference(PublicComponent.FIREBASE_CHAT_BASE);
                                    tempRef.child(tempMap.get(CHAT_CHANNEL_ID)).orderByChild(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                                    tempMap.put(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP,ds.child(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP).getValue(String.class));
                                                    tempMap.put(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE,ds.child(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE).getValue(String.class));
                                                }
                                                receiverList.add(tempMap);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            throw databaseError.toException();
                                        }
                                    });
                                }

                                //TODO
                                //if list is empty show sth else
                                for(int i = 0; i < receiverList.size(); i++){
                                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View rowView = inflater.inflate(R.layout.field_chat_channel, linearLayoutChatList, false);
                                    linearLayoutChatList.addView(rowView);

                                    HashMap<String,String> tempMap = receiverList.get(i);

                                    CircleImageView civReceiverProfilePic = (CircleImageView) ((View)rowView).findViewById(R.id.civ_receiver_profile_pic);
                                    TextView tvReceiverName = (TextView) ((View) rowView).findViewById(R.id.tv_receiver_name);
                                    TextView tvLastChatTime = (TextView) ((View) rowView).findViewById(R.id.tv_last_chat_time);
                                    TextView tvLastChatMessage = (TextView) ((View) rowView).findViewById(R.id.tv_last_chat_message);
                                    TextView tvChatChannelId = (TextView) ((View) rowView).findViewById(R.id.tv_chat_channel_id);
                                    TextView tvEmailTo = (TextView) ((View) rowView).findViewById(R.id.tv_email_to);

                                    getPic(tempMap.get(EMAIL_TO),tempMap.get(RECEIVER_TYPE),civReceiverProfilePic);
                                    tvReceiverName.setText(tempMap.get(RECEIVER_NAME));
                                    tvLastChatTime.setText(PublicComponent.parseTimestampToString(tempMap.get(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP)));
                                    tvLastChatMessage.setText(tempMap.get(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE));
                                    tvChatChannelId.setText(tempMap.get(CHAT_CHANNEL_ID));
                                    tvEmailTo.setText(tempMap.get(EMAIL_TO));

                                    rowView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //TODO
                                        }
                                    });
                                }
                            }
                            else{
                                progressBarChat.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Error, Please Try Again Later",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e){
                            Log.e("Error", e.toString());
                            Toast.makeText(getApplicationContext(), "Error, Please Try Again Later",
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
                        Toast.makeText(getApplicationContext(), "Error, Please Try Again Later",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Get Pic function from Forum Activity
    public void getPic(final String email, final String type, final CircleImageView view) {
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
                                Log.e("TAG", "pic: " + picture);

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
    }

    //TODO
    public void createNewChat(){

    }
}
