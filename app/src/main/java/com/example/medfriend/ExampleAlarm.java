package com.example.medfriend;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.util.ArrayList;

public class ExampleAlarm implements Parcelable {

    private String alarmName;
    private boolean isMonday;
    private boolean isTuesday;
    private boolean isWednesday;
    private boolean isThursday;
    private boolean isFriday;
    private boolean isSaturday;
    private boolean isSunday;
    private ArrayList<String> setTimes;

    // Constructor for this class
    public ExampleAlarm(String inputAlarmName, boolean[] inputDaysOfWeek, ArrayList<String> inputTimes){
        alarmName = inputAlarmName;
        isMonday = inputDaysOfWeek[0];
        isTuesday = inputDaysOfWeek[1];
        isWednesday = inputDaysOfWeek[2];
        isThursday = inputDaysOfWeek[3];
        isFriday = inputDaysOfWeek[4];
        isSaturday = inputDaysOfWeek[5];
        isSunday = inputDaysOfWeek[6];

        setTimes = inputTimes;
        //setTimes.add("4:00PM");
//        for(int i = 0; i < inputTimes.length; i++){
//            String inputTimeString = inputTimes[i];
//            setTimes.add(inputTimeString);
//        }

    }

    // Master Updater for All, big setter basically
    public void masterUpdate(String inputAlarmName, boolean[] inputDaysOfWeek, String[] inputTimes){

        // Resets the alarm name
        alarmName = inputAlarmName;

        // Resets the entire days of the week
        isMonday = inputDaysOfWeek[0];
        isTuesday = inputDaysOfWeek[1];
        isWednesday = inputDaysOfWeek[2];
        isThursday = inputDaysOfWeek[3];
        isFriday = inputDaysOfWeek[4];
        isSaturday = inputDaysOfWeek[5];
        isSunday = inputDaysOfWeek[6];

//        // Clears out the array, making it entirely empty
//        setTimes.clear();
//        for(int i = 0; i < inputTimes.length; i++){
//            String inputTimeString = inputTimes[i];
//            setTimes.add(inputTimeString);
//        }

    }


    // Individual Setters --------------------------------------------------------------------------
    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public void setMonday(boolean monday) {
        isMonday = monday;
    }

    public void setTuesday(boolean tuesday) {
        isTuesday = tuesday;
    }

    public void setWednesday(boolean wednesday) {
        isWednesday = wednesday;
    }

    public void setThursday(boolean thursday) {
        isThursday = thursday;
    }

    public void setFriday(boolean friday) {
        isFriday = friday;
    }

    public void setSaturday(boolean saturday) {
        isSaturday = saturday;
    }

    public void setSunday(boolean sunday) {
        isSunday = sunday;
    }

    public void setSetTimes(ArrayList<String> setTimes) {
        this.setTimes = setTimes;
    }
    // End of Setters -----------------------------------------------------------------------------

    // Individual Getters -------------------------------------------------------------------------

    public String getAlarmName() {
        return alarmName;
    }

    // Combined getter for the days of the week
    public boolean [] getAllDaysofWeek() {
        boolean [] allDays = {getMonday(), getTuesday(), getWednesday(), getThursday(), getFriday(), getSaturday(), getSunday()};
        return allDays;
    }

    public boolean getMonday() {
        return isMonday;
    }

    public boolean getTuesday() {
        return isTuesday;
    }

    public boolean getWednesday() {
        return isWednesday;
    }

    public boolean getThursday() {
        return isThursday;
    }

    public boolean getFriday() {
        return isFriday;
    }

    public boolean getSaturday() {
        return isSaturday;
    }

    public boolean getSunday() {
        return isSunday;
    }

    public ArrayList<String> getSetTimes() {
        return setTimes;
    }

    // End of Getters -----------------------------------------------------------------------------

    // This are the requirements for the Alarm Object to be Parcelable ============================

    // I have no idea what this method is for, and it didn't appear like it was used
    @Override
    public int describeContents() {
        return 0;
    }

    // Creates the variables that make up the parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(alarmName);
        parcel.writeInt(booleanToInt(isMonday));
        parcel.writeInt(booleanToInt(isTuesday));
        parcel.writeInt(booleanToInt(isWednesday));
        parcel.writeInt(booleanToInt(isThursday));
        parcel.writeInt(booleanToInt(isFriday));
        parcel.writeInt(booleanToInt(isSaturday));
        parcel.writeInt(booleanToInt(isSunday));
        parcel.writeString(arrayListToString(setTimes));
    }

    public static final Creator<ExampleAlarm> CREATOR = new Creator<ExampleAlarm>() {

        // This just calls the constructor below to recreate the alarm from a parcel
        @Override
        public ExampleAlarm createFromParcel(Parcel in) {
            return new ExampleAlarm(in);
        }

        // This is a constructor for an array of your object parcels
        @Override
        public ExampleAlarm[] newArray(int size) {
            return new ExampleAlarm[size];
        }
    };

    // This acts like a constructor the recreates your alarm variable from Parcel
    private ExampleAlarm(Parcel in) {
        alarmName = in.readString();
        isMonday = intToBoolean(in.readInt());
        isTuesday = intToBoolean(in.readInt());
        isWednesday= intToBoolean(in.readInt());
        isThursday = intToBoolean(in.readInt());
        isFriday = intToBoolean(in.readInt());
        isSaturday = intToBoolean(in.readInt());
        isSunday = intToBoolean(in.readInt());
        setTimes  = stringToArrayList(in.readString());
    }

    // Helper method that turns int into boolean
    private boolean intToBoolean(int i) {
        if(i <= 0){
            return false;
        } else {
            return true;
        }
    }

    // Helper Method that turns boolean into int
    private int booleanToInt(boolean y) {
        if(y == true){
            return 1;
        } else {
            return 0;
        }
    }

    // Helper Method that turns arrayList into String
    private String arrayListToString(ArrayList<String> array) {
        String returnString = "";
        String delimiter = "$";
        for(int i = 0; i < array.size() - 1; i++){
            String timeString = array.get(i);
            returnString = returnString + timeString + delimiter;
        }
        int last_index = array.size() - 1;
        String timeString = array.get(last_index);
        returnString = returnString + timeString;

        return returnString;
    }

    // Helper method that turns string into ArrayList
    private ArrayList<String> stringToArrayList(String value) {
        String[] cutStringArray = value.split("$");
        List<String> mylist = Arrays.asList(cutStringArray);
        ArrayList<String> returnList = new ArrayList<String>(mylist);
        return returnList;
    }
    // End of Parcelable Requirements ==============================================================
}
