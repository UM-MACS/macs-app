package com.example.project1.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.project1.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class FullScreenPostImageActivity extends AppCompatActivity {
    private PhotoView fullScreenPostImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_post_image);

        fullScreenPostImageView = findViewById(R.id.fullScreenPostImageView);
        Intent callingActivityIntent = getIntent();
        if (callingActivityIntent != null) {
            byte[] byteArray = callingActivityIntent.getByteArrayExtra("byteArray");
            if (byteArray != null && fullScreenPostImageView != null) {
                Glide.with(this)
                        .asBitmap()
                        .load(byteArray)
                        .into(fullScreenPostImageView);
            }
        }
    }
}