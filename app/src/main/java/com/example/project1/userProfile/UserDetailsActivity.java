package com.example.project1.userProfile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.project1.PublicComponent;
import com.example.project1.R;
import com.example.project1.changeLanguage.ChangeLanguageActivity;
import com.example.project1.changePassword.ChangePasswordActivity;
import com.example.project1.chat.ChatChannelListActivity;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.eventReminder.EventReminderActivity;
import com.example.project1.exercise.ExerciseDashboardActivity;
import com.example.project1.forum.ForumActivity;
import com.example.project1.forum.caregiver.CaregiverForumActivity;
import com.example.project1.forum.imageFile.ImgLoader;
import com.example.project1.forum.specialist.SpecialistForumActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.login.component.SessionManager;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.onboarding.OnboardingBaseActivity;
import com.example.project1.questionnaire.QuestionnaireActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsActivity extends BaseActivity {
    private SessionManager sessionManager;
    private EditText nameET, emailET, ageET, phoneET;
    private String nric;
    private RelativeLayout container;
    private String localhost, URL_GET_DETAILS, URL_UPDATE_DETAILS;
    private Button uploadButton;
    private CircleImageView profile_pic;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        sessionManager = new SessionManager(this);

        //drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if(CurrentUser.getInstance().getUserType().equals("Admin")){
            bottomNavigationView.setVisibility(View.GONE);
        }
        if(CurrentUser.getInstance().getUserType().equals("Caregiver")||
                CurrentUser.getInstance().getUserType().equals("Specialist")){
            MenuItem item = bottomNavigationView.getMenu().findItem(R.id.navigation_exercise);
            item.setVisible(false);
        }
//        MenuItem itemForum = bottomNavigationView.getMenu().findItem(R.id.navigation_forum);
//        itemForum.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_emotion_assessment:
                        Intent i2 = new Intent(UserDetailsActivity.this, EmotionAssessmentActivity.class);
                        startActivity(i2);
                        break;
                    case R.id.navigation_exercise:
                        Intent i3 = new Intent(UserDetailsActivity.this, ExerciseDashboardActivity.class);
                        startActivity(i3);
                        break;
