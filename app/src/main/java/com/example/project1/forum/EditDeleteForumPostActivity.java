package com.example.project1.forum;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.example.project1.chat.OnEditTextRightDrawableTouchListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.R;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.forum.imageFile.ImgLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditDeleteForumPostActivity extends BaseActivity {
    private String localhost;
    private static String URL_GET_POSTS;
    private static String URL_GETPIC;
    private static String URL_UPDATE_POST;
    private static String URL_DELETE_POST;
    private String picture, ID;
    private TextView nullPost, username, threadTitle, threadContent, threadID, threadTime
            , emailContainer, typeContainer, postPhotoString;
    private TextView expandedName, expandedTitle, expandedContent, expandedID, expandedTime;
    private LinearLayout forumParentLinearLayout;
    private CircleImageView user_pic, expanded_user_pic;
    private FloatingActionButton b1;
    private BottomNavigationView bottomNavigationView;
    private Button editButton, deleteButton, postButton, updateButton;
    private EditText postTitle, postContent;
    private EditText searchEditText;
    private Button searchButton,cancelButton;
    private ImageView fav_icon;
    private ProgressBar progressBar;
    private CheckBox anonymousCheckbox;
    private TextView isAnonymousTV, displayAnonymousTV;
    private String currentPostParentID, currentPostPhotoString;
    private ImageView postImage, editPostImage;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        localhost = getString(R.string.localhost);
        URL_GET_POSTS = localhost+"/getMyPost/";
        URL_GETPIC = localhost+"/getPatientPic";
        URL_UPDATE_POST = localhost+"/updatePost/";
        URL_DELETE_POST = localhost+"/deletePost/";

        searchEditText = (EditText)findViewById(R.id.search_edit_text);
        searchButton = (Button)findViewById(R.id.search_button);
        searchEditText.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        forumParentLinearLayout = (LinearLayout)findViewById(R.id.parent_linear_layout_forum);
        nullPost = (TextView) findViewById(R.id.nullPost);
        b1 = (FloatingActionButton) findViewById(R.id.create_post_button);
        b1.hide();
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setVisibility(View.GONE);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        getMyPosts(CurrentUser.getInstance().getNRIC());

    }

    private void getMyPosts(final String email) {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_POSTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            ArrayList<String> name = new ArrayList<>();
                            ArrayList<String> title = new ArrayList<>();
                            ArrayList<String> content = new ArrayList<>();
                            ArrayList<String> id = new ArrayList<>();
                            ArrayList<String> date = new ArrayList<>();
                            ArrayList<String> email = new ArrayList<>();
                            ArrayList<String> type = new ArrayList<>();
                            ArrayList<String> anonymous = new ArrayList<>();
                            ArrayList<String> postPhoto = new ArrayList<>();
                            Log.e("TAG", "success"+success );

                            if(success.equals("1")){
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    name.add(object.getString("name"));
                                    title.add(object.getString("title"));
                                    content.add(object.getString("content"));
                                    id.add(object.getString("id"));
                                    date.add(object.getString("date"));
                                    email.add(object.getString("email"));
                                    type.add(object.getString("type"));
                                    anonymous.add(object.getString("anonymous"));
                                    postPhoto.add(object.getString("postPhoto"));
                                }

                                for(int i=0; i<name.size();i++){
                                    Log.e("TAG", "name "+name.get(i));
                                    Log.e("TAG", "title "+title.get(i));
                                    Log.e("TAG", "content "+content.get(i));
                                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View rowView = inflater.inflate(R.layout.field_forum, forumParentLinearLayout, false);
                                    forumParentLinearLayout.addView(rowView, forumParentLinearLayout.getChildCount() - 1);
                                    user_pic = (CircleImageView)((View)rowView).findViewById(R.id.user_profile_pic);
                                    getPic(email.get(i),type.get(i),user_pic);
                                    emailContainer= (TextView) ((View) rowView).findViewById(R.id.email_container);
                                    emailContainer.setText(email.get(i));
                                    typeContainer = (TextView) ((View) rowView).findViewById(R.id.type_container);
                                    typeContainer.setText(type.get(i));
                                    username = (TextView) ((View) rowView).findViewById(R.id.user_name);
                                    username.setText(name.get(i));
                                    threadTitle = (TextView) ((View) rowView).findViewById(R.id.thread_title);
                                    threadTitle.setText(title.get(i));
                                    threadContent = (TextView) ((View) rowView).findViewById(R.id.thread_content);
                                    threadContent.setText(content.get(i));
                                    threadID = (TextView)((View)rowView).findViewById(R.id.thread_id);
                                    threadID.setText(id.get(i));
                                    threadTime = (TextView)((View) rowView).findViewById(R.id.thread_time);
                                    postPhotoString = (TextView) ((View) rowView).findViewById(R.id.postPhotoString);
                                    postPhotoString.setText(postPhoto.get(i));

                                    if (anonymous.get(i).equals("true")) {
                                        isAnonymousTV = (TextView)((View)rowView.findViewById(R.id.isAnonymoustv));
                                        isAnonymousTV.setText("Anonymous");
                                    }
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    try {
                                        Date d = dateFormat.parse(date.get(i));
                                        long epoch = d.getTime();
                                        CharSequence time = DateUtils.getRelativeTimeSpanString(epoch,System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                                        Log.e("TAG", "time: "+time );
                                        threadTime.setText(time);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                                progressBar.setVisibility(View.GONE);

                            } else if(success.equals("-1")){
                                nullPost.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            } else{
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email",email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void getPic(final String email,final String type, final CircleImageView view){
        if(type.equals("Specialist")){
            URL_GETPIC = localhost+"/getSpecialistPic";
        }else{
            URL_GETPIC = localhost+"/getPatientPic";
        }
        Log.e("TAG", "getPic: get pic url"+URL_GETPIC );
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETPIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")) {
                                picture = jsonObject.getString("photo");
//                                Log.e("TAG", "pic: " + picture);

                                //load picture example
                                int loader = R.drawable.ic_user;
                                ImgLoader imgLoader = new ImgLoader(getApplicationContext());
                                imgLoader.DisplayImage(picture, loader, view);
                                Log.e("TAG", "success loading photo" );
                            }
                        } catch (JSONException e) {
                            Log.e("TAG", "fail to load photo");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "fail to load photo");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email",email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onExpand(View v){
        emailContainer = (TextView)((View)v).findViewById(R.id.email_container);
        final String getEmail = emailContainer.getText().toString();
        typeContainer = (TextView)((View)v).findViewById(R.id.type_container);
        final String getType = typeContainer.getText().toString();
        username = (TextView) ((View) v).findViewById(R.id.user_name);
        final String getName = username.getText().toString();
        threadTitle = (TextView) ((View) v).findViewById(R.id.thread_title);
        final String getTitle = (String) threadTitle.getText().toString();
        threadContent = (TextView) ((View) v).findViewById(R.id.thread_content);
        final String getContent = (String) threadContent.getText().toString();
        threadID = (TextView) ((View) v).findViewById(R.id.thread_id);
        final String getID = (String) threadID.getText().toString();
        threadTime = (TextView) ((View) v).findViewById(R.id.thread_time);
        final String getTime = (String) threadTime.getText().toString();
        isAnonymousTV = (TextView)((View)v.findViewById(R.id.isAnonymoustv));
        final String isAnonymous = (String) isAnonymousTV.getText().toString();
        postPhotoString = (TextView) ((View)v).findViewById(R.id.postPhotoString);
        final String getPostPhotoString = (String) postPhotoString.getText().toString();
        currentPostPhotoString = getPostPhotoString;

        setContentView(R.layout.activity_forum_expand);
        editButton = (Button)findViewById(R.id.edit_post);
        editButton.setVisibility(View.VISIBLE);
        deleteButton = (Button)findViewById(R.id.delete_post);
        deleteButton.setVisibility(View.VISIBLE);
        fav_icon = (ImageView)findViewById(R.id.addFav);
        fav_icon.setVisibility(View.GONE);
        TextView favDes = (TextView)findViewById(R.id.fav_des);
        favDes.setVisibility(View.GONE);
        expandedName = (TextView)findViewById(R.id.expanded_user_name);
        expandedTitle = (TextView)findViewById(R.id.expanded_thread_title);
        expandedContent=(TextView)findViewById(R.id.expanded_thread_content);
        expandedID = (TextView)findViewById(R.id.expanded_thread_id);
        expandedTime = (TextView) findViewById(R.id.expanded_thread_time);
        expanded_user_pic = (CircleImageView)findViewById(R.id.expanded_user_profile_pic);
        postImage = (ImageView) findViewById(R.id.expanded_post_image);

        getPic(getEmail,getType,expanded_user_pic);
        if(!getPostPhotoString.equals("")) {
            postImage.setVisibility(View.VISIBLE);
            ImgLoader imgLoader = new ImgLoader(getApplicationContext());
            byte [] byteArray = imgLoader.DisplayPostImage(getPostPhotoString, postImage);
            postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent fullScreenIntent = new Intent(EditDeleteForumPostActivity.this, FullScreenPostImageActivity.class);
                    fullScreenIntent.putExtra("byteArray", byteArray);
                    startActivity(fullScreenIntent);
                }
            });
        }
        expandedName.setText(getName);
        expandedTitle.setText(getTitle);
        expandedContent.setText(getContent);
        expandedID.setText(getID);
        expandedTime.setText(getTime);
        if(isAnonymous.equals("Anonymous")){
            displayAnonymousTV = (TextView)findViewById(R.id.anonymous_post_tv);
            displayAnonymousTV.setVisibility(View.VISIBLE);
        }

        //click on edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditPost(getID, getTitle,getContent,getPostPhotoString);
            }
        });

        //click on delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = AskOption(getID);
                diaBox.show();
            }
        });

    }

    public void onEditPost(final String ID, final String title, final String content, final String getPostPhotoString){
        setContentView(R.layout.activity_create_post);
        anonymousCheckbox = (CheckBox)findViewById(R.id.anonymous_checkbox);
        anonymousCheckbox.setVisibility(View.GONE);
        postButton = (Button) findViewById(R.id.post_button);
        postButton.setVisibility(View.GONE);
        updateButton = (Button)findViewById(R.id.update_button);
        updateButton.setVisibility(View.VISIBLE);
        postTitle = (EditText)findViewById(R.id.post_title);
        postContent = (EditText)findViewById(R.id.post_content);
        cancelButton = (Button) findViewById(R.id.post_cancel);
        editPostImage = (ImageView) findViewById(R.id.post_image);

        postTitle.setOnTouchListener(new OnEditTextRightDrawableTouchListener(postTitle) {
            @Override
            public void OnEditTextClick() { showKeyboard(); }
            @Override
            public void OnDrawableClick() {
                choosePicture();
            }
        });

        if(!getPostPhotoString.equals("")){
            editPostImage.setVisibility(View.VISIBLE);
            ImgLoader imgLoader = new ImgLoader(getApplicationContext());
            byte [] byteArray = imgLoader.DisplayPostImage(getPostPhotoString, editPostImage);
            editPostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent fullScreenIntent = new Intent(EditDeleteForumPostActivity.this, FullScreenPostImageActivity.class);
                    fullScreenIntent.putExtra("byteArray", byteArray);
                    startActivity(fullScreenIntent);
                }
            });
        }
        postTitle.setText(title);
        postContent.setText(content);
        //onPost(id)
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdate(ID);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    public void onUpdate(final String id){
        postTitle = (EditText)findViewById(R.id.post_title);
        postContent = (EditText)findViewById(R.id.post_content);
        final String title = postTitle.getText().toString();
        final String content = postContent.getText().toString();
        if(title.equals("")||content.equals("")){
            Toast.makeText(getApplicationContext(), getString(R.string.enter_title_content),
                                        Toast.LENGTH_SHORT).show();
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(), getString(R.string.update_success),
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("title",title);
                String img= "";
                if (bitmap != null){
                    img = getStringImage(bitmap);
                    params.put("postPhoto",img);
                }
                else{
                    params.put("postPhoto",currentPostPhotoString);
                }
                params.put("content",content);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private AlertDialog AskOption(final String id) {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle(R.string.delete_post)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        onDeletePost(id);
                        dialog.dismiss();
                    }

                })

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    private void onDeletePost(final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                Log.e("TAG", "success" );
                                Toast.makeText(getApplicationContext(), getString(R.string.delete_post_success),
                                        Toast.LENGTH_SHORT).show();
//                                Intent i = new Intent(EditDeleteForumPostActivity.this,
//                                        EditDeleteForumPostActivity.class);
//                                startActivity(i);
//                                finish();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // ========================= Adding upload photo features code ========================
    public void showKeyboard(){
        postTitle.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(postTitle, InputMethodManager.SHOW_IMPLICIT);
    }

    public void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), filePath);
                editPostImage.setVisibility(View.VISIBLE);
                editPostImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray,Base64.DEFAULT);
        Log.e("TAG", "encodedImage"+encodedImage );
        return encodedImage;
    }

//    @Override
//    public void onBackPressed() {
//        Intent i = new Intent(EditDeleteForumPostActivity.this,EditDeleteForumPostActivity.class);
//        startActivity(i);
//    }
}
