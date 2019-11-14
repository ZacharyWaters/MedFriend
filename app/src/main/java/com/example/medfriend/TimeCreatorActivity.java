package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class TimeCreatorActivity extends AppCompatActivity {

    // Buttons
    Button cancelButton;
    Button addTimeButton;

    // Time Picker
    TimePicker myTimePicker;

    // On-click Listener for the cancel button
    View.OnClickListener cancelListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            Intent intentToReturnbyCancel = new Intent();
            setResult(2, intentToReturnbyCancel);
            finish();
        }
    };

    // On-click Listener for the add-time button
    View.OnClickListener addTimeListener = new View.OnClickListener(){
        public void  onClick  (View  v){

            // Extracts the Values from the Time picker
            int extactedHour = ViewUtils.getTimePickerHour(myTimePicker);

            // Extracts the Values from the Time picker
            int extactedMinute = ViewUtils.getTimePickerMinute(myTimePicker);

            // This is the intent that returns to the homepage, we will be filling it with variables
            Intent intentWithNewTime = new Intent();

            // Filling the intent with the variables
            intentWithNewTime.putExtra("Hour", extactedHour);
            intentWithNewTime.putExtra("Minute", extactedMinute);

            // Sets the proper result code so the homepage knows what to do
            setResult(3, intentWithNewTime);

            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // Default OnCreate Stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_creator);

        // Initializes and locates all the Buttons
        cancelButton = findViewById(R.id.cancelButton);
        addTimeButton = findViewById(R.id.acceptTimeButton);

        // Initializes the onClickListeners for the Buttons
        cancelButton.setOnClickListener(cancelListener);
        addTimeButton.setOnClickListener(addTimeListener);

        // Initializes and locates the Time picker
        myTimePicker = (TimePicker)findViewById(R.id.new_time_time_picker);
    }
}
