package com.example.project1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register3Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ProgressBar loading;
    //    private static String URL_REGIST ="http://192.168.0.187/jee/register.php";
    private String localhost;
    private static String URL_REGIST ;
    private static String URL_UPLOAD ;
    private DatabaseHelper db;
    private EditText e1,e2,e3,e5,e6,e7;
    private Button b1,b2;
    private Spinner spinner;
    private TextView inputLabel;
    private TextView mTextMessage;
    private Button uploadButton;
    private Bitmap bitmap;
    private CircleImageView profile_pic;
//    private Patient patient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        //Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        Intent i1 = new Intent(Register3Activity.this,MainActivity.class);
                        startActivity(i1);
                        break;
                }
                return true;
            }
        });
        //finish

        localhost = getString(R.string.localhost);
        URL_REGIST =localhost+":3000/register3/";
        URL_UPLOAD =localhost+"/jee/setPic.php";
        db = new DatabaseHelper(this);
        loading = (ProgressBar)findViewById(R.id.loading);
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



        //click on login button
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register3Activity.this, LoginActivity.class);
                startActivity(intent);
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
                final String s5 = e5.getText().toString().trim(); //name
                final String s6 = e6.getText().toString(); //contact no
                final String s7 = e7.getText().toString(); //age

                //check if fields are empty
                if (s1.equals("") || s2.equals("") || s3.equals("") || s5.equals("") || s6.equals("") || s7.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field(s) are empty", Toast.LENGTH_SHORT).show();
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
                        /* mysql posting */

                        Log.e("TAG", "log"+ s1+ " "+s2+" "+s5+" "+s6+" "+s7 );
                        registerAccount(s1,s2,s5,s6,s7);
                        setProfile_pic(s1,s5);

                        /* set user instance */
                        User.getInstance().setEmail(s1); //email
                        User.getInstance().setPassword(s2); //pw
                        User.getInstance().setUserName(s5);
                        User.getInstance().setContact(s6);
                        User.getInstance().setAge(s7);
                        Log.e("Tag", "hello");
                    } else {
                        Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        b1.setVisibility(View.VISIBLE);
                    }
                }
            }



        });
        mTextMessage = (TextView) findViewById(R.id.message);

        //click upload button
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });


    }

    private void registerAccount(final String email, final String password, final String name,
                                 final String contact, final String age) {
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
                        Intent intent = new Intent(Register3Activity.this, LoginActivity.class);
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
                params.put("email",email);
                params.put("password",password);
                params.put("name",name);
                params.put("contact",contact);
                params.put("age",age);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void setProfile_pic(final String email, final String name) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String success = jsonObject.getString("success");
                    Log.e("TAG", "success of pic "+success );
                    if(success.equals("1")){
//                        Toast.makeText(getApplicationContext(),"Register Success", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                        startActivity(intent);
                    }
                    else if(success.equals("0")){
//                        Toast.makeText(getApplicationContext(),"Error, Please Try Again Later", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        b1.setVisibility(View.VISIBLE);
                    }
                    else if(success.equals("-1")){
//                        Toast.makeText(getApplicationContext(),"Email is Used", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(getApplicationContext(),"Register Fail", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        b1.setVisibility(View.VISIBLE);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("photo",getStringImage(bitmap));
                params.put("name",name);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


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

