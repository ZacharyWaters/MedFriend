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

    Button cancelButton;
    View.OnClickListener cancelListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            startActivity(new Intent(ForgotPassword.this, Login.class));
        }
    };
    Button recoverButton;
    TextView emailInput;
    FirebaseAuth myAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = (TextView)findViewById(R.id.editEmail);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        recoverButton = (Button)findViewById(R.id.recoverButton);
        cancelButton.setOnClickListener(cancelListener);

        myAuth = FirebaseAuth.getInstance();
        recoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String theirEmail = emailInput.getText().toString();
                myAuth.sendPasswordResetEmail(theirEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
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
