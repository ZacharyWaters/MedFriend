package com.example.medfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    // Text-views
    TextView nameInput;
    TextView emailInput;
    TextView passwordInput;

    // Buttons
    Button registerButton;
    Button cancelButton;

    // Progress Bar
    ProgressBar progressBar;

    //Firebase Stuff
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default OnCreate Stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initializes and locates all the user input text-fields
        nameInput = findViewById(R.id.nameEdit);
        emailInput = findViewById(R.id.emailEdit);
        passwordInput = findViewById(R.id.passwordEdit);

        // Initializes and locates all the buttons to be used
        registerButton =  findViewById(R.id.registerButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Initializes and locates the progress bar to be used
        progressBar = findViewById(R.id.progressBar);

        // Gets the Current Instance of the Database
        mAuth = FirebaseAuth.getInstance();

        // This is the listener for the cancel button, sending you to the login activity
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Starts the login activity
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        // This is the listener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // This makes the progress bar visible
                progressBar.setVisibility(View.VISIBLE);

                // This extracts the strings from the text-fields
                final String theirName = nameInput.getText().toString();
                final String email = emailInput.getText().toString();
                final String password = passwordInput.getText().toString();

                // This is the default built-in firebase method
                // for creating users with emails and password
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                // The task is to check if the email is valid,
                                // then it does the verification
                                if(task.isSuccessful()){

                                    // This sends the default email verification
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            // This makes the progress bar visible
                                            progressBar.setVisibility(View.GONE);

                                            // The task here is if they can send an
                                            // email to the account, and registers the user
                                            if(task.isSuccessful()){

                                                // The Toast tells the user the registration is done
                                                // now you just need to verified
                                                Toast.makeText(Register.this,
                                                        "Registered Successfully, please check your email for verification",
                                                        Toast.LENGTH_LONG).show();

                                                // The database reference to the UsersID&Name path
                                                DatabaseReference myRef = database.getReference("UsersID&Name");

                                                // Gets the userId
                                                String UserId = mAuth.getCurrentUser().getUid();

                                                // Creates the userId and the children values
                                                myRef.child(UserId);

                                                // This is the user's name
                                                myRef.child(UserId).child("Name").setValue(theirName);

                                                // This is the user's Email
                                                myRef.child(UserId).child("Email").setValue(email);

                                                // This is the count of the number of alarms
                                                myRef.child(UserId).child("AlarmNumber").setValue("0");

                                                // This is the child list of alarms
                                                myRef.child(UserId).child("Alarms").setValue("");

                                                // This is the count of how many people are requesting caretaker
                                                myRef.child(UserId).child("CareTakerUserRequestCount").setValue("0");

                                                // This is the child list of requester users
                                                myRef.child(UserId).child("CareTakerUserRequester").setValue("");

                                                // This is the count of how many people you are care-taking
                                                myRef.child(UserId).child("CareTakerUserCount").setValue("0");

                                                // This is the child list of care-taking users
                                                myRef.child(UserId).child("MyPatients").setValue("");

                                                // This is the child list of care-taking users
                                                myRef.child(UserId).child("MyCareTakers").setValue("");

                                                // Empties out the text-fields
                                                nameInput.setText("");
                                                passwordInput.setText("");
                                                emailInput.setText("");
                                            }
                                        }
                                    });

                                } else {

                                    // This Toast handles the exception handling
                                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }
}
