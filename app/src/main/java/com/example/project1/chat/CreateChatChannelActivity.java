package com.example.project1.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
import com.example.project1.chat.adapter.ContactListAdapter;
import com.example.project1.chat.component.ContactItem;
import com.example.project1.login.component.BaseActivity;
import com.example.project1.login.component.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateChatChannelActivity extends BaseActivity {

    private SearchView searchViewContactList;
    private RecyclerView recyclerViewContactList;
    private ProgressBar progressBarContactList;
    private ContactListAdapter contactListAdapter;
    private String NAME = "name";
    private String NRIC = "nric";
    private String PHOTO = "photo";
    private Context context = this;
    private ArrayList<ContactItem> contactItemList = new ArrayList<>();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat_channel);
//        ContactItem i = new ContactItem("Test Name","Test NRIC", "Test type","Test Photo");
//        contactItemList.add(i);
        sessionManager = new SessionManager(this);
        progressBarContactList = findViewById(R.id.progress_bar_contact_list);
        searchViewContactList = findViewById(R.id.search_view_contact_list);
        recyclerViewContactList = findViewById(R.id.recycler_view_contact_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        contactListAdapter = new ContactListAdapter(this, contactItemList);
        recyclerViewContactList.setLayoutManager(layoutManager);
        recyclerViewContactList.setAdapter(contactListAdapter);
        getContactList();
//        setupSearchView();
    }

    private void setupSearchView() {
        searchViewContactList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        progressBarContactList.setVisibility(View.GONE);
    }

    public void getContactList(){
        progressBarContactList.setVisibility(View.VISIBLE);

        getContactList(PublicComponent.URL_GET_ALL_SPECIALIST, PublicComponent.SPECIALIST);
//        getContactList(PublicComponent.URL_GET_ALL_CAREGIVER, PublicComponent.CAREGIVER);
//        getContactList(PublicComponent.URL_GET_ALL_PATIENT, PublicComponent.PATIENT);


    }

    public synchronized void getContactList(final String url, final String type){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String apiStatus = jsonObject.getString(PublicComponent.API_CALL_STATUS);
                            Log.e("list ", jsonArray.toString());

                            if(apiStatus.equals("1") || apiStatus.contentEquals("1")){
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    if(!object.getString(NRIC).equals(sessionManager.getUserDetail().get("NRIC"))) {
                                        final ContactItem item = new ContactItem(object.getString(NAME), object.getString(NRIC), type, object.getString(PHOTO));
                                        contactItemList.add(item);
                                    }
                                }
//                                contactListAdapter = new ContactListAdapter(context, contactItemList);
//                                recyclerViewContactList.setAdapter(contactListAdapter);
//                                contactListAdapter.notifyDataSetChanged();
//                                setupSearchView();

                                if(url.equals(PublicComponent.URL_GET_ALL_SPECIALIST)){
                                    getContactList(PublicComponent.URL_GET_ALL_CAREGIVER, PublicComponent.CAREGIVER);
                                }
                                else if (url.equals(PublicComponent.URL_GET_ALL_CAREGIVER)){
                                    getContactList(PublicComponent.URL_GET_ALL_PATIENT, PublicComponent.PATIENT);
                                }
                                else if(url.equals(PublicComponent.URL_GET_ALL_PATIENT)) {
//                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
//                                    contactListAdapter = new ContactListAdapter(context, contactItemList);
//                                    recyclerViewContactList.setLayoutManager(layoutManager);
//                                    recyclerViewContactList.setAdapter(contactListAdapter);
                                    contactListAdapter.notifyDataSetChanged();
                                    setupSearchView();
                                }
                            }
                            else{
                                progressBarContactList.setVisibility(View.GONE);
                                Log.e("onResponse1: ", apiStatus);
                                Toast.makeText(getApplicationContext(), "Error1, Please Try Again Later",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e){
                            Log.e("Error", e.toString());
                            Toast.makeText(getApplicationContext(), "Error2, Please Try Again Later",
                                    Toast.LENGTH_SHORT).show();
                            progressBarContactList.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBarContactList.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Error3, Please Try Again Later",
                                Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        return params;
                    }
                };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,ChatChannelListActivity.class);
        startActivity(i);
    }
}
