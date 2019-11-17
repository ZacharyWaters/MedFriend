package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class AlarmActivity extends AppCompatActivity {

    // Static Text-Views
    TextView staticLabel;
    TextView staticTimeWarning;

    // User Entry Text-views
    TextView alarmNameInput;

    // Buttons
    Button addNewTimeButton;
    Button acceptAlarmButton;
    Button cancelButton;

    // Radio-Button
    RadioButton monday_Button;
    RadioButton tuesday_Button;
    RadioButton wednesday_Button;
    RadioButton thursday_Button;
    RadioButton friday_Button;
    RadioButton saturday_Button;
    RadioButton sunday_Button;

    // Creates the Variables that manage the Radio-Buttons
    boolean monday_Toggle = false;
    boolean tuesday_Toggle = false;
    boolean wednesday_Toggle = false;
    boolean thursday_Toggle = false;
    boolean friday_Toggle = false;
    boolean saturday_Toggle = false;
    boolean sunday_Toggle = false;

    //This is the recycler view that stores the time items
    private RecyclerView timeRecyclerView;

    // This is the adapter for the time recycler view to make sure it runs efficiently
    private RecyclerView.Adapter timeRecyclerViewAdapter;

    // This is the manager for the time recycler view to make sure it lines up the items nicely
    private RecyclerView.LayoutManager timeRecyclerLayoutManager;

    // ArrayList for the recycler Time Items
    ArrayList <ExampleTime> exampleTimeArraylist;

    // Request code
    // 6 == default
    // 7 == Edit
    int requestCode;

    String oldAlarmKey;

    // This method initialises the time Array
    public void createTimeExampleList(){

        // Initializes the exampleTimeArray
        exampleTimeArraylist = new ArrayList<>();
    }

    public void buildRecyclerView(){
        // Initializes the Recycler-View and all its stuff
        timeRecyclerView = findViewById(R.id.timeRecycler);
        timeRecyclerLayoutManager = new LinearLayoutManager(this);
        timeRecyclerViewAdapter = new ExampleTimeAdapter(exampleTimeArraylist, AlarmActivity.this);
        timeRecyclerView.setLayoutManager(timeRecyclerLayoutManager);
        timeRecyclerView.setAdapter(timeRecyclerViewAdapter);
    }

    public void insertItem(int position, ExampleTime providedTime) {
        exampleTimeArraylist.add(position, providedTime);
        timeRecyclerViewAdapter.notifyItemInserted(position);
    }
    public void removeItemAtIndex(int position) {
        exampleTimeArraylist.remove(position);
        timeRecyclerViewAdapter.notifyItemRemoved(position);
    }
    public int getIndexofTime(ExampleTime lookingTime) {
        int i = exampleTimeArraylist.indexOf(lookingTime);
        return i;
    }

    public int getIndexofTimeMessage(String oldMessage){
        int i = 0;
        boolean brakes = false;
        while(i < exampleTimeArraylist.size() && brakes != true){
            String existingMessage = exampleTimeArraylist.get(i).getExampleTimeMessage();
            if(oldMessage.equals(existingMessage)){
                brakes = true;
            }
        }
        if(brakes == true){
            return i;
        } else {
            return -1;
        }
    }

    public void checkWarningTextVisible() {
        // Gets the Number of elements in the Recycler View
        int timeRecyclerCount = timeRecyclerViewAdapter.getItemCount();

        // Checks if there is at least 1 element in the RecyclerView
        // If so, hides the warning text, else activates it
        if(timeRecyclerCount >= 1){
            staticTimeWarning.setVisibility(View.INVISIBLE);
        } else{
            staticTimeWarning.setVisibility(View.VISIBLE);
        }
    }


    // toggleCount is the toggles active, to make sure the user can't move down to zero
    int toggleCount = 0;

    // Creates the Listeners, that would  uses by the Radio buttons  ===========================================================

    // Creates the Monday Listener
    View.OnClickListener mondayButtonListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            // If not already selected, activate, increment ToggleCount
            if(monday_Toggle == false){
                monday_Toggle = true;
                monday_Button.setChecked(true);
                toggleCount = toggleCount + 1;
                // If already selected and you have at least 1 other day selected, unToggle, and decrement
            } else if(toggleCount > 1){
                monday_Toggle = false;
                monday_Button.setChecked(false);
                toggleCount = toggleCount - 1;
            } // Else, do nothing because you need at least one day selected
        }
    };

    // Creates the Tuesday Listener
    View.OnClickListener tuesdayButtonListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            // If not already selected, activate, increment ToggleCount
            if(tuesday_Toggle == false){
                tuesday_Toggle = true;
                tuesday_Button.setChecked(true);
                toggleCount = toggleCount + 1;
                // If already selected and you have at least 1 other day selected, unToggle, and decrement
            } else if(toggleCount > 1){
                tuesday_Toggle = false;
                tuesday_Button.setChecked(false);
                toggleCount = toggleCount - 1;
            } // Else, do nothing because you need at least one day selected
        }
    };

    // Creates the wednesday Listener
    View.OnClickListener wednesdayButtonListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            // If not already selected, activate, increment ToggleCount
            if(wednesday_Toggle == false){
                wednesday_Toggle = true;
                wednesday_Button.setChecked(true);
                toggleCount = toggleCount + 1;
                // If already selected and you have at least 1 other day selected, unToggle, and decrement
            } else if(toggleCount > 1){
                wednesday_Toggle = false;
                wednesday_Button.setChecked(false);
                toggleCount = toggleCount - 1;
            } // Else, do nothing because you need at least one day selected
        }
    };

    // Creates the thursday Listener
    View.OnClickListener thursdayButtonListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            // If not already selected, activate, increment ToggleCount
            if(thursday_Toggle == false){
                thursday_Toggle = true;
                thursday_Button.setChecked(true);
                toggleCount = toggleCount + 1;
                // If already selected and you have at least 1 other day selected, unToggle, and decrement
            } else if(toggleCount > 1){
                thursday_Toggle = false;
                thursday_Button.setChecked(false);
                toggleCount = toggleCount - 1;
            } // Else, do nothing because you need at least one day selected
        }
    };

    // Creates the friday Listener
    View.OnClickListener fridayButtonListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            // If not already selected, activate, increment ToggleCount
            if(friday_Toggle == false){
                friday_Toggle = true;
                friday_Button.setChecked(true);
                toggleCount = toggleCount + 1;
                // If already selected and you have at least 1 other day selected, unToggle, and decrement
            } else if(toggleCount > 1){
                friday_Toggle = false;
                friday_Button.setChecked(false);
                toggleCount = toggleCount - 1;
            } // Else, do nothing because you need at least one day selected
        }
    };

    // Creates the saturday Listener
    View.OnClickListener saturdayButtonListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            // If not already selected, activate, increment ToggleCount
            if(saturday_Toggle == false){
                saturday_Toggle = true;
                saturday_Button.setChecked(true);
                toggleCount = toggleCount + 1;
                // If already selected and you have at least 1 other day selected, unToggle, and decrement
            } else if(toggleCount > 1){
                saturday_Toggle = false;
                saturday_Button.setChecked(false);
                toggleCount = toggleCount - 1;
            } // Else, do nothing because you need at least one day selected
        }
    };

    // Creates the sunday Listener
    View.OnClickListener sundayButtonListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            // If not already selected, activate, increment ToggleCount
            if(sunday_Toggle == false){
                sunday_Toggle = true;
                sunday_Button.setChecked(true);
                toggleCount = toggleCount + 1;
                // If already selected and you have at least 1 other day selected, unToggle, and decrement
            } else if(toggleCount > 1){
                sunday_Toggle = false;
                sunday_Button.setChecked(false);
                toggleCount = toggleCount - 1;
            } // Else, do nothing because you need at least one day selected
        }
    };

    // End of the Radio Button Listeners ====================================================================================

    // Listener for the add new Time Button
    View.OnClickListener addTimeListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            Intent intent = new Intent(AlarmActivity.this, TimeCreatorActivity.class);
            intent.putExtra("requestCode", 1);
            startActivityForResult(intent, 1);
        }
    };

    // On-click Listener for the cancel button
    View.OnClickListener cancelListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            Intent intentToReturnbyCancel = new Intent();
            setResult(1, intentToReturnbyCancel);
            finish();
        }
    };

    // On-click Listener for the Accept button
    View.OnClickListener acceptAlarmListener = new View.OnClickListener(){
        public void  onClick  (View  v){

            // These are used to make sure you have the proper user inputs before continuing
            boolean failFlagZeroCount = false;
            boolean failFlagNoName = false;

            // gets the number of Times to make sure there is at least one
            int countOfTimes = exampleTimeArraylist.size();

            if(countOfTimes == 0){
                failFlagZeroCount = true;
            }

            // Extracts the name from the User Input Field
            String extractedName = alarmNameInput.getText().toString();

            // Checks to make sure the name is not empty
            if(extractedName.isEmpty()) {
                failFlagNoName = true;
            }

            if(failFlagZeroCount == true){
                Toast.makeText(AlarmActivity.this,
                        "You must have at least one time for your alarm",
                        Toast.LENGTH_LONG).show();

            } else if(failFlagNoName == true){
                Toast.makeText(AlarmActivity.this,
                        "You must assign a label for the alarm",
                        Toast.LENGTH_LONG).show();
            } else {
                // This is the intent that returns to the homepage, we will be filling it with variables
                Intent intentToReturnByAccept = new Intent();

                // Gets the Dates
                boolean[] weekDatesArray = {monday_Toggle, tuesday_Toggle, wednesday_Toggle, thursday_Toggle, friday_Toggle, saturday_Toggle, sunday_Toggle};


                // Gets the Times
                ArrayList<String> extractedTimes = new ArrayList<String>();
                for(int i = 0; i < exampleTimeArraylist.size(); i++){
                    ExampleTime iterativeTimeValue = exampleTimeArraylist.get(i);
                    String iterativeTimeMessage = iterativeTimeValue.getExampleTimeMessage();
                    extractedTimes.add(iterativeTimeMessage);
                }
//            String[] convertedToSend = new String[extractedTimes.size()];
//            convertedToSend = extractedTimes.toArray(convertedToSend);


                // Filling the intent with the variables
                intentToReturnByAccept.putExtra("Name", extractedName);

                intentToReturnByAccept.putExtra("Weekdays", weekDatesArray);

                intentToReturnByAccept.putExtra("Times", extractedTimes);



                if(requestCode == 6){
                    // Creating the actual alarm #################################################################

                    // Gets the current Time to use as as the key for the new ExampleAlarm
                    Calendar calendar = Calendar.getInstance();

                    // Returns current time in millis
                    long timeMilli = calendar.getTimeInMillis();

                    // Converts that millisecond value into a String for the key
                    String alarmKeyString = Long.toString(timeMilli);

                    // Adds the alarmKeyString to the Intent to be passed to te Landing Screen Class
                    intentToReturnByAccept.putExtra("alarmKey", alarmKeyString);

                    AlarmInitializer.setAlarmClosestTime(extractedName, weekDatesArray, exampleTimeArraylist, getApplicationContext(), alarmKeyString);

                    // END OF ACTUALLY CREATING ALARM #################################################################

                    // Sets the proper result code so the homepage knows what to do
                    setResult(5, intentToReturnByAccept);

                    finish();


                } else if (requestCode == 7){

                    Context applicationContext = getApplicationContext();

                    // Disable the alarm
                    AlarmInitializer.cancelAlarmWithKey(oldAlarmKey,applicationContext);

                    // Remove it from the active list
                    ((GlobalVariables)applicationContext).deleteActiveAlarm(oldAlarmKey);

                    // Pass oldKey back so it can be removed from the recycler list
                    intentToReturnByAccept.putExtra("OldAlarmKey", oldAlarmKey);

                    // Creating the actual alarm #################################################################

                    // Gets the current Time to use as as the key for the new ExampleAlarm
                    Calendar calendar = Calendar.getInstance();

                    // Returns current time in millis
                    long timeMilli = calendar.getTimeInMillis();

                    // Converts that millisecond value into a String for the key
                    String alarmKeyString = Long.toString(timeMilli);

                    // Adds the alarmKeyString to the Intent to be passed to te Landing Screen Class
                    intentToReturnByAccept.putExtra("alarmKey", alarmKeyString);

                    AlarmInitializer.setAlarmClosestTime(extractedName, weekDatesArray, exampleTimeArraylist, getApplicationContext(), alarmKeyString);

                    // END OF ACTUALLY CREATING ALARM #################################################################

                    // Sets the proper result code so the homepage knows what to do
                    setResult(8, intentToReturnByAccept);

                    finish();


                }

            }
        }
    };

    // Helper method that converts the military time into a natural time in string form
    public String militaryToNatural(int inputHour, int inputMinute){
        String minuteString = String.valueOf(inputMinute);
        if(inputMinute < 10){
            minuteString = "0" + minuteString;
        }
        String hourString;
        String AM_OR_PM;
        if(inputHour == 12){
            hourString = String.valueOf(inputHour);
            AM_OR_PM = "PM";
        } else if(inputHour == 0){
            hourString = String.valueOf(12);
            AM_OR_PM = "AM";
        } else if(inputHour >= 13){
            inputHour = inputHour - 12;
            hourString = String.valueOf(inputHour);
            AM_OR_PM = "PM";
        } else {
            hourString = String.valueOf(inputHour);
            AM_OR_PM = "AM";
        }
        String returnString = hourString + ":" + minuteString + " " + AM_OR_PM;
        return returnString;
    }

    // This is what happens when you return from the addTime Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // A result Code of 3 means the user selected "Accept" for the time creator
        if (resultCode == 3) {

            //Extracts the hour and minute provided by the user
            int extracted_Hour = data.getIntExtra("Hour", 0);
            int extracted_Minute = data.getIntExtra("Minute", 0);

            // Converts the given hour and minute into AM or PM time
            String naturalLabel = militaryToNatural(extracted_Hour, extracted_Minute);


            // Makes a new time object for the RecyclerView
            ExampleTime userCreatedTime = new ExampleTime(naturalLabel,extracted_Hour,extracted_Minute);

            // check if the array is empty, else
            int timeArrayLength = exampleTimeArraylist.size();

            if(timeArrayLength == 0){
                insertItem(0,userCreatedTime);
            } else {
                int i = 0;
                boolean brakes = false;
                boolean isDuplicate = false;
                while(i < timeArrayLength && brakes != true) {

                    // gets the time at the index i
                    ExampleTime iterativeTime = exampleTimeArraylist.get(i);

                    // gets the hour and minute of the iterative time
                    int realIterativeHour = iterativeTime.getRealHourValue();
                    int realiterativeMinute = iterativeTime.getRealMinuteValue();

                    // compares starting with hour, then minute, then checking if duplicate
                    if(extracted_Hour < realIterativeHour){

                        // Sets the brakes to true
                        brakes = true;

                        // this counteracts the increment below
                        i = i - 1;

                        // This account for minutes
                    } else if((extracted_Hour == realIterativeHour) && (extracted_Minute < realiterativeMinute)){

                        // Sets the brakes to true
                        brakes = true;

                        // this counteracts the increment below
                        i = i - 1;
                    } else if((extracted_Hour == realIterativeHour) && (extracted_Minute == realiterativeMinute)){
                        isDuplicate = true;
                    }

                    // increments i
                    i = i + 1;
                }
                // Inserts it into the array at the exact position required to be ordered chronologically
                if(isDuplicate == false){

                    insertItem(i,userCreatedTime);
                }
                // added alarm was a duplicate, so we make a toast
                else {

                    Toast.makeText(AlarmActivity.this, "Time Already Added", Toast.LENGTH_LONG).show();

                }
            }

            //Update the warning Text
            checkWarningTextVisible();

            // Reloads the Recycler View
        // THIS MEANS THE USER CLICKED ACCEPT AFTER EDITING THEIR TIME
        } else if(resultCode == 4){
            // get the string oldMessage value
            String extracted_Old_Text = data.getStringExtra("OldText");

            // find the index of the time that corresponds to oldMessage
            int indexToDelete = getIndexofTimeMessage(extracted_Old_Text);

            // delete that index
            removeItemAtIndex(indexToDelete);

            //Extracts the hour and minute provided by the user
            int extracted_Hour = data.getIntExtra("Hour", 0);
            int extracted_Minute = data.getIntExtra("Minute", 0);

            // Converts the given hour and minute into AM or PM time
            String naturalLabel = militaryToNatural(extracted_Hour, extracted_Minute);


            // Makes a new time object for the RecyclerView
            ExampleTime userCreatedTime = new ExampleTime(naturalLabel,extracted_Hour,extracted_Minute);


            // check if the array is empty, else
            int timeArrayLength = exampleTimeArraylist.size();

            if(timeArrayLength == 0){
                insertItem(0,userCreatedTime);
            } else {
                int i = 0;
                boolean brakes = false;
                boolean isDuplicate = false;
                while(i < timeArrayLength && brakes != true) {

                    // gets the time at the index i
                    ExampleTime iterativeTime = exampleTimeArraylist.get(i);

                    // gets the hour and minute of the iterative time
                    int realIterativeHour = iterativeTime.getRealHourValue();
                    int realiterativeMinute = iterativeTime.getRealMinuteValue();

                    // compares starting with hour, then minute, then checking if duplicate
                    if(extracted_Hour < realIterativeHour){

                        // Sets the brakes to true
                        brakes = true;

                        // this counteracts the increment below
                        i = i - 1;

                        // This account for minutes
                    } else if((extracted_Hour == realIterativeHour) && (extracted_Minute < realiterativeMinute)){

                        // Sets the brakes to true
                        brakes = true;

                        // this counteracts the increment below
                        i = i - 1;
                    } else if((extracted_Hour == realIterativeHour) && (extracted_Minute == realiterativeMinute)){
                        isDuplicate = true;
                    }

                    // increments i
                    i = i + 1;
                }
                // Inserts it into the array at the exact position required to be ordered chronologically
                if(isDuplicate == false){

                    insertItem(i,userCreatedTime);
                }
                // added alarm was a duplicate, so we make a toast
                else {

                    Toast.makeText(AlarmActivity.this, "Time Already Added", Toast.LENGTH_LONG).show();

                }
            }

            //Update the warning Text
            checkWarningTextVisible();
        }
    }

    public void setDaysofWeekButtons(boolean[] preset){
        if(preset[0] == true){
            monday_Button.performClick();
        }
        if(preset[1] == true){
            tuesday_Button.performClick();
        }
        if(preset[2] == true){
            wednesday_Button.performClick();
        }
        if(preset[3] == true){
            thursday_Button.performClick();
        }
        if(preset[4] == true){
            friday_Button.performClick();
        }
        if(preset[5] == true){
            saturday_Button.performClick();
        }
        if(preset[6] == true){
            sunday_Button.performClick();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default OnCreate Stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // Initializes and locates all the static textViews
        staticLabel = findViewById(R.id.staticTextLabel);
        staticTimeWarning = findViewById(R.id.staticRecyclerWarnText);

        // Initializes and locates all the user entry textViews
        alarmNameInput = findViewById(R.id.edit_alarm_label);

        // Initializes and locates all the Buttons
        addNewTimeButton = findViewById(R.id.addNewTimeButton);
        acceptAlarmButton = findViewById(R.id.acceptAlarmButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Initializes and locates all the Radio Buttons
        monday_Button = findViewById(R.id.radioMondayButton);
        tuesday_Button = findViewById(R.id.radioTuesdayButton);
        wednesday_Button = findViewById(R.id.radioWednesdayButton);
        thursday_Button = findViewById(R.id.radioThursdayButton);
        friday_Button = findViewById(R.id.radioFridayButton);
        saturday_Button = findViewById(R.id.radioSaturdayButton);
        sunday_Button = findViewById(R.id.radioSundayButton);

        // Initializes the onClickListeners for the radio buttons
        monday_Button.setOnClickListener(mondayButtonListener);
        tuesday_Button.setOnClickListener(tuesdayButtonListener);
        wednesday_Button.setOnClickListener(wednesdayButtonListener);
        thursday_Button.setOnClickListener(thursdayButtonListener);
        friday_Button.setOnClickListener(fridayButtonListener);
        saturday_Button.setOnClickListener(saturdayButtonListener);
        sunday_Button.setOnClickListener(sundayButtonListener);

        // Initializes the onClickListeners for the addTime Button
        addNewTimeButton.setOnClickListener(addTimeListener);

        // Initializes the Cancel and Accept Button
        cancelButton.setOnClickListener(cancelListener);
        acceptAlarmButton.setOnClickListener(acceptAlarmListener);


        //Start by adding all the time elements of the existing alarm, if you are in edit mode

        // Initializes and Sets up the RecyclerView Stuff
        createTimeExampleList();
        buildRecyclerView();

        // Initial Check of the warning Text
        checkWarningTextVisible();


        // DEFAULT STUFF VS EDIT STUFF
        // ON EDIT STUFF:
        Intent creatorIntent = getIntent();
        requestCode = creatorIntent.getIntExtra("requestCode", 6);

        if(requestCode == 6){
            Calendar calendar = Calendar.getInstance();
            int current_Day = calendar.get(Calendar.DAY_OF_WEEK);

            // Automatically toggles the current day of the week radiobutton
            switch (current_Day) {
                case Calendar.MONDAY:
                    // Current day is Monday
                    monday_Button.setChecked(true);
                    monday_Toggle = true;
                    toggleCount = 1;
                    break;
                case Calendar.TUESDAY:
                    // Current day is Tuesday
                    tuesday_Button.setChecked(true);
                    tuesday_Toggle = true;
                    toggleCount = 1;
                    break;
                case Calendar.WEDNESDAY:
                    // Current day is Wednesday
                    wednesday_Button.setChecked(true);
                    wednesday_Toggle = true;
                    toggleCount = 1;
                    break;
                case Calendar.THURSDAY:
                    // Current day is Thursday
                    thursday_Button.setChecked(true);
                    thursday_Toggle = true;
                    toggleCount = 1;
                    break;
                case Calendar.FRIDAY:
                    // Current day is Friday
                    friday_Button.setChecked(true);
                    friday_Toggle = true;
                    toggleCount = 1;
                    break;
                case Calendar.SATURDAY:
                    // Current day is Saturday
                    saturday_Button.setChecked(true);
                    saturday_Toggle = true;
                    toggleCount = 1;
                    break;
                case Calendar.SUNDAY:
                    // Current day is Sunday
                    sunday_Button.setChecked(true);
                    sunday_Toggle = true;
                    toggleCount = 1;
                    break;
            }
        // EDIT STUFF
        } else if(requestCode == 7){

            // Extract Variables from Intent:
            String pregen_alarmName = creatorIntent.getStringExtra("AlarmName");
            boolean[] pregen_DaysofWeek = creatorIntent.getBooleanArrayExtra("DaysOfWeek");
            String pregen_AlarmID = creatorIntent.getStringExtra("AlarmID");
            ArrayList<String> pregen_TimesArray = creatorIntent.getStringArrayListExtra("TimeArray");

            // Helper method that turns arraylist of strings into array list of example times
            // method that adds time in order to array
            // Helper method that activates the buttons based on the pregen_DaysofWeek

            // Save one of them into a class variable to be used when the button is pressed
            // set the pre-set variables up
            alarmNameInput.setText(pregen_alarmName);
            setDaysofWeekButtons(pregen_DaysofWeek);
            oldAlarmKey = pregen_AlarmID;
            ArrayList<ExampleTime> pregenTimesConvertedArray = zHelperMethods.turnStringListTimesIntoExampleTimesList(pregen_TimesArray);
            for(int i = 0; i < pregenTimesConvertedArray.size(); i++){
                ExampleTime pregenTime = pregenTimesConvertedArray.get(i);
                insertItem(i,pregenTime);
            }

        }


        // Automatically sets the timePicker to the current Time




    }
}