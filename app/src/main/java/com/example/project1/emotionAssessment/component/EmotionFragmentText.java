package com.example.project1.emotionAssessment.component;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project1.R;
import com.example.project1.login.component.CurrentUser;
import com.example.project1.login.component.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmotionFragmentText extends Fragment {
    Button b1,b2,b3,b4,b5,b6,submitButton;
    ArrayList<String> arrayList;
    EditText expression;
    FrameLayout frameLayout;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    Button button;
    MyTask task;
    private String localhost;
    private static String URL;
    private static String URL_API_SA;
    Date currentTime;
    private TextView mTextMessage;
    SessionManager sessionManager;


    public EmotionFragmentText() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());
        sessionManager.checkLogin();

        View thisFragment = inflater.inflate(R.layout.fragment_emotion_text, container, false);
        progressBarHolder = (FrameLayout) thisFragment.findViewById(R.id.progressBarHolder);
        localhost = getString(R.string.localhost);
        URL =localhost+"/emotion";
        URL_API_SA ="https://api.deepai.org/api/sentiment-analysis";
        HashMap<String,String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);
        String mEmail = user.get(sessionManager.NRIC);
        String mType = user.get(sessionManager.TYPE);
        Log.e("TAG", "shared preference name is "+mName );
        CurrentUser.getInstance().setNRIC(mEmail);
        CurrentUser.getInstance().setUserName(mName);
        CurrentUser.getInstance().setUserType(mType);
        task = new MyTask();

        arrayList = new ArrayList<String>();
        expression = (EditText) thisFragment.findViewById(R.id.expression);
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");



        //submit input in text box
//        expression.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                expression.setCursorVisible(true);
//            }
//        });
        submitButton = (Button) thisFragment.findViewById(R.id.submitExpressionButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = expression.getText().toString();
                Date d = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                final String date = dateFormat.format(d);
                if (text.equals("")) {
                    Toast.makeText(getContext(),
                            getString(R.string.enter_something),
                            Toast.LENGTH_LONG).show();
                } else {
                    task.execute();
                    final String input =  text.replaceAll("[^a-zA-Z ]", "").toLowerCase();
                    Pattern re = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)",
                            Pattern.MULTILINE | Pattern.COMMENTS);
                    Matcher reMatcher = re.matcher(input);
                    String splitText="";
                    while (reMatcher.find()) {
                        splitText = (reMatcher.group());
                    }
                    String[] sArray = splitText.split("\\s{2,}");
                    for (int i =0; i<sArray.length; i++){
                        sentimentAnalysis(CurrentUser.getInstance().getNRIC(), CurrentUser.getInstance().getUserType(), date,
                                sArray[i]);
                    }
                }
            }
        });

        return thisFragment;
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
                            Log.e("TAG", "success: "+success );
                            if(success.equals("1")){
                                Log.e("TAG", "success" );
                                AlertDialog diaBox = AskOption();
                                diaBox.show();
                                task.cancel(true);

                            }
                            else if(success.equals("0")){
//                    Toast.makeText(getApplicationContext(),"Fail to submit",
//                            Toast.LENGTH_SHORT).show();
                                expression.setText("");
                                AlertDialog diaBox = alertError();
                                diaBox.show();
                                task.cancel(true);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),"Error, Please Try Again Later",
//                            Toast.LENGTH_SHORT).show();
                            AlertDialog diaBox = alertError();
                            diaBox.show();
                            task.cancel(true);
                        }
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),"Error, Please Try Again Later",
//                                Toast.LENGTH_SHORT).show();
                        AlertDialog diaBox = alertError();
                        diaBox.show();
                        task.cancel(true);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void sentimentAnalysis(final String email, final String type, final String date,
                                   final String expressionInput) {

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
                Toast.makeText(getContext(), getString(R.string.try_later),
                                        Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("text", expressionInput);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<>();
//                header.put("api-key","369aa822-9f19-4ae2-826f-df963534c2c9");
//                header.put("api-key","d92743d6-c2d6-426c-81f1-034b93667aec");
                header.put("api-key","b403d4a8-76f1-4100-b2fa-6ea0f8ebb2d8");
//                header.put("api-key","8f56b1cb-7e11-46f7-b054-9f5f0da0729d");

//                header.put("Content-Type","text/plain");
                return header;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle(R.string.success)
                .setMessage(R.string.feedback_recorded)
                .setPositiveButton(R.string.return_to, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Intent intent = getActivity().getIntent();
                        getActivity().finish();
                        startActivity(intent);
                    }

                })
                .create();
        return myQuittingDialogBox;

    }

    private AlertDialog alertError() {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle(R.string.fail)
                .setMessage(R.string.try_later)
                .setPositiveButton(R.string.try_later, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        Intent intent = getActivity().getIntent();
                        getActivity().finish();
                        startActivity(intent);
                    }

                })
                .create();
        return myQuittingDialogBox;

    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            button.setEnabled(false);
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
//            button.setEnabled(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (int i = 0; i < 99; i++) {
                    Log.d("log", "Emulating some task.. Step " + i);
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
