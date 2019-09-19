package com.example.project1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewFavouriteList extends AppCompatActivity {
    private String localhost;
    private static String URL_GET_FAV;
    private static String URL_GETPIC;
    private static String URL_ADD_FAV;
    private static String URL_DEL_FAV;
    private String picture, ID;
    private TextView nullPost, username, threadTitle, threadContent, threadID;
    private LinearLayout forumParentLinearLayout;
    private CircleImageView user_pic, expanded_user_pic;
    private FloatingActionButton b1;
    private BottomNavigationView bottomNavigationView;
    private TextView expandedName, expandedTitle, expandedContent, expandedID;
    private Button editButton, deleteButton, postButton;
    private EditText postTitle, postContent;
    private EditText searchEditText;
    private Button searchButton;
    private ImageView fav_icon, unfav_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        localhost = getString(R.string.localhost);
        URL_GET_FAV = localhost+":3000/myFavouriteList/";
        URL_ADD_FAV = localhost+":3000/addToFavourite/";
        URL_DEL_FAV = localhost+":3000/removeFavourite/";
        URL_GETPIC = localhost+"/jee/getPic.php";

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
                            Log.e("TAG", "success"+success );

                            if(success.equals("1")){
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    name.add(object.getString("name"));
                                    title.add(object.getString("title"));
                                    content.add(object.getString("content"));
                                    id.add(object.getString("id"));
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
        Log.e("TAG", "get name"+ getName );
        threadTitle = (TextView) ((View) v).findViewById(R.id.thread_title);
        final String getTitle = (String) threadTitle.getText().toString();
        Log.e("TAG", "get title"+ getTitle );
        threadContent = (TextView) ((View) v).findViewById(R.id.thread_content);
        final String getContent = (String) threadContent.getText().toString();
        Log.e("TAG", "get content"+ getContent );
        threadID = (TextView) ((View) v).findViewById(R.id.thread_id);
        final String getID = (String) threadID.getText().toString();
        Log.e("TAG", "get id"+ getID );
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
        fav_icon = (ImageView)findViewById(R.id.addFav);
        unfav_icon = (ImageView)findViewById(R.id.removeFav);
        fav_icon.setVisibility(View.GONE);
        unfav_icon.setVisibility(View.VISIBLE);

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
