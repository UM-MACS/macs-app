package com.example.project1.userManagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.project1.userManagement.UserDetailActivity;
import com.example.project1.userManagement.component.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private ArrayList<User> userList;
    private LayoutInflater mInflater;
    private Context mContext;
    private EventHandler handler;

    // data is passed into the constructor
    public UserListAdapter(Context c, ArrayList<User> data, EventHandler handler) {
        this.mContext = c;
        this.mInflater = LayoutInflater.from(c);
        this.userList = data;
        this.handler = handler;
    }

    // inflates the row layout from xml when needed
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_user_list, parent, false);
        return new UserListAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(UserListAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(userList.get(position).getName());
        holder.chosenUser = userList.get(position);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return this.userList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        Button btnView, btnDelete;
        User chosenUser;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            btnView = itemView.findViewById(R.id.btn_view);
            btnDelete = itemView.findViewById(R.id.btn_delete);

            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickView();
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickDelete();
                }
            });
        }

        private void onClickDelete() {
            String url;
            if(chosenUser.getType().equals(PublicComponent.PATIENT)) url = PublicComponent.DELETE_PATIENT_URL;
            else if(chosenUser.getType().equals(PublicComponent.CAREGIVER)) url = PublicComponent.DELETE_CAREGIVER_URL;
            else if(chosenUser.getType().equals(PublicComponent.SPECIALIST)) url = PublicComponent.DELETE_SPECIALIST_URL;
            //if really no match
            else url = PublicComponent.DELETE_PATIENT_URL;

            handler.handle(true);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {
                                    userList.remove(chosenUser);
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, mContext.getString(R.string.delete_user_success),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, mContext.getString(R.string.delete_user_fail),
                                            Toast.LENGTH_SHORT).show();
                                }
                                handler.handle(false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(mContext, mContext.getString(R.string.delete_user_fail),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(mContext, mContext.getString(R.string.delete_user_fail),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put(PublicComponent.ID, chosenUser.getId());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(stringRequest);
        }

        private void onClickView() {
            Intent i = new Intent(mContext, UserDetailActivity.class);
            i.putExtra(PublicComponent.NAME,chosenUser.getName());
            i.putExtra(PublicComponent.EMAIL,chosenUser.getEmail());
            i.putExtra(PublicComponent.NRIC,chosenUser.getNric());
            i.putExtra(PublicComponent.CONTACT_NO,chosenUser.getContactNo());
            i.putExtra(PublicComponent.AGE,chosenUser.getAge());
            i.putExtra(PublicComponent.TYPE,chosenUser.getType());
            i.putExtra(PublicComponent.ID,chosenUser.getId());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        }
    }

    public interface EventHandler {
        void handle(boolean bool);
    }
}
