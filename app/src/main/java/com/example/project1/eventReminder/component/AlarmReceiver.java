package com.example.project1.eventReminder.component;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.project1.eventReminder.EventReminderActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.SessionManager;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.R;

import static android.content.pm.PackageManager.GET_META_DATA;


public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager notifManager;
    private SessionManager sessionManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        String s = sessionManager.getLanguagePref(context);
        if(s.equals("en")){
            s = context.getResources().getString(R.string.upcoming_app);
        } else{
            s = "Anda mempunyai suatu acara yang akan datang";
        }
        final int NOTIFY_ID = 0; // ID of notification
        String id = "Channel 1"; // default_channel_id
        String title = "title channel 1"; // Default Channel
        PendingIntent pendingIntent;
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
            intent = new Intent(context, EventReminderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle("MACS")                            // required
                    .setSmallIcon(R.drawable.app_icon)
                    .setBadgeIconType(R.drawable.app_icon)
                    .setContentText(s)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))
                    .setContentIntent(pendingIntent)
                    .setTicker("MACS")
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, EventReminderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle("MACS")                            // required
                    .setContentText(s)
                    .setSmallIcon(R.drawable.app_icon)
                    .setBadgeIconType(R.drawable.app_icon)
                    .setTicker("MACS")
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);

    }
}
