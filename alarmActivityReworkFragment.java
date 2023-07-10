package com.example.baads.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.baads.R;
import com.example.baads.databinding.FragmentAlarmclockBinding;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.Date;

/** @alarmActivityReworkFragment
 *  @author Aidan LePage
 *  Deals with alarm functionality and setting up the fragment page to be used by the program.
 *  Assigns functionality to switch, which takes user input, if within bounds, and creates an
 *  alarm that sounds on the specified time for 20 seconds and every minute unless turned off
 *  by the user.
 *
 *  Huge credit to Foxandroid
 *  source: https://www.youtube.com/watch?v=xSrVWFCtgaE.
 *  Much of the code is sourced from their alarm tutorial on youtube.
 *
 *  Alarm clock sound used.
 *  Sound used: https://freesound.org/people/joedeshon/sounds/78562/
 *  Creative license: https://creativecommons.org/licenses/by/4.0/
 *
 *  https://stackoverflow.com/questions/42211527/getpackagename-in-fragment
 *  When converting this from an activity to a fragment, had a hard time figuring out how to
 *  acquire certain variables for functions. Used throughout project in fragments.
 *  Credit to Steve for the answer.
 *
 *  https://abhiandroid.com/ui/edittext
 *  Manipulating variables. Credit toabhiandroid
 *
 *  https://stackoverflow.com/questions/4531396/get-value-of-a-edit-text-field
 *  Getting text value of textView. Credit to svdree
 *
 *  https://stackoverflow.com/questions/45919392/disable-sound-from-notificationchannel
 *  Setting sound to nothing. Credit to mVck
 *
 *
 */
public class alarmActivityReworkFragment extends Fragment {

    private FragmentAlarmclockBinding binding;

    //Checks whether AM or PM.
    //False = AM
    //True = PM
    //Initialized to AM
    private static boolean isPM = false;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentAlarmclockBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setValuesBackToUserInput();

        binding.AMorPM.setOnClickListener(e->switchAMPMButton());
        //Set the switch to do alarmAction.
        binding.AlarmSwitch1.setOnClickListener(e->doAlarmFunction());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**Source https://www.youtube.com/watch?v=xSrVWFCtgaE
     * Foxandroids private variables
     */
    private AlarmManager mainAlarm;
    private PendingIntent pendingIntent;

    private NotificationChannel channel;
    private NotificationManager notificationManager;

    public static String hour1 ="";
    public static String minute1 = "";
    public static boolean time1Flipped = false;

    /**@isCorrectSyntax(String hour, String minute)
     * @param hour
     * @param minute
     * @return whether or not was correct syntax
     */
    public boolean isCorrectSyntax(String hour, String minute){
        if(hour.isEmpty()||minute.isEmpty()){
            return false;
        }
        if((Integer.parseInt(hour)>=0)
                &&(Integer.parseInt(hour)<=12)
                &&(Integer.parseInt(minute)>=0)
                &&(Integer.parseInt(minute)<=60)){
            return true;
        }
        return false;
    }

