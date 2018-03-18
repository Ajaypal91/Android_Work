package edu.sjsu.ajay.whatsfordinner.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajay on 3/16/18.
 */

public class NewRecipe implements Serializable {

    String recipeName, imagePath, directions;
    List<String> ingredients;


    public NewRecipe(){
        recipeName = "";
        imagePath = "";
        ingredients = new ArrayList<>();
    }

    public NewRecipe(String r, String i, List<String> ing, String directions){
        recipeName = r;
        imagePath = i;
        ingredients = ing;
        this.directions = directions;
    }


    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }


    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
