package com.example.project1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class FAQ extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        Log.d(TAG, "onCreate: started.");

        initImageBitmaps();
    }



    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://www.google.com/search?q=q&rlz=1C1CHBF_enMY814MY814&source=lnms&tbm=isch&sa=X&ved=0ahUKEwj7-Nnb6JLiAhUK6XMBHWcIDlUQ_AUIDigB&biw=767&bih=744&dpr=1.25#imgrc=8gln5w2XGi-DVM:");
        mNames.add("Question");

        mImageUrls.add("https://www.google.com/search?q=q&rlz=1C1CHBF_enMY814MY814&source=lnms&tbm=isch&sa=X&ved=0ahUKEwj7-Nnb6JLiAhUK6XMBHWcIDlUQ_AUIDigB&biw=767&bih=744&dpr=1.25#imgrc=8gln5w2XGi-DVM:");
        mNames.add("Question");

        mImageUrls.add("https://www.google.com/search?q=q&rlz=1C1CHBF_enMY814MY814&source=lnms&tbm=isch&sa=X&ved=0ahUKEwj7-Nnb6JLiAhUK6XMBHWcIDlUQ_AUIDigB&biw=767&bih=744&dpr=1.25#imgrc=8gln5w2XGi-DVM:");
        mNames.add("Question");

        mImageUrls.add("https://www.google.com/search?q=q&rlz=1C1CHBF_enMY814MY814&source=lnms&tbm=isch&sa=X&ved=0ahUKEwj7-Nnb6JLiAhUK6XMBHWcIDlUQ_AUIDigB&biw=767&bih=744&dpr=1.25#imgrc=8gln5w2XGi-DVM:");
        mNames.add("Question");

        mImageUrls.add("https://www.google.com/search?q=q&rlz=1C1CHBF_enMY814MY814&source=lnms&tbm=isch&sa=X&ved=0ahUKEwj7-Nnb6JLiAhUK6XMBHWcIDlUQ_AUIDigB&biw=767&bih=744&dpr=1.25#imgrc=8gln5w2XGi-DVM:");
        mNames.add("Question");

        mImageUrls.add("https://www.google.com/search?q=q&rlz=1C1CHBF_enMY814MY814&source=lnms&tbm=isch&sa=X&ved=0ahUKEwj7-Nnb6JLiAhUK6XMBHWcIDlUQ_AUIDigB&biw=767&bih=744&dpr=1.25#imgrc=8gln5w2XGi-DVM:");
        mNames.add("Question");

        mImageUrls.add("https://www.google.com/search?q=q&rlz=1C1CHBF_enMY814MY814&source=lnms&tbm=isch&sa=X&ved=0ahUKEwj7-Nnb6JLiAhUK6XMBHWcIDlUQ_AUIDigB&biw=767&bih=744&dpr=1.25#imgrc=8gln5w2XGi-DVM:");
        mNames.add("Question");

        mImageUrls.add("https://www.google.com/search?q=q&rlz=1C1CHBF_enMY814MY814&source=lnms&tbm=isch&sa=X&ved=0ahUKEwj7-Nnb6JLiAhUK6XMBHWcIDlUQ_AUIDigB&biw=767&bih=744&dpr=1.25#imgrc=8gln5w2XGi-DVM:");
        mNames.add("Question");



        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        com.example.project1.RecyclerViewAdapter adapter = new com.example.project1.RecyclerViewAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}
