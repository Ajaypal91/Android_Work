package edu.sjsu.ajay.whatsfordinner.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajay on 3/18/18.
 */

public class MealsToDisplay implements Serializable {

    List<String> meals;
    int carbs, calories, vitamins, minerals;


    public  MealsToDisplay(){
        meals = new ArrayList<>();
        this.calories = 0;
        this.carbs = 0;
        this.minerals = 0;
        this.vitamins = 0;
    }

    public List<String> getMeals() {
        return meals;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getVitamins() {
        return vitamins;
    }

    public void setVitamins(int vitamins) {
        this.vitamins = vitamins;
    }

    public int getMinerals() {
        return minerals;
    }

    public void setMinerals(int minerals) {
        this.minerals = minerals;
    }

    public void setMeals(List<String> meals) {
        this.meals = meals;
    }
}
