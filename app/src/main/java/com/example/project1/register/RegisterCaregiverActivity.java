package com.example.project1.register;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.login.LoginActivity;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.mainPage.MainActivity;
import com.example.project1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterCaregiverActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    private ProgressBar loading;
    private String localhost;
    private static String URL_REGIST;
    private static String URL_UPLOAD ;
//    private DatabaseHelper db;
    private EditText e1,e2,e3,e5,e6,e7;
    private Button b1,b2;
    private Spinner spinner;
    private TextView inputLabel;
    private TextView mTextMessage;
    private Button uploadButton;
    private Bitmap bitmap;
    private CircleImageView profile_pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_caregiver);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        Intent i1 = new Intent(RegisterCaregiverActivity.this, MainActivity.class);
                        startActivity(i1);
                        break;
                }
                return true;
            }
        });
        //finish

        localhost = getString(R.string.localhost);
        URL_REGIST =localhost+"/register2/";
        URL_UPLOAD =localhost+"/jee/setPic2.php";
//        db = new DatabaseHelper(this);
        loading = (ProgressBar)findViewById(R.id.register2_loading);
        e1 = (EditText)findViewById(R.id.email);
        e2 = (EditText)findViewById(R.id.confirm_password);
        e3 = (EditText)findViewById(R.id.confirm_password2);
        e5 = (EditText)findViewById(R.id.editText); //name
        e6 = (EditText)findViewById(R.id.editText3); //contact no
        e7 = (EditText)findViewById(R.id.editText4); //age
        b1 = (Button)findViewById(R.id.register);
        b2 = (Button)findViewById(R.id.login);
        uploadButton = (Button)findViewById(R.id.upload_button);
        profile_pic = (CircleImageView)findViewById(R.id.upload_pic);

        //select relationship
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.relationship, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);


        //click on login button
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterCaregiverActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //click upload button
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        //click on register button
        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                b1.setVisibility(View.GONE);
                final String s1 = e1.getText().toString().trim(); //email
                final String s2 = e2.getText().toString().trim(); //pw
                String s3 = e3.getText().toString(); //confirm pw
                final String s4 = spinner.getSelectedItem().toString(); //relationship
                final String s5 = e5.getText().toString().trim(); //name
                final String s6 = e6.getText().toString(); //contact no
                final String s7 = e7.getText().toString(); //age


                //check if fields are empty
                if (s1.equals("") || s2.equals("") || s3.equals("") || s4.equals("Please Select One") || s5.equals("") || s6.equals("") || s7.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Field(s) are empty", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    b1.setVisibility(View.VISIBLE);
                } else if(!s1.contains("@")){
                    Toast.makeText(getApplicationContext(), "Please Enter a Valid Email", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    b1.setVisibility(View.VISIBLE);
                }
                else if (Integer.parseInt(s7)>120){
                    Toast.makeText(getApplicationContext(), "Please Enter a Valid Age", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    b1.setVisibility(View.VISIBLE);
                }
                else {
                    if (s2.equals(s3)) { //check if password matches
                        registerAccount(s1,s5,s6,s2,s4,s7);

//                        setProfile_pic(s1,s5);

                        /* set user instance */
//                        User.getInstance().setEmail(s1); //email
//                        User.getInstance().setPassword(s2); //pw
//                        User.getInstance().setUserName(s5);
//                        User.getInstance().setContact(s6);
//                        User.getInstance().setAge(s7);
                        Log.e("Tag", "hello");
                    }
//                    else if(s6.length()>11 || s6.length()<10){
//                        Toast.makeText(getApplicationContext(), "Please Enter a Valid Phone Number"
//                                , Toast.LENGTH_SHORT).show();
//                        loading.setVisibility(View.GONE);
//                        b1.setVisibility(View.VISIBLE);
//                    }

                    else {
                        Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        b1.setVisibility(View.VISIBLE);
                    }
                }
            }



        });
        mTextMessage = (TextView) findViewById(R.id.message);

    }

    private void registerAccount(final String email, final String name, final String contact
                                ,final String password, final String relationship
                                ,final String age){
        /* mysql posting */
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String success = jsonObject.getString("success");
                    Log.e("TAG", "success"+success );
                    if(success.equals("1")){
                        Toast.makeText(getApplicationContext(),"Register Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterCaregiverActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else if(success.equals("0")){
                        Toast.makeText(getApplicationContext(),"Error, Please Try Again Later", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        b1.setVisibility(View.VISIBLE);
                    }
                    else if(success.equals("-1")){
                        Toast.makeText(getApplicationContext(),"Email is Used", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        b1.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Register Fail", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    b1.setVisibility(View.VISIBLE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Register Fail", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        b1.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String img= "";
                if (bitmap != null){
                    img = getStringImage(bitmap);
                }
                params.put("name",name);
                params.put("email",email);
                params.put("password",password);
                params.put("contact",contact);
                params.put("age",age);
                params.put("relationship",relationship);
                params.put("photo",img);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


//    private void setProfile_pic(final String email, final String name) {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try{
//                    JSONArray jsonArray = new JSONArray(response);
//                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                    String success = jsonObject.getString("success");
//                    Log.e("TAG", "success of pic "+success );
//                    if(success.equals("1")){
////                        Toast.makeText(getApplicationContext(),"Register Success", Toast.LENGTH_SHORT).show();
////                        Intent intent = new Intent(RegisterPatientActivity.this, LoginActivity.class);
////                        startActivity(intent);
//                    }
//                    else if(success.equals("0")){
////                        Toast.makeText(getApplicationContext(),"Error, Please Try Again Later", Toast.LENGTH_SHORT).show();
//                        loading.setVisibility(View.GONE);
//                        b1.setVisibility(View.VISIBLE);
//                    }
//                    else if(success.equals("-1")){
////                        Toast.makeText(getApplicationContext(),"Email is Used", Toast.LENGTH_SHORT).show();
//                        loading.setVisibility(View.GONE);
//                        b1.setVisibility(View.VISIBLE);
//                    }
//                } catch (JSONException e){
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),"Register Fail", Toast.LENGTH_SHORT).show();
//                    loading.setVisibility(View.GONE);
//                    b1.setVisibility(View.VISIBLE);
//                }
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
////                        Toast.makeText(getApplicationContext(),"Register Fail", Toast.LENGTH_SHORT).show();
//                        loading.setVisibility(View.GONE);
//                        b1.setVisibility(View.VISIBLE);
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("email",email);
//                params.put("photo",getStringImage(bitmap));
//                params.put("name",name);
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        requestQueue.add(stringRequest);
//    }


    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Photo"),1);
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



    public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

        parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> parent){

    }

}

