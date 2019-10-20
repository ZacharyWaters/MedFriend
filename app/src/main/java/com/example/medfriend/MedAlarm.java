package com.example.medfriend;

import android.app.AlarmManager;
import android.app.PendingIntent;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class MedAlarm implements Serializable {

    private GregorianCalendar time;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int interval;
    private int ID;
    private PendingIntent p;
    private boolean enabled;

    public MedAlarm(int year, int month, int day, int hour, int minute, int interval, int id) {
        time = new GregorianCalendar();

        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.interval = interval;

        this.ID = id;

        time.set(this.year, this.month, this.day, this.hour, this.minute);
        enabled = true;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public long getTime() {
        return time.getTimeInMillis();
    }

    public int getInterval() {
        return interval;
    }



    public int getID() {
        return ID;
    }

    public String toString() {
        return "" + year + "" + hour + "" + minute + "" + day + "" + month + "a" + minute;
    }


}
