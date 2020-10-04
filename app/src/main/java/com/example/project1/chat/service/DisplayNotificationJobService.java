package com.example.project1.chat.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.project1.PublicComponent;
import com.example.project1.R;
import com.example.project1.chat.ChatChannelListActivity;
import com.example.project1.chat.component.CurrentChatUser;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.login.component.SessionManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.android.volley.VolleyLog.TAG;

@TargetApi(21)
public class DisplayNotificationJobService extends JobService {

    private SessionManager sessionManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        sessionManager = new SessionManager(this);
        childEventListener = getChildEventListener();

        if(sessionManager.isLogin()) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference(PublicComponent.FIREBASE_NOTIFICATION_BASE).child(CurrentUser.getInstance().getNRIC());

            databaseReference.addChildEventListener(childEventListener);
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
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

        final int NOTIFY_ID = 3334; // ID of notification
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
            intent = new Intent(context, ChatChannelListActivity.class);
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
//        notifManager.notify(NOTIFY_ID, notification);
        startForeground(NOTIFY_ID,notification);
    }

    ChildEventListener getChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(!CurrentChatUser.getInstance().getCurrentNRIC().equals("")){
                    notifyUser();
                }
                databaseReference.removeValue();
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
