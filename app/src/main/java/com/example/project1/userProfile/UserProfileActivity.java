package com.example.project1.userProfile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.project1.forum.caregiver.CaregiverEditDeletePostActivity;
import com.example.project1.forum.caregiver.CaregiverViewForumFavouriteListActivity;
import com.example.project1.forum.EditDeleteForumPostActivity;
import com.example.project1.R;
import com.example.project1.forum.ViewForumFavouriteListActivity;
import com.example.project1.forum.specialist.SpecialistEditDeleteForumPostActivity;
import com.example.project1.forum.specialist.SpecialistViewForumFavouriteActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.User;

public class UserProfileActivity extends BaseActivity {
    private Button viewMyPosts, viewMyEmotions, viewMyFavourite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        viewMyPosts = (Button)findViewById(R.id.view_my_posts_button);
        viewMyFavourite = (Button)findViewById(R.id.view_my_favourite_button);

        viewMyPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User.getInstance().getUserType().equalsIgnoreCase("Caregiver")) {
                    Intent i = new Intent(UserProfileActivity.this, CaregiverEditDeletePostActivity.class);
                    startActivity(i);
                } else if (User.getInstance().getUserType().equalsIgnoreCase("Patient")){
                    Intent i = new Intent(UserProfileActivity.this, EditDeleteForumPostActivity.class);
                    startActivity(i);
                } else if (User.getInstance().getUserType().equalsIgnoreCase("Specialist")) {
                    Intent i = new Intent(UserProfileActivity.this, SpecialistEditDeleteForumPostActivity.class);
                    startActivity(i);
                }
            }
        });

        viewMyFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User.getInstance().getUserType().equalsIgnoreCase("Caregiver")) {
                    Intent i = new Intent(UserProfileActivity.this, CaregiverViewForumFavouriteListActivity.class);
                    startActivity(i);
                } else if (User.getInstance().getUserType().equalsIgnoreCase("Patient")){
                    Intent i = new Intent(UserProfileActivity.this, ViewForumFavouriteListActivity.class);
                    startActivity(i);
                } else if (User.getInstance().getUserType().equalsIgnoreCase("Specialist")) {
                    Intent i = new Intent(UserProfileActivity.this, SpecialistViewForumFavouriteActivity.class);
                    startActivity(i);
                }
            }
        });
    }

}