//                    Intent i4 = new Intent(ForumActivity.this, QuestionnaireListActivity.class);
//                        startActivity(i4);
//                        break;
//                    case R.id.navigation_faq:
//                        Intent i5 = new Intent(ForumActivity.this, FAQActivity.class);
//                        startActivity(i5);
//                        break;
                    case R.id.navigation_forum:
                        if(CurrentUser.getInstance().getUserType().equalsIgnoreCase(PublicComponent.SPECIALIST)
                                || CurrentUser.getInstance().getUserType().equalsIgnoreCase(PublicComponent.ADMIN)){
                            Intent i6 = new Intent(UserDetailsActivity.this, SpecialistForumActivity.class);
                            startActivity(i6);
                            break;
                        } else if(CurrentUser.getInstance().getUserType().equalsIgnoreCase(PublicComponent.CAREGIVER)){
                            Intent i6 = new Intent(UserDetailsActivity.this, CaregiverForumActivity.class);
                            startActivity(i6);
                            break;
                        } else {
                            Intent i6 = new Intent(UserDetailsActivity.this, ForumActivity.class);
                            startActivity(i6);
                            break;
                        }
                    case R.id.navigation_chat:
                        Intent i7 = new Intent(UserDetailsActivity.this, ChatChannelListActivity.class);
                        startActivity(i7);
                        break;
                }
                return true;
            }
        });

        uploadButton = (Button)findViewById(R.id.upload_button);
        profile_pic = (CircleImageView)findViewById(R.id.upload_pic);
        nameET = (EditText)findViewById(R.id.name_et);
        emailET = (EditText)findViewById(R.id.email_et);
        ageET = (EditText)findViewById(R.id.age_et);
        phoneET = (EditText)findViewById(R.id.phone_et);
        nric = CurrentUser.getInstance().getNRIC();

        container = (RelativeLayout)findViewById(R.id.container);

        localhost = getString(R.string.localhost);
        if(CurrentUser.getInstance().getUserType().equals("Patient")){
            URL_GET_DETAILS = localhost+"/getDetailsPatient";
            URL_UPDATE_DETAILS = localhost+"/updateDetailsPatient";
        }
        else if (CurrentUser.getInstance().getUserType().equals("Caregiver")){
            URL_GET_DETAILS = localhost+"/getDetailsCaregiver";
            URL_UPDATE_DETAILS = localhost+"/updateDetailsCaregiver";
        } else{
            URL_GET_DETAILS = localhost+"/getDetailsSpecialist";
            URL_UPDATE_DETAILS = localhost+"/updateDetailsSpecialist";
        }
        onGetDetail(nric);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

    }

    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                getString(R.string.select_photo)),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && resultCode == RESULT_OK
                && data !=null && data.getData()!= null){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), filePath);
                profile_pic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new
                ByteArrayOutputStream();
        int width = bitmap.getWidth();
        int height = (int) (bitmap.getHeight() / (float) width * 120.0f);
        bitmap = Bitmap.createScaledBitmap(bitmap,120,height,false);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,
                byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray
                ,Base64.DEFAULT);
        Log.e("TAG", "encodedImage"+encodedImage );
        return encodedImage;
    }

    public void getPic(final String photo, final CircleImageView view){
        //load picture example
        int loader = R.drawable.ic_user;
        ImgLoader imgLoader = new ImgLoader(getApplicationContext());
        imgLoader.DisplayImage(photo, loader, view);
        Log.e("TAG", "success loading photo" );
    }

    public void onGetDetail(final String nric){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            Log.e("TAG", "success: "+ success);
                            ArrayList<String> nameList = new ArrayList<>();
                            ArrayList <String> emailList = new ArrayList<>();
                            ArrayList <String> phoneNoList = new ArrayList<>();
                            ArrayList <String> ageList = new ArrayList<>();
                            ArrayList<String> photo = new ArrayList<>();

                            if(success.equals("1")){
                                for (int i=0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    nameList.add(object.getString("name"));
                                    emailList.add(object.getString("email"));
                                    phoneNoList.add(object.getString("phoneNo"));
                                    ageList.add(object.getString("age"));
                                    photo.add(object.getString("photo"));
                                }

                                for (int i = 0; i < nameList.size(); i++) {
                                    nameET.setText(nameList.get(i));
                                    emailET.setText(emailList.get(i));
                                    phoneET.setText(phoneNoList.get(i));
                                    ageET.setText(ageList.get(i));
                                    getPic(photo.get(i), profile_pic);
                                }
                                container.setVisibility(View.GONE);
                            } else{
                                container.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),getString(R.string.try_later),Toast.LENGTH_SHORT).show();
                            container.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        container.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),getString(R.string.try_later),Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nric",nric);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void onUpdateDetails(View view) {
        final String name = nameET.getText().toString().trim();
        final String email = emailET.getText().toString().trim();
        final String phoneNo = phoneET.getText().toString().trim();
        final String age = ageET.getText().toString().trim();

        if(name.equals("")||phoneNo.equals("")||age.equals("")){
            Toast.makeText(getApplicationContext(), getString(R.string.empty_details),Toast.LENGTH_SHORT).show();
        } else if (!email.equals("") && !email.contains("@")){
            Toast.makeText(getApplicationContext(), getString(R.string.enter_valid_email),Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(age)>120){
            Toast.makeText(getApplicationContext(), getString(R.string.enter_valid_age),Toast.LENGTH_SHORT).show();
        }
        else {
            container.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_DETAILS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                                    container.setVisibility(View.GONE);
                                    ageET.setCursorVisible(false);
                                    nameET.setCursorVisible(false);
                                    emailET.setCursorVisible(false);
                                    phoneET.setCursorVisible(false);
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_SHORT).show();
                                    container.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_SHORT).show();
                                container.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            container.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), getString(R.string.try_later), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    String img= "";
                    if (bitmap != null){
                        img = getStringImage(bitmap);
                    }
                    Map<String, String> params = new HashMap<>();
                    params.put(PublicComponent.NRIC, nric);
                    params.put(PublicComponent.NAME, name);
                    params.put(PublicComponent.EMAIL, email);
                    params.put(PublicComponent.CONTACT_NO, phoneNo);
                    params.put(PublicComponent.AGE, age);
                    params.put("photo",img);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(sessionManager.isLogin()) {
            if (CurrentUser.getInstance().getUserType().equals(PublicComponent.PATIENT)) {
                getMenuInflater().inflate(R.menu.nav, menu);
                return true;
            } else if (CurrentUser.getInstance().getUserType().equals(PublicComponent.ADMIN)){
                getMenuInflater().inflate(R.menu.admin_nav, menu);
                return true;
            } else {
                getMenuInflater().inflate(R.menu.other_users_nav, menu);
                return true;
            }
        } return true;
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
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            CurrentUser.getInstance().setUserName("");
            CurrentUser.getInstance().setNRIC("");
            CurrentUser.getInstance().setUserType("");
            return true;
        }

        if (id == R.id.action_change_password){
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_user_profile){
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_faq) {
            Intent intent = new Intent(this, OnboardingBaseActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_questionnaire){
            Intent intent = new Intent(this, QuestionnaireActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_event_reminder){
            Intent intent = new Intent(this, EventReminderActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_switch_language){
            Intent intent = new Intent(this, ChangeLanguageActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
