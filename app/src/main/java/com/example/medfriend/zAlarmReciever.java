package com.example.medfriend;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getActivity;


public class zAlarmReciever extends BroadcastReceiver
{
    //private static final String BUNDLE_EXTRA = "bundle_extra";
    //private static final String ALARM_KEY = "alarm_key";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        // This gets the alarm's information that is currently going off
        Bundle bundle = intent.getExtras();
        String alarmName = bundle.getString("alarmName");
        boolean[] daysOfWeek = bundle.getBooleanArray("daysOfWeek");

        // Extracts all the relevant information of the alarm, Name
        String virtualTimes = bundle.getString("virtualTimes");
        ArrayList<ExampleTime> reconstructedTimes = new ArrayList<ExampleTime>();
        String[] tempArray1 = virtualTimes.split("#");

        // Reconstructs the virtual time arraylist
        for(int i = 0; i < tempArray1.length; i ++){
            String objectBlock = tempArray1[i];
            String[] tempArray2 = objectBlock.split("@");
            int hour = Integer.parseInt(tempArray2[0]);
            int minute = Integer.parseInt(tempArray2[1]);
            String message = tempArray2[2];
            ExampleTime reconstructedTime = new ExampleTime(message,hour,minute);
            reconstructedTimes.add(reconstructedTime);
        }

        // Uses this information to create a new alarm
        AlarmInitializer.setAlarmClosestTime(alarmName, daysOfWeek, reconstructedTimes, context);


        // This makes a Toast that Appears with the message "Take your medicine"
        Toast.makeText(context, "Alarm! Take your medicine!", Toast.LENGTH_LONG).show();


        // We now want to direct the user to an activity where they can either "take" or "ignore"
        // their medication
        try {

            // Make the new intent for the Activity
            Intent newIntent = new Intent(context, AlarmTrigger.class);

            // pass all the necessary information to the next intent,
            // such that it can deduce what alarm is going off

            // newIntent.putExtra("alarm_message", message);
            newIntent.putExtra("name", alarmName);

            // Get the current Time and Pass that along too
            // that will be used at the key for the timelineMark in the database
            Calendar calendar = Calendar.getInstance();
            //Returns current time in millis
            long timeMilli = calendar.getTimeInMillis();
            //Converts that millisecond value into a String for the key
            String timeDatabaseKey = Long.toString(timeMilli);
            newIntent.putExtra("timeKey", timeDatabaseKey);


            // This flags the intent as SUPER IMPORTANT
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(newIntent);


        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
