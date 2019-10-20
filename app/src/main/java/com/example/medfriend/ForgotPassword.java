package com.example.medfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    // Buttons
    Button cancelButton;
    Button recoverButton;

    // Text-view
    TextView emailInput;

    // Firebase Authentication
    FirebaseAuth myAuth;

    // Progress Bar
    ProgressBar progressBar;

    // This is the cancel Listener, returns the user back to the login screen
    View.OnClickListener cancelListener = new View.OnClickListener(){
        public void  onClick  (View  v){

            // Starts the Login Activity
            startActivity(new Intent(ForgotPassword.this, Login.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default OnCreate Stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initializes and locates all the email text-field
        emailInput = (TextView)findViewById(R.id.editEmail);

        // Initializes and locates the progressBar
        progressBar = (ProgressBar)findViewById(R.id.progressBar2);

        // Initializes and locates all the Buttons
        cancelButton = (Button)findViewById(R.id.cancelButton);
        recoverButton = (Button)findViewById(R.id.recoverButton);

        // Sets up the Cancel Button Listener
        cancelButton.setOnClickListener(cancelListener);

        // Gets the instance of the firebase
        myAuth = FirebaseAuth.getInstance();

        // Sets up the Recover Button Listener
        recoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Makes the Progress bar visible
                progressBar.setVisibility(View.VISIBLE);

                // Extracts the user input email from the text field
                String theirEmail = emailInput.getText().toString();

                // Default firebase password reset method
                myAuth.sendPasswordResetEmail(theirEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                // Makes the Progress bar invisible
                                progressBar.setVisibility(View.GONE);
                                if(task.isSuccessful()){
                                    Toast.makeText(ForgotPassword.this,
                                            "Password sent to your email",
                                            Toast.LENGTH_LONG).show();

                                } else{
                                    Toast.makeText(ForgotPassword.this,
                                            task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}
