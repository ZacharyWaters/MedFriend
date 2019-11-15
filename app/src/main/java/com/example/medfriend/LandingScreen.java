package com.example.medfriend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class LandingScreen extends AppCompatActivity {

    // Static Text-Views
    TextView staticLabel;
    TextView staticAlarmWarning;

    // Buttons
    Button addCareTakerButton;
    Button addAlarmButton;

    // The counter of people requesting you for care-taking
    int requestCounter;

    // The counter of people you are currently care-taking
    int careTakerCount;

    // Listener for the add new Time Button
    View.OnClickListener addAlarmListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            Intent intent = new Intent(LandingScreen.this, AlarmActivity.class);
            startActivityForResult(intent, 1);
        }
    };

    // Listener for the add Caretaker Button
    View.OnClickListener addCaretakerListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            Intent intent = new Intent(LandingScreen.this, AddCaretaker.class);
            startActivity(intent);
        }
    };

    // This is the Firebase Database
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference firebaseRootRef = firebaseDatabase.getReference();

    //This is the recycler view that stores the Alarm items
    private RecyclerView alarmRecyclerView;

    // This is the adapter for the time recycler view to make sure it runs efficiently
    private RecyclerView.Adapter alarmRecyclerViewAdapter;

    // This is the manager for the time recycler view to make sure it lines up the items nicely
    private RecyclerView.LayoutManager alarmRecyclerLayoutManager;

    // ArrayList for the recycler Alarm Items
    ArrayList<ExampleAlarm> exampleAlarmItemsList;

    // This method initialises the Alarm Array
    public void createAlarmExampleList(){
        // Initializes the exampleAlarmArray
        exampleAlarmItemsList = new ArrayList<>();
    }

    public void buildRecyclerView(){
        // Initializes the Recycler-View and all its stuff
        alarmRecyclerView = findViewById(R.id.alarmsRecycler);
        alarmRecyclerLayoutManager = new LinearLayoutManager(this);
        alarmRecyclerViewAdapter = new ExampleAlarmAdapter(exampleAlarmItemsList);
        alarmRecyclerView.setLayoutManager(alarmRecyclerLayoutManager);
        alarmRecyclerView.setAdapter(alarmRecyclerViewAdapter);
    }

    public void insertItem(int position, ExampleAlarm providedTime) {
        exampleAlarmItemsList.add(position, providedTime);
        alarmRecyclerViewAdapter.notifyItemInserted(position);
    }
    public void removeItemAtIndex(int position) {
        exampleAlarmItemsList.remove(position);
        alarmRecyclerViewAdapter.notifyItemRemoved(position);
    }

    public void checkWarningTextVisible() {
        // Gets the Number of elements in the Recycler View
        int AlarmRecyclerCount = alarmRecyclerViewAdapter.getItemCount();

        // Checks if there is at least 1 element in the RecyclerView
        // If so, hides the warning text, else activates it
        if(AlarmRecyclerCount >= 1){
            staticAlarmWarning.setVisibility(View.INVISIBLE);
        } else{
            staticAlarmWarning.setVisibility(View.VISIBLE);
        }
    }


    // This is what happens when you return from the addTime Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // A result Code of 5 means the user selected "Accept" for the alarm creator Class
        if (resultCode == 5) {

            //Extracts the Values provided by the user
            String extractedName = data.getStringExtra("Name");
            ArrayList <String> extractedTimes = data.getStringArrayListExtra("Times");
            boolean [] extractedDaysofWeek = data.getBooleanArrayExtra("Weekdays");

            // Gets the current Time to use as as the key for the new ExampleAlarm
            Calendar calendar = Calendar.getInstance();
            //Returns current time in millis
            long timeMilli = calendar.getTimeInMillis();
            //Converts that millisecond value into a String for the key
            String timeDatabaseKey = Long.toString(timeMilli);

            // Creates a new alarm recycler item
            ExampleAlarm providedAlarm = new ExampleAlarm(extractedName, extractedDaysofWeek, extractedTimes, timeDatabaseKey);

            // Creates a Database Element for the Alarm Like So
            //   - Alarms
            //        -Key(Date Made)
            //              -Name
            //              -DaysOfWeek, as 0101010 String 0 meaning off, 1 meaning on
            //              -Times
            //                     -time: HourOfDay@Minute

            String databaseWeekdays = zHelperMethods.turnWeekdayBoolsintoDatabaseString(extractedDaysofWeek);

            // Gets the active userId to properly put into the database
            String activeID = ((GlobalVariables) LandingScreen.this.getApplication()).getCurrentUserID();
            firebaseRootRef.child("UsersID&Name").child(activeID).child("Alarms").child(timeDatabaseKey);
            firebaseRootRef.child("UsersID&Name").child(activeID).child("Alarms").child(timeDatabaseKey).child("Name").setValue(extractedName);
            firebaseRootRef.child("UsersID&Name").child(activeID).child("Alarms").child(timeDatabaseKey).child("Weekdays").setValue(databaseWeekdays);
            firebaseRootRef.child("UsersID&Name").child(activeID).child("Alarms").child(timeDatabaseKey).child("Times");

            // Loops through the times and adds them to the database

            for(int i = 0; i < extractedTimes.size(); i++) {
                String timeElement = extractedTimes.get(i);
                String concatenatedTime = zHelperMethods.turnNaturalTimeintoConcatenatedDatabaseText(timeElement);
                firebaseRootRef.child("UsersID&Name").child(activeID).child("Alarms").child(timeDatabaseKey).child("Times").child(timeElement).setValue(concatenatedTime);
            }

            // adds the new alarm recycler to the array
            insertItem(0,providedAlarm);

            ///*** now we need to add this alarm to the "ActiveAlarmsList"
            ///*** you can do this by simply adding the key to the globalArrayList

            // Updates the Warning Text
            checkWarningTextVisible();


        }
    }

    // Listener for the android back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(LandingScreen.this,
                "logging out",
                Toast.LENGTH_LONG).show();

        Intent signOutintent = new Intent(LandingScreen.this, Login.class);
        startActivity(signOutintent);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default OnCreate Stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        // Initializes and locates all the Static Text-Views
        staticLabel = findViewById(R.id.staticTextLabel);
        staticAlarmWarning = findViewById(R.id.staticAlarmRecyclerWarnText);

        // Initializes and locates all the Buttons
        addCareTakerButton = findViewById(R.id.addCaretakerButton);
        addAlarmButton = findViewById(R.id.addAlarmButton);

        // Sets the On click listener for the buttons
        addAlarmButton.setOnClickListener(addAlarmListener);
        addCareTakerButton.setOnClickListener(addCaretakerListener);

        // Initializes and Sets up the RecyclerView Stuff
        createAlarmExampleList();
        buildRecyclerView();

        // Gets the activeID of the current user to use as the database key
        final String activeID = ((GlobalVariables) LandingScreen.this.getApplication()).getCurrentUserID();

        // This block of code connects to fire-base and handles any pending caretaker requests
        // ===============================================================================================================================================================================
        // ===============================================================================================================================================================================
        // ===============================================================================================================================================================================
        FirebaseDatabase.getInstance().getReference().child("UsersID&Name").
                addListenerForSingleValueEvent(new ValueEventListener() {

                    //The On dataChange Method
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // Gets the Username of the user
                        String userName = dataSnapshot.child(activeID).child("Name").getValue().toString();

                        // Sets the Global Username value
                        ((GlobalVariables) LandingScreen.this.getApplication()).setCurrentUserName(userName);

                        // gets the user's request count
                        requestCounter = Integer.parseInt(dataSnapshot.child(activeID).child("CareTakerUserRequestCount").getValue().toString());

                        // gets the user's care taker count
                        careTakerCount = Integer.parseInt(dataSnapshot.child(activeID).child("CareTakerUserCount").getValue().toString());

                        // gets the user's alarm count
                        int alarmCount = Integer.parseInt(dataSnapshot.child(activeID).child("AlarmNumber").getValue().toString());

                        // Sets the user's global alarm count
                        ((GlobalVariables) LandingScreen.this.getApplication()).setCurrentAlarmCount(alarmCount);

                        // Loops through the user's alarms and adds them to the Homepage
                        for (DataSnapshot alarmDatabaseIterator : dataSnapshot.child(activeID).child("Alarms").getChildren()){
                            String extractedAlarmKey = alarmDatabaseIterator.getKey();
                            String extractedAlarmName = alarmDatabaseIterator.child("Name").getValue().toString();
                            String extractedWeekdaysString = alarmDatabaseIterator.child("Weekdays").getValue().toString();
                            //Converts the Weekdays into an array
                            boolean[] extractWeekdayArray = zHelperMethods.turnStringWeekdaytoBoolArray(extractedWeekdaysString);

                            // makes array list of String times
                            ArrayList<String> extractedTimes2 = new ArrayList<>();

                            // Loops through the times field
                            for (DataSnapshot timesIterator : dataSnapshot.child(activeID).child("Alarms").child(extractedAlarmKey).child("Times").getChildren()){

                                // gets the key value from each item in this field
                                String extractedSingleTime = timesIterator.getKey();

                                // adds that to the array
                                extractedTimes2.add(extractedSingleTime);

                            }

                            ///*** Activate the alarm by calling:
                            ///*** AlarmInitializer.setAlarmClosestTime(extractedName, extractedDaysofWeek, YYYY, getApplicationContext());
                            ///*** YYY is an arraylist of <ExampleTime> objects that correspond to that alarm
                            ///*** in order to create YYY for each alarm you will have to use the for loop above at line 250
                            ///*** you need to get the three things in order to make a ExampleTime:
                            ///*** the String TimeMessage,int HourOfDay, and int Minutes
                            ///*** By calling:
                            ///*** String extractedSingleTime = timesIterator.getValue().toString();
                            ///*** in the loop above you will get back a string that follows the format  realHourText+"@"+minuteText;
                            ///*** use the .Split function on the @ character to break it into an array of size 2
                            ///*** the first index will be the hour of day, and the second will be the minutes
                            ///*** the TimeMessage String is the: extractedSingleTime value already gotten at line 253
                            ///*** so make a new Example time for each time as you loop through, add them to the example time array
                            ///*** after the loop ends you will have you YYY object.
                            ///*** Now call and activate the alarm, then store the alarms value in a ArrayList that
                            ///*** I will refer to as the "ActiveAlarmsList",
                            ///*** In the GlobalVariables class make a global arrayList that holds strings, thats "ActiveAlarmsList",
                            ///*** Fill that arrayList with the "key"-timeDatabaseKey of each alarm as we activate it
                            ///*** Now we need to make sure we don't keep activating the same alarms over and over again
                            ///*** make it so that in order for it to be activated it must not be on the "ActiveAlarmsList"
                            ///*** you can accomplish this by simply looping through and checking if there are any duplicate keys to the alarm
                            ///*** you are currently trying to activate


                             // Sets the alarms
                             // will need to keep an active list of the ones that are set to prevent "resetting them"
                            // so i propose a global list of alarmKey's that you simply search though
                            // you will have to update such a list in two locations, at the homepage after the leave the edit/create/delete screen
                            // or when you start up the app

                             // Creates a new alarm recycler item
                            ExampleAlarm providedAlarm = new ExampleAlarm(extractedAlarmName, extractWeekdayArray, extractedTimes2, extractedAlarmKey);

                             // adds the new alarm recycler to the array
                            insertItem(0,providedAlarm);

                             // Updates the Warning Text
                            checkWarningTextVisible();

                        }

                        // if the requestCounter is greater than zero, we iterate through the requests
                        if(requestCounter > 0){

                            // This starts iterating through the requests
                            for (DataSnapshot iterator : dataSnapshot.child(activeID).child("CareTakerUserRequester").getChildren()){

                                // This gets the key value of the requester
                                final String secondKey = iterator.getKey();

                                // This gets the email of the requester
                                // this has been updated during sprint 3 to instead be the userID value
                                // everything else still works as intended
                                final String savedEmails = iterator.getValue().toString();

                                // This is the actual email String of the user, used for the request
                                // popup only now
                                String realEmailString = dataSnapshot.child(savedEmails).child("Email").getValue().toString();

                                // This creates the alert for accepting or rejecting caretaker requests
                                AlertDialog.Builder altdial = new AlertDialog.Builder(LandingScreen.this);

                                //  This creates the dialog for accepting or rejecting caretaker requests
                                altdial.setMessage("Do you want to be the Caretaker for user: " +realEmailString).

                                        // This makes it so you can't cancel out of the alt-dial
                                                setCancelable(false).

                                        // This is the "Yes" button
                                                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog1, int i) {

                                                // This decrements the number of requests from the request counter
                                                requestCounter = requestCounter - 1;

                                                // This turns the request counter back into a string for storage in the database
                                                String newValue = String.valueOf(requestCounter);

                                                // This stores the new request counter back in the database
                                                firebaseRootRef.child("UsersID&Name").child(activeID).child("CareTakerUserRequestCount").setValue(newValue);

                                                // this removes the requester email from the requester user list
                                                firebaseRootRef.child("UsersID&Name").child(activeID).child("CareTakerUserRequester").child(secondKey).removeValue();

                                                // This increases the amount of caretaker values
                                                careTakerCount = careTakerCount + 1;

                                                // This turns the caretaker count back into a string for storage in the database
                                                String SecondNewValue = String.valueOf(careTakerCount);

                                                // This stores the new caretaker counter back in the database
                                                firebaseRootRef.child("UsersID&Name").child(activeID).child("CareTakerUserCount").setValue(SecondNewValue);

                                                // This stores the user's email back into the caretaker list
                                                firebaseRootRef.child("UsersID&Name").child(activeID).child("CareTakerUsers").child(SecondNewValue).setValue(savedEmails);

                                                // Closes the Dialog Box
                                                dialog1.cancel();

                                            }
                                            // This is the "No" button
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog2, int i) {

                                        // This decrements the number of requests from the request counter
                                        requestCounter = requestCounter - 1;

                                        // This turns the request counter back into a string for storage in the database
                                        String newValue = String.valueOf(requestCounter);

                                        // This stores the new request counter back in the database
                                        firebaseRootRef.child("UsersID&Name").child(activeID).child("CareTakerUserRequestCount").setValue(newValue);

                                        // this removes the requester email from the requester user list
                                        firebaseRootRef.child("UsersID&Name").child(activeID).child("CareTakerUserRequester").child(secondKey).removeValue();

                                        // Closes the Dialog Box
                                        dialog2.cancel();
                                    }
                                });

                                // Creates, Titles, and Shows the Alert
                                AlertDialog alert = altdial.create();
                                alert.setTitle("Dialog Header");
                                alert.show();
                            }
                        }
                    }

                    // This is the database Error Handler
                    @Override
                    public void onCancelled(DatabaseError databaseError){}
                });

        // End of the database connection that does caretaker requests
        // ===============================================================================================================================================================================
        // ===============================================================================================================================================================================
        // ===============================================================================================================================================================================



    }
}
