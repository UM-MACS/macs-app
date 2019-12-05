package com.example.project1;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class GalleryActivity2 extends AppCompatActivity {
    private static final String TAG = "GalleryActivity2";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_assessment);
        Log.d(TAG, "onCreate: started.");

        getIncomingIntent();
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        if(getIntent().hasExtra("event_name")){
            Log.d(TAG, "getIncomingIntent: found intent extras,");

            String eventName = getIntent().getStringExtra("event_name");


        }
    }


}
