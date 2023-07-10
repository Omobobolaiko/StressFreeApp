package com.example.baads.mainFiles;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.baads.R;
import com.example.baads.databinding.FragmentFirst2Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/** LoginFragment
 *
 *  This fragment deals with establishing a connection to the FirestoneDatabase and searching for a users information
 *  If found, it will allow a login and store a username within the usernameStorage class
 *      This username can be accessed by calling usernameStorage.username
 *
 *  Source: https://firebase.google.com/docs/firestore/query-data/get-data?utm_source=studio
 *  Sourced their code to make a connection, and find from our database.
 */
public class LoginFragment extends Fragment {

    private FragmentFirst2Binding binding;
    private String username = "";
    private String password = "";
    private FirebaseFirestore databaseLoginInfoConnection;
    private boolean loggedIn = false;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirst2Binding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseLoginInfoConnection = FirebaseFirestore.getInstance();

        binding.skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_loginFragment_to_FirstFragment);
            }
        });

        TextView errorTextModify = binding.errorText;
        errorTextModify.setAlpha(0);

        errorTextModify.setText("ERROR: NO ACCOUNT TIED TO THAT USERNAME OR PASSWORD");

        TextView usernameInteraction = binding.username;
        usernameInteraction.setOnClickListener(e->errorTextModify.setAlpha(0));

        TextView passwordInteraction = binding.password;
        passwordInteraction.setOnClickListener(e->errorTextModify.setAlpha(0));

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                username = String.valueOf(binding.username.getText());
                password = String.valueOf(binding.password.getText());
                if(!(username.isEmpty()||password.isEmpty())) {

                    //Sourced from https://firebase.google.com/docs/firestore/query-data/get-data?utm_source=studio
                    //Gets the document reference.
                    //This this case, it takes from the users collection, and finds the user with the inputted username.
                    DocumentReference docRef = databaseLoginInfoConnection.collection("users").document(username);

                    //This is what we use to be able to login.
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            //If the task is complete, go into the if statement.
                            if (task.isSuccessful()) {
                                errorTextModify.setAlpha(0);
                                //gets the document that contains the user information
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    //checks if the document contains the users password.
                                    if(document.getData().containsValue(password)) {

                                        //source : https://stackoverflow.com/questions/1944656/android-global-variable
                                        //Trying to figure out how to store a variable (in this case a username) across all activities.
                                        //Used this idea from the stackoverflow
                                        usernameStorage.username = username;
                                        NavHostFragment.findNavController(LoginFragment.this)
                                                .navigate(R.id.action_loginFragment_to_FirstFragment);
                                    }else{
                                        errorTextModify.setAlpha(1);
                                    }
                                } else {
                                    errorTextModify.setAlpha(1);
                                }
                            } else {
                                errorTextModify.setAlpha(1);
                            }
                        }
                    });
                }else{
                    errorTextModify.setAlpha(1);
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}