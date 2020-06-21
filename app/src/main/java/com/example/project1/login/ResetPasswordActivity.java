package com.example.project1.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.CurrentUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends BaseActivity {
    private String email;
    private static String URL ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        URL = PublicComponent.URL_RESET_PASSWORD_PATIENT;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                email = extras.getString("nric");
                Log.e("TAG", "getExtras: Reset Password of email : "+email );

            }
        }
    }

    public void onResetPassword(View view) {
        if(CurrentUser.getInstance().getUserType().equals("Patient")){
            URL = PublicComponent.URL_RESET_PASSWORD_PATIENT;
        } else if (CurrentUser.getInstance().getUserType().equals("Caregiver")){
            URL = PublicComponent.URL_RESET_PASSWORD_CAREGIVER;
        } else if(CurrentUser.getInstance().getUserType().equals("Specialist")){
            URL = PublicComponent.URL_RESET_PASSWORD_SPECIALIST;
        }
        final EditText et1 = (EditText) findViewById(R.id.password);
        final EditText et2 = (EditText) findViewById(R.id.confirm_password);
        final String pw = et1.getText().toString().trim();
        String pw2 = et2.getText().toString().trim();
        if (pw.equals(pw2)) {

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
                                    Toast.makeText(getApplicationContext(),
                                            getString(R.string.reset_pw_success),
                                        Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ResetPasswordActivity.this,
                                            LoginActivity.class);
                                    startActivity(i);
                                } else if (success.equals("0")) {
                                    Toast.makeText(getApplicationContext(),
                                            getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
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
                    params.put("password", pw);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.unmatch_new_pw), Toast.LENGTH_LONG).show();
            et1.setText("");
            et2.setText("");
        }
    }


}
