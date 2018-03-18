package edu.sjsu.ajay.whatsfordinner;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.sjsu.ajay.whatsfordinner.Entities.Groceries;
import edu.sjsu.ajay.whatsfordinner.Entities.NewRecipe;
import edu.sjsu.ajay.whatsfordinner.HelperMethods.GetSaveData;

public class NewDishActivity extends AppCompatActivity {

    private static final String TAG = NewDishActivity.class.getSimpleName();
    private final int PICK_IMAGE = 1;

    List<String> recipesName;
    boolean isDuplicateRecipe = false;
    List<String> ingredients;
    Set<String> setRecipeNames;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);

        //load saved data from the file
        loadData();

        //update adapters
        updateAdapters();

        //set onFocusChange
        onFocusChangeOfRecipe();
    }

    public void onFocusChangeOfRecipe(){

        final EditText editText = (EditText)findViewById(R.id.recipe_name);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String recipeName = editText.getText().toString().trim();
                if(!b){
                    if(setRecipeNames.contains(recipeName.toLowerCase())){
                        isDuplicateRecipe = true;
                        editText.setTextColor(getResources().getColor(R.color.REDCOLOR));
                    }
                    else{
                        isDuplicateRecipe = false;
                    }
                }
                if(!isDuplicateRecipe)
                    editText.setTextColor(getResources().getColor(R.color.BLACKCOLOR));
            }
        });

    }

    public void updateAdapters(){

        List<AutoCompleteTextView> ingredientsView = getIngredientsListView();
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 ,ingredients);
        for(AutoCompleteTextView a : ingredientsView){
            a.setAdapter(ingredientsAdapter);
        }

    }

    public void loadData(){

        recipesName = new ArrayList<>();
        ingredients = new ArrayList<>();
        setRecipeNames = new HashSet<>();
        imagePath = "";

        List<NewRecipe> data = GetSaveData.read(this);
        Set<String> ing = new HashSet<>();
        if (data == null){
            Log.i(TAG, "New Dish Activity: Cannot load data");
        }
        else{
            for (NewRecipe r : data){
                recipesName.add(r.getRecipeName());
                setRecipeNames.add(r.getRecipeName().toLowerCase());
                ing.addAll(r.getIngredients());
            }
        }
        ingredients.addAll(ing);
    }

    public void addImage(View view){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){

            Uri uri = data.getData();
            imagePath = uri.getPath();
            InputStream inputStream;
            try{

                inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                ImageView imageView = (ImageView)findViewById(R.id.dish_image);
                imageView.setImageBitmap(scaled);

            }
            catch (Exception e){
                Toast.makeText(this, "Unable to open the image", Toast.LENGTH_SHORT).show();
            }

            Log.i(TAG,uri.getPath());

        }
    }

    public List<AutoCompleteTextView> getIngredientsListView(){

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ingredient_linear_layout);
        List<AutoCompleteTextView> result = new ArrayList<>();

        int i = 1;
        while(i <= 10){
            result.add((AutoCompleteTextView)linearLayout.getChildAt(i));
            i++;
        }

        return result;
    }

    public List<String> getIngredients(List<AutoCompleteTextView> lst){

        List<String> result = new ArrayList<>();
        for (AutoCompleteTextView a: lst){
            String text = a.getText().toString().trim();
            if (!text.isEmpty())
                result.add(text);
        }
        return result;

    }

    public void saveRecipe(View view){

        //create the new recipe entity
        String recipeName = ((EditText)findViewById(R.id.recipe_name)).getText().toString();
        if(isDuplicateRecipe || recipeName.isEmpty()){

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (recipeName.isEmpty())
                builder.setMessage(R.string.nullRecipeName);
            else
                builder.setMessage(R.string.duplicateRecipe);
            builder.setNeutralButton(R.string.OK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
            return;
        }

        List<String> ingredients = getIngredients(getIngredientsListView());
        String directions = ((EditText)findViewById(R.id.directions)).getText().toString().trim();
        NewRecipe newRecipe = new NewRecipe(recipeName,imagePath,ingredients,directions);
        Groceries groceries = GetSaveData.getGroceriesList(this);
        if(groceries == null)
            groceries = new Groceries();

        //save the new data along with previous one
        List<NewRecipe> data = GetSaveData.read(this);
        if (data == null)
            data = new ArrayList<>();
        data.add(newRecipe);
        //update the groceries list to buy
        GetSaveData.updateGroceriesList(this, groceries,newRecipe);
        //save the groceries list
        GetSaveData.saveGroceries(this,groceries);

        GetSaveData.Save(this, data);

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        Toast.makeText(this, "Recipe Saved", Toast.LENGTH_SHORT).show();
    }

}
