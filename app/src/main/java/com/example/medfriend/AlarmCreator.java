package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmCreator extends AppCompatActivity {

    TextView timerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_creator);

        timerText = (TextView)findViewById(R.id.alarmText);
    }
}
