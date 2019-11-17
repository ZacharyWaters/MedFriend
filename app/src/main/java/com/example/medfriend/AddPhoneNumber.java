package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddPhoneNumber extends AppCompatActivity {

    // This is the Firebase Database
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference firebaseRootRef = firebaseDatabase.getReference();

    String number = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone_number);

        final EditText phoneField = findViewById(R.id.phoneField);
        Button addButton = findViewById(R.id.addButton);
        Button cancelButton = findViewById(R.id.backButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = phoneField.getText().toString();
                if(number.length() == 10){
                    number = "+1" + number;
                    //TODO: Zach does database calls here
                    String activeUserId = ((GlobalVariables) AddPhoneNumber.this.getApplication()).getCurrentUserID();
                    firebaseRootRef.child("UsersID&Name").child(activeUserId).child("PhoneNumber").setValue(number);
                    finish();
                }
                else{
                    Toast.makeText(AddPhoneNumber.this,
                            "Not a valid Phone Number",
                            Toast.LENGTH_LONG).show();

                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }





}
