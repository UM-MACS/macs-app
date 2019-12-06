package com.example.project1;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.session.MediaSessionManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class emotionActivity extends AppCompatActivity{
    DatabaseHelper db;
    Button b1,b2,b3,b4,b5,b6,submitButton;
    ArrayList<String> arrayList;
    EditText expression;
    FrameLayout frameLayout;
    private String localhost;
    private static String URL;
    private static String URL_API_SA;
    Date currentTime;
    private TextView mTextMessage;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(User.getInstance().getUserType().equals("Specialist")){
            Intent i = new Intent(emotionActivity.this,DisplayAnalysisActivity.class);
            startActivity(i);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        localhost = getString(R.string.localhost);
        URL =localhost+":3000/emotion";
        URL_API_SA ="https://api.deepai.org/api/sentiment-analysis";
        HashMap<String,String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);
        String mEmail = user.get(sessionManager.EMAIL);
        String mType = user.get(sessionManager.TYPE);
        Log.e("TAG", "shared preference name is "+mName );
        User.getInstance().setEmail(mEmail);
        User.getInstance().setUserName(mName);
        User.getInstance().setUserType(mType);


        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) findViewById(R.id.navigation);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_emotion_tracking);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(emotionActivity.this,
                                emotionActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(emotionActivity.this,
                                viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(emotionActivity.this,
                                eventActivity.class);
                        startActivity(i4);
                        break;
                    case R.id.navigation_chat:
                        Intent i=getPackageManager().getLaunchIntentForPackage("com.example.fypchat");
                        startActivity(i);
                        break;
                    case R.id.navigation_forum:
                        if(User.getInstance().getUserType().equalsIgnoreCase("Caregiver")){
                            Intent i6 = new Intent(emotionActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if(User.getInstance().getUserType().equalsIgnoreCase("Patient")){
                            Intent i6 = new Intent(emotionActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        } else{
                            Intent i6 = new Intent(emotionActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        }


                }
                return true;
            }
        });


        //end
        frameLayout = (FrameLayout) findViewById(R.id.emotion_page);
        frameLayout.getForeground().setAlpha(0);
        b1 = (Button) findViewById(R.id.emotion1);
        b2 = (Button) findViewById(R.id.emotion2);
        b3 = (Button) findViewById(R.id.emotion3);
        b4 = (Button) findViewById(R.id.emotion4);
        b5 = (Button) findViewById(R.id.emotion5);
        b6 = (Button) findViewById(R.id.emotion6);
        arrayList = new ArrayList<String>();
        expression = (EditText) findViewById(R.id.expression);
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        db = new DatabaseHelper(this);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String date = dateFormat.format(d);
//                sentimentAnalysis(User.getInstance().getEmail(), User.getInstance().getUserType(), date,
//                        "Very Happy(def)");
                insert(User.getInstance().getEmail(),User.getInstance().getUserType(),
                        date,"Happy(def)","Positive");
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String date = dateFormat.format(d);
//                sentimentAnalysis(User.getInstance().getEmail(), User.getInstance().getUserType(), date,
//                        "Happy(def)");
                insert(User.getInstance().getEmail(),User.getInstance().getUserType(),
                        date,"Surprised(def)","Positive");
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String date = dateFormat.format(d);
//                sentimentAnalysis(User.getInstance().getEmail(), User.getInstance().getUserType(), date,
//                        "Smiling(def)");
                insert(User.getInstance().getEmail(),User.getInstance().getUserType(),
                        date,"Disgusted(def)","Negative");
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String date = dateFormat.format(d);
//                sentimentAnalysis(User.getInstance().getEmail(), User.getInstance().getUserType(), date,
//                        "Unhappy(def)");
                insert(User.getInstance().getEmail(),User.getInstance().getUserType(),
                        date,"Fear(def)","Neutral");
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String date = dateFormat.format(d);
//                sentimentAnalysis(User.getInstance().getEmail(), User.getInstance().getUserType(), date,
//                        "Angry(def)");
                insert(User.getInstance().getEmail(),User.getInstance().getUserType(),
                        date,"Angry(def)","Negative");
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String date = dateFormat.format(d);
//                sentimentAnalysis(User.getInstance().getEmail(), User.getInstance().getUserType(), date,
//                        "Sad(def)");
                insert(User.getInstance().getEmail(),User.getInstance().getUserType(),
                        date,"Sad(def)","Negative");
            }
        });

        //submit input in text box
        expression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expression.setCursorVisible(true);
            }
        });
        submitButton = (Button) findViewById(R.id.submitExpressionButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = expression.getText().toString();
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                final String date = dateFormat.format(d);
                if (text.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter something in the text box",
                            Toast.LENGTH_LONG).show();
                } else {
                    sentimentAnalysis(User.getInstance().getEmail(), User.getInstance().getUserType(), date,
                            text);
                }
            }
        });
    }

    private void insert(final String email, final String type, final String date,
                        final String expressionInput,final String output) {
        Log.e("TAG", "expression input: "+expressionInput );
        Log.e("TAG", "date: "+date );
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String success = jsonObject.getString("success");
                if(success.equals("1")){
                    Log.e("TAG", "success" );
                    //Create pop up window
                    LayoutInflater inflater1 =
                            (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View view = inflater1.inflate(R.layout.pop_up_message, null);
                    // create a focusable PopupWindow with the given layout and correct size
                    final PopupWindow pw = new PopupWindow(view,
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    //dim background
                    frameLayout.getForeground().setAlpha(220);
                    ((Button) view.findViewById(R.id.pop_up_button)).setOnClickListener
                            (new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pw.dismiss();
                            frameLayout.getForeground().setAlpha(0);
                            expression.setText("");
                        }
                    });
                    pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    pw.setTouchInterceptor(new View.OnTouchListener() {
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                                pw.dismiss();
                                return true;
                            }
                            return false;
                        }
                    });
                    pw.setOutsideTouchable(true);
                    // display the pop-up in the center
                    pw.showAtLocation(view, Gravity.CENTER, 0, 0);
                }
                else if(success.equals("0")){
                    Toast.makeText(getApplicationContext(),"Fail to submit",
                            Toast.LENGTH_SHORT).show();
                    expression.setText("");
                }
            }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error, Please Try Again Later",
                            Toast.LENGTH_SHORT).show();
                }
        }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Error, Please Try Again Later",
                                Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email",email);
                params.put("type",type);
                params.put("date",date);
                params.put("expression",expressionInput);
                params.put("output",output);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void sentimentAnalysis(final String email, final String type, final String date,
                                   final String expressionInput) {
        final String input =  expressionInput.replaceAll("[^a-zA-Z ]", "").toLowerCase();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_API_SA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("TAG", "object " );
                            JSONObject object = new JSONObject(response);
                            JSONArray output = object.getJSONArray("output");
                            Log.e("TAG", "output1 is "+output );
//                            output = output.substring(2,output.length()-2);

                            for (int i =0; i<output.length();i++){
                                insert(email,type,date,expressionInput,output.getString(i));
                                Log.e("TAG", "output2 is "+output.getString(i) );
                            }
//                            updateAnalysis(id,output);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("TAG", "exception e");
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
                params.put("text", input);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<>();
                header.put("api-key","369aa822-9f19-4ae2-826f-df963534c2c9");
//                header.put("Content-Type","text/plain");
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            Intent intent = new Intent(emotionActivity.this,MainActivity.class);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setUserType("");
//            finish();
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(emotionActivity.this,ChangePassword.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(emotionActivity.this,UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(emotionActivity.this, FAQ.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
