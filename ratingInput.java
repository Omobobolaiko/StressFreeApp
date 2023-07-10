package com.example.baads.raterFolder;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.baads.R;
import com.example.baads.databinding.FragmentRatingBinding;
import com.example.baads.mainFiles.MainPage;
import com.example.baads.mainFiles.usernameStorage;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ratingInput extends Fragment {

    private FragmentRatingBinding binding;
    private FirebaseFirestore databaseLoginInfoConnection;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRatingBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void buttonAction(){

        @SuppressLint("RestrictedApi") Context context = getApplicationContext();
        CharSequence text = "Only enter numbers from 1 to 10!";
        int duration = Toast.LENGTH_SHORT;


        EditText editField = getActivity().findViewById(R.id.editTextNumberDecimal);
        String numString = editField.getText().toString();
        int numInt = Integer.parseInt(numString.replaceAll("[\\D]",""));
        int userInput = numInt;
        String date = "";
        Calendar calendar = Calendar.getInstance();
        Date actualTime = calendar.getTime();
        date = actualTime.toString();


//All you need to do is swap out"descriptive sentence" and "task" for their respective variables

        if(userInput <= 10 && userInput > 0) {

            databaseLoginInfoConnection = FirebaseFirestore.getInstance();

            Map<String, Object> data1 = new HashMap<>();

            data1.put("Rating", userInput);


            databaseLoginInfoConnection.collection("users")
                    .document(usernameStorage.username)
                    .collection("stressRating")
                    .document(date)
                    .set(data1);

        }
        else{

            Toast.makeText(context, text, duration).show();

        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //Sourced from https://firebase.google.com/docs/firestore/manage-data/add-data
        super.onViewCreated(view, savedInstanceState);

        //Button button1 = getActivity().findViewById(R.id.button_submit);
        //button1.setOnClickListener(e->buttonAction());

        //Button buttonGraph = getActivity().findViewById(R.id.button_graph);

        binding.buttonGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ratingInput.this)
                        .navigate(R.id.action_ratingInput_to_ratingGraph);
            }
        });

        Button button1 = getActivity().findViewById(R.id.button_submit);
        button1.setOnClickListener(e->buttonAction());

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}