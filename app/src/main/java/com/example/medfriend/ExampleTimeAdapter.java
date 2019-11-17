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
    Context myContext;


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

    public ExampleTimeAdapter(ArrayList<ExampleTime> exampleList, Context classContext) {
        mExampleTimeList = exampleList;
        myContext = classContext;
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
        final ExampleTime currentTimeitem = mExampleTimeList.get(position);

        // Get the backend values from the element that you plan on using
        String storedTimeMessage = currentTimeitem.getExampleTimeMessage();

        // Set the front end XML value based of the back end's element's value
        holder.timeDisplayMessage.setText(storedTimeMessage);



        holder.deleteTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int DeleteIndex = ((AlarmActivity) myContext).getIndexofTime(currentTimeitem);
                ((AlarmActivity) myContext).removeItemAtIndex(DeleteIndex);
            }
        });

        holder.editTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Make intent to go to time activity
                Intent intentToEdit = new Intent(((AlarmActivity) myContext), TimeCreatorActivity.class);

                // Add information to "pre-generate" the activity so it represents the object
                // This would be the time of the object you want to edit
                intentToEdit.putExtra("RealHour", currentTimeitem.getRealHourValue());
                intentToEdit.putExtra("RealMinute", currentTimeitem.getRealMinuteValue());
                intentToEdit.putExtra("Message", currentTimeitem.getExampleTimeMessage());
                intentToEdit.putExtra("requestCode", 2);

                // ADD a code to the intent so that the class knows to use pre-gen information
                ((AlarmActivity) myContext).startActivityForResult(intentToEdit, 1);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mExampleTimeList.size();
    }
}
