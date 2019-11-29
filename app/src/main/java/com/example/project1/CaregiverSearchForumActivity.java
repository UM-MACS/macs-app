package com.example.project1;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CaregiverSearchForumActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private String localhost;
    private static String URL;
    private static String URL_GETPIC;
    private static String URL_GET_REPLY;
    private static String URL_POST_REPLY;
    private static String URL_PIN_POST;
    private static String URL_SEARCH_POST;
    private static String URL_GET_POSTS;
    private String picture, searchTitle, searchContent;
    private LinearLayout forumParentLinearLayout, expandedForumParentLinearLayout;
    private TextView nullPost, username, threadTitle, threadContent, threadID,threadTime;
    private FloatingActionButton createPostButton;
    private TextView expandedName, expandedTitle, expandedContent, expandedID,expandedTime;
    private CircleImageView user_pic, expanded_user_pic;
    private ImageView pinned_pic, unpinned_pic;
    private EditText replyText, searchEditText;
    private Button submitReplyButton, searchButton;
    //private String getName, getTitle, getContent, getID;
    private LinearLayout layoutAdjust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_forum);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_forum);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(CaregiverSearchForumActivity.this, emotionActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(CaregiverSearchForumActivity.this, viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(CaregiverSearchForumActivity.this, eventAssessment.class);
                        startActivity(i4);
                        break;
                    case R.id.navigation_faq:
                        Intent i5 = new Intent(CaregiverSearchForumActivity.this, FAQ.class);
                        startActivity(i5);
                        break;
                    case R.id.navigation_forum:
                        Intent i6 = new Intent(CaregiverSearchForumActivity.this, CaregiverForumActivity.class);
                        startActivity(i6);
                        break;
                }
                return true;
            }
        });

        localhost = getString(R.string.localhost);
        URL = localhost+":3000/getCaregiverForumPost";
        URL_GETPIC = localhost+"/jee/getPic2.php";
        URL_GET_REPLY = localhost+":3000/getReplyPostCaregiver/";
        URL_POST_REPLY = localhost+":3000/postReplyCaregiver/";
        URL_PIN_POST = localhost+":3000/pinPostCaregiver/";
        URL_SEARCH_POST = localhost+":3000/searchPostCaregiver/";
        URL_GET_POSTS= localhost+":3000/getPostCaregiver/";
        sessionManager = new SessionManager(this);
        forumParentLinearLayout = (LinearLayout)findViewById(R.id.parent_linear_layout_forum);
        nullPost = (TextView) findViewById(R.id.nullPost);
        nullPost.setVisibility(View.GONE);

        searchButton = (Button)findViewById(R.id.search_button);
        searchEditText = (EditText)findViewById(R.id.search_edit_text);
        searchButton.setVisibility(View.GONE);
        searchEditText.setVisibility(View.GONE);
        String searchText;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                searchText= null;
            } else {
                searchText= extras.getString("searchText");
            }
        } else {
            searchText = (String) savedInstanceState.getSerializable("searchText");
        }
        search(searchText);

        createPostButton = (FloatingActionButton) findViewById(R.id.create_post_button);
        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CaregiverSearchForumActivity.this, CaregiverCreatePostActivity.class);
                startActivity(i);
            }
        });
    }


    public void getPic(final String name, final CircleImageView view){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETPIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            picture = jsonObject.getString("picture");
                            Log.e("TAG", "pic: "+picture );

                            //load picture example
                            int loader = R.drawable.ic_user;
                            ImgLoader imgLoader = new ImgLoader(getApplicationContext());
                            imgLoader.DisplayImage(picture,loader,view);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
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
                params.put("name",name);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onExpand(View v){
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
        threadTime = (TextView) ((View) v).findViewById(R.id.thread_time);
        final String getTime = (String) threadTime.getText().toString();

        setContentView(R.layout.activity_forum_expand);
        expandedName = (TextView) findViewById(R.id.expanded_user_name);
        expandedTitle = (TextView) findViewById(R.id.expanded_thread_title);
        expandedContent = (TextView) findViewById(R.id.expanded_thread_content);
        expandedID = (TextView) findViewById(R.id.expanded_thread_id);
        expandedTime = (TextView) findViewById(R.id.expanded_thread_time);
        expanded_user_pic = (CircleImageView) findViewById(R.id.expanded_user_profile_pic);
        replyText = (EditText) findViewById(R.id.reply_input);
        getPic(getName, expanded_user_pic);
        expandedName.setText(getName);
        expandedTitle.setText(getTitle);
        expandedContent.setText(getContent);
        expandedID.setText(getID);
        expandedTime.setText(getTime);
        getReplyPost(getID);
        submitReplyButton = (Button)findViewById(R.id.submit_reply_button);
        submitReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReply(getID);
            }
        });
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
                                    getPic(name.get(i),expanded_user_pic);
                                    expandedName.setText(name.get(i));
                                    expandedContent.setText(content.get(i));
                                    expandedID.setText(id.get(i));
                                    threadTime = (TextView)((View) rowView).findViewById(R.id.expanded_thread_time);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                                Toast.makeText(getApplicationContext(), "Error Loading", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Error Loading",Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error Loading",Toast.LENGTH_SHORT).show();
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
        replyText = (EditText) findViewById(R.id.reply_input);
        final String text = replyText.getText().toString().trim();
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String date = dateFormat.format(d);
        if(text.equals("")){
            Toast.makeText(getApplicationContext(),"Please Write Something in the text box",
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
                                if (success.equals("1")) {
                                    Toast.makeText(getApplicationContext(), "Successfully Posted",
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
                                    getPic(User.getInstance().getUserName(), expanded_user_pic);
                                    expandedName.setText(User.getInstance().getUserName());
                                    expandedContent.setText(text);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                                    Toast.makeText(getApplicationContext(), "Error",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error",
                            Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", User.getInstance().getEmail());
                    params.put("name", User.getInstance().getUserName());
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

    public void search(final String searchText){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEARCH_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            ArrayList<String> name = new ArrayList<>();
                            ArrayList<String> title = new ArrayList<>();
                            ArrayList<String> content = new ArrayList<>();
                            ArrayList<String> id = new ArrayList<>();
                            ArrayList<String> anonymous = new ArrayList<>();
                            ArrayList<String> pinned = new ArrayList<>();
                            ArrayList<String> parentID = new ArrayList<>();
                            ArrayList<String> date = new ArrayList<>();

                            if(success.equals("1")){
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    name.add(object.getString("name"));
                                    title.add(object.getString("title"));
                                    content.add(object.getString("content"));
                                    id.add(object.getString("id"));
                                    anonymous.add(object.getString("anonymous"));
                                    pinned.add(object.getString("pinned"));
                                    parentID.add(object.getString("parentID"));
                                    date.add(object.getString("date"));

                                }
                                for (int i =0; i<name.size();i++){
                                    if(pinned.get(i).equals("true")) {
                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        final View rowView = inflater.inflate(R.layout.field_forum, forumParentLinearLayout, false);
                                        forumParentLinearLayout.addView(rowView, forumParentLinearLayout.getChildCount() - 1);
                                        pinned_pic = (ImageView) ((View) rowView).findViewById(R.id.pinned);
                                        pinned_pic.setVisibility(View.VISIBLE);
                                        if (anonymous.get(i).equals("true")) {
                                            username = (TextView) ((View) rowView).findViewById(R.id.user_name);
                                            username.setText("Anonymous");
                                            user_pic = (CircleImageView) ((View) rowView).findViewById(R.id.user_profile_pic);
                                            getPic("lee", user_pic);
                                        } else {
                                            username = (TextView) ((View) rowView).findViewById(R.id.user_name);
                                            username.setText(name.get(i));
                                            user_pic = (CircleImageView) ((View) rowView).findViewById(R.id.user_profile_pic);
                                            getPic(name.get(i), user_pic);
                                        }

                                        searchTitle = title.get(i).replaceAll("(?i)" + searchText, "<font color='blue'>" + searchText + "</font>");
                                        threadTitle = (TextView) ((View) rowView).findViewById(R.id.thread_title);
                                        threadTitle.setText(Html.fromHtml(searchTitle));
                                        searchContent = content.get(i).replaceAll("(?i)" + searchText, "<font color='blue'>" + searchText + "</font>");
                                        threadContent = (TextView) ((View) rowView).findViewById(R.id.thread_content);
                                        threadContent.setText(Html.fromHtml(searchContent));
                                        threadID = (TextView) ((View) rowView).findViewById(R.id.thread_id);
                                        threadID.setText(id.get(i));
                                        threadTime = (TextView)((View) rowView).findViewById(R.id.thread_time);
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                                }
                                for(int i=0; i<name.size();i++){
                                    if(pinned.get(i).equals("")){
                                        if (parentID.get(i).equals("")) {
                                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            final View rowView = inflater.inflate(R.layout.field_forum, forumParentLinearLayout, false);
                                            forumParentLinearLayout.addView(rowView, forumParentLinearLayout.getChildCount() - 1);
                                            if(User.getInstance().getUserType().equals("Specialist")){
                                                unpinned_pic = (ImageView) ((View)rowView).findViewById(R.id.unpinned);
                                                unpinned_pic.setVisibility(View.VISIBLE);
                                            }
                                            if (anonymous.get(i).equals("true")) {
                                                username = (TextView) ((View) rowView).findViewById(R.id.user_name);
                                                username.setText("Anonymous");
                                                user_pic = (CircleImageView) ((View) rowView).findViewById(R.id.user_profile_pic);
                                                getPic("lee", user_pic);
                                            } else {
                                                username = (TextView) ((View) rowView).findViewById(R.id.user_name);
                                                username.setText(name.get(i));
                                                user_pic = (CircleImageView) ((View) rowView).findViewById(R.id.user_profile_pic);
                                                getPic(name.get(i), user_pic);
                                            }
                                            searchTitle = title.get(i).replaceAll("(?i)"+searchText, "<font color='blue'>"+searchText+"</font>");
                                            threadTitle = (TextView) ((View) rowView).findViewById(R.id.thread_title);
                                            threadTitle.setText(Html.fromHtml(searchTitle));
                                            searchContent = content.get(i).replaceAll("(?i)"+searchText, "<font color='blue'>"+searchText+"</font>");
                                            threadContent = (TextView) ((View) rowView).findViewById(R.id.thread_content);
                                            threadContent.setText(Html.fromHtml(searchContent));
                                            threadID = (TextView) ((View) rowView).findViewById(R.id.thread_id);
                                            threadID.setText(id.get(i));
                                            threadTime = (TextView)((View) rowView).findViewById(R.id.thread_time);
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try {
                                                Date d = dateFormat.parse(date.get(i));
                                                long epoch = d.getTime();
                                                CharSequence time = DateUtils.getRelativeTimeSpanString(epoch,System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                                                Log.e("TAG", "time: "+time );
                                                threadTime.setText(time);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        } else{
                                            getMyPosts(parentID.get(i));
                                        }
                                    }
                                }

                            } else if (success.equals("-1")){
                                nullPost.setVisibility(View.VISIBLE);
                                nullPost.setText("No Search Result for "+searchText);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("search",searchText);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getMyPosts(final String id) {
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
                            Log.e("TAG", "success"+success );

                            if(success.equals("1")){
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    name.add(object.getString("name"));
                                    title.add(object.getString("title"));
                                    content.add(object.getString("content"));
                                    id.add(object.getString("id"));
                                    date.add(object.getString("date"));
                                }

                                for(int i=0; i<name.size();i++){
                                    Log.e("TAG", "name "+name.get(i));
                                    Log.e("TAG", "title "+title.get(i));
                                    Log.e("TAG", "content "+content.get(i));
                                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View rowView = inflater.inflate(R.layout.field_forum, forumParentLinearLayout, false);
                                    forumParentLinearLayout.addView(rowView, forumParentLinearLayout.getChildCount() - 1);
                                    user_pic = (CircleImageView)((View)rowView).findViewById(R.id.user_profile_pic);
                                    getPic(name.get(i),user_pic);
                                    username = (TextView) ((View) rowView).findViewById(R.id.user_name);
                                    username.setText(name.get(i));
                                    threadTitle = (TextView) ((View) rowView).findViewById(R.id.thread_title);
                                    threadTitle.setText(title.get(i));
                                    threadContent = (TextView) ((View) rowView).findViewById(R.id.thread_content);
                                    threadContent.setText(content.get(i));
                                    threadID = (TextView)((View)rowView).findViewById(R.id.thread_id);
                                    threadID.setText(id.get(i));
                                    threadTime = (TextView)((View) rowView).findViewById(R.id.thread_time);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

                            }
                        } catch (JSONException e) {
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
                params.put("id",id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //for specialists only
    public void onPin(final View v){

        threadID = (TextView) ((View)v.getParent()).findViewById(R.id.thread_id);
        final String id = (String) threadID.getText().toString();
        Log.e("TAG", "id is "+id );

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PIN_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                pinned_pic = (ImageView)((View)v.getParent()).findViewById(R.id.pinned);
                                unpinned_pic = (ImageView)((View)v.getParent()).findViewById(R.id.unpinned);
                                pinned_pic.setVisibility(View.VISIBLE);
                                unpinned_pic.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(getApplicationContext(), "Error",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("pin","true" );
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onUnpin(final View v){

        threadID = (TextView) ((View)v.getParent()).findViewById(R.id.thread_id);
        final String id = (String) threadID.getText().toString();
        Log.e("TAG", "id is "+id );

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PIN_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                pinned_pic = (ImageView)((View)v.getParent()).findViewById(R.id.pinned);
                                unpinned_pic = (ImageView)((View)v.getParent()).findViewById(R.id.unpinned);
                                pinned_pic.setVisibility(View.GONE);
                                unpinned_pic.setVisibility(View.VISIBLE);

                            } else {
                                Toast.makeText(getApplicationContext(), "Error",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("pin","" );
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CaregiverSearchForumActivity.this,CaregiverForumActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            sessionManager.logout();
            Intent intent = new Intent(CaregiverSearchForumActivity.this,MainActivity.class);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setUserType("");
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(CaregiverSearchForumActivity.this,ChangePassword.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(CaregiverSearchForumActivity.this,UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}