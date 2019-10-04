package com.example.medfriend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Homepage extends AppCompatActivity {

    Button addDoseButton;
    View.OnClickListener addDoseListener = new View.OnClickListener(){
        public void  onClick  (View  v){
            startActivity(new Intent(Homepage.this, AlarmCreator.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        addDoseButton = (Button) findViewById(R.id.addDoseButton);
        addDoseButton.setOnClickListener(addDoseListener);
    }
}
