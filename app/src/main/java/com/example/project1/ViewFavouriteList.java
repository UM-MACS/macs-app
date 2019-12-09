package com.example.project1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

public class ViewFavouriteList extends AppCompatActivity {
    private String localhost;
    private static String URL_GET_FAV;
    private static String URL_GETPIC;
    private static String URL_ADD_FAV;
    private static String URL_DEL_FAV;
    private static String URL_GET_REPLY;
    private static String URL_POST_REPLY;
    private static String URL_GET_IS_FAV;
    private static String URL_REPORT_POST;
    private String picture, ID;
    private TextView nullPost, username, threadTitle, threadContent, threadID, threadTime;
    private TextView expandedName, expandedTitle, expandedContent, expandedID, expandedTime
            ,emailContainer,typeContainer, reportButton;
    private LinearLayout forumParentLinearLayout;
    private CircleImageView user_pic, expanded_user_pic;
    private FloatingActionButton b1;
    private BottomNavigationView bottomNavigationView;
    private Button editButton, deleteButton, postButton;
    private EditText postTitle, postContent;
    private EditText searchEditText, replyText;
    private Button searchButton, submitReplyButton;
    private ImageView fav_icon, unfav_icon;
    private LinearLayout expandedForumParentLinearLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        localhost = getString(R.string.localhost);
        URL_GET_FAV = localhost+":3000/myFavouriteList/";
        URL_ADD_FAV = localhost+":3000/addToFavourite/";
        URL_DEL_FAV = localhost+":3000/removeFavourite/";
        URL_GETPIC = localhost+"/jee/getPic.php";
        URL_GET_REPLY = localhost+":3000/getReplyPost/";
        URL_POST_REPLY = localhost+":3000/postReply/";
        URL_GET_IS_FAV = localhost+":3000/getIsFavourite/";
        URL_REPORT_POST = localhost+":3000/reportPost/";

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
        getMyPosts(User.getInstance().getEmail());

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
            URL_GETPIC = localhost+"/jee/getPic3.php";
        }else{
            URL_GETPIC = localhost+"/jee/getPic.php";
        }
        Log.e("TAG", "getPic: get pic url"+URL_GETPIC );
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
        threadTime = (TextView) ((View)v).findViewById(R.id.thread_time);
        final String getTime = threadTime.getText().toString();

        setContentView(R.layout.activity_forum_expand);
        expandedName = (TextView) findViewById(R.id.expanded_user_name);
        expandedTitle = (TextView) findViewById(R.id.expanded_thread_title);
        expandedContent = (TextView) findViewById(R.id.expanded_thread_content);
        expandedID = (TextView) findViewById(R.id.expanded_thread_id);
        expandedTime = (TextView) findViewById(R.id.expanded_thread_time);
        expanded_user_pic = (CircleImageView) findViewById(R.id.expanded_user_profile_pic);
        replyText = (EditText) findViewById(R.id.reply_input);
        getPic(getEmail,getType, expanded_user_pic);
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
                AlertDialog diaBox = AskOption(getID);
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
                                        "This post has been reported successfully",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Error, please report again",
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
                params.put("report","true" );
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private AlertDialog AskOption(final String id) {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Report This Post")
                .setMessage("Are you sure you want to report this post?")
                .setPositiveButton("Report", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        onReport(id);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

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
                                    getPic(User.getInstance().getEmail(),User.getInstance().getUserType(), expanded_user_pic);
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
                                    Toast.makeText(getApplicationContext(), "Error replying",
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
                    params.put("type", User.getInstance().getUserType());
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

    public void onFavourite(final View v){
        threadID = (TextView) ((View)v.getParent().getParent()).findViewById(R.id.expanded_thread_id);
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
                params.put("postID",id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onRemoveFavourite (final View v){
        threadID = (TextView) ((View)v.getParent().getParent()).findViewById(R.id.expanded_thread_id);
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
                params.put("postID",id );
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
