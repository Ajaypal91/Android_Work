package edu.sjsu.ajay.fitnessapp.Entities;

/**
 * Created by ajay on 5/6/18.
 */

public class AllData {

    public float distance;
    public int time;
    public int noOfWorkouts;
    public int calories;

    @Override
    public String toString() {
        String retStr = "{ noOfWorkouts: " + noOfWorkouts
                + ", distance: " + distance
                + ", time: " + time
                + ", calories: " + calories + " }";
        return retStr;
    }
}
