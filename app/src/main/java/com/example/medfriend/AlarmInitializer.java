package com.example.medfriend;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Context.ALARM_SERVICE;


public class AlarmInitializer {


    public static void setAlarmClosestTime(String alarmName, boolean[] daysOfWeek, ArrayList<ExampleTime> times, Context context, String alarmKeyString){

        // This is the alarm manager object, this is what we call the alarms on
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        //long time = calendar.getTimeInMillis();
        Intent in = new Intent(context, zAlarmReciever.class);
        //in.putExtra("alarmName", alarmName);
        //in.putExtra("daysOfWeek", daysOfWeek);
        Bundle bundle = new Bundle();
        bundle.putString("alarmName", alarmName);
        bundle.putBooleanArray("daysOfWeek",daysOfWeek);
        bundle.putString("alarmKey", alarmKeyString);
        //bundle.putSerializable("times", times);

        // Turn TimesIntoString
        String virtualTimesArray = "";
        for(int i = 0; i < times.size() - 1; i++){
            ExampleTime indexTime = times.get(i);
            String hourValue = String.valueOf(indexTime.getRealHourValue());
            String minuteValue = String.valueOf(indexTime.getRealMinuteValue());
            String textValue = indexTime.getExampleTimeMessage();
            String objectBlock = hourValue + "@" + minuteValue + "@" + textValue;
            virtualTimesArray = virtualTimesArray + objectBlock + "#";
        }
        int lastIndex = times.size() - 1;
        ExampleTime indexTime = times.get(lastIndex);
        String hourValue = String.valueOf(indexTime.getRealHourValue());
        String minuteValue = String.valueOf(indexTime.getRealMinuteValue());
        String textValue = indexTime.getExampleTimeMessage();
        String objectBlock = hourValue + "@" + minuteValue + "@" + textValue;
        virtualTimesArray = virtualTimesArray + objectBlock;
        bundle.putString("virtualTimes", virtualTimesArray);
        in.putExtras(bundle);



        // put times into intent

        // Check if the alarm is active

        Calendar c = Calendar.getInstance();
        int todayDay = c.get(Calendar.DAY_OF_WEEK);

        int dayOfWeekIndex = turnCalenderDayintoIndexDay(todayDay);

        // Check if the alarm can go off today
        boolean checkToday = canTheAlarmActivateToday(daysOfWeek, dayOfWeekIndex);

        // Check if the alarm has a time that can go off today
        // We only want to do this check if we know day was true
        // Turn this into a method that returns two value, a true false value, and a index.
        int todayTimeFound = 0;
        int todayTimeIndex = 0;
        if(checkToday == true) {

            int currentHour = c.get(Calendar.HOUR_OF_DAY);
            int currentMinute = c.get(Calendar.MINUTE);
            int[] timeLeftResults = getnextTimeIfExistFromToday(currentHour, currentMinute, times);
            todayTimeFound = timeLeftResults[0];
            todayTimeIndex = timeLeftResults[1];

        }

        if(checkToday == true && todayTimeFound == 1){

            Log.d("ZZZ","There is an Alarm Time left for Today");

            ExampleTime upcomingTime = times.get(todayTimeIndex);
            int currentYear = c.get(Calendar.YEAR);
            int currentMonth = c.get(Calendar.MONTH);
            int currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

            GregorianCalendar calendar = new GregorianCalendar(currentYear, currentMonth,currentDayOfMonth, upcomingTime.getRealHourValue(), upcomingTime.getRealMinuteValue());

            Log.d("ZZZ", "Key Before Converting: " + alarmKeyString);
            // We have to concatenate the Key Request Code because its too long for an int
            String  lastSevenDigits = alarmKeyString.substring(alarmKeyString.length() - 7);
            int alarmKeyAsRequestCode = Integer.valueOf(lastSevenDigits);
            Log.d("ZZZ", "Key After Converting: " + String.valueOf(alarmKeyAsRequestCode));

            // OLD PENDING INTENT
            //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, in, 0);

            // NEW PENDING INTENT
            // THIS SHOULD NOT CAUSE ALARMS TO OVERRIDE EACH OTHER
            final PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmKeyAsRequestCode,
                    in,
                    0
            );

            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  pendingIntent);

