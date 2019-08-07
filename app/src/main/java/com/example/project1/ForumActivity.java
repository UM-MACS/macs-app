package com.example.project1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForumActivity extends AppCompatActivity {
private SessionManager sessionManager;
private static String URL = "http://192.168.0.187:3000/getForumPost";
private static String URL_GETPIC = "http://192.168.0.187/jee/getPic.php";
private String picture;
private LinearLayout forumParentLinearLayout;
private TextView nullPost, username, threadTitle, threadContent, threadID;
private FloatingActionButton createPostButton;
private TextView expandedName, expandedTitle, expandedContent, expandedID;
private CircleImageView user_pic, expanded_user_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

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
                        Intent i2 = new Intent(ForumActivity.this, emotionActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(ForumActivity.this, viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(ForumActivity.this, eventAssessment.class);
                        startActivity(i4);
                        break;
                    case R.id.navigation_faq:
                        Intent i5 = new Intent(ForumActivity.this, FAQ.class);
                        startActivity(i5);
                        break;
                    case R.id.navigation_forum:
                        Intent i6 = new Intent(ForumActivity.this, ForumActivity.class);
                        startActivity(i6);
                        break;
                }
                return true;
            }
        });

        sessionManager = new SessionManager(this);
        forumParentLinearLayout = (LinearLayout)findViewById(R.id.parent_linear_layout_forum);
        nullPost = (TextView) findViewById(R.id.nullPost);
//        picture = "http://192.168.0.187/jee/profile_image/user.png";
        getPosts();
        createPostButton = (FloatingActionButton) findViewById(R.id.create_post_button);

        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForumActivity.this, CreatePostActivity.class);
                startActivity(i);
            }
        });
    }

    private void getPosts() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
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

                            if(success.equals("1")){
//                                JSONArray jsonArray1 = jsonObject.getJSONArray("name");
//                                JSONArray jsonArray2 = jsonObject.getJSONArray("title");
//                                JSONArray jsonArray3 = jsonObject.getJSONArray("content");
//                                JSONArray jsonArray4 = jsonObject.getJSONArray("id");
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    name.add(object.getString("name"));
                                    title.add(object.getString("title"));
                                    content.add(object.getString("content"));
                                    id.add(object.getString("id"));
                                }
                                nullPost.setText("");

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
                return super.getParams();
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
        setContentView(R.layout.activity_forum_expand);
        expandedName = (TextView)findViewById(R.id.expanded_user_name);
        expandedTitle = (TextView)findViewById(R.id.expanded_thread_title);
        expandedContent=(TextView)findViewById(R.id.expanded_thread_content);
        expandedID = (TextView)findViewById(R.id.expanded_thread_id);
        expanded_user_pic = (CircleImageView)findViewById(R.id.expanded_user_profile_pic);
        getPic(getName,expanded_user_pic);
        expandedName.setText(getName);
        expandedTitle.setText(getTitle);
        expandedContent.setText(getContent);
        expandedID.setText(getID);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ForumActivity.this,ForumActivity.class);
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
            Intent intent = new Intent(ForumActivity.this,MainActivity.class);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setUserType("");
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(ForumActivity.this,ChangePassword.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(ForumActivity.this,UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
