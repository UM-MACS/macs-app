package com.example.project1.chat.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.example.project1.PublicComponent;
import com.example.project1.R;
import com.example.project1.chat.ChatChannelListActivity;
import com.example.project1.chat.component.CurrentChatUser;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.login.component.SessionManager;
import com.example.project1.mainPage.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.android.volley.VolleyLog.TAG;

public class NotificationService extends Service {

    private SessionManager sessionManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, chatDatabaseReference;
    private ChildEventListener childEventListener;
    private final String RECEIVER_PIC = "receiverPic";
    private final String NRIC_TO = "NRICTo";
    private final String CHAT_CHANNEL_ID = "chatChannelId";
    private final String RECEIVER_NAME = "receiverName";
    private final String RECEIVER_TYPE = "receiverType";

    public NotificationService(){
        childEventListener = getChildEventListener();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: " + CurrentUser.getInstance().getNRIC());
        sessionManager = new SessionManager(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: " + CurrentUser.getInstance().getNRIC());
        if(sessionManager.isLogin()) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference(PublicComponent.FIREBASE_NOTIFICATION_BASE).child(CurrentUser.getInstance().getNRIC());
            databaseReference.addChildEventListener(childEventListener);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForeground();
                System.out.println("Foregroun detected");
            }

            else {
                startForeground(1, new Notification());
                System.out.println("Older foreground detected");
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(childEventListener);
        System.out.println("Notifcations is shutting down");
        stopForeground(true);
        if(sessionManager.isLogin()){
            Intent broadcast = new Intent();
            broadcast.setAction("com.example.project1.chat.service.restartservice");
            broadcast.setClass(this,NotificationBroadcastReceiver.class);
            sendBroadcast(broadcast);
//            startService(new Intent(this,NotificationService.class));
        }
    }

    private void startForeground(){
        final int NOTIFY_ID = 2222; // ID of notification
        String NOTFY_ID = "Channel 2"; // default_channel_id
        String NOTIFY_TITLE = "title channel 2"; // Default Channel
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotificationCompat.Builder builder;
        Context context = getApplicationContext();
        NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(NOTFY_ID);
            if (mChannel == null) {
                mChannel = new NotificationChannel(NOTFY_ID, NOTIFY_TITLE, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
        }
        startForeground(NOTIFY_ID, new NotificationCompat.Builder(this, NOTFY_ID)
                .setOngoing(true)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("MACS")
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build());
    }

    private void notifyUser(){
        Log.e(TAG, "notify: " + databaseReference.toString());
        Context context = getApplicationContext();
        NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String s = sessionManager.getLanguagePref(context);
        String message = "";
        if(s.equals("en")){
            message = "You have a new message! ";
        } else if (s.equals("ms")){
            message = "Anda ada mesej baru! ";
        } else {
            message = "您有一条新信息! ";
        }

        final int NOTIFY_ID = 3333; // ID of notification
        String id = "Channel 3"; // default_channel_id
        String title = "title channel 3"; // Default Channel
        PendingIntent pendingIntent;
        Intent intent;
        NotificationCompat.Builder builder;
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
            intent = new Intent(context, ChatChannelListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
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
            intent = new Intent(context, ChatChannelListActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
//        startForeground(NOTIFY_ID,notification);
    }

    ChildEventListener getChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                notifyUser();
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
        };
    }

}
