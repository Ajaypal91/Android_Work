package edu.sjsu.ajay.whatsfordinner.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajay on 3/18/18.
 */

public class MealsToDisplay implements Serializable {

    List<String> meals;

    public  MealsToDisplay(){
        meals = new ArrayList<>();
    }

    public List<String> getMeals() {
        return meals;
    }

    public void setMeals(List<String> meals) {
        this.meals = meals;
    }
}
