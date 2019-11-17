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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CaregiverEditDeletePostActivity extends AppCompatActivity {
    private String localhost;
    private static String URL_GET_POSTS;
    private static String URL_GETPIC;
    private static String URL_UPDATE_POST;
    private static String URL_DELETE_POST;
    private String picture, ID;
    private TextView nullPost, username, threadTitle, threadContent, threadID, threadTime;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_forum);

        localhost = getString(R.string.localhost);
        URL_GET_POSTS = localhost+":3000/getMyPostCaregiver/";
        URL_GETPIC = localhost+"/jee/getPic2.php";
        URL_UPDATE_POST = localhost+":3000/updatePostCaregiver/";
        URL_DELETE_POST = localhost+":3000/deletePostCaregiver/";

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
        getMyPosts(User.getInstance().getEmail());

    }

    private void getMyPosts(final String email) {
        nullPost.setVisibility(View.VISIBLE);
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
                            ArrayList<String> parentID = new ArrayList<>();
                            Log.e("TAG", "success"+success );

                            if(success.equals("1")){
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    name.add(object.getString("name"));
                                    title.add(object.getString("title"));
                                    content.add(object.getString("content"));
                                    id.add(object.getString("id"));
                                    date.add(object.getString("date"));
                                    parentID.add(object.getString("parentID"));
                                }

                                for(int i=0; i<name.size();i++){
                                    if(parentID.get(i).equals("")) {
                                        nullPost.setVisibility(View.INVISIBLE);
                                        Log.e("TAG", "name " + name.get(i));
                                        Log.e("TAG", "title " + title.get(i));
                                        Log.e("TAG", "content " + content.get(i));
                                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        final View rowView = inflater.inflate(R.layout.field_forum, forumParentLinearLayout, false);
                                        forumParentLinearLayout.addView(rowView, forumParentLinearLayout.getChildCount() - 1);
                                        user_pic = (CircleImageView) ((View) rowView).findViewById(R.id.user_profile_pic);
                                        getPic(name.get(i), user_pic);
                                        username = (TextView) ((View) rowView).findViewById(R.id.user_name);
                                        username.setText(name.get(i));
                                        threadTitle = (TextView) ((View) rowView).findViewById(R.id.thread_title);
                                        threadTitle.setText(title.get(i));
                                        threadContent = (TextView) ((View) rowView).findViewById(R.id.thread_content);
                                        threadContent.setText(content.get(i));
                                        threadID = (TextView) ((View) rowView).findViewById(R.id.thread_id);
                                        threadID.setText(id.get(i));
                                        threadTime = (TextView) ((View) rowView).findViewById(R.id.thread_time);
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        try {
                                            Date d = dateFormat.parse(date.get(i));
                                            long epoch = d.getTime();
                                            CharSequence time = DateUtils.getRelativeTimeSpanString(epoch, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                                            Log.e("TAG", "time: " + time);
                                            threadTime.setText(time);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }

                            } else if(success.equals("-1")){
                                nullPost.setVisibility(View.VISIBLE);
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
                params.put("email",email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
                                Log.e("TAG", "success load pic" );
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
        threadTitle = (TextView) ((View) v).findViewById(R.id.thread_title);
        final String getTitle = (String) threadTitle.getText().toString();
        threadContent = (TextView) ((View) v).findViewById(R.id.thread_content);
        final String getContent = (String) threadContent.getText().toString();
        threadID = (TextView) ((View) v).findViewById(R.id.thread_id);
        final String getID = (String) threadID.getText().toString();
        threadTime = (TextView) ((View) v).findViewById(R.id.thread_time);
        final String getTime = (String) threadTime.getText().toString();

        setContentView(R.layout.activity_forum_expand);
        editButton = (Button)findViewById(R.id.edit_post);
        editButton.setVisibility(View.VISIBLE);
        deleteButton = (Button)findViewById(R.id.delete_post);
        deleteButton.setVisibility(View.VISIBLE);
        fav_icon = (ImageView)findViewById(R.id.addFav);
        fav_icon.setVisibility(View.GONE);
        expandedName = (TextView)findViewById(R.id.expanded_user_name);
        expandedTitle = (TextView)findViewById(R.id.expanded_thread_title);
        expandedContent=(TextView)findViewById(R.id.expanded_thread_content);
        expandedID = (TextView)findViewById(R.id.expanded_thread_id);
        expandedTime = (TextView) findViewById(R.id.expanded_thread_time);
        expanded_user_pic = (CircleImageView)findViewById(R.id.expanded_user_profile_pic);
        getPic(getName,expanded_user_pic);
        expandedName.setText(getName);
        expandedTitle.setText(getTitle);
        expandedContent.setText(getContent);
        expandedID.setText(getID);
        expandedTime.setText(getTime);

        //click on edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditPost(getID, getTitle,getContent);
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

    public void onEditPost(final String ID, final String title, final String content){
        setContentView(R.layout.activity_create_post);
        postButton = (Button) findViewById(R.id.post_button);
        postButton.setVisibility(View.GONE);
        updateButton = (Button)findViewById(R.id.update_button);
        updateButton.setVisibility(View.VISIBLE);
        postTitle = (EditText)findViewById(R.id.post_title);
        postContent = (EditText)findViewById(R.id.post_content);
        cancelButton = (Button) findViewById(R.id.post_cancel);
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
                Intent i = new Intent(CaregiverEditDeletePostActivity.this, CaregiverEditDeletePostActivity.class);
                startActivity(i);
            }
        });
    }

    public void onUpdate(final String id){
        postTitle = (EditText)findViewById(R.id.post_title);
        postContent = (EditText)findViewById(R.id.post_content);
        final String title = postTitle.getText().toString();
        final String content = postContent.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),"Update Success",
                                        Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(CaregiverEditDeletePostActivity.this,
                                        CaregiverEditDeletePostActivity.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Post Fail",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Post Fail",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Post Fail",
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("title",title);
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
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_cancel_button)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        onDeletePost(id);
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
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
                                Toast.makeText(getApplicationContext(),"Post Deleted",
                                        Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(CaregiverEditDeletePostActivity.this,
                                        CaregiverEditDeletePostActivity.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Error Deleting,",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error Deleting,",
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

//    @Override
//    public void onBackPressed() {
//        Intent i = new Intent(EditDeletePostActivity.this,EditDeletePostActivity.class);
//        startActivity(i);
//    }
}
