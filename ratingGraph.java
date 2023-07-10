package com.example.baads.raterFolder;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.baads.R;
import com.example.baads.addNewActivity;
import com.example.baads.databinding.FragmentDataratingBinding;
import com.example.baads.mainFiles.usernameStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.BreakIterator;
import java.util.concurrent.atomic.AtomicInteger;

public class ratingGraph extends Fragment {

    private FragmentDataratingBinding binding;
    FirebaseFirestore firebaseDatabase;
    DatabaseReference databaseReference;
    private TextView retrieveDataText;
    private TextView seeAverage;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentDataratingBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    //Sourced https://firebase.google.com/docs/firestore/query-data/get-data
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseDatabase = FirebaseFirestore.getInstance();
        retrieveDataText = getActivity().findViewById(R.id.retrieveData);
        seeAverage = getActivity().findViewById(R.id.seeAverage);
        String[] result = new String[1];
        result[0] ="";
        firebaseDatabase.collection("users").document(usernameStorage.username).collection("stressRating").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int sum = 0;
                int count = 0;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // cast
                    int numInt = Integer.parseInt(document.getData().get("Rating").toString().replaceAll("[\\D]",""));
                    sum += numInt;
                    count++;
                    // datapoint ...
                    result[0] += document.getId() + "\n" + "Rating: " + document.getData().get("Rating") + "\n";
                    //document.getId();
                }
                sum = sum/count;
                seeAverage.setText("All Time Average: " + String.valueOf(sum));
                retrieveDataText.setText(result[0]);
            } else {
                Log.d(addNewActivity.TAG, "Error getting documents: ", task.getException());
            }

        });



    }

    private <value> void getdata() {

        databaseReference.addValueEventListener(new ValueEventListener() {
        @Override

        public void onDataChange(@NonNull DataSnapshot snapshot) {
        String value = snapshot.getValue(String.class);
        retrieveDataText.setText(value);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCancelled(@NonNull DatabaseError error) {

           Toast.makeText(getApplicationContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
    }

    });

    }

    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}