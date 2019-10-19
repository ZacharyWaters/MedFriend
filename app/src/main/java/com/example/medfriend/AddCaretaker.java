package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class AddCaretaker extends AppCompatActivity {

    Button cancelButton;
    View.OnClickListener cancelListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            startActivity(new Intent(AddCaretaker.this, Homepage.class));
        }
    };
    Button requestButton;
    TextView emailInput;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference firebaseRootRef = firebaseDatabase.getReference();
    //itemsRef = firebaseRootRef.child("UsersID&Name");
    DatabaseReference userIDs = firebaseRootRef.child("UsersID&Name");
    public void parseData(DataSnapshot dataSnapshot){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Default OnCreate Stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_caretaker);

        //Creates a Cancel Button that returns you to the Homepage
        cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(cancelListener);

        //This is the button that when pressed marks the intended user for being a Caretaker
        requestButton = (Button)findViewById(R.id.requestButton);

        // This is the EmailInput
        emailInput = (TextView)findViewById(R.id.editEmail);

        // This is the Request Button
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String theirEmail = emailInput.getText().toString();
                FirebaseDatabase.getInstance().getReference().child("UsersID&Name").
                        addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean wasFound = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            String storedEmail = snapshot.child("Email").getValue().toString();
                            //snapshot.getChildren();
                            //String something = snapshot.getValue().toString();
                            //here is your every post
                            String key = snapshot.getKey();
                            //Object value = snapshot.getValue();
                            Log.d("MYLOG", key);
                            //Log.d("MYLOG", theirEmail);
                            //Log.d("MYLOG", storedEmail);
                            if(theirEmail.equalsIgnoreCase(storedEmail)){
                                wasFound = true;
                                String activeEmail = ((GlobalVariables) AddCaretaker.this.getApplication()).getCurrentUserEmail();
                                if(theirEmail.equalsIgnoreCase(activeEmail)){
                                    Toast.makeText(AddCaretaker.this,
                                            "You cannot add yourself as a caretaker",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    firebaseRootRef.child("UsersID&Name").child(key).child("CareTakerFlag").setValue(1);
                                    firebaseRootRef.child("UsersID&Name").child(key).child("CareTakerUserRequester").child(activeEmail);
                                    Toast.makeText(AddCaretaker.this,
                                            "Caretaker request sent to user",
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                        if(wasFound == false){
                            Toast.makeText(AddCaretaker.this,
                                    "No User found",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(AddCaretaker.this,
                                "Database Error",
                                Toast.LENGTH_LONG).show();
                    }
                });


                emailInput.setText("");
            }});
    }
}