    /**@setValuesBackToUser()
     * Resets the page back to the what the user has set it.
     */
    private void setValuesBackToUserInput(){
        Switch alarmSwitch = getActivity().findViewById(R.id.AlarmSwitch1);
        TextView textViewHour = getActivity().findViewById(R.id.AlarmTimeInputHour1);
        TextView textViewMinute = getActivity().findViewById(R.id.AlarmTimeInputMinute1);
        alarmSwitch.setChecked(time1Flipped);
        textViewHour.setText(String.valueOf(hour1));
        if((isPM)&&(Integer.parseInt(hour1)!=12)){
            int conversionHour = Integer.parseInt(hour1);
            String checker = String.valueOf(conversionHour - 12);
            textViewHour.setText(checker);
        }
        textViewMinute.setText(minute1);
        ToggleButton AMorPM = getActivity().findViewById(R.id.AMorPM);
        AMorPM.setChecked(isPM);
    }
    /**
     * @startAlarmClock(Calendar calendar)
     * @param calendar
     * takes a calendar object, sends a request to MyReceiver, and starts the alarm.
     * source: https://www.youtube.com/watch?v=xSrVWFCtgaE
     * Credit to Foxandroid. Sourced from their tutorial
     */
    private void startAlarmClock(Calendar calendar){
        mainAlarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(),1,intent,0);
        mainAlarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), 1000,
                pendingIntent);
    }
    /**@cancelAlarm()
     * @return void
     * Needed to use in order to cancel the alarm.
     * Credit to Foxandroid. Sourced from their tutorial
     * Source: https://www.youtube.com/watch?v=xSrVWFCtgaE
     */
    private void cancelAlarm(){
        Intent intent = new Intent(getActivity(),MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(),1,intent,0);
        if(mainAlarm == null){
            mainAlarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        }
        try{
            notificationManager.deleteNotificationChannel("Alarm System");
        }catch (NullPointerException e){
        }
        mainAlarm.cancel(pendingIntent);
        try {
            //This is the media player within the receiver. Due to some weird interaction I need to instead
            //have the sound play through the media player.
            MyReceiver.alarmSounder.stop();
        }catch(NullPointerException e){
            //This try catch is in-case the media player is not playing.
        }
        Toast.makeText(getActivity(), "Alarm Cancelled", Toast.LENGTH_SHORT).show();
    }

    /**@doAlarmFunction()
     * @return void
     *
     * This is the main alarm function.
     * Flips variable that says alarm is active
     * If alarm is inactive
     * Get the text within the user input
     *
     * If it is correct syntax
     *  create the notification for alarm
     *  Pull values from user input and create a calendar
     *  Toast the input back to the user
     *  Start alarm
     *
     * If not correct syntax
     *  flip variable back
     *  set alarm to boolean
     *  reset input
     *  Toast error text
     *
     * If alarm is inactive
     *  cancel alarm
     *
     *  Sources:
     *  https://abhiandroid.com/ui/edittext
     *      Manipulating variables
     *  https://stackoverflow.com/questions/4531396/get-value-of-a-edit-text-field
     *      Getting value of textViews
     *  https://www.youtube.com/watch?v=xSrVWFCtgaE
     *      Credit to Foxandroid. Parts sourced from their tutorial.
     */
    private void doAlarmFunction(){
        time1Flipped = !time1Flipped;
        if(time1Flipped) {
            TextView textViewHour = getActivity().findViewById(R.id.AlarmTimeInputHour1);
            TextView textViewMinute = getActivity().findViewById(R.id.AlarmTimeInputMinute1);
            if(isCorrectSyntax(
                    textViewHour.getText().toString(),
                    textViewMinute.getText().toString())) {
                //Credit to Foxandroid
                createNotificationForAlarm();
                //End of Credit
                String hour = textViewHour.getText().toString();
                String minute = textViewMinute.getText().toString();
                hour1 = hour;
                minute1 = minute;
                if(isPM&&((Integer.parseInt(hour)!=12))){
                    int beforeConversionHour = Integer.parseInt(hour);
                    hour1 = String.valueOf(beforeConversionHour + 12);
                }
                getActivity().findViewById(R.id.AMorPM);
                //Credit to Foxandroid
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hour1));
                calendar.set(Calendar.MINUTE,Integer.parseInt(textViewMinute.getText().toString()));
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                //End of Credit
                Date date = calendar.getTime();
                //In the case the user wants to set an alarm for tomorrow.
                int displayHours = date.getHours();
                int displayMinutes = date.getMinutes();
                String displayAMorPM;
                if(isPM){
                    displayAMorPM = "pm";
                    if(displayHours!=12) {
                        displayHours = displayHours - 12;
                    }
                }else{
                    displayAMorPM = "am";
                }
                String extraZeroIfNeeded="";
                if(displayMinutes<10){
                    extraZeroIfNeeded="0";
                }
                if(calendar.getTimeInMillis()<System.currentTimeMillis()){
                    calendar.setTimeInMillis(calendar.getTimeInMillis()+(24*60*60*1000));
                    Toast.makeText(getActivity().getApplication(), "Alarm set for tomorrow "+displayHours+":"+extraZeroIfNeeded+displayMinutes+" "+displayAMorPM, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity().getApplication(), "Alarm set for today "+displayHours+":"+extraZeroIfNeeded+displayMinutes+" "+displayAMorPM, Toast.LENGTH_SHORT).show();
                }
                //Credit to Foxandroid
                startAlarmClock(calendar);
                //End of credit
                setClickableButtons(false);
            }else{
                Toast.makeText(getActivity(), "Error, input valid time", Toast.LENGTH_SHORT).show();
                time1Flipped = false;
                Switch alarmSwitch = getActivity().findViewById(R.id.AlarmSwitch1);
                alarmSwitch.setChecked(time1Flipped);
                textViewHour.setText("");
                textViewMinute.setText("");
                setClickableButtons(true);
            }
        }else{
            //If you're switching the alarm off, stop the alarm.
            cancelAlarm();
            setClickableButtons(true);
        }
    }

    private void setClickableButtons(boolean input){
        TextView inputHour = getActivity().findViewById(R.id.AlarmTimeInputHour1);
        TextView inputMinute = getActivity().findViewById(R.id.AlarmTimeInputMinute1);
        ToggleButton amOrPm = getActivity().findViewById(R.id.AMorPM);
        inputHour.setEnabled(input);
        inputMinute.setEnabled(input);
        amOrPm.setEnabled(input);
    }
    /**
     * Switches variable isAm
     * if isPM = true
     * set to pm
     * if isPM = false
     * set to am
     */
    private void switchAMPMButton(){
        isPM = !isPM;
        //binding.AMorPM.setChecked(isPM);
    }

    /**@createNotificationForAlarm()
     * @return void
     * source: https://www.youtube.com/watch?v=xSrVWFCtgaE
     * All credit goes to Foxandroid. Request for the alarm system. This builds and
     * creates an object that will be used as a notification for the user to wake up.
     *
     * Sourced https://stackoverflow.com/questions/45919392/disable-sound-from-notificationchannel
     * Used for setting sound to nothing
     *
     */
    private void createNotificationForAlarm(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            CharSequence name = "baadsstressreliefChannel";
            String description = "Channel For Alarm";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            channel = new NotificationChannel("Alarm System",name,importance);
            channel.setDescription(description);
            //we dont want sound from here.
            //Sourced https://stackoverflow.com/questions/45919392/disable-sound-from-notificationchannel
            channel.setSound(null,null);

            notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}