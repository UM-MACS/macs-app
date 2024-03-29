package com.example.project1.forum.caregiver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.FullScreenPostImageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class CaregiverViewForumFavouriteListActivity extends BaseActivity {
    private String localhost;
    private static String URL_GET_FAV;
    private static String URL_GETPIC;
    private static String URL_ADD_FAV;
    private static String URL_DEL_FAV;
    private static String URL_GET_REPLY;
    private static String URL_POST_REPLY;
    private static String URL_EDIT_REPLY;
    private static String URL_DELETE_REPLY;
    private static String URL_GET_IS_FAV;
    private static String URL_REPORT_POST;
    private String picture, ID;
    private TextView nullPost, username, threadTitle, threadContent, threadID, threadTime;
    private TextView expandedName, expandedTitle, expandedContent, expandedID, expandedTime
            ,emailContainer, typeContainer, reportButton, fav_des, postPhotoString, editReplyButton, deleteReplyButton;
    private LinearLayout forumParentLinearLayout;
    private CircleImageView user_pic, expanded_user_pic;
    private FloatingActionButton b1;
    private BottomNavigationView bottomNavigationView;
    private Button editButton, deleteButton, postButton;
    private EditText postTitle, postContent;
    private EditText searchEditText;
    private EmojiconEditText replyText;
    private View view;
    private EmojIconActions emojIconActions;
    private Button searchButton, submitReplyButton;
    private ImageView fav_icon, unfav_icon, emoji;
    private LinearLayout expandedForumParentLinearLayout;
    private ProgressBar progressBar;
    private String currentPostParentID;
    private ImageView postImage;

    // === All the components layout in edit the reply layout ===
    private CheckBox anonymousCheckbox;
    private Button updateButton, cancelButton;
    private EditText editPostTitle, editPostContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_forum);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        localhost = getString(R.string.localhost);
        URL_GET_FAV = localhost+"/myFavouriteListCaregiver/";
        URL_ADD_FAV = localhost+"/addToFavouriteCaregiver/";
        URL_DEL_FAV = localhost+"/removeFavouriteCaregiver/";
        URL_GETPIC = localhost+"/getCaregiverPic";
        URL_GET_REPLY = localhost+"/getReplyPostCaregiver/";
        URL_POST_REPLY = localhost+"/postReplyCaregiver/";
        URL_EDIT_REPLY = localhost+"/updateReplyPostCaregiver/";
        URL_DELETE_REPLY = localhost+"/deleteReplyPostCaregiver/";
        URL_GET_IS_FAV = localhost+"/getIsFavouriteCaregiver/";
        URL_REPORT_POST = localhost+"/reportPostCaregiver/";

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_FAV,
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
                                progressBar.setVisibility(View.GONE);
                                nullPost.setVisibility(View.VISIBLE);
                            } else{
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
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
            URL_GETPIC = localhost+"/getCaregiverPic";
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
                                Log.e("TAG", "pic: " + picture);

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
        Log.e("TAG", "get name"+ getName );
        threadTitle = (TextView) ((View) v).findViewById(R.id.thread_title);
        final String getTitle = (String) threadTitle.getText().toString();
        Log.e("TAG", "get title"+ getTitle );
        threadContent = (TextView) ((View) v).findViewById(R.id.thread_content);
        final String getContent = (String) threadContent.getText().toString();
        Log.e("TAG", "get content"+ getContent );
        threadID = (TextView) ((View) v).findViewById(R.id.thread_id);
        final String getID = (String) threadID.getText().toString();
        currentPostParentID = getID;
        threadTime = (TextView) ((View)v).findViewById(R.id.thread_time);
        final String getTime = threadTime.getText().toString();
        postPhotoString = (TextView) ((View)v).findViewById(R.id.postPhotoString);
        final String getPostPhotoString = postPhotoString.getText().toString();

        setContentView(R.layout.activity_forum_expand);
        expandedName = (TextView) findViewById(R.id.expanded_user_name);
        expandedTitle = (TextView) findViewById(R.id.expanded_thread_title);
        expandedContent = (TextView) findViewById(R.id.expanded_thread_content);
        expandedID = (TextView) findViewById(R.id.expanded_thread_id);
        expandedTime = (TextView) findViewById(R.id.expanded_thread_time);
        expanded_user_pic = (CircleImageView) findViewById(R.id.expanded_user_profile_pic);

        replyText = (EmojiconEditText) findViewById(R.id.reply_input);
        emoji = findViewById(R.id.reply_emoji_icon);
        view = findViewById(R.id.forum_expand_view);
        emojIconActions = new EmojIconActions(this, view, replyText, emoji);
        emojIconActions.ShowEmojIcon();

        postImage = (ImageView) findViewById(R.id.expanded_post_image);

        getPic(getEmail,getType, expanded_user_pic);
        if(!getPostPhotoString.equals("")){
            postImage.setVisibility(View.VISIBLE);
            ImgLoader imgLoader = new ImgLoader(getApplicationContext());
            byte [] byteArray = imgLoader.DisplayPostImage(getPostPhotoString, postImage);
            postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent fullScreenIntent = new Intent(CaregiverViewForumFavouriteListActivity.this, FullScreenPostImageActivity.class);
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

        getIsFavourite(getID);
        getReplyPost(getID);
        submitReplyButton = (Button)findViewById(R.id.submit_reply_button);
        submitReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReply(getID);
            }
        });

        reportButton = (TextView) findViewById(R.id.report_button);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //confirmation pop up window
                AlertDialog diaBox = AskOption(getID, "report");
                diaBox.show();
            }
        });
    }

    private void getIsFavourite(final String id) {
        Log.e("TAG", "getIsFavourite: id "+id );

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_IS_FAV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                fav_icon = (ImageView)(findViewById(R.id.addFav));
                                unfav_icon = (ImageView)(findViewById(R.id.removeFav));
                                fav_icon.setVisibility(View.GONE);
                                unfav_icon.setVisibility(View.VISIBLE);
                                fav_des = (TextView) (findViewById(R.id.fav_des));
                                fav_des.setText(R.string.remove_favourite);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", CurrentUser.getInstance().getNRIC());
                params.put("postID",id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onReport(final String id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REPORT_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.report_success),
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("report","true" );
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private AlertDialog AskOption(final String id, final String type) {
        if(type.equals("report")){
            AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                    //set message, title, and icon
                    .setTitle(R.string.report_post)
                    .setMessage(R.string.report_post_confirm)
                    .setPositiveButton(R.string.report, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            onReport(id);
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
        else{
            AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                    //set message, title, and icon
                    .setTitle(R.string.delete_reply)
                    .setMessage(R.string.delete_reply_confirm)
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            onDeleteReply(id);
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
    }

    private void getReplyPost(final String parentID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_REPLY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            ArrayList<String> name = new ArrayList<>();
                            ArrayList<String> email = new ArrayList<>();
                            ArrayList<String> type = new ArrayList<>();
                            ArrayList<String> content = new ArrayList<>();
                            ArrayList<String> id = new ArrayList<>();
                            ArrayList<String> date = new ArrayList<>();
                            if(success.equals("1")){
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    name.add(object.getString("name"));
                                    content.add(object.getString("content"));
                                    id.add(object.getString("id"));
                                    date.add(object.getString("date"));
                                    email.add(object.getString("email"));
                                    type.add(object.getString("type"));
                                }
                                //displaying reply
                                for(int i=0; i<name.size();i++){
                                    Log.e("TAG", "name "+name.get(i));
                                    Log.e("TAG", "content "+content.get(i));
                                    expandedForumParentLinearLayout = (LinearLayout)findViewById(R.id.parent_linear_layout_expanded_forum);
                                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View rowView = inflater.inflate(R.layout.field_forum_expand, expandedForumParentLinearLayout, false);
                                    expandedForumParentLinearLayout.addView(rowView, expandedForumParentLinearLayout.getChildCount() - 1);
                                    Log.e("TAG", "number "+(expandedForumParentLinearLayout.getChildCount() + 1 ));
                                    expandedName = (TextView) ((View) rowView).findViewById(R.id.expanded_user_name);
                                    expandedContent=(TextView) ((View) rowView).findViewById(R.id.expanded_thread_content);
                                    expandedID = (TextView) ((View) rowView).findViewById(R.id.expanded_thread_id);
                                    expanded_user_pic = (CircleImageView) ((View) rowView).findViewById(R.id.expanded_user_profile_pic);
                                    getPic(email.get(i),type.get(i),expanded_user_pic);
                                    expandedName.setText(name.get(i));
                                    expandedContent.setText(content.get(i));
                                    expandedID.setText(id.get(i));
                                    threadTime = (TextView)((View) rowView).findViewById(R.id.expanded_thread_time);
                                    editReplyButton = (TextView)((View) rowView).findViewById(R.id.edit_reply_button);
                                    deleteReplyButton = (TextView)((View) rowView).findViewById(R.id.delete_reply_button);
                                    if(email.get(i).equals(CurrentUser.getInstance().getNRIC())){
                                        editReplyButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //confirmation pop up window
                                                onEditReply(expandedID.getText().toString(), expandedContent.getText().toString());
                                            }
                                        });
                                        deleteReplyButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //confirmation pop up window
                                                AlertDialog diaBox = AskOption(expandedID.getText().toString(), "");
                                                diaBox.show();
                                            }
                                        });
                                    }
                                    else{
                                        editReplyButton.setVisibility(View.GONE);
                                        deleteReplyButton.setVisibility(View.GONE);

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
                            } else if (success.equals("-1")) {
                                Log.e("TAG", "no reply post");
                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), getString(R.string.try_later),Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.try_later),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("parentID",parentID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onReply(final String parentID){
        replyText = (EmojiconEditText) findViewById(R.id.reply_input);
        final String text = replyText.getText().toString().trim();
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        final String date = dateFormat.format(d);
        if(text.equals("")){
            Toast.makeText(getApplicationContext(), getString(R.string.enter_something),
                                        Toast.LENGTH_SHORT).show();
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST_REPLY,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String success = jsonObject.getString("success");
                                String id = jsonObject.getString("id");
                                if (success.equals("1")) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.post_success),
                                        Toast.LENGTH_SHORT).show();
                                    replyText.setText("");
                                    expandedForumParentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout_expanded_forum);
                                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View rowView = inflater.inflate(R.layout.field_forum_expand, expandedForumParentLinearLayout, false);
                                    expandedForumParentLinearLayout.addView(rowView, expandedForumParentLinearLayout.getChildCount() - 1);
                                    expandedName = (TextView) ((View) rowView).findViewById(R.id.expanded_user_name);
                                    expandedContent = (TextView) ((View) rowView).findViewById(R.id.expanded_thread_content);
                                    expanded_user_pic = (CircleImageView) ((View) rowView).findViewById(R.id.expanded_user_profile_pic);
                                    expandedTime = (TextView)((View)rowView).findViewById(R.id.expanded_thread_time);
                                    getPic(CurrentUser.getInstance().getNRIC(), CurrentUser.getInstance().getUserType(), expanded_user_pic);
                                    expandedName.setText(CurrentUser.getInstance().getUserName());
                                    expandedContent.setText(text);
                                    editReplyButton = (TextView)((View) rowView).findViewById(R.id.edit_reply_button);
                                    deleteReplyButton = (TextView)((View) rowView).findViewById(R.id.delete_reply_button);
                                    editReplyButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //confirmation pop up window
                                            onEditReply(id, expandedContent.getText().toString());
                                        }
                                    });

                                    deleteReplyButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //confirmation pop up window
                                            AlertDialog diaBox = AskOption(id, "");
                                            diaBox.show();
                                        }
                                    });
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    try {
                                        Date d = dateFormat.parse(date);
                                        long epoch = d.getTime();
                                        CharSequence time = DateUtils.getRelativeTimeSpanString(epoch,System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                                        Log.e("TAG", "time: "+time );
                                        expandedTime.setText(time);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", CurrentUser.getInstance().getNRIC());
                    params.put("type", CurrentUser.getInstance().getUserType());
                    params.put("name", CurrentUser.getInstance().getUserName());
                    params.put("content", text);
                    params.put("parentID", parentID);
                    params.put("date",date);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    // === Method for delete the reply ===
    public void onDeleteReply(final String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_REPLY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.delete_reply_success),
                                        Toast.LENGTH_SHORT).show();
                                expandedForumParentLinearLayout.removeAllViews();
                                getReplyPost(currentPostParentID);

                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // === Method for create layout for user edit the reply ===
    public void onEditReply(final String id, final String content){
        setContentView(R.layout.activity_create_post);
        anonymousCheckbox = (CheckBox)findViewById(R.id.anonymous_checkbox);
        anonymousCheckbox.setVisibility(View.GONE);
        postButton = (Button) findViewById(R.id.post_button);
        postButton.setVisibility(View.GONE);
        updateButton = (Button)findViewById(R.id.update_button);
        updateButton.setVisibility(View.VISIBLE);
        postTitle = (EditText)findViewById(R.id.post_title);
        postTitle.setVisibility(View.GONE);
        postContent = (EditText)findViewById(R.id.post_content);
        cancelButton = (Button) findViewById(R.id.post_cancel);
        postContent.setText(content);
        //onPost(id)
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateReply(id, postContent.getText().toString());
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToExpandedForum();
            }
        });
    }

    private void backToExpandedForum(){
        final String getEmail = emailContainer.getText().toString();
        final String getType = typeContainer.getText().toString();
        final String getName = username.getText().toString();
        final String getTitle = (String) threadTitle.getText().toString();
        final String getContent = (String) threadContent.getText().toString();
        final String getID = (String) threadID.getText().toString();
        final String getPostPhotoString = (String) postPhotoString.getText().toString();
        currentPostParentID = getID;
        final String getTime = threadTime.getText().toString();

        setContentView(R.layout.activity_forum_expand);
        expandedName = (TextView) findViewById(R.id.expanded_user_name);
        expandedTitle = (TextView) findViewById(R.id.expanded_thread_title);
        expandedContent = (TextView) findViewById(R.id.expanded_thread_content);
        expandedID = (TextView) findViewById(R.id.expanded_thread_id);
        expandedTime = (TextView) findViewById(R.id.expanded_thread_time);
        expanded_user_pic = (CircleImageView) findViewById(R.id.expanded_user_profile_pic);
        postImage = (ImageView) findViewById(R.id.expanded_post_image);

        replyText = (EmojiconEditText) findViewById(R.id.reply_input);
        emoji = findViewById(R.id.reply_emoji_icon);
        view = findViewById(R.id.forum_expand_view);
        emojIconActions = new EmojIconActions(this, view, replyText, emoji);
        emojIconActions.ShowEmojIcon();

        if(getName.equals("Anonymous")) {
            getPic("lee","", expanded_user_pic);
        } else{
            getPic(getEmail, getType, expanded_user_pic);
        }
        if(!getPostPhotoString.equals("")) {
            postImage.setVisibility(View.VISIBLE);
            ImgLoader imgLoader = new ImgLoader(getApplicationContext());
            byte [] byteArray = imgLoader.DisplayPostImage(getPostPhotoString, postImage);
            postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent fullScreenIntent = new Intent(CaregiverViewForumFavouriteListActivity.this, FullScreenPostImageActivity.class);
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

        getIsFavourite(getID);
        getReplyPost(getID);

        submitReplyButton = (Button)findViewById(R.id.submit_reply_button);
        submitReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReply(getID);
            }
        });

        reportButton = (TextView) findViewById(R.id.report_button);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //confirmation pop up window
                AlertDialog diaBox = AskOption(getID, "report");
                diaBox.show();
            }
        });
    }

    // === Method for update the reply content to database ===
    public void onUpdateReply(final String id, final String content){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT_REPLY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.update_reply_success),
                                        Toast.LENGTH_SHORT).show();
                                backToExpandedForum();

                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("content", content);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onFavourite(final View v){
        threadID = (TextView) ((View)v.getParent().getParent().getParent()).findViewById(R.id.expanded_thread_id);
        final String id = (String) threadID.getText().toString();
        Log.e("TAG", "id is "+id );

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_FAV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                fav_icon = (ImageView)((View)v.getParent()).findViewById(R.id.addFav);
                                unfav_icon = (ImageView)((View)v.getParent()).findViewById(R.id.removeFav);
                                fav_icon.setVisibility(View.GONE);
                                unfav_icon.setVisibility(View.VISIBLE);
                                fav_des = (TextView)((View)((View) v.getParent())
                                        .findViewById(R.id.fav_des));
                                fav_des.setText(R.string.add_favourite);

                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", CurrentUser.getInstance().getNRIC());
                params.put("postID",id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onRemoveFavourite (final View v){
        threadID = (TextView) ((View)v.getParent().getParent().getParent()).findViewById(R.id.expanded_thread_id);
        final String id = (String) threadID.getText().toString();
        Log.e("TAG", "id is "+id );

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DEL_FAV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                fav_icon = (ImageView)((View)v.getParent()).findViewById(R.id.addFav);
                                unfav_icon = (ImageView)((View)v.getParent()).findViewById(R.id.removeFav);
                                fav_icon.setVisibility(View.VISIBLE);
                                unfav_icon.setVisibility(View.GONE);
                                fav_des = (TextView)((View)((View) v.getParent())
                                        .findViewById(R.id.fav_des));
                                fav_des.setText(R.string.add_favourite);

                            } else {
                                Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", CurrentUser.getInstance().getNRIC());
                params.put("postID",id );
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
