package com.example.medfriend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class Homepage extends AppCompatActivity {

    // I think this is an arraylist for alarms
    public ArrayList<MedAlarm> alarms;

    private FragmentManager fm;
    private static int id = 1;

    // The counter of people requesting you for care-taking
    int requestCounter;

    // The counter of people you are currently care-taking
    int careTakerCount;

    // Buttons
    Button addDoseButton;
    Button addCareTakerButton;

    // sets the button for dose manager to take you to the AlarmCreator activity
    View.OnClickListener addDoseListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            Intent intent = new Intent(Homepage.this, AlarmCreator.class);
            startActivityForResult(intent, 1);
        }
    };


//    View.OnClickListener addCaretakerListener = new View.OnClickListener(){
//        public void  onClick  (View  v){
//            Intent intent = new Intent(Homepage.this, AddCaretaker.class);
//            startActivityForResult(intent, 1);
//        }
//    };

    // sets the button for dose manager to take you to the Caretaker activity

    View.OnClickListener addCaretakerListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            Intent intent = new Intent(Homepage.this, AddCaretaker.class);
            startActivityForResult(intent, 1);
        }
    };

    // This is the Firebase Database
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference firebaseRootRef = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default OnCreate Stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Creates an arrayList for the alarms
        alarms = new ArrayList<>();

        // Establishes the add Dose Button
        addDoseButton = findViewById(R.id.addDoseButton);
        addDoseButton.setOnClickListener(addDoseListener);

        // Establishes the add Caretaker Button
        addCareTakerButton = findViewById(R.id.addCaretakerButton);
        addCareTakerButton.setOnClickListener(addCaretakerListener);

        fm = this.getSupportFragmentManager();

        // Gets the activeID of the current user to use as the database key
        final String activeID = ((GlobalVariables) Homepage.this.getApplication()).getCurrentUserID();

        // Firebase addListener to get value from the database
        FirebaseDatabase.getInstance().getReference().child("UsersID&Name").
                addListenerForSingleValueEvent(new ValueEventListener() {

                    //The On dataChange Method
                    @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {

                        // Gets the Username of the user
                        String userName = dataSnapshot.child(activeID).child("Name").getValue().toString();

                        // Sets the Global Username value
                        ((GlobalVariables) Homepage.this.getApplication()).setCurrentUserName(userName);

                        // gets the user's request count
                        requestCounter = Integer.parseInt(dataSnapshot.child(activeID).child("CareTakerUserRequestCount").getValue().toString());

                        // gets the user's care taker count
                        careTakerCount = Integer.parseInt(dataSnapshot.child(activeID).child("CareTakerUserCount").getValue().toString());

                        // gets the user's alarm count
                        int alarmCount = Integer.parseInt(dataSnapshot.child(activeID).child("AlarmNumber").getValue().toString());
                        ((GlobalVariables) Homepage.this.getApplication()).setCurrentAlarmCount(alarmCount);

                        // if the requestCounter is greater than zero, we iterate through the requests
                        if(requestCounter > 0){

                            // This starts iterating through the requests
                            for (DataSnapshot iterator : dataSnapshot.child(activeID).child("CareTakerUserRequester").getChildren()){

                                // This gets the key value of the requester
                                final String secondKey = iterator.getKey();

                                // This gets the email of the requester
                                final String savedEmails = iterator.getValue().toString();

                                // This creates the alert for accepting or rejecting caretaker requests
                                AlertDialog.Builder altdial = new AlertDialog.Builder(Homepage.this);

                                //  This creates the dialog for accepting or rejecting caretaker requests
                                altdial.setMessage("Do you want to be the Caretaker for user: " +savedEmails).

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
            int repeatinterval = data.getIntExtra("Interval", 1);
            String name = data.getStringExtra("name");


            if (year != 0 && hour != 0 && year != 1920) {
                int myAlarmNumber = ((GlobalVariables) Homepage.this.getApplication()).getCurrentAlarmCount();
                myAlarmNumber = myAlarmNumber + 1;
                ((GlobalVariables) Homepage.this.getApplication()).setCurrentAlarmCount(myAlarmNumber);
                String stringAlarmCount = String.valueOf(myAlarmNumber);
                String activeID = ((GlobalVariables) Homepage.this.getApplication()).getCurrentUserID();
                firebaseRootRef.child("UsersID&Name").child(activeID).child("AlarmNumber").setValue(stringAlarmCount);
                firebaseRootRef.child("UsersID&Name").child(activeID).child("Alarms").child(stringAlarmCount);
                firebaseRootRef.child("UsersID&Name").child(activeID).child("Alarms").child(stringAlarmCount).child("year").setValue(year);
                firebaseRootRef.child("UsersID&Name").child(activeID).child("Alarms").child(stringAlarmCount).child("month").setValue(month);
                firebaseRootRef.child("UsersID&Name").child(activeID).child("Alarms").child(stringAlarmCount).child("day").setValue(day);
                firebaseRootRef.child("UsersID&Name").child(activeID).child("Alarms").child(stringAlarmCount).child("hour").setValue(hour);
                firebaseRootRef.child("UsersID&Name").child(activeID).child("Alarms").child(stringAlarmCount).child("minute").setValue(minute);
                firebaseRootRef.child("UsersID&Name").child(activeID).child("Alarms").child(stringAlarmCount).child("repeatinterval").setValue(repeatinterval);



                Toast.makeText(Homepage.this, "Making Fragment", Toast.LENGTH_LONG).show();
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                GregorianCalendar calendar = new GregorianCalendar(year, month, day, hour, minute);
                long time = calendar.getTimeInMillis();
                Intent in = new Intent(getApplicationContext(), AlarmReceiver.class);

                in.putExtra("name", name);

                int pendingID = (int) (Math.random() * 10);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, in, 0);



                am.setRepeating(AlarmManager.RTC_WAKEUP, time, 86400000 / repeatinterval, pendingIntent);

                MedAlarm medalarm = new MedAlarm(year, month, day, hour, minute, repeatinterval, id, name);
                alarms.add(medalarm);

                FragmentTransaction fragmenttransaction = fm.beginTransaction();
                AlarmListRow newRow = AlarmListRow.newInstance(medalarm);
                newRow.setAl();
                fragmenttransaction.add(R.id.listview_medalertlist, newRow, medalarm.toString());
                fragmenttransaction.commit();
                id += 1;

            } else {
                Toast.makeText(Homepage.this, "Invalid Date and Time", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void deleteAlarm(MedAlarm m, String alarm) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment frag:fragmentList ) {
            if(alarm.equals(frag.getTag())) {
                Log.d("g", frag.getTag() + "     " + alarm + "    " + alarm.equals(frag.getTag()));
                if(frag != null) {
                    FragmentTransaction fragmenttransaction = fm.beginTransaction();
                    fragmenttransaction.remove(frag);
                    fragmenttransaction.commitNow();
                }
                //Log.d("del", "specific fragment removed");
            }
        }

        //LinearLayout formLayout = (LinearLayout)findViewById(R.id.formLayout);
        //formLayout.removeAllViews();
        int alarmID = m.getID();
        Intent in = new Intent(getApplicationContext(), AlarmReceiver.class);
        long time = m.getTime();
        PendingIntent p = PendingIntent.getBroadcast(getApplicationContext(), m.getID(), in, 0);

        am.cancel(p);
        //Log.d("del", "alarm cancelled");
    }

    public void disableAlarm(MedAlarm m) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent in = new Intent(getApplicationContext(), AlarmReceiver.class);
        long time = m.getTime();
        PendingIntent p = PendingIntent.getBroadcast(getApplicationContext(), m.getID(), in, 0);
        Log.d("del", m.getID() + " deled");


        am.cancel(p);
    }

    public void enableAlarm(MedAlarm m) {

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent in = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), m.getID(), in, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, m.getTime(), 86400000 / m.getInterval(), pendingIntent);
    }
}