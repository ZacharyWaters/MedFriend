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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmCreator extends AppCompatActivity {

    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_creator);

        btnDatePicker = (Button) findViewById(R.id.datebutton);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);


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
        int hour = (hr  > 12) ? (hr - 12) : (hr);
        PMAM = (hr > 12) ? "PM" : "AM";
        txtTime.setText(String.valueOf(hour) + ":" + String.valueOf(min) + PMAM);
    }

    public void setDate(int year, int month, int day) {
        txtDate = findViewById(R.id.in_date);
        txtDate.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year) + " ");
    }
}
