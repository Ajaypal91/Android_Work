package edu.sjsu.ajay.whatsfordinner.HelperMethods;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

import edu.sjsu.ajay.whatsfordinner.Entities.Groceries;
import edu.sjsu.ajay.whatsfordinner.Entities.Meals;
import edu.sjsu.ajay.whatsfordinner.Entities.MealsToDisplay;
import edu.sjsu.ajay.whatsfordinner.Entities.NewRecipe;
import edu.sjsu.ajay.whatsfordinner.R;

/**
 * Created by ajay on 3/16/18.
 */

public class GetSaveData {

    private static final String TAG = GetSaveData.class.getSimpleName();
    private static boolean mealsToDisplayFileAvailable = false;

    public static void Save(Context context, List<NewRecipe> data){

        String fileName = context.getResources().getString(R.string.databaseFile);
        FileOutputStream fileStream;
        ObjectOutputStream oStrm;

        try{

            fileStream = new FileOutputStream(context.getFilesDir().getPath()+fileName);
            oStrm = new ObjectOutputStream(fileStream);
            oStrm.writeObject(data);
            oStrm.close();
            fileStream.close();
        }
        catch (Exception e){
            Log.i(TAG,"GetSaveData - SAVE:cannot save the data");
            e.printStackTrace();
        }

    }

    public static List<NewRecipe> read(Context context){

        String fileName = context.getResources().getString(R.string.databaseFile);
        FileInputStream fileStream;
        ObjectInputStream oStrm;
        List<NewRecipe> result = null;

        try{
            fileStream = new FileInputStream(context.getFilesDir().getPath()+fileName);
            oStrm = new ObjectInputStream(fileStream);
            result = (List<NewRecipe>) oStrm.readObject();
            oStrm.close();
            fileStream.close();
        }
        catch (Exception e){
            Log.i(TAG,"GetSaveData - read: Cannot read the data");
            e.printStackTrace();
        }

        return result;

    }

    public static void saveGroceries(Context context, Groceries groceries){

        String fileName = context.getResources().getString(R.string.groceriesFile);
        FileOutputStream fileStream;
        ObjectOutputStream oStrm;

        try{

            fileStream = new FileOutputStream(context.getFilesDir().getPath()+fileName);
            oStrm = new ObjectOutputStream(fileStream);
            oStrm.writeObject(groceries);
            oStrm.close();
            fileStream.close();
        }
        catch (Exception e){
            Log.i(TAG,"GetSaveData - SAVE:cannot save the data");
            e.printStackTrace();
        }


    }

    public static Groceries getGroceriesList(Context context){

        String fileName = context.getResources().getString(R.string.groceriesFile);
        FileInputStream fileStream;
        ObjectInputStream oStrm;
        Groceries result = null;

        try{
            fileStream = new FileInputStream(context.getFilesDir().getPath()+fileName);
            oStrm = new ObjectInputStream(fileStream);
            result = (Groceries) oStrm.readObject();
            oStrm.close();
            fileStream.close();
        }
        catch (Exception e){
            Log.i(TAG,"GetSaveData - read: Cannot read the data");
            e.printStackTrace();
        }

        return result;

    }

    public static void  updateGroceriesList(Context context, Groceries groceries, NewRecipe n){

        String[] items = context.getResources().getStringArray(R.array.ingredients);

        Map<String, Integer> map = groceries.getGroceriesToBuy();

       List<String> ingredients = n.getIngredients();

            for (String s : ingredients) {

                String grocery = "";

                //To make potatos to potato
                for (String item : items){
                    if (item.toLowerCase().contains(s.toLowerCase())){
                        grocery = item;
                    }
                }

                if(grocery.isEmpty())
                    grocery = s;
                if (map.containsKey(s)){
                    map.put(grocery,map.get(s)+1);
                }
                else{
                    map.put(grocery,1);
                }
            }


    }

    public static void SaveMeals(Context context, Meals data){

        String fileName = context.getResources().getString(R.string.mealsFile);
        FileOutputStream fileStream;
        ObjectOutputStream oStrm;

        try{

            fileStream = new FileOutputStream(context.getFilesDir().getPath()+fileName);
            oStrm = new ObjectOutputStream(fileStream);
            oStrm.writeObject(data);
            oStrm.close();
            fileStream.close();
        }
        catch (Exception e){
            Log.i(TAG,"GetSaveData - SAVE:cannot save the data");
            e.printStackTrace();
        }

    }

    public static Meals readMeals(Context context){

        String fileName = context.getResources().getString(R.string.mealsFile);
        FileInputStream fileStream;
        ObjectInputStream oStrm;
        Meals result = null;

        try{
            fileStream = new FileInputStream(context.getFilesDir().getPath()+fileName);
            oStrm = new ObjectInputStream(fileStream);
            result = (Meals) oStrm.readObject();
            oStrm.close();
            fileStream.close();
        }
        catch (Exception e){
            Log.i(TAG,"GetSaveData - read: Cannot read the data");
            e.printStackTrace();
        }

        return result;
    }

    public static void saveMealsToDisplay(Context context, MealsToDisplay data){

        String fileName = context.getResources().getString(R.string.mealsToDisplayFile);
        mealsToDisplayFileAvailable = true;
        FileOutputStream fileStream;
        ObjectOutputStream oStrm;

        try{

            fileStream = new FileOutputStream(context.getFilesDir().getPath()+fileName);
            oStrm = new ObjectOutputStream(fileStream);
            oStrm.writeObject(data);
            oStrm.close();
            fileStream.close();
        }
        catch (Exception e){
            Log.i(TAG,"GetSaveData - SAVE:cannot save the data");
            e.printStackTrace();
        }

    }

    public static MealsToDisplay readMealsToDisplay(Context context){

        String fileName = context.getResources().getString(R.string.mealsToDisplayFile);
        FileInputStream fileStream;
        ObjectInputStream oStrm;
        MealsToDisplay result = null;

        if(!mealsToDisplayFileAvailable)
            return null;

        try{
            fileStream = new FileInputStream(context.getFilesDir().getPath()+fileName);
            oStrm = new ObjectInputStream(fileStream);
            result = (MealsToDisplay) oStrm.readObject();
            oStrm.close();
            fileStream.close();
        }
        catch (Exception e){
            Log.i(TAG,"GetSaveData - read: Cannot read the data");
        }

        return result;
    }

}
