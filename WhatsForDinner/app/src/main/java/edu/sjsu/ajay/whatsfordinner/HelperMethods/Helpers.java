package edu.sjsu.ajay.whatsfordinner.HelperMethods;

/**
 * Created by ajay on 3/18/18.
 */

public class Helpers {

    public static String getNextDay(String day){

        switch (day){
            case "monday":
                return "TUESDAY";
            case "tuesday":
                return "WEDNESDAY";
            case "wednesday":
                return "THURSDAY";
            case "thursday":
                return "FRIDAY";
            case "friday":
                return "SATURDAY";
            case "saturday":
                return "SUNDAY";
            case "sunday":
                return "MONDAY";
            default:
                return "MONDAY";
        }

    }

    public static String getPreviousDay(String day){

        switch (day){
            case "monday":
                return "SUNDAY";
            case "tuesday":
                return "MONDAY";
            case "wednesday":
                return "TUESDAY";
            case "thursday":
                return "WEDNESDAY";
            case "friday":
                return "THURSDAY";
            case "saturday":
                return "FRIDAY";
            case "sunday":
                return "SATURDAY";
            default:
                return "MONDAY";
        }

    }
}
