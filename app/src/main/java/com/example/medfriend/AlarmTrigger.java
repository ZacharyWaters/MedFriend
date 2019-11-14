package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

public class AlarmTrigger extends AppCompatActivity {

    // Static Text View
    TextView staticTriggerText;

    // Buttons
    Button skipMedicineButton;
    Button takeMedicineButton;

    // Ringtone
    Ringtone theTone;

    // Handler, this is used to time out the alarm
    Handler testHandler = new Handler();


    // Listener for the skip button
    View.OnClickListener skipMedicineListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            theTone.stop();
            // Remove any Lingering Handlers
            testHandler.removeCallbacks(shortTimerRunnable);
            Intent intent = new Intent(AlarmTrigger.this, LandingScreen.class);
            startActivity(intent);
        }
    };

    // Listener for the take button
    View.OnClickListener takeMedicineListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            theTone.stop();
            // Remove any Lingering Handlers
            testHandler.removeCallbacks(shortTimerRunnable);
            Intent intent = new Intent(AlarmTrigger.this, LandingScreen.class);
            startActivity(intent);
        }
    };

    // Listener for the android back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        skipMedicineButton.performClick();
    }

    private Runnable shortTimerRunnable = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(AlarmTrigger.this, "Timed out, marked as missed", Toast.LENGTH_LONG).show();
            skipMedicineButton.performClick();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default OnCreate Stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_trigger);

        // Initializes and locates all the static textViews
        staticTriggerText = findViewById(R.id.staticTriggerText);

        // Initializes and locates all the Buttons
        skipMedicineButton = findViewById(R.id.skipMedicineButton);
        takeMedicineButton = findViewById(R.id.takeMedicineButton);

        // Initializes the onClickListeners for the buttons
        skipMedicineButton.setOnClickListener(skipMedicineListener);
        takeMedicineButton.setOnClickListener(takeMedicineListener);

        // renames the static text field
        Intent triggerIntent = getIntent();
        String alarmName = triggerIntent.getStringExtra("name");
        staticTriggerText.setText("It is time to take " +alarmName);

        // Creates a Timer that goes off after a waiting period, triggering the skip button
        // 30000 milliseconds is 30 seconds
        testHandler.postDelayed(shortTimerRunnable, 30000);

        //Ringtone
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(alarmUri == null){
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        theTone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);

        // Triggers the Ringtone here
        theTone.play();

    }
}
