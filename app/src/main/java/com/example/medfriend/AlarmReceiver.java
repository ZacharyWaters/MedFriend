package com.example.medfriend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context, "Alarm! Take your medicine!", Toast.LENGTH_LONG).show();

//        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if (alarmUri == null)
//        {
//            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        }
//        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
//        ringtone.play();

        try {
            Bundle bundle = intent.getExtras();
            String message = bundle.getString("alarm_message");
            String name = intent.getStringExtra("name");
            
            Intent newIntent = new Intent(context, PopupActivity.class);
            newIntent.putExtra("alarm_message", message);
            newIntent.putExtra("name", name);
            // Get the current Time and Pass that along too
            // that will be used at the key for the timelineMark in the database


            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(newIntent);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}