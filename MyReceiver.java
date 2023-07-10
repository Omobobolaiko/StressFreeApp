package com.example.baads.alarm;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.baads.R;

/** @MyReceiver
 *  This is the receiver for the alarm notification.
 *
 *  Much of the code is sourced from https://www.youtube.com/watch?v=xSrVWFCtgaE
 *  Huge credit to Foxandroid, followed their tutorial.
 *
 *  Credit to http://www.java2s.com/example/java-api/android/app/notificationchannel/setsound-2-0.html
 *  Format for Uri creation
 *
 *  https://developer.android.com/guide/topics/media/mediaplayer
 *  MediaPlayer documentation for playing sound
 *
 */
public class MyReceiver extends BroadcastReceiver {

    public static MediaPlayer alarmSounder;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        //Source Java2s
        Uri alarmSound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alarm_sound);
        //Source Java2s

        //https://developer.android.com/guide/topics/media/mediaplayer
        alarmSounder = MediaPlayer.create(context,alarmSound);
        alarmSounder.start();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Alarm System")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("BAADS stress relief Alarm Manager")
                .setContentText("Wake up time!")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());
    }
}