package edu.sjsu.ajay.whatsfordinner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.sjsu.ajay.whatsfordinner.Entities.MealsToDisplay;
import edu.sjsu.ajay.whatsfordinner.HelperMethods.GetSaveData;

public class NutritionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        MealsToDisplay meals = GetSaveData.readMealsToDisplay(this);
        if (meals == null)
            meals = new MealsToDisplay();

        String carbs = Integer.toString(meals.getCarbs());
        String calories = Integer.toString(meals.getCalories());
        String minerals = Integer.toString(meals.getMinerals());
        String vitamins = Integer.toString(meals.getVitamins());

        ((EditText)findViewById(R.id.carb_text)).setText(carbs);
        ((EditText)findViewById(R.id.calories_text)).setText(calories);
        ((EditText)findViewById(R.id.minerals_text)).setText(minerals);
        ((EditText)findViewById(R.id.vitamins_text)).setText(vitamins);
    }

    public void saveWeeklyNutrition(View view){


        String carbs    = ((EditText)findViewById(R.id.carb_text)).getText().toString();
        String calories = ((EditText)findViewById(R.id.calories_text)).getText().toString();
        String minerals = ((EditText)findViewById(R.id.minerals_text)).getText().toString();
        String vitamins = ((EditText)findViewById(R.id.vitamins_text)).getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("carbs", carbs);
        bundle.putString("cals", calories);
        bundle.putString("minerals", minerals);
        bundle.putString("vits", vitamins);

        Intent intent=new Intent();
        intent.putExtras(bundle);

        setResult(2,intent);
        finish();//finishing activity
    }

}
