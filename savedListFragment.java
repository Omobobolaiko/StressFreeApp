package com.example.baads.savedList;

import static android.content.ContentValues.TAG;
import static android.util.Log.d;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.baads.R;
import com.example.baads.databinding.ActivitySelfCareListBinding;
import com.example.baads.mainFiles.usernameStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class savedListFragment extends Fragment {

    private FirebaseFirestore databaseLoginInfoConnection;
    private ActivitySelfCareListBinding binding;


    String tips[]
            = { "Practice mindfulness", "Take a break",
            "Play video games", "Listen to music",
            "Read a book", "Listen to a podcast",
            "Reflect on things you are grateful for",
            "Eat a healthy meal", "Engage in exercise",
            "Go for a walk", "Drink water", "Practice good sleep hygiene",
            "Call-text a friend", "Connect with nature",
            "Meditate", "Treat yourself", "Maintain a routine", "Take deep breaths"};
    //for testing

    ArrayList<String> savedArrayList = new ArrayList<>();
    //actual list


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ActivitySelfCareListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseLoginInfoConnection = FirebaseFirestore.getInstance();
        CollectionReference colRef = databaseLoginInfoConnection.collection("users")
                .document(usernameStorage.username)
                .collection("SavedIdeas");

        ArrayAdapter<String> tasks = new ArrayAdapter<>(getActivity(),
                R.layout.selfcarelist_item_view, R.id.itemTextView,
                savedArrayList);
//set to tips for full list
//set to savedArrayList for saved

        ListView listTasks = getActivity().findViewById(R.id.listView);
        listTasks.setAdapter(tasks);


                colRef.get()
                .addOnCompleteListener(task -> {;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                savedArrayList.add(document.getId());
                                //adds all doc IDs in SavedIdeas collection
                                tasks.notifyDataSetChanged();
                                //notifies adapter of change in ListView

                               if (document.exists()) {

                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            }
                        } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                    });



        listTasks.setOnItemClickListener((adapter, v, position, id) -> {
            String item = adapter.getItemAtPosition(position).toString();
            Map<String, Object> data1 = new HashMap<>();
            databaseLoginInfoConnection.collection("users")
                    .document(usernameStorage.username)
                    .collection("SavedIdeas")
                    .document(item)
                    .delete();
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

