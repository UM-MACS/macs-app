package com.example.project1.chat.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.PublicComponent;
import com.example.project1.R;
import com.example.project1.chat.ChatPageActivity;
import com.example.project1.chat.component.CurrentChatUser;
import com.example.project1.exercise.ExerciseDashboardActivity;
import com.example.project1.exercise.NotificationReceiver;
import com.example.project1.forum.imageFile.ImgLoader;
import com.example.project1.login.component.SessionManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationService extends Service {

    private SessionManager sessionManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, chatDatabaseReference;
    private final String RECEIVER_PIC = "receiverPic";
    private final String NRIC_TO = "NRICTo";
    private final String CHAT_CHANNEL_ID = "chatChannelId";
    private final String RECEIVER_NAME = "receiverName";
    private final String RECEIVER_TYPE = "receiverType";

    public NotificationService(){
        sessionManager = new SessionManager(getApplicationContext());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(sessionManager.isLogin()) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference(PublicComponent.FIREBASE_CHAT_UNREAD_BASE).child(sessionManager.getUserDetail().get("NRIC"));

            databaseReference.addChildEventListener(
                    new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Map map = dataSnapshot.getValue(Map.class);
                            String chatChannelId = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_CHANNEL_ID).toString();
                            String message = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_MESSAGE).toString();
                            String nricTo = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_NRIC_TO).toString();
                            String senderName = map.get(PublicComponent.FIREBASE_CHAT_HISTORY_SENDER_NAME).toString();

                            if(!nricTo.equals(CurrentChatUser.getInstance().getCurrentNRIC())){
                                notifyUser(senderName,message,nricTo,chatChannelId);
                            }

                            databaseReference.child(dataSnapshot.getKey()).removeValue();

                            chatDatabaseReference = firebaseDatabase.getReference(PublicComponent.FIREBASE_CHAT_BASE).child(chatChannelId).child(PublicComponent.FIREBASE_CHAT_CHANNEL_CHAT_HISTORY);
                            chatDatabaseReference.push().setValue(map);
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
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(sessionManager.isLogin()){
            sendBroadcast(new Intent("com.example.project1.chat.service.restartservice"));
        }
    }

    private void notifyUser(String senderName, String message, String nricTo, String chatChannelId){

        Context context = getApplicationContext();

        NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String s = sessionManager.getLanguagePref(context);
        if(s.equals("en")){
            s = "New message from " + senderName;
        } else{
            s = "Mesej baru dari " + senderName;
        }

        final int NOTIFY_ID = 3; // ID of notification
        String id = "Channel 3"; // default_channel_id
        String title = "title channel 3"; // Default Channel
        PendingIntent pendingIntent;
        Intent intent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, ChatPageActivity.class);
            intent.putExtra(NRIC_TO, nricTo);
            intent.putExtra(RECEIVER_NAME, senderName);
            intent.putExtra(RECEIVER_TYPE, "null");
            intent.putExtra(CHAT_CHANNEL_ID, chatChannelId);
            intent.putExtra(RECEIVER_PIC, "null");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            // REQUIRED
            builder.setContentTitle("MACS")
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentText(message)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))
                    .setContentIntent(pendingIntent)
                    .setTicker(s)
                    .setWhen(System.currentTimeMillis())
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, ChatPageActivity.class);
            intent.putExtra(NRIC_TO, nricTo);
            intent.putExtra(RECEIVER_NAME, senderName);
            intent.putExtra(RECEIVER_TYPE, "null");
            intent.putExtra(CHAT_CHANNEL_ID, chatChannelId);
            intent.putExtra(RECEIVER_PIC, "null");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle("MACS")                            // required
                    .setContentText(message)
                    .setSmallIcon(R.drawable.app_icon)
                    .setTicker(s)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }

}
