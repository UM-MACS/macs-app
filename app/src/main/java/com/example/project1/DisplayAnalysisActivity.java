package com.example.project1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class DisplayAnalysisActivity extends AppCompatActivity {
    private static String URL_API_SA;
    private static String URL_GET_EMOTION;
    private static String URL_UPDATE_EMOTION;
    private static String localhost;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_analysis);
        sessionManager = new SessionManager(this);
        URL_API_SA = "https://api.deepai.org/api/sentiment-analysis";
        localhost = getString(R.string.localhost);
        URL_GET_EMOTION = localhost+":3000/getEmotion";
        URL_UPDATE_EMOTION = localhost+":3000/updateEmotion";
        getEmotion();

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_emotion_tracking);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(DisplayAnalysisActivity.this, DisplayAnalysisActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(DisplayAnalysisActivity.this, viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(DisplayAnalysisActivity.this, eventAssessment.class);
                        startActivity(i4);
                        break;
                    case R.id.navigation_faq:
                        Intent i5 = new Intent(DisplayAnalysisActivity.this, FAQ.class);
                        startActivity(i5);
                        break;
                    case R.id.navigation_forum:
                        Intent i6 = new Intent(DisplayAnalysisActivity.this, SpecialistForumActivity.class);
                        startActivity(i6);
                        break;

                }
                return true;
            }
        });
    }

    private void getEmotion() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_EMOTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            Log.e("TAG", "success: "+success );
                            ArrayList<String> textArray = new ArrayList<>();
                            ArrayList<String>idArray = new ArrayList<>();

                            if(success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String text = object.getString("expression");
                                    String id = object.getString("id");
                                    textArray.add(text);
                                    idArray.add(id);
                                    Log.e("TAG", "text"+text );
                                    Log.e("TAG", "id"+id );
                                }
                                for (int i =0; i<jsonArray.length();i++){
                                    String text = textArray.get(i);
                                    String id = idArray.get(i);
                                Log.e("TAG", "text1 and id1 "+text+id );
                                }

                            } else{
                                Log.e("TAG", "fail" );
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
                return super.getParams();
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(DisplayAnalysisActivity.this, SpecialistForumActivity.class);
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
            Intent intent = new Intent(DisplayAnalysisActivity.this,MainActivity.class);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setUserType("");
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(DisplayAnalysisActivity.this,ChangePassword.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(DisplayAnalysisActivity.this,UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
