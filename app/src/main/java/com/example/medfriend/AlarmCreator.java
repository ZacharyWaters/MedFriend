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

public class AlarmCreator extends AppCompatActivity {

    Button btnDatePicker, btnTimePicker, saveAlarmButton, cancelAlarmButton;
    TextView txtDate, txtTime;
    private int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_creator);

        btnDatePicker = findViewById(R.id.datebutton);
        btnTimePicker = findViewById(R.id.btn_time);
        saveAlarmButton = findViewById(R.id.saveAlarmButton);
        cancelAlarmButton = findViewById(R.id.cancelAlarmButton);
        txtDate = findViewById(R.id.in_date);
        txtTime = findViewById(R.id.in_time);
        year = 0;
        month = 0;
        day = 0;
        hour = 0;
        minute = 0;

        saveAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (year == 0 || hour == 0) {
                    Toast.makeText(AlarmCreator.this, "Please select a date and time", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("year", year);
                    intent.putExtra("month", month);
                    intent.putExtra("day", day);
                    intent.putExtra("hour", hour);
                    intent.putExtra("minute", minute);
                    setResult(1, intent);
                    finish();
                }
            }
        });

        cancelAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                setResult(1, intent);
                finish();
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
        int hourtxt = (hr  > 12) ? (hr - 12) : (hr);
        PMAM = (hr > 12) ? "PM" : "AM";
        txtTime.setText(String.valueOf(hourtxt) + ":" + String.valueOf(min) + PMAM);
        hour = hr;
        minute = min;
    }

    public void setDate(int yr, int mnth, int dy) {
        txtDate = findViewById(R.id.in_date);
        txtDate.setText(String.valueOf(dy) + "/" + String.valueOf(mnth) + "/" + String.valueOf(yr) + " ");
        year = yr;
        month = mnth;
        day = dy;
    }
}
