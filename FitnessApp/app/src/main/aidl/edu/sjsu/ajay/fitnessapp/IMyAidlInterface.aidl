// IMyAidlInterface.aidl
package edu.sjsu.ajay.fitnessapp;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    void startCounting();

    void stopCounting();

    int getCurrentWorkoutStepCount();
    long getCurrentWorkoutStartTime();
}
