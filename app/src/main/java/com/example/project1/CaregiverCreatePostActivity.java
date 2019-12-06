package com.example.project1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CaregiverCreatePostActivity extends AppCompatActivity {
    private EditText titleInput, contentInput;
    private Button cancelButton, postButton;
    private CheckBox checkBox;
    private String localhost;
    private String name, email;
    private static String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Log.e("TAG", "Caregiver user name "+User.getInstance().getEmail() );
        email = User.getInstance().getEmail();
        name = User.getInstance().getUserName();
        localhost = getString(R.string.localhost);
        URL = localhost+":3000/postingToCaregiverForum/";

        titleInput = (EditText)findViewById(R.id.post_title);
        contentInput = (EditText)findViewById(R.id.post_content);
        cancelButton = (Button) findViewById(R.id.post_cancel);
        postButton = (Button)findViewById(R.id.post_button);
        checkBox = (CheckBox)findViewById(R.id.anonymous_checkbox);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CaregiverCreatePostActivity.this, CaregiverForumActivity.class);
                startActivity(i);
            }
        });
    }

    public void onPost(View v){
        final String title = titleInput.getText().toString();
        final String content = contentInput.getText().toString();
        final String anonymous;
        if(checkBox.isChecked()){
            anonymous = "true";
        } else{
            anonymous = "";
        }
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String date = dateFormat.format(d);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),"Post Success",
                                        Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(CaregiverCreatePostActivity.this, CaregiverForumActivity.class);
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
                params.put("email",email);
                params.put("type",User.getInstance().getUserType());
                params.put("name",name);
                params.put("title", title);
                params.put("content", content);
                params.put("anonymous",anonymous);
                params.put("date",date);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
