package com.example.medfriend;

public class ExampleTime implements java.io.Serializable{

    // This will be the time displayed by the recycle-view time item
    // it was done this way so instead of appearing as military time, i can make the time appear
    // more like standard time
    private String shownTimeMessage;

    // These will be the real time values stored to make any algorithm stuff easier
    private int realHourValue;
    private int realMinuteValue;

    // Constructor for this class
    public ExampleTime(String inputTimeMessage, int inputHour, int intputMinute){
        shownTimeMessage = inputTimeMessage;
        realHourValue = inputHour;
        realMinuteValue = intputMinute;
    }

    // Setters ------------------------------------------------------------------------------------
    public void setRealHourValue(int realHourValue) {
        this.realHourValue = realHourValue;
    }

    public void setRealMinuteValue(int realMinuteValue) {
        this.realMinuteValue = realMinuteValue;
    }

    public void setShownTimeMessage(String shownTimeMessage) {
        this.shownTimeMessage = shownTimeMessage;
    }
    // End of Setters -----------------------------------------------------------------------------


    // Getters ------------------------------------------------------------------------------------
    public String getExampleTimeMessage(){
        return shownTimeMessage;
    }

    public int getRealHourValue() {
        return realHourValue;
    }

    public int getRealMinuteValue() {
        return realMinuteValue;
    }
    // End of Getters -----------------------------------------------------------------------------
}
