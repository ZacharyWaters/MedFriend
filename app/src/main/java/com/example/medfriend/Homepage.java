package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class Homepage extends AppCompatActivity {

    public ArrayList<MedAlarm> alarms;
    private FragmentManager fm;
    private static int id = 0;

    Button addDoseButton;
    Button addCareTakerButton;
    View.OnClickListener addDoseListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            Intent intent = new Intent(Homepage.this, AlarmCreator.class);
            startActivityForResult(intent, 1);
        }
    };

//    View.OnClickListener addCaretakerListener = new View.OnClickListener(){
//        public void  onClick  (View  v){
//            Intent intent = new Intent(Homepage.this, AddCaretaker.class);
//            startActivityForResult(intent, 1);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        alarms = new ArrayList<>();

        addDoseButton = findViewById(R.id.addDoseButton);
        addCareTakerButton = findViewById(R.id.addCaretakerButton);
        addDoseButton.setOnClickListener(addDoseListener);
        //addCareTakerButton.setOnClickListener(addCaretakerListener);
        fm = this.getSupportFragmentManager();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            int year = data.getIntExtra("year", 1920);
            int month = data.getIntExtra("month", 12);
            int day = data.getIntExtra("day", 25);
            int hour = data.getIntExtra("hour", 10);
            int minute = data.getIntExtra("minute", 30);
            int repeatinterval = data.getIntExtra("Interval", 1);



            if (year != 0 && hour != 0 && year != 1920) {
                Toast.makeText(Homepage.this, "Making Fragment", Toast.LENGTH_LONG).show();
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                GregorianCalendar calendar = new GregorianCalendar(year, month, day, hour, minute);
                long time = calendar.getTimeInMillis();
                Intent in = new Intent(getApplicationContext(), AlarmReceiver.class);
                int pendingID = (int) (Math.random() * 10);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, in, PendingIntent.FLAG_CANCEL_CURRENT);
                am.setRepeating(AlarmManager.RTC_WAKEUP, time, 86400000 / repeatinterval, pendingIntent);
                MedAlarm medalarm = new MedAlarm(year, month, day, hour, minute, repeatinterval, id);
                alarms.add(medalarm);

                FragmentTransaction fragmenttransaction = fm.beginTransaction();
                AlarmListRow newRow = AlarmListRow.newInstance(medalarm);
                newRow.setAl();
                fragmenttransaction.add(R.id.listview_medalertlist, newRow, medalarm.toString());
                fragmenttransaction.commit();
                id += 1;

            } else {
                Toast.makeText(Homepage.this, "Invalid Date and Time", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void deleteAlarm(MedAlarm m, String alarm) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment frag:fragmentList ) {
            if(alarm.equals(frag.getTag())) {
                Log.d("g", frag.getTag() + "     " + alarm + "    " + alarm.equals(frag.getTag()));
                if(frag != null) {
                    FragmentTransaction fragmenttransaction = fm.beginTransaction();
                    fragmenttransaction.remove(frag);
                    fragmenttransaction.commitNow();
                }
                //Log.d("del", "specific fragment removed");
            }
        }

        //LinearLayout formLayout = (LinearLayout)findViewById(R.id.formLayout);
        //formLayout.removeAllViews();
        int alarmID = m.getID();
        Intent in = new Intent(getApplicationContext(), AlarmReceiver.class);
        long time = m.getTime();
        PendingIntent p = PendingIntent.getBroadcast(getApplicationContext(), m.getID(), in, 0);

        am.cancel(p);
        //Log.d("del", "alarm cancelled");
    }

    public void disableAlarm(MedAlarm m) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent in = new Intent(getApplicationContext(), AlarmReceiver.class);
        long time = m.getTime();
        PendingIntent p = PendingIntent.getBroadcast(getApplicationContext(), m.getID(), in, 0);
        Log.d("del", m.getID() + " deled");


        am.cancel(p);
    }

    public void enableAlarm(MedAlarm m) {

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent in = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), m.getID(), in, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, m.getTime(), 86400000 / m.getInterval(), pendingIntent);
    }
}