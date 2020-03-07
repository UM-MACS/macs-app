package com.example.project1.selfAssessment.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.project1.R;
import com.example.project1.selfAssessment.SelfAssessmentActivity;

import java.util.ArrayList;

public class SelfAssessmentActivityAdapter extends RecyclerView.Adapter<SelfAssessmentActivityAdapter.ViewHolder>{

    private static final String TAG = "EventAssessmentActivity";

    private ArrayList<String> mEventNames = new ArrayList<>();
    private Context mContext;


    public SelfAssessmentActivityAdapter(Context context, ArrayList<String> eventName) {
        mEventNames = eventName;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_assessment_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.eventName.setText(mEventNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG, "onClick: clicked on: " + mEventNames.get(position));

                Intent intent = new Intent(mContext, SelfAssessmentActivity.class);
                intent.putExtra("event_name", mEventNames.get(position));
                mContext.startActivity(intent);
            }

        });
    }



    @Override
    public int getItemCount() {
        return mEventNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView eventName;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}






