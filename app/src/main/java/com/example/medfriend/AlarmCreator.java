package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AlarmCreator extends AppCompatActivity {

    Button btnDatePicker, btnTimePicker, alarmButton;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    EditText txtDate, txtTime;
    private int year, month, day, hour, minute;
    long time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_creator);


        btnDatePicker = (Button) findViewById(R.id.datebutton);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        alarmButton = findViewById(R.id.alarmbutton);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);




        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                GregorianCalendar calendar = new GregorianCalendar(year, month, day, hour, minute);
                Toast.makeText(AlarmCreator.this, String.valueOf(calendar.getTime()), Toast.LENGTH_SHORT).show();
                calendar.add(Calendar.SECOND, 30);
                time = calendar.getTimeInMillis();
                //time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
//                if(System.currentTimeMillis()>time)
//                {
//                    if (calendar.AM_PM == 0)
//                        time = time + (1000*60*60*12);
//                    else
//                        time = time + (1000*60*60*24);
//                }
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 30);
                time = cal.getTimeInMillis();
                Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

                am.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
            }
        });
        //btnTimePicker.setOnClickListener(this);

    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void setTime(int hr, int min) {
        String PMAM = "";
        txtTime = findViewById(R.id.in_time);
        int hou = (hr  > 12) ? (hr - 12) : (hr);
        PMAM = (hr > 12) ? "PM" : "AM";
        this.hour = hr;
        this.minute = min;
        txtTime.setText(String.valueOf(hou) + ":" + String.valueOf(min) + PMAM);

    }

    public void setDate(int year, int month, int day) {
        txtDate = findViewById(R.id.in_date);
        txtDate.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year) + " ");
        this.year = year;
        this.month = month;
        this.day = day;


    }
}