            Log.d("ZZZ",String.valueOf(calendar.getTimeInMillis()));

            // make an alarm here using the time at i index, today date,
            int y;

        } else{

            // Otherwise grab the next closest day of the week (not counting today);
            Log.d("ZZZ","No Alarm Times to set for the remainder of today");

            int currentYear = c.get(Calendar.YEAR);
            int currentMonth = c.get(Calendar.MONTH);
            int currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
            int currentHour = c.get(Calendar.HOUR_OF_DAY);
            int currentMinute = c.get(Calendar.MINUTE);
            GregorianCalendar calendar = new GregorianCalendar(currentYear, currentMonth,currentDayOfMonth, currentHour, currentMinute);

            int[] closestDayTuple = returnDaysDifferenceofClosestNextDayAndIndex(daysOfWeek, dayOfWeekIndex);
            int differenceofDays = closestDayTuple[0];
            int closestDayIndex = closestDayTuple[1];

            ExampleTime firstTime = times.get(0);
            long dayDifferenceinMili = 86400000 * differenceofDays;
            long firstTimeinMili = convertHourAndMinuteToMili(firstTime.getRealHourValue(), firstTime.getRealMinuteValue());
            long timeleftTodayinMili = 86400000 - convertHourAndMinuteToMili(currentHour, currentMinute);

            long currentTimeinMili = calendar.getTimeInMillis();
            long totalDifferenceTimeinMili = dayDifferenceinMili + firstTimeinMili + timeleftTodayinMili;
            long newFinalTimeinMili = currentTimeinMili + totalDifferenceTimeinMili;


            Log.d("ZZZ", "Key Before Converting: " + alarmKeyString);
            // We have to concatenate the Key Request Code because its too long for an int
            String  lastSevenDigits = alarmKeyString.substring(alarmKeyString.length() - 7);
            int alarmKeyAsRequestCode = Integer.valueOf(lastSevenDigits);
            Log.d("ZZZ", "Key After Converting: " + String.valueOf(alarmKeyAsRequestCode));

            // OLD PENDING INTENT
            // PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, in, 0);

            // NEW PENDING INTENT
            final PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmKeyAsRequestCode,
                    in,
                    0
            );

            am.setExact(AlarmManager.RTC_WAKEUP, newFinalTimeinMili,  pendingIntent);

            Log.d("ZZZ",String.valueOf(newFinalTimeinMili));

        }



        // Otherwise grab the next closest day(not counting today)
        // get the closest time on that day
    }

    public static boolean canTheAlarmActivateToday(boolean[] daysOfWeek, int todayDayConverted){
        if(daysOfWeek[todayDayConverted] == true) {
            return true;
        } else {
            return false;
        }
    }

    public static long convertHourAndMinuteToMili(int hours, int minutes) {

        int totalMiliseconds = 0;

        totalMiliseconds = totalMiliseconds + (3600000 * hours);

        totalMiliseconds = totalMiliseconds + (60000 * minutes);

        return totalMiliseconds;
    }

    // This returns the difference of days between the current day, and the index of the next active
    // the difference is the amount of full days that can be accounted for
    // so the difference between monday and tuesday is actually 0,
    // while the difference between monday and wednesday is 1
    public static int[] returnDaysDifferenceofClosestNextDayAndIndex(boolean[] daysOfWeek, int todayDayConverted){

        // return int array will have values [difference, index]
        int[] returnArray = {-1, -1};

        // will use this variable as the index
        int index = todayDayConverted;

        // Check Tomorrow Initially ----------------------------------------------------------------

        // This handles the wrap around from sunday and monday
        if(index == 6){
            if(daysOfWeek[0] == true){
                returnArray[0] = 0;
                returnArray[1] = 0;
                return returnArray;
            }
        } else{
            if(daysOfWeek[index + 1] == true){
                returnArray[0] = 0;
                returnArray[1] = index + 1;
                return returnArray;
            }

        }
        // End of Check Tomorrow Initially ---------------------------------------------------------

        // Check Remainder of Week

        // This makes sure you don't check yourself initially
        index = index + 1;
        int count = 0;
        boolean found = false;
        while(index < daysOfWeek.length && found == false){

            // This checks if the day of the week is actually valid
            if(daysOfWeek[index] == true){
                found = true;
            }
            // This is to make sure you don't increment after finding the value
            if(found == false){
                index = index + 1;
                count = count + 1;
            }
        }
        // This means we found it before having to start again from the front
        if(found == true){
            returnArray[0] = count;
            returnArray[1] = index;
            return returnArray;
        }

        // Check from beginning of week
        int newindex = 0;
        int newcount = 0;
        while(newindex < todayDayConverted && found == false){

            // This checks if the day of the week is actually valid
            if(daysOfWeek[newindex] == true){
                found = true;
            }
            // This is to make sure you don't increment after finding the value
            if(found == false){
                newindex = newindex + 1;
                newcount = newcount + 1;
            }
        }
        // This means we found it before having to start again from the front
        if(found == true){
            returnArray[0] = newcount + count;
            returnArray[1] = newindex;
            return returnArray;
        }

        // Last option means that there are no other alarms set for that week
        returnArray[0] = 6;
        returnArray[1] = todayDayConverted;
        return returnArray;
    }

    public static int turnCalenderDayintoIndexDay(int todayDay){

        // My array is built like so:
        // [0], [1], [2], [3], [4], [5], [6]
        // Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday

        // the way Calender.Day of the week works:
        // [1], [2], [3], [4], [5], [6], [7]
        // Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday

        // This method converts an index from the second array to the first

        int myCurrentIndex = 0;
        if(todayDay == 0){
            myCurrentIndex = 6;
        }
        // Sunday
        else if(todayDay == 1){
            myCurrentIndex = 6;
        }
        // Monday
        else if(todayDay == 2){
            myCurrentIndex = 0;
        }
        // Tuesday
        else if(todayDay == 3){
            myCurrentIndex = 1;
        }
        // Wednesday
        else if(todayDay == 4){
            myCurrentIndex = 2;
        }
        // Thursday
        else if(todayDay == 5){
            myCurrentIndex = 3;
        }
        // Friday
        else if(todayDay == 6){
            myCurrentIndex = 4;
        }
        // Saturday
        else if(todayDay == 7){
            myCurrentIndex = 5;
        }
        return myCurrentIndex;
    }

    public static int[] getnextTimeIfExistFromToday(int currentHour, int currentMinute, ArrayList<ExampleTime> times){

        // look through and see if you can find a time that comes after the currentHour:currentMinute
        int i = 0;
        boolean brakes = false;
        int todayTimeFound = 0;
        while(i < times.size() && brakes != true) {

            // gets the time at the index i
            ExampleTime iterativeTime = times.get(i);

            // gets the hour and minute of the iterative time
            int realIterativeHour = iterativeTime.getRealHourValue();
            int realiterativeMinute = iterativeTime.getRealMinuteValue();

            // compares starting with hour, then minute, then checking if duplicate
            if(currentHour < realIterativeHour){

                // Sets the brakes to true
                brakes = true;

                // this counteracts the increment below
                i = i - 1;

                // Sets the found Flag to True
                todayTimeFound = 1;

                // This account for minutes
            } else if((currentHour == realIterativeHour) && (currentMinute < realiterativeMinute)){

                // Sets the brakes to true
                brakes = true;

                // this counteracts the increment below
                i = i - 1;

                // Sets the found Flag to True
                todayTimeFound = 1;
            }

            // increments i
            i = i + 1;
        }

        // Comes in the form of Boolean, index
        int [] result = {todayTimeFound, i}   ;
        return result;
    }

}



