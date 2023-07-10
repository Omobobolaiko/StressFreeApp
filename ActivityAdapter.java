package com.example.baads;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ActivityAdapter extends FirestoreRecyclerAdapter<Activity, ActivityAdapter.ActivityViewHolder> {
   Context context;
    public ActivityAdapter(@NonNull FirestoreRecyclerOptions<Activity> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ActivityViewHolder holder, int position, @NonNull Activity activity) {
        holder.dailyTextView.setText(activity.daily);
        holder.timestampTextView.setText(Utility.timestampToString(activity.timestamp));


        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context,newdailyActivity.class );
            intent.putExtra("daily", activity.daily);

            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_activity_item,parent,false);
       return new ActivityViewHolder(view);
    }

    class ActivityViewHolder extends RecyclerView.ViewHolder{
        TextView dailyTextView, timestampTextView;

        public ActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            dailyTextView = itemView.findViewById(R.id.activity_daily_text_view);
            timestampTextView = itemView.findViewById(R.id.activity_timestamp_text_view);
        }
    }

}
