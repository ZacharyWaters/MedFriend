package com.example.medfriend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExampleTimeAdapter extends RecyclerView.Adapter<ExampleTimeAdapter.ExampleViewHolder> {

    private ArrayList<ExampleTime> mExampleTimeList;


    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView timeDisplayMessage;
        public ImageButton deleteTimeButton;
        public ImageButton editTimeButton;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            timeDisplayMessage = itemView.findViewById(R.id.timeMessage);
            deleteTimeButton = itemView.findViewById(R.id.delete);
            editTimeButton = itemView.findViewById(R.id.edit);
        }
    }

    public ExampleTimeAdapter(ArrayList<ExampleTime> exampleList) {
        mExampleTimeList = exampleList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    // This is maps each backEnd element's values to the corresponding Frontend xml Element
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {

        // Extract the element from the corresponding Array at the position index
        ExampleTime currentTimeitem = mExampleTimeList.get(position);

        // Get the backend values from the element that you plan on using
        String storedTimeMessage = currentTimeitem.getExampleTimeMessage();

        // Set the front end XML value based of the back end's element's value
        holder.timeDisplayMessage.setText(storedTimeMessage);

//        //Here is where we are gonna set the buttons to work
//        holder.editTimeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(currentContext, TimeCreatorActivity.class);
//                currentContext.startActivity(intent);
//            }
//        });
//    }
    }

    @Override
    public int getItemCount() {
        return mExampleTimeList.size();
    }
}
