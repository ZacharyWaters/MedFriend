package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Homepage extends AppCompatActivity {

    private ArrayList<MedAlarm> alarms;

    Button addDoseButton;
    View.OnClickListener addDoseListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            Intent intent = new Intent(Homepage.this, AlarmCreator.class);
            startActivityForResult(intent, 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        addDoseButton = findViewById(R.id.addDoseButton);
        addDoseButton.setOnClickListener(addDoseListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            int year = data.getIntExtra("year", 1920);
            int month = data.getIntExtra("month", 12);
            int day = data.getIntExtra("day", 25);
            int hour = data.getIntExtra("hour", 10);
            int minute = data.getIntExtra("minute", 30);
            if (year == 0 || hour == 0 || year == 1920)
            alarms.add(new MedAlarm(year, month, day, hour, minute));
        }
    }
}