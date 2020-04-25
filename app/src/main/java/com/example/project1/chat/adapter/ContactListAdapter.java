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

    private List<ContactItem> contactItemList;
    private List<ContactItem> contactItemListFull;
    private Context context;
    private SessionManager sessionManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private final String EMAIL_FROM = "emailFrom";
    private final String EMAIL_TO = "emailTo";
    private final String CHAT_CHANNEL_ID = "chatChannelId";
    private final String RECEIVER_NAME = "receiverName";
    private final String RECEIVER_TYPE = "receiverType";

    class ContactListViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civContactProfilePic;
        TextView tvContactName;

        ContactListViewHolder(View itemView){
            super(itemView);
            civContactProfilePic = itemView.findViewById(R.id.civ_contact_profile_pic);
            tvContactName = itemView.findViewById(R.id.tv_contact_name);
        }
    }

    public ContactListAdapter(Context con, List<ContactItem> list){
        this.context = con;
        this.contactItemList = list;
        this.contactItemListFull = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ContactListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item, viewGroup, false);
        return new ContactListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListAdapter.ContactListViewHolder contactListViewHolder, int i) {
        final ContactItem currentItem = contactItemList.get(i);

        getPic(currentItem.getEmail(), currentItem.getType(), contactListViewHolder.civContactProfilePic);
        contactListViewHolder.tvContactName.setText(currentItem.getName());

        contactListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewChatChannel(currentItem);
            }
        });
    }

    private void createNewChatChannel(ContactItem currentItem) {
        sessionManager = new SessionManager(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(PublicComponent.FIREBASE_CHAT_BASE);

        final String chatChannelId = databaseReference.push().getKey();
        final String receiverName = currentItem.getName();
        final String receiverType = currentItem.getType();
        final String emailTo = currentItem.getEmail();
        final String emailFrom = sessionManager.getUserDetail().get("EMAIL");

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
                                i.putExtra(EMAIL_TO, emailTo);
                                i.putExtra(RECEIVER_NAME, receiverName);
                                i.putExtra(RECEIVER_TYPE, receiverType);
                                i.putExtra(CHAT_CHANNEL_ID, chatChannelId);
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
                params.put(EMAIL_FROM, emailFrom);
                params.put(EMAIL_TO, emailTo);
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
        return 0;
    }

    @Override
    public Filter getFilter() {
        return contactItemListFilter;
    }

    private Filter contactItemListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ContactItem> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
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
            contactItemList.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

    public void getPic(final String email, final String type, final CircleImageView view) {
        String URL_GETPIC;
        if (type.equals(PublicComponent.SPECIALIST)) {
            URL_GETPIC = PublicComponent.URL_SPECIALIST_PIC;
        } else if (type.equals(PublicComponent.CAREGIVER)){
            URL_GETPIC = PublicComponent.URL_CAREGIVER_PIC;
        } else {
            URL_GETPIC = PublicComponent.URL_PATIENT_PIC;
        }
//        Log.e("TAG", "getPic: get pic url" + URL_GETPIC);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETPIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                String picture = jsonObject.getString("photo");
                                Log.e("TAG", "pic: " + picture);

                                //load picture example
                                int loader = R.drawable.ic_user;
                                ImgLoader imgLoader = new ImgLoader(context);
                                imgLoader.DisplayImage(picture, loader, view);
                                Log.e("TAG", "success loading photo");
                            }
                        } catch (JSONException e) {
                            Log.e("TAG", "fail to load photo");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "fail to load photo");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
