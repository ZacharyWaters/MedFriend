package com.example.medfriend;

import android.app.Application;

import java.util.ArrayList;

public class GlobalVariables extends Application {

    private String currentUserName;
    private String currentUserEmail;
    private String currentUserPassword;
    private String currentUserID;
    private int alarmCount;
    private ArrayList<String> activatedAlarms = new ArrayList<>();

    public String getCurrentUserName() {
        return currentUserName;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public String getCurrentUserPassword() {
        return currentUserPassword;
    }

    public String getCurrentUserID() {
        return currentUserID;
    }

    public int getCurrentAlarmCount() {
        return alarmCount;
    }

    public void setCurrentUserName(String someVariable) {
        this.currentUserName = someVariable;
    }

    public void setCurrentUserEmail(String someVariable) {
        this.currentUserEmail = someVariable;
    }

    public void setCurrentUserPassword(String someVariable) {
        this.currentUserPassword = someVariable;
    }

    public void setCurrentUserID(String someVariable) {
        this.currentUserID = someVariable;
    }

    public void setCurrentAlarmCount(int someVariable) {
        this.alarmCount = someVariable;
    }

    public void addActiveAlarm(String newAlarmKey){
        activatedAlarms.add(newAlarmKey);
    }
    public boolean doesActiveAlarmsContain(String AlarmKey){
        if(activatedAlarms.contains(AlarmKey)){
            return true;
        } else{
            return false;
        }
    }
    public void deleteActiveAlarm(String AlarmKey) {
        activatedAlarms.remove(AlarmKey);
    }

}
