package com.example.project1.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.example.project1.chat.ChatChannelListActivity;
import com.example.project1.chat.ChatPageActivity;
import com.example.project1.chat.component.ContactItem;
import com.example.project1.emotionAssessment.EmotionAssessmentActivity;
import com.example.project1.forum.imageFile.ImgLoader;
import com.example.project1.login.component.SessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactListViewHolder> implements Filterable {

    private ArrayList<ContactItem> contactItemList;
    private ArrayList<ContactItem> contactItemListFull;
    private Context context;
    private SessionManager sessionManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private final String NRIC_FROM = "NRICFrom";
    private final String NRIC_TO = "NRICTo";
    private final String CHAT_CHANNEL_ID = "chatChannelId";
    private final String RECEIVER_NAME = "receiverName";
    private final String RECEIVER_TYPE = "receiverType";
    private final String RECEIVER_PIC = "receiverPic";
    private LayoutInflater mInflater;

    class ContactListViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civContactProfilePic;
        TextView tvContactName;

        ContactListViewHolder(View itemView){
            super(itemView);
            civContactProfilePic = itemView.findViewById(R.id.civ_contact_profile_pic);
            tvContactName = itemView.findViewById(R.id.tv_contact_name);
        }
    }

    public ContactListAdapter(Context con, ArrayList<ContactItem> list){
        Log.e("ContactListAdapter: ", list.toString());
        this.mInflater = LayoutInflater.from(con);
        this.context = con;
        this.contactItemList = list;
        this.contactItemListFull = (ArrayList<ContactItem>) list.clone();
    }

    @NonNull
    @Override
    public ContactListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = mInflater.inflate(R.layout.contact_item, viewGroup, false);
        return new ContactListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactListViewHolder contactListViewHolder, int i) {
        final ContactItem currentItem = contactItemList.get(i);

        getPic(currentItem.getPhoto(), contactListViewHolder.civContactProfilePic);
        contactListViewHolder.tvContactName.setText(currentItem.getName());

        contactListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkChannelExist(currentItem);
//                createNewChatChannel(currentItem);
            }
        });
    }

    private void checkChannelExist(final ContactItem currentItem){
        sessionManager = new SessionManager(context);
        final String receiverName = currentItem.getName();
        final String receiverType = currentItem.getType();
        final String NRICTo = currentItem.getNRIC();
        final String NRICFrom = sessionManager.getUserDetail().get("NRIC");
        final String pic = currentItem.getPhoto();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PublicComponent.URL_GET_CHAT_IF_EXIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String apiStatus = jsonObject.getString(PublicComponent.API_CALL_STATUS);

                            if(apiStatus.equals("1")){
                                Intent i = new Intent(context, ChatPageActivity.class);
                                i.putExtra(NRIC_TO, NRICTo);
                                i.putExtra(RECEIVER_NAME, receiverName);
                                i.putExtra(RECEIVER_TYPE, receiverType);
                                i.putExtra(CHAT_CHANNEL_ID, jsonObject.getString(PublicComponent.FIREBASE_CHAT_HISTORY_CHANNEL_ID));
                                i.putExtra(RECEIVER_PIC, pic);
                                context.startActivity(i);
                            }
                            else if(apiStatus.equals("2")){
                                createNewChatChannel(currentItem);
                            }
                            else{
                                Toast.makeText(context, "Error, Please Try Again Later",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e){
                            Log.e("Error", e.toString());
                            Toast.makeText(context, "Error, Please Try Again Later",
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error, Please Try Again Later",
                                Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(NRIC_FROM, NRICFrom);
                params.put(NRIC_TO, NRICTo);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void createNewChatChannel(final ContactItem currentItem) {
        sessionManager = new SessionManager(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(PublicComponent.FIREBASE_CHAT_BASE);

        final String chatChannelId = databaseReference.push().getKey();
        final String receiverName = currentItem.getName();
        final String receiverType = currentItem.getType();
        final String NRICTo = currentItem.getNRIC();
        final String NRICFrom = sessionManager.getUserDetail().get("NRIC");
        final String pic = currentItem.getPhoto();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PublicComponent.URL_POST_CHAT_CHANNEL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String apiStatus = jsonObject.getString(PublicComponent.API_CALL_STATUS);

                            if(apiStatus.equals("1")){
                                createNewReceiverChatChannel(currentItem,chatChannelId);
//                                Intent i = new Intent(context, ChatPageActivity.class);
//                                i.putExtra(NRIC_TO, NRICTo);
//                                i.putExtra(RECEIVER_NAME, receiverName);
//                                i.putExtra(RECEIVER_TYPE, receiverType);
//                                i.putExtra(CHAT_CHANNEL_ID, chatChannelId);
//                                i.putExtra(RECEIVER_PIC, pic);
//                                context.startActivity(i);
                            }
                            else{
                                Toast.makeText(context, "Error, Please Try Again Later",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e){
                            Log.e("Error", e.toString());
                            Toast.makeText(context, "Error, Please Try Again Later",
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error, Please Try Again Later",
                                Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(NRIC_FROM, NRICFrom);
                params.put(NRIC_TO, NRICTo);
                params.put(CHAT_CHANNEL_ID, chatChannelId);
                params.put(RECEIVER_NAME, receiverName);
                params.put(RECEIVER_TYPE, receiverType);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void createNewReceiverChatChannel(final ContactItem currentItem, String key) {
        sessionManager = new SessionManager(context);

        final String chatChannelId = key;
        final String receiverName = sessionManager.getUserDetail().get("NAME");
        final String receiverType = sessionManager.getUserDetail().get("TYPE");
        final String NRICTo = sessionManager.getUserDetail().get("NRIC");
        final String NRICFrom = currentItem.getNRIC();
        final String pic = currentItem.getPhoto();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PublicComponent.URL_POST_CHAT_CHANNEL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String apiStatus = jsonObject.getString(PublicComponent.API_CALL_STATUS);

                            if(apiStatus.equals("1")){
                                Intent i = new Intent(context, ChatPageActivity.class);
                                i.putExtra(NRIC_TO, currentItem.getNRIC());
                                i.putExtra(RECEIVER_NAME, currentItem.getName());
                                i.putExtra(RECEIVER_TYPE, currentItem.getType());
                                i.putExtra(CHAT_CHANNEL_ID, chatChannelId);
                                i.putExtra(RECEIVER_PIC, currentItem.getPhoto());
                                context.startActivity(i);
                            }
                            else{
                                Toast.makeText(context, "Error, Please Try Again Later",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e){
                            Log.e("Error", e.toString());
                            Toast.makeText(context, "Error, Please Try Again Later",
                                    Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error, Please Try Again Later",
                                Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(NRIC_FROM, NRICFrom);
                params.put(NRIC_TO, NRICTo);
                params.put(CHAT_CHANNEL_ID, chatChannelId);
                params.put(RECEIVER_NAME, receiverName);
                params.put(RECEIVER_TYPE, receiverType);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return contactItemList.size();
    }

    @Override
    public Filter getFilter() {
        return contactItemListFilter;
    }

    private Filter contactItemListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ContactItem> filteredList = new ArrayList<>();

            if(constraint == null || constraint.toString().length() == 0){
                filteredList.addAll(contactItemListFull);
            }
            else{
                String query = constraint.toString().toLowerCase().trim();

                for(ContactItem item : contactItemListFull){
                    if(item.getName().toLowerCase().contains(query)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactItemList.clear();
            contactItemList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

    public void getPic(final String photo, final CircleImageView view) {
        int loader = R.drawable.ic_user;
        ImgLoader imgLoader = new ImgLoader(context);
        imgLoader.DisplayImage(photo, loader, view);
    }
}
