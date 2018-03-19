package edu.sjsu.ajay.whatsfordinner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.sjsu.ajay.whatsfordinner.Entities.Groceries;
import edu.sjsu.ajay.whatsfordinner.Entities.MealsToDisplay;
import edu.sjsu.ajay.whatsfordinner.Entities.NewRecipe;
import edu.sjsu.ajay.whatsfordinner.HelperMethods.GetSaveData;

public class RecipeActivity extends AppCompatActivity implements ListFrag.RecipeSelectedListener {

    private static final String TAG = RecipeActivity.class.getSimpleName();

    List<NewRecipe> recipes = new ArrayList<>();

    TextView recipeNameView;
    ListView ingredientListView;
    TextView recipeDescriptionView;
    ImageView foodIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        initViews();

        showHideFragments();
    }

    public void showHideFragments(){

        recipes = GetSaveData.read(this);
        if(recipes == null)
            recipes = new ArrayList<>();

        FragmentManager fragmentManager = this.getSupportFragmentManager();

        if(findViewById(R.id.recipes_layout_portrait) != null){
            fragmentManager.beginTransaction()
                    .hide(fragmentManager.findFragmentById(R.id.detailsFrag))
                    .show(fragmentManager.findFragmentById(R.id.listFrag))
                    .commit();
        }

        if(findViewById(R.id.recipes_layout_land) != null){
            fragmentManager.beginTransaction()
                    .show(fragmentManager.findFragmentById(R.id.detailsFrag))
                    .show(fragmentManager.findFragmentById(R.id.listFrag))
                    .commit();
        }


    }

    public void initViews(){
        recipeNameView = findViewById(R.id.recipe_name_recipes_screen);
        ingredientListView = findViewById(R.id.recipes_screen_ingredientsList);
        recipeDescriptionView = findViewById(R.id.recipes_screen_directions);
        foodIconImageView = findViewById(R.id.recipes_screen_food_icon);
    }

    @Override
    public void onRecipeSelected(int index) {

        //in case the portrait mode is selected
        if(findViewById(R.id.recipes_layout_portrait) != null){

            //save the data for the groceries menu
            Groceries groceries = GetSaveData.getGroceriesList(this);

            if(groceries == null)
                groceries = new Groceries();

            NewRecipe newRecipe = recipes.get(index);

            //update the groceries list to buy
            GetSaveData.updateGroceriesList(this, groceries,newRecipe);
            //save the groceries list
            GetSaveData.saveGroceries(this,groceries);

            Toast.makeText(this,"Ingredients added to the groceries list", Toast.LENGTH_LONG).show();

            //save the data for the meals menu
            MealsToDisplay meals = GetSaveData.readMealsToDisplay(this);
            if (meals == null)
                meals = new MealsToDisplay();
            meals.getMeals().add(newRecipe.getRecipeName());
            GetSaveData.saveMealsToDisplay(this, meals);

            Toast.makeText(this,"Recipe available in meals selections for week menu", Toast.LENGTH_LONG).show();

        }
        //in case landscape mode is selected
        else{

            if(recipes.size() != 0) {
                NewRecipe selectedRecipe = recipes.get(index);

                recipeNameView.setText(selectedRecipe.getRecipeName());
                recipeDescriptionView.setText(selectedRecipe.getDirections());

                List<String> ingredients = selectedRecipe.getIngredients();
                ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredients);
                ingredientListView.setAdapter(adapter);


                InputStream inputStream;
                try {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                5551);
                    }
                    if (!selectedRecipe.getImagePath().isEmpty()) {
                        Uri uri =Uri.parse(selectedRecipe.getImagePath());
//                                Uri.parse(selectedRecipe.getImagePath());
                        inputStream = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                        foodIconImageView.setImageBitmap(scaled);
                    } else {
                        foodIconImageView.setImageDrawable(getResources().getDrawable(R.drawable.burger));
                    }

                } catch (FileNotFoundException ex) {
                    Log.d(TAG, "File Not Found!");
                    ex.printStackTrace();
                }
            }
        }

    }


}
