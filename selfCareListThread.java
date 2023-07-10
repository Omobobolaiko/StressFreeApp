package com.example.baads.selfCareList;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.baads.R;
import com.example.baads.positiveThoughts.positiveAffirmationsReworkFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;


public class selfCareListThread implements Runnable{

    Thread selfCareList;
    Activity activityThread;
    private NotificationManager notificationManager;
    private FirebaseFirestore databaseLoginInfoConnection;
    private static int countVariable = 1;
    selfCareListThread(Activity activity){
        activityThread = activity;
        databaseLoginInfoConnection = FirebaseFirestore.getInstance();
        selfCareList = new Thread(this, "selfCareList");
        selfCareList.start();
    }
    @Override
    public void run() {
        /*
        CollectionReference colRef = databaseLoginInfoConnection.collection("SavedIdeas");


        colRef
                .update("Take a break", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });




        */




    }


}
