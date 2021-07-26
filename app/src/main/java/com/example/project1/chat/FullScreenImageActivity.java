package com.example.project1.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.project1.R;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        
        ImageView fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);
        Intent callingActivityIntent = getIntent();
        if (callingActivityIntent != null) {
            Uri imageUri = callingActivityIntent.getData();
            if (imageUri != null && fullScreenImageView != null) {
                Glide.with(this)
                        .load(imageUri)
                        .into(fullScreenImageView);
            }
        }

    }
}