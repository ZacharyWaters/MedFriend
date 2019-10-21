package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

    Button btnDatePicker, btnTimePicker, saveAlarmButton, cancelAlarmButton;
    TextView txtDate, txtTime;
    private int year, month, day, hour, minute;
    int repeatinterval = -10;

    private static final String[] MEDICATION_NAMES = new String[]{
            "Acetaminophen","Amitriptyline", "Aspirin", "Tylenol"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_creator);

        btnDatePicker = findViewById(R.id.datebutton);
        btnTimePicker = findViewById(R.id.btn_time);
        saveAlarmButton = findViewById(R.id.saveAlarmButton);
        cancelAlarmButton = findViewById(R.id.cancelAlarmButton);
        Spinner repeatSpinner = findViewById(R.id.repeatSpinner);

        AutoCompleteTextView nameText = findViewById(R.id.autoCompleteName);
        ArrayAdapter<String> autoCompleteadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MEDICATION_NAMES);
        nameText.setAdapter(autoCompleteadapter);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.options_array, android.R.layout.simple_spinner_item);
// Set the layout to use for each dropdown item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        repeatSpinner.setAdapter(adapter);
        txtDate = findViewById(R.id.in_date);
        txtTime = findViewById(R.id.in_time);
        year = 0;
        month = 0;
        day = 0;
        hour = 0;
        minute = 0;

        repeatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                repeatinterval = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if (year == 0 || hour == 0) {

                if (year == 0) {
                    Toast.makeText(AlarmCreator.this, "Please select a date and time", Toast.LENGTH_LONG).show();
                } else {

                    //TODO: move code into the fragment/homepage so that we can cancel the alarms there.
                    //this is the code to set the alarm for now.
                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    GregorianCalendar calendar = new GregorianCalendar(year, month, day, hour, minute);
                    long time = calendar.getTimeInMillis();
                    Intent in = new Intent(getApplicationContext(), AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, in, 0);
                    am.setRepeating(AlarmManager.RTC_WAKEUP, time, 86400000 / repeatinterval, pendingIntent);



                    Intent intent = new Intent();
                    intent.putExtra("year", year);
                    intent.putExtra("month", month + 1);
                    intent.putExtra("day", day);
                    intent.putExtra("hour", hour);
                    intent.putExtra("minute", minute);
                    intent.putExtra("Interval", repeatinterval);
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
                intent.putExtra("month", month + 1);
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
        if (min < 10) {
            txtTime.setText(String.valueOf(hourtxt) + ":0" + String.valueOf(min) + PMAM);
        } else {
            txtTime.setText(String.valueOf(hourtxt) + ":" + String.valueOf(min) + PMAM);
        }
        hour = hr;
        minute = min;
    }

    public void setDate(int yr, int mnth, int dy) {
        txtDate = findViewById(R.id.in_date);
        txtDate.setText(String.valueOf(mnth+1) + "/" + String.valueOf(dy) + "/" + String.valueOf(yr) + " ");
        year = yr;
        month = mnth;
        day = dy;
    }
}
