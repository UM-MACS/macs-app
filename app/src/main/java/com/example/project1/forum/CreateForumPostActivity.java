package com.example.project1.forum;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
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
import androidx.core.app.ActivityCompat;

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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateForumPostActivity extends BaseActivity {
    private EditText titleInput, contentInput;
    private Button cancelButton, postButton;
    private CheckBox checkBox;
    private String localhost;
    private static String URL;
    private ImageView postImage;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        localhost = getString(R.string.localhost);
        URL = localhost+"/postingToForum/";

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
                Intent i = new Intent(CreateForumPostActivity.this, ForumActivity.class);
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
            Log.e("TAG", "onPost: Date: "+date );

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
                                    Intent i = new Intent(CreateForumPostActivity.this, ForumActivity.class);
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
                    params.put("email", CurrentUser.getInstance().getNRIC());
                    params.put("type", CurrentUser.getInstance().getUserType());
                    params.put("name", CurrentUser.getInstance().getUserName());
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
            Toast.makeText(getApplicationContext(),
                                            getString(R.string.enter_title_content),Toast.LENGTH_SHORT).show();
        }
    }

    // === Adding upload photo features code ===
    public void showKeyboard(){
        titleInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(titleInput, InputMethodManager.SHOW_IMPLICIT);
    }

    public void choosePicture(){
        try {
            if (ActivityCompat.checkSelfPermission(CreateForumPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CreateForumPostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            String filePath = RealPathUtil.getRealPath(getApplicationContext(),uri);
            bitmap = resizeAndCompressImageBeforeSend(filePath);
            postImage.setVisibility(View.VISIBLE);
            postImage.setImageBitmap(bitmap);
        }
    }

    public Bitmap resizeAndCompressImageBeforeSend(String filePath){
        final int MAX_IMAGE_SIZE = 700 * 1024; // max final file size in kilobytes

        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 300, 300);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig= Bitmap.Config.ARGB_8888;

        Bitmap bmpPic = BitmapFactory.decodeFile(filePath,options);
        return bmpPic;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        // Image nin islenmeden onceki genislik ve yuksekligi
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag,"image height: "+height+ "---image width: "+ width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag,"inSampleSize: "+inSampleSize);
        return inSampleSize;
    }

    public String getStringImage(Bitmap bmpPic) {
        final int MAX_IMAGE_SIZE = 700 * 1024;
        int compressQuality = 100; // quality decreasing by 5 every loop.
        int streamLength;
        byte[] bmpPicByteArray;
        do {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= 5;
            Log.d("compressBitmap", "Size: " + streamLength / 1024 + " kb");
        } while (streamLength >= MAX_IMAGE_SIZE);
        String encodedImage = Base64.encodeToString(bmpPicByteArray, Base64.DEFAULT);
        return encodedImage;
    }

//    public String getStringImage(Bitmap bitmap){
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap = BITMAP_RESIZER(bitmap, 300, 300);
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 30 , byteArrayOutputStream);
//        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
//        String encodedImage = Base64.encodeToString(imageByteArray,Base64.DEFAULT);
//        Log.e("TAG", "encodedImage"+encodedImage );
//        return encodedImage;
//    }
}
