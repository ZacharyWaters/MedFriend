package com.example.medfriend;

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
}
