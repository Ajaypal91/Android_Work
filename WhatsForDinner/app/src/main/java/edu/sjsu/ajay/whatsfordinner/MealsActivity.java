package edu.sjsu.ajay.whatsfordinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import edu.sjsu.ajay.whatsfordinner.Entities.Meals;
import edu.sjsu.ajay.whatsfordinner.Entities.NewRecipe;
import edu.sjsu.ajay.whatsfordinner.HelperMethods.GetSaveData;
import edu.sjsu.ajay.whatsfordinner.HelperMethods.Helpers;

public class MealsActivity extends AppCompatActivity {

    Meals data;
    Map<Meals.Tuple,String> map; //mapping for each day and meal time to actual meal
    Map<String , List<List<String>>> mealsCalender; // create mapping such as Monday: brekfast_val, lunch_val, dinner_val
    public static final String breakfast = "breakfast", lunch = "lunch", dinner = "dinner";
    Map<String, List<String>> usedMeals;
    Map<String, Map<String, Integer>> usedMealsMap;
    ArrayAdapter adapter1, adapter2, adapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals);

        updateValues();

        bindSpinnerListners();

    }

    protected AdapterView.OnItemSelectedListener selectionListner = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedView, int position, long id) {

            if(parentView.getId() == R.id.breakfast_spinner){
                onItemSelectedInSpinner(parentView,position,0);
            }
            else if(parentView.getId() == R.id.lunch_spinner){
                onItemSelectedInSpinner(parentView,position,1);
            }
            else if(parentView.getId() == R.id.dinner_spinner){
                onItemSelectedInSpinner(parentView,position,2);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    public void bindSpinnerListners(){

        Spinner breakfastSpinner = (Spinner)findViewById(R.id.breakfast_spinner);
        Spinner lunchSpinner = (Spinner)findViewById(R.id.lunch_spinner);
        Spinner dinnerSpinner = (Spinner)findViewById(R.id.dinner_spinner);

        breakfastSpinner.setOnItemSelectedListener(selectionListner);
        lunchSpinner.setOnItemSelectedListener(selectionListner);
        dinnerSpinner.setOnItemSelectedListener(selectionListner);

    }

    public void onItemSelectedInSpinner(AdapterView<?> adapterView, int position, int index){
        String newselectedMeal = adapterView.getItemAtPosition(position).toString();
        TextView textView = (TextView)findViewById(R.id.days);
        String currDay = textView.getText().toString();
        String previousMeal = mealsCalender.get(currDay.toLowerCase()).get(index).get(0);


        if(previousMeal.equals(newselectedMeal))
            return;

        List<String> mealsUsed = usedMeals.get(currDay.toLowerCase());
        Map<String,Integer> mealsUsedMap = usedMealsMap.get(currDay.toLowerCase());

        if(!newselectedMeal.equals(Meals.defaultMeal)){
            if(!mealsUsed.contains(newselectedMeal))
                mealsUsed.add(newselectedMeal);
            if(!mealsUsedMap.containsKey(newselectedMeal))
                mealsUsedMap.put(newselectedMeal,1);
            else
                mealsUsedMap.put(newselectedMeal,mealsUsedMap.get(newselectedMeal)+1);
            List<String> mealForday = mealsCalender.get(currDay.toLowerCase()).get(index);
            int i = mealForday.lastIndexOf(newselectedMeal);
            mealForday.remove(i);
            mealForday.add(0,newselectedMeal);
        }

        //if new meal is Eating Out
        if(newselectedMeal.equals(Meals.defaultMeal)){
            List<String> mealForday = mealsCalender.get(currDay.toLowerCase()).get(index);
            mealForday.remove(Meals.defaultMeal);
            mealForday.add(0,Meals.defaultMeal);
        }

        //add the previous Meal in other adapters as well
        if(!previousMeal.equals(Meals.defaultMeal)) {
            if(mealsUsedMap.containsKey(previousMeal)){
                int count = mealsUsedMap.get(previousMeal);
                count--;
                if(count == 0){
                    mealsUsed.remove(previousMeal);
                    mealsUsedMap.remove(previousMeal);
                }
                else {
                    mealsUsedMap.put(previousMeal,count);
                }

            }

            updateMealsCalenderForOther2Meals(previousMeal, index, currDay);
        }
       
        setAdapters();
    }

    //1 for add 2 for remove
    public void updateMealsCalenderForOther2Meals(String meal, int index, String day){

        List<List<String>> mealsForDay = mealsCalender.get(day.toLowerCase());

        for(int i = 0; i < 3; i++){
            if(i != index){
                List<String> lst = mealsForDay.get(i);
                lst.add(meal);
            }
        }

    }

    public void updateValues(){

        data = GetSaveData.readMeals(this);
        mealsCalender = new LinkedHashMap<>();

        //for first time intialize the data with Eating out for each day meal combo
        if(data == null) {
            data = new Meals();
        }

        //this the data structure saved in the file store
        map = data.getMap();

        //get List of recipes and update the meal calender as well
        List<String> recipes = getRecipesList();
        usedMeals = new HashMap<>();
        usedMealsMap = new HashMap<>();

        for(Meals.Days day : Meals.Days.values()){
            List<String> lst1 = new ArrayList<>(recipes);
            if(!lst1.contains(Meals.defaultMeal))
                lst1.add(Meals.defaultMeal);
            List<String> lst2 = new ArrayList<>(lst1);
            List<String> lst3 = new ArrayList<>(lst1);
            List<List<String>> res = new ArrayList<>();
            res.add(lst1); res.add(lst2); res.add(lst3);
            mealsCalender.put(day.name().toLowerCase(), res);
            usedMeals.put(day.name().toLowerCase(), new ArrayList<String>());
            usedMealsMap.put(day.name().toLowerCase(), new HashMap<String, Integer>());
        }

        for(Meals.Tuple t : map.keySet()){

            String val = map.get(t);
            String m = t.getM().name();
            String d = t.getD().name();
            List<String> lst = new ArrayList<>();
            switch (m.toLowerCase()){
                case "breakfast":
                    lst = mealsCalender.get(d.toLowerCase()).get(0);
                    break;
                case "lunch":
                    lst = mealsCalender.get(d.toLowerCase()).get(1);
                    break;
                case "dinner":
                    lst = mealsCalender.get(d.toLowerCase()).get(2);
                    break;
            }

            if(lst.contains(val)) {
                if(!val.equals(Meals.defaultMeal)){
                    List<String> mealsUsed = usedMeals.get(d.toLowerCase());
                    Map<String,Integer> mealUsedMap = usedMealsMap.get(d.toLowerCase());
                    if(!mealsUsed.contains(val))
                        mealsUsed.add(val);
                    if(!mealUsedMap.containsKey(val))
                        mealUsedMap.put(val,1);
                    else
                        mealUsedMap.put(val, mealUsedMap.get(val)+1);
                }
                int i = lst.lastIndexOf(val);
                lst.remove(i);
            }
            lst.add(0,val);
        }

        updateAdaptersBasedonUsedMeals();

        setAdapters();
    }

    //update the other two adapters that does not have the used meal for that particular day
    public void updateAdaptersBasedOnDay(String day){

        //update the lists based on usedMeals
        List<String> mealsUsed = usedMeals.get(day.toLowerCase());
        List<List<String>> lst = mealsCalender.get(day.toLowerCase());
        Map<String, Integer> mealsUsedMap = usedMealsMap.get(day.toLowerCase());

        for (String meal : mealsUsed){

            int countOfMeals = mealsUsedMap.get(meal);

            if(!lst.get(0).get(0).equals(meal)){
                updateList(lst.get(0),countOfMeals,meal);
            }
            else {
                if(countOfMeals>1){
                    updateList(lst.get(0),countOfMeals-1,meal);
                }
            }
            if(!lst.get(1).get(0).equals(meal)){
                updateList(lst.get(1),countOfMeals,meal);
            }
            else {
                if(countOfMeals>1){
                    updateList(lst.get(1),countOfMeals-1,meal);
                }
            }
            if(!lst.get(2).get(0).equals(meal)){
                updateList(lst.get(2),countOfMeals,meal);
            }
            else {
                if(countOfMeals>1){
                    updateList(lst.get(2),countOfMeals-1,meal);
                }
            }
        }


    }

    public void updateList(List<String> lst, int count, String val){
        while(count!=0){
            int i = lst.lastIndexOf(val);
            if(i!=-1)
                lst.remove(i);
            count--;
        }
    }

    //update the other two adapters that does not have the used meal
    public void updateAdaptersBasedonUsedMeals(){
        //update the lists based on usedMeals
        for(String day: usedMeals.keySet()){

            List<String> mealsUsed = usedMeals.get(day);
            List<List<String>> lst = mealsCalender.get(day);
            Map<String, Integer> mealsUsedMap = usedMealsMap.get(day);

            for (String meal : mealsUsed){

                int countOfMeals = mealsUsedMap.get(meal);

                if(!lst.get(0).get(0).equals(meal)){
                    updateList(lst.get(0),countOfMeals,meal);
                }
                else {
                    if(countOfMeals>1){
                        updateList(lst.get(0),countOfMeals-1,meal);
                    }
                }
                if(!lst.get(1).get(0).equals(meal)){
                    updateList(lst.get(1),countOfMeals,meal);
                }
                else {
                    if(countOfMeals>1){
                        updateList(lst.get(1),countOfMeals-1,meal);
                    }
                }
                if(!lst.get(2).get(0).equals(meal)){
                    updateList(lst.get(2),countOfMeals,meal);
                }
                else {
                    if(countOfMeals>1){
                        updateList(lst.get(2),countOfMeals-1,meal);
                    }
                }
            }

        }
    }

    public void setAdapters(){
        //set Values in the UI
        TextView txtView = (TextView)findViewById(R.id.days);
        String dayOfView = txtView.getText().toString().toLowerCase();
        Spinner breakfastSpinner = (Spinner)findViewById(R.id.breakfast_spinner);
        Spinner lunchSpinner = (Spinner)findViewById(R.id.lunch_spinner);
        Spinner dinnerSpinner = (Spinner)findViewById(R.id.dinner_spinner);

        //set adapters
        List<List<String>> daysValues = mealsCalender.get(dayOfView);
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daysValues.get(0));
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daysValues.get(1));
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,daysValues.get(2));
        breakfastSpinner.setAdapter(adapter1);
        lunchSpinner.setAdapter(adapter2);
        dinnerSpinner.setAdapter(adapter3);
    }

    public List<String> getRecipesList(){

        //change this
        List<String> lst = new ArrayList<>(Arrays.asList(new String[]{"Recipe 1", "Recipe 2", "Recipe 1"}));
//        List<String> result = new ArrayList<>();
//        for(NewRecipe n : lst){
//            result.add(n.getRecipeName());
//        }

        return lst;
    }

    public void updateMealData(String day, String breakfastVal, String lunchVal, String dinnerVal){

        for(Meals.Tuple t : map.keySet()){

            if(Objects.equals(t.getD().name().toLowerCase(),day.toLowerCase())){
                switch (t.getM().name().toLowerCase()){
                    case breakfast:
                        map.put(t,breakfastVal);
                        break;
                    case lunch:
                        map.put(t,lunchVal);
                        break;
                    case dinner:
                        map.put(t,dinnerVal);
                        break;
                }
            }

        }

        GetSaveData.SaveMeals(this, data);
    }

    public void previousClicked(View view){

        TextView textView = (TextView)findViewById(R.id.days);
        String currDay = textView.getText().toString();

        String previousDay = Helpers.getPreviousDay(currDay.toLowerCase());
        textView.setText(previousDay);

        setAdapters();

    }

    public void nextClicked(View view){

        TextView textView = (TextView)findViewById(R.id.days);
        String currDay = textView.getText().toString();

        String nextDay = Helpers.getNextDay(currDay.toLowerCase());
        textView.setText(nextDay);

        setAdapters();

    }

    public void updateUsedMealsForAdapterView(String day, String meal, int index){
        //update the adapters
        List<List<String>> lst = mealsCalender.get(day.toLowerCase());
        List<String> mealsUsed = usedMeals.get(day.toLowerCase());
        if(!mealsUsed.contains(meal) && !meal.equals(Meals.defaultMeal)) {
            mealsUsed.add(meal);
        }
        int i = lst.get(index).lastIndexOf(meal);
        lst.get(index).remove(i);
        lst.get(index).add(0,meal);
    }

    public void saveMeal(View view){

        String breakfastVal = ((Spinner)findViewById(R.id.breakfast_spinner)).getSelectedItem().toString();
        String lunchVal = ((Spinner)findViewById(R.id.lunch_spinner)).getSelectedItem().toString();
        String dinnerVal = ((Spinner)findViewById(R.id.dinner_spinner)).getSelectedItem().toString();


        TextView textView = (TextView)findViewById(R.id.days);
        String currDay = textView.getText().toString();

        //save and update the meal data in file store
        updateMealData(currDay,breakfastVal,lunchVal,dinnerVal);

        //update the used meals
        updateUsedMealsForAdapterView(currDay,breakfastVal,0);
        updateUsedMealsForAdapterView(currDay,lunchVal,1);
        updateUsedMealsForAdapterView(currDay,dinnerVal,2);

        //update the adapters based on this day
        updateAdaptersBasedOnDay(currDay);
        setAdapters();

        Toast.makeText(this,"Meals Saved for the day!",Toast.LENGTH_LONG).show();

    }
}
