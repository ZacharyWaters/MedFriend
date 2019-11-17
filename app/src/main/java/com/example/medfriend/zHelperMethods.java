package com.example.medfriend;

import java.util.ArrayList;

public class zHelperMethods {

    public static String turnWeekdayBoolsintoDatabaseString(boolean[] weekdays){
        String boolsDatabaseString = "";
        for(int i = 0; i < weekdays.length; i++){
            if(weekdays[i] == true){
                boolsDatabaseString = boolsDatabaseString + "1";
            } else {
                boolsDatabaseString = boolsDatabaseString + "0";
            }
        }
        return boolsDatabaseString;
    }

    public static ArrayList<ExampleTime> turnStringListTimesIntoExampleTimesList(ArrayList<String> unconvertedTimes){
        ArrayList<ExampleTime> timesReturn = new ArrayList<>();
        for(int i = 0; i < unconvertedTimes.size(); i++){
            String toConvert = unconvertedTimes.get(i);
            String connectedTuple = turnNaturalTimeintoConcatenatedDatabaseText(toConvert);
            String[] brokenTuple = connectedTuple.split("@");
            String realHourText = brokenTuple[0];
            String realMinuteText = brokenTuple[1];
            int realHourofDayInt = Integer.valueOf(realHourText);
            int realMinutesInt = Integer.valueOf(realMinuteText);
            ExampleTime recreatedExampleTime = new ExampleTime(toConvert,realHourofDayInt,realMinutesInt);
            timesReturn.add(recreatedExampleTime);
        }
        return timesReturn;
    }

    public static String turnNaturalTimeintoConcatenatedDatabaseText(String alarmText){
        String _AMPMtext = alarmText.substring(alarmText.length() - 2);
        int posOfSemicolen = alarmText.indexOf(':');
        String hourText = alarmText.substring(0 , posOfSemicolen);
        String minuteText = alarmText.substring(posOfSemicolen + 1, posOfSemicolen + 3);

        String realHourText = "";
        boolean amBoolean = false;
        if(_AMPMtext.equals("AM")){
            amBoolean = true;
        }
        if(amBoolean == true){

            if(hourText.equals("12")){

                realHourText = "0";

            } else {
                realHourText = hourText;
            }
        } else {

            if(hourText.equals("12")){

                realHourText = "12";

            } else {
                int hourMath = Integer.valueOf(hourText);
                hourMath = hourMath + 12;
                realHourText = String.valueOf(hourMath);
            }

        }

        String returnBlock = realHourText+"@"+minuteText;
        return returnBlock;
    }

    public static boolean[] turnStringWeekdaytoBoolArray(String boolString){
        boolean[] returnBoolArray = new boolean[7];
        for (int i = 0; i < boolString.length(); i++){
            char c = boolString.charAt(i);
            if(c == '0'){
                returnBoolArray[i] = false;
            } else {
                returnBoolArray[i] = true;
            }
        }
        return returnBoolArray;
    }
}
