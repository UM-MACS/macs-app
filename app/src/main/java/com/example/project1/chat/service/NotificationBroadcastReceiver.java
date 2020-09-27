package com.example.project1.chat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, NotificationService.class));
        } else {
            context.startService(new Intent(context, NotificationService.class));
        }
//        context.startService(new Intent(context,NotificationService.class));
    }
}
