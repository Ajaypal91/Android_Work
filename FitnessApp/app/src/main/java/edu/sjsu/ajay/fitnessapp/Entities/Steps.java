package edu.sjsu.ajay.fitnessapp.Entities;

/**
 * Created by ajay on 1/6/18.
 */

public class Steps {

    public String timestamp;
    public int count;
    public int calories;

    @Override
    public String toString() {
        String retStr = "{ timestamp: " + timestamp
                + ", count: " + count
                + ", calories: " + calories + " }";
        return retStr;
    }
}
