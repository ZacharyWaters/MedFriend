package com.example.medfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    TextView emailInput;
    TextView passwordInput;

    ImageButton registerButton;
    View.OnClickListener registerListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            startActivity(new Intent(Login.this, Register.class));
        }
    };

    Button loginButton;


    ImageButton forgottenButton;
    View.OnClickListener forgottenListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            startActivity(new Intent(Login.this, ForgotPassword.class));
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = (TextView)findViewById(R.id.emailEdit);
        passwordInput = (TextView)findViewById(R.id.passwordEdit);

        mAuth = FirebaseAuth.getInstance();

        registerButton = (ImageButton)findViewById(R.id.registerIbutton);
        registerButton.setOnClickListener(registerListener);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                final String password = passwordInput.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(mAuth.getCurrentUser().isEmailVerified()){
                                startActivity(new Intent(Login.this, Homepage.class));
                            } else {
                                Toast.makeText(Login.this, "Please Verify your email address", Toast.LENGTH_LONG).show();
                            }
                        } else{
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        forgottenButton = (ImageButton) findViewById(R.id.forgotIbutton);
        forgottenButton.setOnClickListener(forgottenListener);

    }
}
