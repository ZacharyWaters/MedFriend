package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddPhoneNumber extends AppCompatActivity {

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
                //TODO: Zach does database calls here

                finish();
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
