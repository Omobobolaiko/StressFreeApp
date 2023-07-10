package com.example.baads;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class DailyActivity extends AppCompatActivity {

    FloatingActionButton adddailybtn;
    RecyclerView recyclerView;
    ImageButton menuButton;
    ActivityAdapter activityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);
        adddailybtn = findViewById(R.id.adddaily);
        recyclerView = findViewById(R.id.rview);

        adddailybtn.setOnClickListener((v)-> startActivity(new Intent(DailyActivity.this,newdailyActivity.class)));
        setupRecyclerview();
    }
    void setupRecyclerview(){
        Query query = Utility.getCollectionReferenceForActivities().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Activity> options = new FirestoreRecyclerOptions.Builder<Activity>()
                .setQuery(query,Activity.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityAdapter = new ActivityAdapter(options, this);
        recyclerView.setAdapter(activityAdapter);


    }
    @Override
    protected void onStart() {
        super.onStart();
        activityAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
      activityAdapter.stopListening();
    }
    @Override
    protected void onResume() {
        super.onResume();
        activityAdapter.notifyDataSetChanged();
    }



}