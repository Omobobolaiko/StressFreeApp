package com.example.baads.positiveThoughts;


import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.baads.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.Random;

/**
 * Source: https://www.youtube.com/watch?v=xSrVWFCtgaE
 * Tutorial for creating notifications. Credit to Foxandroid
 *
 * Source: https://firebase.google.com/docs/firestore/query-data/aggregation-queries
 * Sourced firebase documentation in order to create queries
 *
 * Source: https://www.tutorialspoint.com/how-to-create-a-thread-in-java
 * Used their tutorial in order to create threads in java.
 *
 * Source: https://freesound.org/people/IENBA/sounds/545495/
 * License: https://creativecommons.org/licenses/by/4.0/
 * Used sound for Positive notifications
 *
 * Source: https://stackoverflow.com/questions/40800288/math-random-generates-same-number
 * Used this random number generator instead of Math.Random()
 * Credit to developer
 *
 * Source: http://www.java2s.com/example/java-api/android/app/notificationchannel/setsound-2-0.html
 * Used java2s tutorial in order to create AudioAttribute.
 * Used their creation of them in this document. Same with Uri creation
 */
public class positiveAffirmationsThread implements Runnable {

    Thread positiveAffirmations;
    Activity activityThread;
    private NotificationManager notificationManager;
    private FirebaseFirestore databaseLoginInfoConnection;
    private static int countVariable = 1;
    positiveAffirmationsThread(Activity activity){
        activityThread = activity;
        databaseLoginInfoConnection = FirebaseFirestore.getInstance();
        positiveAffirmations = new Thread(this, "positiveAffirmation");
        positiveAffirmations.start();
    }
    @Override
    public void run() {
        DocumentReference docRef;
        //While the switch in positive Thoughts is flipped, it will continue to do this.
        //Once turned off, it will no longer call this.
        while (positiveAffirmationsReworkFragment.isSwitchFlipped) {
            final String[] affirmation = {"Affirmation"};
            final String[] returnValue = new String[1];
            //initializing value INCASE for some reason it cannot connect to the database fast enough.
            returnValue[0]= "You've got this!";
            CollectionReference collection = databaseLoginInfoConnection.collection("Affirmations");
            Query query = collection;
            AggregateQuery countingAffirmations = query.count();
            countingAffirmations.get(AggregateSource.SERVER).addOnCompleteListener(task ->{
                if(task.isSuccessful()){
                    AggregateQuerySnapshot snapshot = task.getResult();
                    countVariable = (int) snapshot.getCount();
                }
            });
            //Try catch for thread. necessary in-case interrupted. Also necessary as it takes some time to pull.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //https://stackoverflow.com/questions/40800288/math-random-generates-same-number
            //Math.random() was being weird, so this helps with a random number generator.
            //This is used to count how many affirmations we have in our database.
            Random random = new Random();
            int randomValue = random.nextInt()%countVariable;
            randomValue = Math.abs(randomValue);
            affirmation[0] +=randomValue;

            //This gets a document reference to the affirmations collection. And pulls the value stored within to display as a notification.
            docRef = databaseLoginInfoConnection.collection("Affirmations").document(affirmation[0]);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    //If the task is complete, go into the if statement.
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            returnValue[0] = document.getData().values().toString();
                            //Gets rid of brackets
                            returnValue[0] = returnValue[0].replace("[","");
                            returnValue[0] = returnValue[0].replace("]","");
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            //Needs to wait for the above statement to complete.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Source: Foxandroid
            //Creates a notificationcompat and a notification channel.
            NotificationCompat.Builder builder = new NotificationCompat.Builder(activityThread, "Positive Thoughts")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Affirmation Notification")
                    .setContentText(returnValue[0])
                    .setTimeoutAfter(15000)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Positive Notification";
                String description = "Affirmation Notification Channel";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel("Positive Thoughts", name, importance);

                //Source: http://www.java2s.com/example/java-api/android/app/notificationchannel/setsound-2-0.html
                //AudioAttribute creator, sourcing java2s's AudioAttribute creation
                //in order to be able to create an audioattribute to attach to an alarmsound.
                //AudioAttributes audioAttributes = new AudioAttributes.Builder()
                //        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                //        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                //        .build();

                //Source: http://www.java2s.com/example/java-api/android/app/notificationchannel/setsound-2-0.html
                //Format for Uri creation.
                //Old way of doing it, for some reason has issues where it will not play sound.
                //channel.setSound(alarmSound,audioAttributes);
                channel.setDescription(description);
                Uri alarmSound = Uri.parse("android.resource://" + activityThread.getPackageName() + "/" + R.raw.positive_sound);
                MediaPlayer positiveSound = MediaPlayer.create(activityThread.getApplicationContext(),alarmSound);
                positiveSound.start();
                //Set sound to null. Sourced from stackoverflow, source in alarmActivityReworkFragment.
                channel.setSound(null,null);



                notificationManager = activityThread.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activityThread);
            notificationManager.notify(100, builder.build());
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
