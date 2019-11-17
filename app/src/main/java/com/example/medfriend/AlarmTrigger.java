package com.example.medfriend;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AlarmTrigger extends AppCompatActivity {

    final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    // Static Text View
    TextView staticTriggerText;

    // Buttons
    Button skipMedicineButton;
    Button takeMedicineButton;

    // Ringtone
    Ringtone theTone;

    // Handler, this is used to time out the alarm
    Handler testHandler = new Handler();

    // This is the Firebase Database
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference firebaseRootRef = firebaseDatabase.getReference();

    String alarmName;
    String databaseTimeKey;

    // This is the phone number Array
    ArrayList<String> phoneNumbers = new ArrayList<>();



    // Listener for the skip button
    View.OnClickListener skipMedicineListener = new View.OnClickListener(){
        public void  onClick  (View  v) {
            theTone.stop();
            // Remove any Lingering Handlers
            testHandler.removeCallbacks(shortTimerRunnable);

            // Adds a Timeline mark to the Database
            String activeID = ((GlobalVariables) AlarmTrigger.this.getApplication()).getCurrentUserID();
            String negativeMark = alarmName + "|" + "SKIPPED";
            firebaseRootRef.child("UsersID&Name").child(activeID).child("TimelineMarks").child(databaseTimeKey).setValue(negativeMark);

            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(AlarmTrigger.this,
                    Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(AlarmTrigger.this,
                        Manifest.permission.SEND_SMS)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(AlarmTrigger.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
            }

            // Gets an arrayList of phone numbers from the database of the people you are to text
            FirebaseDatabase.getInstance().getReference().child("UsersID&Name").
                    addListenerForSingleValueEvent(new ValueEventListener() {

                        //The On dataChange Method
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Loops through and creates a list of all the caretaker's ID's you will visit
                            ArrayList<String> careTakersID = new ArrayList<>();
                            String activeUserId = ((GlobalVariables) AlarmTrigger.this.getApplication()).getCurrentUserID();
                            for (DataSnapshot caretaker : dataSnapshot.child(activeUserId).child("MyCaretakers").getChildren()){
                                Log.d("ZZZ", ":)");
                                String idToAdd = caretaker.getKey();
                                careTakersID.add(idToAdd);
                            }

                            Log.d("ll", String.valueOf(careTakersID.size()));
                            for(int i = 0; i < careTakersID.size(); i++){
                                String checkId = careTakersID.get(i);
                                String maybePhoneNumber = dataSnapshot.child(checkId).child("PhoneNumber").getValue().toString();
                                Log.d("lll", maybePhoneNumber);
                                phoneNumbers.add("+16787612383");
                                Log.d("after", ":((((((");
                                Log.d("after2", String.valueOf(phoneNumbers.size()));

                            }


                        }

                        // This is the database Error Handler
                        @Override
                        public void onCancelled(DatabaseError databaseError){}
                    });
            phoneNumbers.add("+16787612383");
            Log.d("lllll", String.valueOf(phoneNumbers.size()));

            for (int i = 0; i < phoneNumbers.size(); i++) {
                SmsManager smsManager = SmsManager.getDefault();
                Log.d("lllll", phoneNumbers.get(i));

                smsManager.sendTextMessage(phoneNumbers.get(i), null, "I forgot to take my medicine just now, please check on me", null, null);
            }


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

            // Adds a Timeline mark to the Database
            String activeID = ((GlobalVariables) AlarmTrigger.this.getApplication()).getCurrentUserID();
            String positiveMark = alarmName + "|" +"TAKEN";
            firebaseRootRef.child("UsersID&Name").child(activeID).child("TimelineMarks").child(databaseTimeKey).setValue(positiveMark);

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
        alarmName = triggerIntent.getStringExtra("name");
        staticTriggerText.setText("It is time to take " +alarmName);

        // Gets the timeKey from the intent
        databaseTimeKey = triggerIntent.getStringExtra("timeKey");

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
