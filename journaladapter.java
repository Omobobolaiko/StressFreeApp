package com.example.baads;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class journaladapter extends FirestoreRecyclerAdapter<Journal, journaladapter.JournalViewHolder>{


   Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public journaladapter(@NonNull FirestoreRecyclerOptions<Journal> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull JournalViewHolder holder, int position, @NonNull Journal journal) {
    holder.titleTextView.setText(journal.title);
    holder.descriptionTextView.setText(journal.description);
    holder.timestampTextView.setText(used.timestampToString(journal.timestamp));

    holder.itemView.setOnClickListener((v) -> {
        Intent intent = new Intent(context,newjournalPage.class );
        intent.putExtra("title", journal.title);
        intent.putExtra("description", journal.description);
        String doc = this.getSnapshots().getSnapshot(position).getId();
        intent.putExtra("doc", doc);
        context.startActivity(intent);

    });
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_journal_items,parent, false);
       return new JournalViewHolder(view);
    }

    class JournalViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView, descriptionTextView, timestampTextView;

        public JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.journaltitletextview);
            descriptionTextView = itemView.findViewById(R.id.journaldescriptiontextview);
            timestampTextView = itemView.findViewById(R.id.journaltimestamptextview);

        }
    }
}




