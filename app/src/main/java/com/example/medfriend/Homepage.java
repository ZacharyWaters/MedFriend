package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Homepage extends AppCompatActivity {

    public ArrayList<MedAlarm> alarms;
    private FragmentManager fm;

    Button addDoseButton;
    Button addCareTakerButton;
    View.OnClickListener addDoseListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            Intent intent = new Intent(Homepage.this, AlarmCreator.class);
            startActivityForResult(intent, 1);
        }
    };

    View.OnClickListener addCaretakerListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            Intent intent = new Intent(Homepage.this, AddCaretaker.class);
            startActivityForResult(intent, 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        alarms = new ArrayList<>();

        addDoseButton = findViewById(R.id.addDoseButton);
        addCareTakerButton = findViewById(R.id.addCaretakerButton);
        addDoseButton.setOnClickListener(addDoseListener);
        addCareTakerButton.setOnClickListener(addCaretakerListener);
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
            if (year != 0 && hour != 0 && year != 1920) {
                Toast.makeText(Homepage.this, "Making Fragment", Toast.LENGTH_LONG).show();
                MedAlarm medalarm = new MedAlarm(year, month, day, hour, minute);
                alarms.add(medalarm);
                /*
                FragmentTransaction fragmenttransaction = fm.beginTransaction();
                AlarmListRow newRow = AlarmListRow.newInstance(medalarm);
                fragmenttransaction.add(R.id.listview_medalertlist, newRow, "HELLO");
                fragmenttransaction.commit();
                 */
            } else {
                Toast.makeText(Homepage.this, "Invalid Date and Time", Toast.LENGTH_LONG).show();
            }
        }

    }
}