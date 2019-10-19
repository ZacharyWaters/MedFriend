package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddCaretaker extends AppCompatActivity {

    Button cancelButton;
    View.OnClickListener cancelListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            startActivity(new Intent(AddCaretaker.this, Homepage.class));
        }
    };
    Button requestButton;
    TextView emailInput;

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

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String theirEmail = emailInput.getText().toString();

            }});
    }
}
