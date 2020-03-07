package com.example.project1.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import com.example.project1.R;
import com.example.project1.login.component.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {
    private String email;
    private String localhost;
    private static String URL ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        localhost = getString(R.string.localhost);
        URL = localhost+":3000/resetPassword";

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                email = extras.getString("email");
                Log.e("TAG", "getExtras: Reset Password of email : "+email );

            }
        }
    }

    public void onResetPassword(View view) {
        if(User.getInstance().getUserType().equals("Patient")){
            URL = localhost+":3000/resetPassword";
        } else if (User.getInstance().getUserType().equals("Caregiver")){
            URL = localhost+":3000/resetPassword2";
        } else if(User.getInstance().getUserType().equals("Specialist")){
            URL = localhost+":3000/resetPassword3";
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
                                            "Password has been reset!",
                                            Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(ResetPasswordActivity.this,
                                            LoginActivity.class);
                                    startActivity(i);
                                } else if (success.equals("0")) {
                                    Toast.makeText(getApplicationContext(),
                                            "Error, Please Try Again Later!",
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
                                    "Error, Please Try Again Later!",
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
            Toast.makeText(getApplicationContext(), "New Passwords are not same!", Toast.LENGTH_LONG).show();
            et1.setText("");
            et2.setText("");
        }
    }


}
