package com.example.project1.forum.caregiver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.R;
import com.example.project1.chat.OnEditTextRightDrawableTouchListener;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.CurrentUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CaregiverCreateForumPostActivity extends BaseActivity {
    private EditText titleInput, contentInput;
    private Button cancelButton, postButton;
    private CheckBox checkBox;
    private String localhost;
    private String name, email;
    private static String URL;
    private ImageView postImage;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        Log.e("TAG", "Caregiver user name "+ CurrentUser.getInstance().getNRIC() );
        email = CurrentUser.getInstance().getNRIC();
        name = CurrentUser.getInstance().getUserName();
        localhost = getString(R.string.localhost);
        URL = localhost+"/postingToCaregiverForum/";

        titleInput = (EditText)findViewById(R.id.post_title);
        contentInput = (EditText)findViewById(R.id.post_content);
        cancelButton = (Button) findViewById(R.id.post_cancel);
        postButton = (Button)findViewById(R.id.post_button);
        checkBox = (CheckBox)findViewById(R.id.anonymous_checkbox);
        postImage = (ImageView)findViewById(R.id.post_image);

        titleInput.setOnTouchListener(new OnEditTextRightDrawableTouchListener(titleInput) {
            @Override
            public void OnEditTextClick() { showKeyboard(); }
            @Override
            public void OnDrawableClick() {
                choosePicture();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CaregiverCreateForumPostActivity.this, CaregiverForumActivity.class);
                startActivity(i);
            }
        });
    }

    public void onPost(View v) {
        final String title = titleInput.getText().toString();
        final String content = contentInput.getText().toString();
        if (!title.equals("") || !content.equals("")) {
            postButton.setEnabled(false);
            final String anonymous;
            if (checkBox.isChecked()) {
                anonymous = "true";
            } else {
                anonymous = "";
            }
            Date d = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            final String date = dateFormat.format(d);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {
                                    Toast.makeText(getApplicationContext(), getString(R.string.post_success),
                                        Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(CaregiverCreateForumPostActivity.this, CaregiverForumActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    String img= "";
                    if (bitmap != null){
                        img = getStringImage(bitmap);
                    }
                    params.put("email", email);
                    params.put("type", CurrentUser.getInstance().getUserType());
                    params.put("name", name);
                    params.put("title", title);
                    params.put("content", content);
                    params.put("anonymous", anonymous);
                    params.put("date", date);
                    params.put("postPhoto",img);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new
                    DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, 1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            );
            requestQueue.add(stringRequest);
        } else{
            Toast.makeText(getApplicationContext(), getString(R.string.enter_title_content),Toast.LENGTH_SHORT)
                    .show();
        }
    }

    // === Adding upload photo features code ===
    public void showKeyboard(){
        titleInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(titleInput, InputMethodManager.SHOW_IMPLICIT);
    }

    public void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), filePath);
                postImage.setVisibility(View.VISIBLE);
                postImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray,Base64.DEFAULT);
        Log.e("TAG", "encodedImage"+encodedImage );
        return encodedImage;
    }
}
