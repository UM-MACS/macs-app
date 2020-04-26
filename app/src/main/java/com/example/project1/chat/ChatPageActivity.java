package com.example.project1.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.project1.PublicComponent;
import com.example.project1.R;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.SessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private String emailTo, chatChannelId, receiverName, receiverType, receiverPic;
    private final String EMAIL_FROM = "emailFrom";
    private final String EMAIL_TO = "emailTo";
    private final String CHAT_CHANNEL_ID = "chatChannelId";
    private final String RECEIVER_NAME = "receiverName";
    private final String RECEIVER_TYPE = "receiverType";
    private final String RECEIVER_PIC = "receiverPic";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        emailTo = getIntent().getExtras().getString(EMAIL_TO);
        chatChannelId = getIntent().getExtras().getString(CHAT_CHANNEL_ID);
        receiverName = getIntent().getExtras().getString(RECEIVER_NAME);
        receiverType = getIntent().getExtras().getString(RECEIVER_TYPE);
        receiverPic = getIntent().getExtras().getString(RECEIVER_PIC);

        sessionManager = new SessionManager(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(PublicComponent.FIREBASE_CHAT_BASE).child(chatChannelId);
        statusReference = databaseReference.child(PublicComponent.FIREBASE_CHAT_CHANNEL_TYPING_STATUS);
        chatHistoryReference = databaseReference.child(PublicComponent.FIREBASE_CHAT_CHANNEL_CHAT_HISTORY);
        sendChatReference = firebaseDatabase.getReference(PublicComponent.FIREBASE_CHAT_UNREAD_BASE).child(emailTo);

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

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,ChatChannelListActivity.class);
        startActivity(i);
    }
}
