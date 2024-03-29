package com.example.medfriend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExampleAlarmAdapter extends RecyclerView.Adapter<ExampleAlarmAdapter.ExampleViewHolder> {

    private ArrayList<ExampleAlarm> myExampleAlarmList;
    Context myContext;
    Context applicationContext;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView alarmName_1;
        public TextView alarmTimes;
        public TextView alarmDaysofWeek;
        public ImageButton alarmEditButton;
        public ImageButton alarmDeleteButton;


        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);

            alarmName_1 = itemView.findViewById(R.id.ar_Name_1);
            alarmTimes = itemView.findViewById(R.id.ar_label);
            alarmDaysofWeek = itemView.findViewById(R.id.ar_days);
            alarmEditButton = itemView.findViewById(R.id.arEdit);
            alarmDeleteButton = itemView.findViewById(R.id.arDelete);

        }
    }

    public ExampleAlarmAdapter(ArrayList<ExampleAlarm> exampleList, Context classContext, Context inputApplicationContext) {
        myExampleAlarmList = exampleList;
        myContext = classContext;
        applicationContext = inputApplicationContext;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        // Extract the element from the corresponding Array at the position index
        final ExampleAlarm currentAlarmItem = myExampleAlarmList.get(position);

        // Get the backend values from the element that you plan on using
        String alarmItemName = currentAlarmItem.getAlarmName();
        boolean [] alarmItemdaysOfWeek = currentAlarmItem.getAllDaysofWeek();
        ArrayList <String> alarmItemSetTimes = currentAlarmItem.getSetTimes();

        //Set the front end XML value based of the back end's element's value
        holder.alarmName_1.setText(alarmItemName);

        // Need to set certain parts of the days of the week to a bold color to show activity
        // holder.alarmDaysofWeek.setText("M Tu W Th F Sa Su");

        String conjoinedWeekday = "";
        String[] weekDayBits = {"M","Tu","W","Th","F","Sa","Su"};
        for(int i = 0; i < alarmItemdaysOfWeek.length; i++) {
            boolean wasChecked = alarmItemdaysOfWeek[i];
            if(wasChecked == true) {
                conjoinedWeekday = conjoinedWeekday + "<font color='" + Color.parseColor("#FF4081") + "'>" + weekDayBits[i] + "</font>";
                //conjoinedWeekday = conjoinedWeekday + "<font color='" + Color.parseColor("#FF4081") + "'>" + weekDayBits[i] + "</font>";
            } else {
                conjoinedWeekday = conjoinedWeekday + weekDayBits[i];
                //conjoinedWeekday = conjoinedWeekday + weekDayBits[i];
            }
            conjoinedWeekday = conjoinedWeekday + " ";

        }
        holder.alarmDaysofWeek.setText(Html.fromHtml(conjoinedWeekday));



        String combinedTimes = "";
        // Need to iterate through all the times, conjoin them like so: X1, X2, X3,...,Xn
        for(int i = 0; i < alarmItemSetTimes.size() - 1; i++){
            String individualTime = alarmItemSetTimes.get(i);

            combinedTimes = combinedTimes + individualTime+ ", ";
        }

        // Adds the last element without a comma
        int lastElementIndex = alarmItemSetTimes.size() - 1;
        String finalIndividualTime = alarmItemSetTimes.get(lastElementIndex);
        combinedTimes = combinedTimes + finalIndividualTime;

        holder.alarmTimes.setText(combinedTimes);

        holder.alarmDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the AlarmIndex by making a helper method in the LandingScreen class
                int deleteIndex = ((LandingScreen) myContext).getIndexOfAlarm(currentAlarmItem);

                // Disable the alarm
                AlarmInitializer.cancelAlarm(currentAlarmItem, applicationContext);

                // Remove it from the active list
                ((GlobalVariables)applicationContext).deleteActiveAlarm(currentAlarmItem.getAlarmDatabaseID());

                // Remove it from the Recycler View
                ((LandingScreen) myContext).removeItemAtIndex(deleteIndex);

                // Remove it from the database
                ((LandingScreen) myContext).deleteAlarmFromDatabase(currentAlarmItem.getAlarmDatabaseID());

                // Update the warning Text
                ((LandingScreen) myContext). checkWarningTextVisible();
            }
        });

        holder.alarmEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Make intent to go to time activity
                Intent intentToEdit = new Intent(((LandingScreen) myContext), AlarmActivity.class);

                // Add information to "pre-generate" the activity so it represents the object
                // This would be the Alarm Name, Times, Weekdays
                intentToEdit.putExtra("AlarmName", currentAlarmItem.getAlarmName());
                intentToEdit.putExtra("DaysOfWeek", currentAlarmItem.getAllDaysofWeek());
                intentToEdit.putExtra("AlarmID", currentAlarmItem.getAlarmDatabaseID());
                intentToEdit.putExtra("TimeArray", currentAlarmItem.getSetTimes());
                intentToEdit.putExtra("requestCode", 7);

                // ADD a code to the intent so that the class knows to use pre-gen information
                ((LandingScreen) myContext).startActivityForResult(intentToEdit, 1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myExampleAlarmList.size();
    }
}
