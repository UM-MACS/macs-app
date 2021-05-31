package com.example.project1.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.R;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.CurrentUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends BaseActivity {
    private static String URL,localhost;
    private EditText etEmail;
    private Button buttonSubmit;
    private TextView textview;
    private String email;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        localhost = getString(R.string.localhost);
        URL = localhost+"/sendSaltToEmail";
        etEmail = (EditText)findViewById(R.id.email);
        buttonSubmit = (Button)findViewById(R.id.button);
        textview = (TextView)findViewById(R.id.textView);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

    }

    public void onForgotPassword(View v){
        if(CurrentUser.getInstance().getUserType().equals("Patient")){
            URL = localhost+"/sendSaltToEmail";
        } else if (CurrentUser.getInstance().getUserType().equals("Caregiver")){
            URL = localhost+"/sendSaltToEmail2";
        } else if(CurrentUser.getInstance().getUserType().equals("Specialist")){
            URL = localhost+"/sendSaltToEmail3";
        }
        progressBar.setVisibility(View.VISIBLE);
        buttonSubmit.setVisibility(View.GONE);
        email = etEmail.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                textview.setText(R.string.temp_pw_sent);
                                etEmail.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.GONE);
                                buttonSubmit.setVisibility(View.VISIBLE);
                                buttonSubmit.setText(R.string.back_login);
                                buttonSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(ForgotPasswordActivity.this,
                                                LoginActivity.class);
                                        startActivity(i);
                                    }
                                });
                            } else if (success.equals("-1")) {
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.nric_not_registered),
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                buttonSubmit.setVisibility(View.VISIBLE);
                            } else{
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                buttonSubmit.setVisibility(View.VISIBLE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
//                params.put("type", CurrentUser.getInstance().getUserType());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void onContactAdmin(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.contact_admin_title);
        alertDialogBuilder.setMessage(R.string.contact_admin_info);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint(R.string.nric);
        input.setPadding(8,8,8,8);
        alertDialogBuilder.setView(input);
        String yes = "<font color='#228B22'>"+getString(R.string.yes)+"</font>";
        String no = "<font color='#228B22'>"+getString(R.string.no)+"</font>";
        alertDialogBuilder.setPositiveButton(Html.fromHtml(yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                        sendContactAdminApi(input.getText().toString().trim());
                    }
                });
        alertDialogBuilder.setNegativeButton(Html.fromHtml(no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void sendContactAdminApi(final String nric) {
        if(CurrentUser.getInstance().getUserType().equals("Patient")){
            URL = localhost+"/sendSaltToEmail";
        } else if (CurrentUser.getInstance().getUserType().equals("Caregiver")){
            URL = localhost+"/sendSaltToEmail2";
        } else if(CurrentUser.getInstance().getUserType().equals("Specialist")){
            URL = localhost+"/sendSaltToEmail3";
        }
        progressBar.setVisibility(View.VISIBLE);
        buttonSubmit.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                textview.setText(R.string.temp_pw_sent);
                                etEmail.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.GONE);
                                buttonSubmit.setVisibility(View.VISIBLE);
                                buttonSubmit.setText(R.string.back_login);
                                buttonSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(ForgotPasswordActivity.this,
                                                LoginActivity.class);
                                        startActivity(i);
                                    }
                                });
                            } else if (success.equals("-1")) {
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.nric_not_registered),
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                buttonSubmit.setVisibility(View.VISIBLE);
                            } else{
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                buttonSubmit.setVisibility(View.VISIBLE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.try_later),
                                Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nric", nric);
//                params.put("type", CurrentUser.getInstance().getUserType());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}
