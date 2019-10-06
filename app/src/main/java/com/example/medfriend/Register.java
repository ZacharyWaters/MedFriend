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

    TextView nameInput;
    TextView emailInput;
    TextView passwordInput;
    Button registerButton;
    ProgressBar progressBar;


    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    //DatabaseReference myRef = database.getReference("Users&Scores");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        nameInput = (TextView)findViewById(R.id.nameEdit);
        emailInput = (TextView)findViewById(R.id.emailEdit);
        passwordInput = (TextView)findViewById(R.id.passwordEdit);
        registerButton =  (Button)findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                final String thierName = nameInput.getText().toString();
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
                                                myRef.child(UserId).child("Name").setValue(thierName);
                                                myRef.child(UserId).child("Email").setValue(email);
                                                myRef.child(UserId).child("AlarmNumber").setValue(0);
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



        //String databaseEntry = "Test";
        //myRef.push().child("User|Score").setValue(databaseEntry);
    }
}
