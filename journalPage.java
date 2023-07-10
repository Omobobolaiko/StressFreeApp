package com.example.baads;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.Query;

public class journalPage extends AppCompatActivity {

  FloatingActionButton FAB2;
  RecyclerView recyclerView;
      ImageButton menuButton;
      journaladapter jonadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_journal_page);

         FAB2 = findViewById(R.id.fab2);
         recyclerView = findViewById(R.id.recyclerviewdaily);
         menuButton = findViewById(R.id.menuButtonjournal2);

         FAB2.setOnClickListener((v)-> startActivity(new Intent(journalPage.this,newjournalPage.class)));
         menuButton.setOnClickListener((v)->showMenu());
         setRecyclerView();

    }


    void showMenu() {

    }
     void setRecyclerView() {
         Query query = used.getCollectionReferencejournal().orderBy("timestamp", Query.Direction.DESCENDING);
         FirestoreRecyclerOptions<Journal> options = new FirestoreRecyclerOptions.Builder<Journal>().setQuery(query, Journal.class).build();
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
         jonadapter = new journaladapter(options, this);
         recyclerView.setAdapter(jonadapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        jonadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        jonadapter.stopListening();
    }
    @Override
    protected void onResume() {
        super.onResume();
        jonadapter.notifyDataSetChanged();
    }


}