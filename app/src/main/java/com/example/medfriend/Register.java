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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                final String theirName = nameInput.getText().toString();
                final String email = emailInput.getText().toString();
                final String password = passwordInput.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if(task.isSuccessful()){
                                                Toast.makeText(Register.this,
                                                        "Registered Successfully, please check your email for verification",
                                                        Toast.LENGTH_LONG).show();
                                                DatabaseReference myRef = database.getReference("UsersID&Name");
                                                String UserId = mAuth.getCurrentUser().getUid();
                                                myRef.child(UserId);
                                                myRef.child(UserId).child("Name").setValue(theirName);
                                                myRef.child(UserId).child("Email").setValue(email);
                                                myRef.child(UserId).child("AlarmNumber").setValue("0");
                                                myRef.child(UserId).child("Alarms").setValue("");
                                                myRef.child(UserId).child("CareTakerUserRequestCount").setValue("0");
                                                myRef.child(UserId).child("CareTakerUserRequester").setValue("");
                                                myRef.child(UserId).child("CareTakerUserCount").setValue("0");
                                                myRef.child(UserId).child("CareTakerUsers").setValue("");
                                                //myRef.push().child("User|Score").setValue(UserId);
                                                nameInput.setText("");
                                                passwordInput.setText("");
                                                emailInput.setText("");
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }
}
