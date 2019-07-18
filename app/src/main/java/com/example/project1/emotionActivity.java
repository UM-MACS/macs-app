package com.example.project1;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import org.json.JSONException;
import org.json.JSONObject;

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
    private static String URL ="http://192.168.0.187/jee/emotion.php";
    Date currentTime;
//    ContentValues values;
    private TextView mTextMessage;
//    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_emotion_tracking);
        item.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_tracking:
                        Intent i2 = new Intent(emotionActivity.this, emotionActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_schedule_appointment:
                        Intent i3 = new Intent(emotionActivity.this, viewEventActivity.class);
                        startActivity(i3);
                        break;
                    case R.id.nagivation_event_assessment:
                        Intent i4 = new Intent(emotionActivity.this, eventAssessment.class);
                        startActivity(i4);
                        break;
                    case R.id.navigation_faq:
                        Intent i5 = new Intent(emotionActivity.this, FAQ.class);
                        startActivity(i5);
                        break;
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
                Date c = Calendar.getInstance().getTime();
                final String date = c.toString();
                insert(User.getInstance().getEmail(), User.getInstance().getUserType(), date, "Very Happy(def)");
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                final String date = c.toString();
                insert(User.getInstance().getEmail(), User.getInstance().getUserType(), date, "Happy(def)");
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                final String date = c.toString();
                insert(User.getInstance().getEmail(), User.getInstance().getUserType(), date, "Smiling(def)");
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                final String date = c.toString();
                insert(User.getInstance().getEmail(), User.getInstance().getUserType(), date, "Unhappy(def)");
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                final String date = c.toString();
                insert(User.getInstance().getEmail(), User.getInstance().getUserType(), date, "Angry(def)");
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                final String date = c.toString();
                insert(User.getInstance().getEmail(), User.getInstance().getUserType(), date, "Sad(def)");
            }
        });

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
                String replacedText = text.replace("'","\\\'");
                replacedText = replacedText.replace(" \"" ,"\\\"");
                Log.e("tag", "text is " + replacedText);
                Date c = Calendar.getInstance().getTime();
                final String date = c.toString();
                if (text.equals("")) {
                    Toast.makeText(getApplicationContext(), "Error! Please Enter something in the text box! ", Toast.LENGTH_LONG).show();
                } else {
                    insert(User.getInstance().getEmail(), User.getInstance().getUserType(), date, replacedText);
                }
            }
        });
    }

    private void insert(final String email, final String type, final String date, final String expressionInput) {
        Log.e("TAG", "expression input: "+expressionInput );
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                if(success.equals("1")){
                    //Create pop up window
                    LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View view = inflater1.inflate(R.layout.pop_up_message, null);
                    // create a focusable PopupWindow with the given layout and correct size
                    final PopupWindow pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    //dim background
                    frameLayout.getForeground().setAlpha(220);
                    ((Button) view.findViewById(R.id.pop_up_button)).setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(getApplicationContext(),"Fail to submit",Toast.LENGTH_SHORT).show();
                    expression.setText("");
                }
            }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error, Please Try Again Later",Toast.LENGTH_SHORT).show();
                }
        }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Error, Please Try Again Later",Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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
            Intent intent = new Intent(emotionActivity.this,MainActivity.class);
            startActivity(intent);
            User.getInstance().setUserName("");
            User.getInstance().setEmail("");
            User.getInstance().setPassword("");
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(emotionActivity.this,ChangePassword.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
