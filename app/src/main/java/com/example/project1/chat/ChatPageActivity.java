package com.example.project1.chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.PublicComponent;
import com.example.project1.R;
import com.example.project1.chat.component.CurrentChatUser;
import com.example.project1.forum.imageFile.ImgLoader;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.SessionManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatPageActivity extends BaseActivity {

    private Toolbar toolbarChat;
    private CircleImageView civChatProfilePic, btnSendChat;
    private TextView tvChatName, tvChatStatus;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ScrollView scrollViewChat, scrollViewEditText;
    private LinearLayout linearLayoutChatContent;
    private EditText etSendChat;
    private SessionManager sessionManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, statusReference, chatHistoryReference, sendChatReference;
    private String NRICTo, chatChannelId, receiverName, receiverType, receiverPic;
    private String tempPic = "";
    private String tempType = "";
    private final String NRIC_FROM = "NRICFrom";
    private final String NRIC_TO = "NRICTo";
    private final String CHAT_CHANNEL_ID = "chatChannelId";
    private final String RECEIVER_NAME = "receiverName";
    private final String RECEIVER_TYPE = "receiverType";
    private final String RECEIVER_PIC = "receiverPic";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        NRICTo = getIntent().getExtras().getString(NRIC_TO);
        chatChannelId = getIntent().getExtras().getString(CHAT_CHANNEL_ID);
        receiverName = getIntent().getExtras().getString(RECEIVER_NAME);
        receiverType = getIntent().getExtras().getString(RECEIVER_TYPE);
        receiverPic = getIntent().getExtras().getString(RECEIVER_PIC);
        CurrentChatUser.getInstance().setCurrentNRIC(NRICTo);

        //handle pic and type null
        //TODO
        // DONE
        if(receiverType == "null"){
            receiverType = getType(chatChannelId);
            if(receiverPic == "null"){
                receiverPic = getPic(NRICTo, receiverType);
            }
        }

        sessionManager = new SessionManager(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(PublicComponent.FIREBASE_CHAT_BASE).child(chatChannelId);
        statusReference = databaseReference.child(PublicComponent.FIREBASE_CHAT_CHANNEL_TYPING_STATUS);
        chatHistoryReference = databaseReference.child(PublicComponent.FIREBASE_CHAT_CHANNEL_CHAT_HISTORY);
        sendChatReference = firebaseDatabase.getReference(PublicComponent.FIREBASE_CHAT_UNREAD_BASE).child(NRICTo);

        toolbarChat = findViewById(R.id.toolbar_chat);
        civChatProfilePic = findViewById(R.id.civ_chat_profile_pic);
        btnSendChat = findViewById(R.id.btn_send_chat);
        tvChatName = findViewById(R.id.tv_chat_name);
        tvChatStatus = findViewById(R.id.tv_chat_status);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        scrollViewChat = findViewById(R.id.scrollview_chat);
        scrollViewEditText = findViewById(R.id.scrollview_edit_text);
        linearLayoutChatContent = findViewById(R.id.linear_layout_chat_content);
        etSendChat = findViewById(R.id.et_send_chat);

        tvChatName.setText(receiverName);
        //TODO maybe
        tvChatStatus.setText(receiverType);
        setPic(receiverPic,civChatProfilePic);

        chatHistoryReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){

                            String message = ds.child(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE).getValue(String.class);
                            String NRICFrom = ds.child(PublicComponent.FIREBASE_CHAT_HISTORY_NRIC_FROM).getValue(String.class);
                            String time = ds.child(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP).getValue(String.class);

                            if(!NRICFrom.equals(sessionManager.getUserDetail().get("NRIC"))){
                                appendMessage(message,time,1);
                            }
                            else{
                                appendMessage(message,time,2);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );

        chatHistoryReference.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        HashMap<String,String> map = dataSnapshot.getValue(HashMap.class);
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        String message = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE).toString();
                        String NRICFrom = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_NRIC_FROM).toString();
                        String time = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP).toString();

                        if(!NRICFrom.equals(sessionManager.getUserDetail().get("NRIC"))){
                            appendMessage(message,time,1);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );

        btnSendChat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String message = etSendChat.getText().toString().trim();
                        etSendChat.setText("");
                        if(!message.equals("") || !message.equals(" ")){
                            HashMap<String,String> map = new HashMap<>();
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE_TYPE,"text");
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE, message);
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_MEDIA_URL, "");
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_NRIC_FROM, sessionManager.getUserDetail().get("NRIC"));
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_NRIC_TO, NRICTo);
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_CHANNEL_ID, chatChannelId);
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_SENDER_NAME, sessionManager.getUserDetail().get("NAME"));

                            Date d = Calendar.getInstance().getTime();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            final String sentDate = dateFormat.format(d);
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP, sentDate);

                            chatHistoryReference.push().setValue(map);

                            appendMessage(message, sentDate,2);
                        }
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,ChatChannelListActivity.class);
        CurrentChatUser.getInstance().setCurrentNRIC("");
        startActivity(i);
    }

    @Override
    protected void onStop() {
        super.onStop();
        CurrentChatUser.getInstance().setCurrentNRIC("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CurrentChatUser.getInstance().setCurrentNRIC("");
    }

    private void appendMessage(String message, String time, int messagePos){
        TextView tv = new TextView(this);

        SpannableString dateString = new SpannableString(time);
        dateString.setSpan(new RelativeSizeSpan(0.7f), 0, time.length(), 0);
        dateString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, time.length(), 0);

        tv.setText(message + "\n");
        tv.append(dateString);
        tv.setTextColor(Color.parseColor("#000000"));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                6f
        );

        // 1 friend
        if (messagePos == 1) {
            tv.setBackgroundResource(R.drawable.messagebg2);
            lp.gravity = Gravity.LEFT;
            lp.setMargins(15, 0, 0, 5);
        }
        //  2 user
        else {
            tv.setBackgroundResource(R.drawable.messagebg1);
            lp.gravity = Gravity.RIGHT;
            lp.setMargins(0, 0, 15, 5);

        }
        tv.setPadding(30, 10, 30, 10);
        tv.setLayoutParams(lp);
        linearLayoutChatContent.addView(tv);

        scrollViewChat.post(new Runnable() {
            @Override
            public void run() {
                scrollViewChat.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void setPic(final String photo, final CircleImageView view) {
        int loader = R.drawable.ic_user;
        ImgLoader imgLoader = new ImgLoader(getApplicationContext());
        imgLoader.DisplayImage(photo, loader, view);
    }

    public String getPic(final String email, final String type) {
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

    public String getType(final String id) {
        String URL_GET_TYPE = PublicComponent.URL_GET_CHAT_BY_CHAT_CHANNEL_ID;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_TYPE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                String type = jsonObject.getString(RECEIVER_TYPE);
                                tempType = type;
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
                params.put(CHAT_CHANNEL_ID, id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        return tempType;
    }
}
