package com.example.project1.chat.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.project1.exercise.NotificationReceiver;
import com.example.project1.login.component.SessionManager;

import java.util.Calendar;

public class NotificationService extends Service {

    SessionManager sessionManager;

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
        return super.onStartCommand(intent, flags, startId);
        //TODO
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(sessionManager.isLogin()){
            sendBroadcast(new Intent("com.example.project1.chat.service.restartservice"));
        }
    }

    private void notifyUser(){
        //TODO
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        final String action = "ConnectivityManager.CONNECTIVITY_ACTION";
//        IntentFilter intentFilter = new IntentFilter(action);
//        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//        NotificationReceiver mReceiver = new NotificationReceiver();
//        getApplicationContext().registerReceiver(mReceiver, intentFilter);
//        Intent i2 = new Intent(action);
//        final PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, i2, 0);
//
//        //Notification for api less than 26
//        Intent notificationIntent = new Intent("android.media.action.exercise.DISPLAY_NOTIFICATION");
//        notificationIntent.addCategory("android.intent.category.exercise.DEFAULT");
//        final PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY,9);
//        calendar.set(Calendar.MINUTE,0);
//        calendar.set(Calendar.SECOND,0);
//
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), broadcast);

    }
}
