package com.example.project1.chat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static android.view.View.VISIBLE;

public class ChatPageActivity extends BaseActivity {

    private Toolbar toolbarChat;
    private CircleImageView civChatProfilePic, btnSendChat, btnRecordAudio;
    private TextView tvChatName, tvChatStatus;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ScrollView scrollViewChat, scrollViewEditText;
    private LinearLayout linearLayoutChatContent;
    private EmojiconEditText etSendChat;
    private EmojIconActions emojIconActions;
    private ImageView emoji;
    private View view;
    private SessionManager sessionManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, statusReference, chatHistoryReference, receiverReference;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private ChildEventListener childEventListener;
    private String NRICTo, chatChannelId, receiverName, receiverType, receiverPic;
    private String tempPic = "";
    private String tempType = "";
    private final String NRIC_FROM = "NRICFrom";
    private final String NRIC_TO = "NRICTo";
    private final String CHAT_CHANNEL_ID = "chatChannelId";
    private final String RECEIVER_NAME = "receiverName";
    private final String RECEIVER_TYPE = "receiverType";
    private final String RECEIVER_PIC = "receiverPic";
    private Uri imageUri;
    private ArrayList<Map<String,Object>> list = new ArrayList<>();
    private MediaRecorder mediaRecorder;
    private String mFileName;
    private static final String LOG_TAG = "Record Log";
    private Runnable runnable;
    final int REQUEST_PERMISSION_CODE = 1000;
    private ScrollView scrollview;

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

        sessionManager = new SessionManager(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(PublicComponent.FIREBASE_CHAT_BASE).child(chatChannelId);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        statusReference = databaseReference.child(PublicComponent.FIREBASE_CHAT_CHANNEL_TYPING_STATUS);
        chatHistoryReference = databaseReference.child(PublicComponent.FIREBASE_CHAT_CHANNEL_CHAT_HISTORY);
        receiverReference = firebaseDatabase.getReference(PublicComponent.FIREBASE_NOTIFICATION_BASE).child(NRICTo);
//        receiverReference = firebaseDatabase.getReference(PublicComponent.FIREBASE_NOTIFICATION_BASE).child(CurrentUser.getInstance().getNRIC());

        scrollview = findViewById(R.id.scrollview_chat);
        toolbarChat = findViewById(R.id.toolbar_chat);
        civChatProfilePic = findViewById(R.id.civ_chat_profile_pic);
        btnSendChat = findViewById(R.id.btn_send_chat);
        btnRecordAudio = findViewById(R.id.btn_record_audio);
        tvChatName = findViewById(R.id.tv_chat_name);
        tvChatStatus = findViewById(R.id.tv_chat_status);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        scrollViewChat = findViewById(R.id.scrollview_chat);
        scrollViewEditText = findViewById(R.id.scrollview_edit_text);
        linearLayoutChatContent = findViewById(R.id.linear_layout_chat_content);

        scrollview.scrollTo(0, 0);
        etSendChat = findViewById(R.id.et_send_chat);
        emoji = findViewById(R.id.emoji_icon);
        view = findViewById(R.id.chat_view);
        emojIconActions = new EmojIconActions(this, view, etSendChat, emoji);
        emojIconActions.ShowEmojIcon();

        tvChatName.setText(receiverName);
        //TODO maybe
        tvChatStatus.setText(receiverType);
        getPic(NRICTo, receiverType, civChatProfilePic);

//        chatHistoryReference.addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for(DataSnapshot ds : dataSnapshot.getChildren()){
//
//                            String message = ds.child(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE).getValue(String.class);
//                            String NRICFrom = ds.child(PublicComponent.FIREBASE_CHAT_HISTORY_NRIC_FROM).getValue(String.class);
//                            String time = ds.child(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP).getValue(String.class);
//
//                            if(!NRICFrom.equals(sessionManager.getUserDetail().get("NRIC"))){
//                                appendMessage(message,time,1);
//                            }
//                            else{
//                                appendMessage(message,time,2);
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                }
//        );

        etSendChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length() == 0){
                    btnSendChat.setVisibility(View.GONE);
                    btnRecordAudio.setVisibility(VISIBLE);
                }
                else{
                    btnSendChat.setVisibility(VISIBLE);
                    btnRecordAudio.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        childEventListener = (
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                        HashMap<String,String> map = dataSnapshot.getValue(HashMap.class);
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        map.put("docID",dataSnapshot.getKey());
                        System.out.println(dataSnapshot.getKey());
                        list.add(map);
                        String message = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE).toString();
                        String NRICFrom = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_NRIC_FROM).toString();
                        String time = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP).toString();
                        String seen = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_IS_SEEN).toString();
                        String type = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE_TYPE).toString();
                        if(!NRICFrom.equals(sessionManager.getUserDetail().get("NRIC"))){
//                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_IS_SEEN,PublicComponent.FIREBASE_CHAT_HISTORY_IS_SEEN_TRUE);
//                            chatHistoryReference.child(dataSnapshot.getKey()).setValue(map);
                            appendMessage(message,time,1,type, dataSnapshot.getKey());
                        }
                        else{
                            appendMessage(message,time,2,type, dataSnapshot.getKey());
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        for(int i=0; i<list.size(); i++){
                            Map<String,Object> map = list.get(i);
                            System.out.println();
                            if(map.containsValue(dataSnapshot.getKey())){
                                list.remove(i);
                                break;
                            }
                        }
                        reloadLayout();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                }
        );

        chatHistoryReference.addChildEventListener(childEventListener);

        etSendChat.setOnTouchListener(new OnEditTextRightDrawableTouchListener(etSendChat) {
            @Override
            public void OnEditTextClick() { showKeyboard(); }
            @Override
            public void OnDrawableClick() {
                choosePicture();
            }
        });

        btnRecordAudio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (checkPermissionFromDevice()) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        startRecording();
                        Toast.makeText(getApplicationContext(), "Recording started...", Toast.LENGTH_SHORT).show();
                    }
                    else if(event.getAction() == MotionEvent.ACTION_UP){
                        Toast.makeText(getApplicationContext(), "Recording stopped...", Toast.LENGTH_SHORT).show();
                        stopRecording();
                    }
                }
                else{
                    requestPermission();
                }
                return true;
            }
        });

        btnSendChat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String message = etSendChat.getText().toString().trim();
                        etSendChat.setText("");
                        if(!message.equals("") && !message.isEmpty()){
                            HashMap<String,String> map = new HashMap<>();
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE_TYPE,"text");
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE, message);
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_MEDIA_URL, "");
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_NRIC_FROM, sessionManager.getUserDetail().get("NRIC"));
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_NRIC_TO, NRICTo);
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_CHANNEL_ID, chatChannelId);
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_SENDER_NAME, sessionManager.getUserDetail().get("NAME"));
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_IS_SEEN, PublicComponent.FIREBASE_CHAT_HISTORY_IS_SEEN_FALSE);

                            Date d = Calendar.getInstance().getTime();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            final String sentDate = dateFormat.format(d);
                            map.put(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP, sentDate);

                            chatHistoryReference.push().setValue(map);
                            receiverReference.push().setValue(map);

