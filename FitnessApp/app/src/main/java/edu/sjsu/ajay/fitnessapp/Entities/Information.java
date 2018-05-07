package edu.sjsu.ajay.fitnessapp.Entities;

import java.util.Date;

/**
 * Created by ajay on 1/6/18.
 */

public class Information {
    public Date startTime;
    public String username;
    public String gender;
    public float weight;

    @Override
    public String toString() {
        String retStr = "{ startTime: " + startTime
                + ", username: " + username
                + ", gender: " + gender
                + ", weight: " + weight + " }";
        return retStr;
    }
}
