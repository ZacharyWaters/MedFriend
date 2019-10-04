package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Login extends AppCompatActivity {

    ImageButton registerButton;
    View.OnClickListener registerListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            startActivity(new Intent(Login.this, Register.class));
        }
    };

    Button loginButton;
    View.OnClickListener loginListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            startActivity(new Intent(Login.this, Homepage.class));
        }
    };

    ImageButton forgottenButton;
    View.OnClickListener forgottenListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            startActivity(new Intent(Login.this, Homepage.class));
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerButton = (ImageButton)findViewById(R.id.registerIbutton);
        registerButton.setOnClickListener(registerListener);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(loginListener);

        forgottenButton = (ImageButton) findViewById(R.id.forgotIbutton);
        loginButton.setOnClickListener(forgottenListener);

    }
}
