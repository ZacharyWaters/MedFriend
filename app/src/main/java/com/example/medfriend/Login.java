package com.example.medfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    // Text-views
    TextView emailInput;
    TextView passwordInput;
    int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    // Buttons
    Button loginButton;
    ImageButton forgottenButton;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Default OnCreate Stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializes and locates all the email and password text-fields
        emailInput = findViewById(R.id.emailEdit);
        passwordInput = findViewById(R.id.passwordEdit);

        // Initializes and locates all the buttons to be used
        forgottenButton = findViewById(R.id.forgotButton);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Login.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this,
                    Manifest.permission.SEND_SMS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(Login.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        // Gets the Current Instance of the Database
        mAuth = FirebaseAuth.getInstance();

        // Sets up the Login Button Listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Grabs the user input email.
                String email = emailInput.getText().toString();

                // Makes a finalCopy version of it for later use in the OnComplete Instance Class
                final String emailCopy = email;

                // Gets the users input password, and makes it final for later use in the Instance Class
                final String password = passwordInput.getText().toString();

                // This is firebase's Auto-Authentication System
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            // This Checks if they are both authenticated and Verified
                            if(mAuth.getCurrentUser().isEmailVerified()){

                                // This gets the current user ID
                                String userId = mAuth.getCurrentUser().getUid();

                                // This now saves the users ID, email, and password globally for faster checks
                                ((GlobalVariables) Login.this.getApplication()).setCurrentUserID(userId);
                                ((GlobalVariables) Login.this.getApplication()).setCurrentUserEmail(emailCopy);
                                ((GlobalVariables) Login.this.getApplication()).setCurrentUserPassword(password);

                                // Clear the login values
                                emailInput.setText("");
                                passwordInput.setText("");

                                // This Sends them to the homepage
                                startActivity(new Intent(Login.this, LandingScreen.class));

                            } else {

                                // This Toast displays a message to the user showing that they
                                // have the correct account, but its not yet verified
                                Toast.makeText(Login.this, "Please Verify your email address", Toast.LENGTH_LONG).show();
                            }
                        } else{
                            // This Toast displays a message to the user showing the exception
                            // for failing to complete the onComplete Task
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        // This is the listener for the forgot password button, sending you to the forgot password activity
        forgottenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Starts the forgot password activity
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });

        // This is the listener for the forgot password button, sending you to the register activity
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Starts the register activity
                startActivity(new Intent(Login.this, Register.class));
            }
        });

    }
}