//                            appendMessage(message, sentDate,2);
                        }
                    }
                }
        );

    }

    @Override
    public void onBackPressed() {
        System.out.println("back");
        Intent i = new Intent(this,ChatChannelListActivity.class);
        chatHistoryReference.removeEventListener(childEventListener);
        CurrentChatUser.getInstance().setCurrentNRIC("");
        startActivity(i);
        finish();
    }

    public void showKeyboard(){
        etSendChat.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etSendChat, InputMethodManager.SHOW_IMPLICIT);
    }

    public void startRecording(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(getPath());
        try{
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public void stopRecording(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        uploadAudio();
    }

    public boolean checkPermissionFromDevice(){
        int write_external_storage_result = ContextCompat.checkSelfPermission(ChatPageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(ChatPageActivity.this, Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public void uploadAudio(){
        final ProgressDialog pd = new ProgressDialog(ChatPageActivity.this);
        pd.setTitle("Uploading Audio...");
        pd.show();
        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("audio/" + randomKey);
        Uri uri = Uri.fromFile(new File(mFileName));
        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Audio Uploaded", Toast.LENGTH_SHORT).show();
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uploadAudioToDatabase(uri);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Percentage: "  + (int)progressPercent+  "%");
                    }
                });
    }

    public void uploadAudioToDatabase(final Uri uri){
        HashMap<String,String> map = new HashMap<>();
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE_TYPE,"audio");
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE, uri.toString());
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_MEDIA_URL, "");
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_NRIC_FROM, sessionManager.getUserDetail().get("NRIC"));
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_NRIC_TO, NRICTo);
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_CHANNEL_ID, chatChannelId);
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_SENDER_NAME, sessionManager.getUserDetail().get("NAME"));
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_IS_SEEN, PublicComponent.FIREBASE_CHAT_HISTORY_IS_SEEN_FALSE);

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String sentDate = dateFormat.format(d);
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP, sentDate);

        chatHistoryReference.push().setValue(map);
        receiverReference.push().setValue(map);
    }


    public String getPath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "macsRecordingFile" + ".mp3");
        mFileName = file.getPath();
        return mFileName;
    }


    private void reloadLayout(){
        linearLayoutChatContent.removeAllViews();
        for(int i=0; i<list.size(); i++){
            Map<String,Object> map = list.get(i);
            String message = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE).toString();
            String NRICFrom = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_NRIC_FROM).toString();
            String time = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP).toString();
            String seen = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_IS_SEEN).toString();
            String type = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE_TYPE).toString();
            String docID = map.get("docID").toString();
            if(!NRICFrom.equals(sessionManager.getUserDetail().get("NRIC"))){
                map.put(PublicComponent.FIREBASE_CHAT_HISTORY_IS_SEEN,PublicComponent.FIREBASE_CHAT_HISTORY_IS_SEEN_TRUE);
                appendMessage(message,time,1,type, docID);
            }
            else{
                appendMessage(message,time,2,type, docID);
            }
        }
    }


    private void appendMessage(String message, String time, int messagePos, String type, String docID){
        if(type.equals("text")){
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
                tv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog diaBox = AskOption(docID);
                        diaBox.show();
                        return false;
                    }
                });

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
        else if(type.equals("image")){
            LinearLayout tv = new LinearLayout(this);
            ImageView imgView = new ImageView(this);
            TextView txtView = new TextView(this);
            tv.setOrientation(LinearLayout.VERTICAL);
            tv.addView(imgView);
            tv.addView(txtView);
            SpannableString dateString = new SpannableString(time);
            dateString.setSpan(new RelativeSizeSpan(0.7f), 0, time.length(), 0);
            dateString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, time.length(), 0);

            txtView.append(dateString);
            txtView.setTextColor(Color.parseColor("#000000"));

            Picasso.get()
                    .load(message)
                    .into(imgView);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    800,
                    800,
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
                tv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog diaBox = AskOption(docID);
                        diaBox.show();
                        return false;
                    }
                });

            }
            final boolean[] isImageFitToScreen = {false};

            tv.setPadding(30, 10, 30, 10);
            tv.setLayoutParams(lp);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent fullScreenIntent = new Intent(ChatPageActivity.this, FullScreenImageActivity.class);
                    fullScreenIntent.setData(Uri.parse(message));
                    startActivity(fullScreenIntent);
                }
            });
            linearLayoutChatContent.addView(tv);

            scrollViewChat.post(new Runnable() {
                @Override
                public void run() {
                    scrollViewChat.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
        
        else if(type.equals("audio")){
            LinearLayout tv = new LinearLayout(this);
            LinearLayout tv2 = new LinearLayout(this);
            TextView txtView = new TextView(this);
            ImageView play = new ImageView(this);
            ImageView pause = new ImageView(this);
            SeekBar mSeelBar = new SeekBar(this);
            play.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
            pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
            pause.setVisibility(View.GONE);

            tv2.setOrientation(LinearLayout.HORIZONTAL);
            tv.setOrientation(LinearLayout.VERTICAL);
            tv2.addView(play);
            tv2.addView(pause);
            tv2.addView(mSeelBar);
            tv.addView(tv2);
            tv.addView(txtView);

            LinearLayout.LayoutParams seelBarLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    6f
            );
            mSeelBar.setLayoutParams(seelBarLp);

            MediaPlayer mediaPlayer = new MediaPlayer();
            Uri uri = Uri.parse(message);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            try{
                mediaPlayer.setDataSource(ChatPageActivity.this, uri);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer.isPlaying()){
                        mSeelBar.setProgress(mediaPlayer.getCurrentPosition());
                        handler.postDelayed(this,50);
                    }
                }
            };

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pause.setVisibility(VISIBLE);
                    play.setVisibility(View.GONE);
                    mediaPlayer.start();
                    mSeelBar.setMax(mediaPlayer.getDuration());
                    handler.postDelayed(runnable,0);
                }
            });
            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play.setVisibility(VISIBLE);
                    pause.setVisibility(View.GONE);
                    mediaPlayer.pause();
                    handler.removeCallbacks(runnable);
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    play.setVisibility(VISIBLE);
                    pause.setVisibility(View.GONE);
                    mediaPlayer.seekTo(0);
                }
            });

            mSeelBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser){
                        mediaPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            SpannableString dateString = new SpannableString(time);
            dateString.setSpan(new RelativeSizeSpan(0.7f), 0, time.length(), 0);
            dateString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, time.length(), 0);



            txtView.append(dateString);
            txtView.setTextColor(Color.parseColor("#000000"));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    600,
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
                tv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog diaBox = AskOption(docID);
                        diaBox.show();
                        return false;
                    }
                });

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
    }



    private AlertDialog AskOption(final String docID) {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle(R.string.delete)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        chatHistoryReference.child(docID).removeValue();
                        dialog.dismiss();
                    }
                })

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    //Get Pic
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

        return tempPic;
    }

    // === Part method for upload image ===
    public void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, 1);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadPicture();
        }
    }

    public void uploadPicture(){
        final ProgressDialog pd = new ProgressDialog(ChatPageActivity.this);
        pd.setTitle("Uploading Image...");
        pd.show();
        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + randomKey);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uploadImageToDatabase(uri);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Percentage: "  + (int)progressPercent+  "%");
                    }
                });
    }

    public void uploadImageToDatabase(final Uri uri){
        HashMap<String,String> map = new HashMap<>();
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE_TYPE,"image");
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE, uri.toString());
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_MEDIA_URL, "");
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_NRIC_FROM, sessionManager.getUserDetail().get("NRIC"));
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_NRIC_TO, NRICTo);
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_CHANNEL_ID, chatChannelId);
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_SENDER_NAME, sessionManager.getUserDetail().get("NAME"));
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_IS_SEEN, PublicComponent.FIREBASE_CHAT_HISTORY_IS_SEEN_FALSE);

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String sentDate = dateFormat.format(d);
        map.put(PublicComponent.FIREBASE_CHAT_HISTORY_TIMESTAMP, sentDate);

        chatHistoryReference.push().setValue(map);
        receiverReference.push().setValue(map);
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
