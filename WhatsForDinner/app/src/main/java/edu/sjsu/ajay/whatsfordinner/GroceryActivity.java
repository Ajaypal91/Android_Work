package edu.sjsu.ajay.whatsfordinner;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.sjsu.ajay.whatsfordinner.Entities.Groceries;
import edu.sjsu.ajay.whatsfordinner.HelperMethods.GetSaveData;

public class GroceryActivity extends AppCompatActivity {

    Groceries groceries;
    String defaultUnit = "Units";
    String[] items, units;
    List<String> stringsToDisplay;
    Map<String, String> itemsToUnitsMap;
    ArrayAdapter<String> adapter;

    public static final String TAG = GroceryActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);

        //updateList
        updateGroceries();

        //menuCreator
        swipeMenuCreator();
    }


    public void swipeMenuCreator(){

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringsToDisplay);
//        adapter.notifyDataSetChanged();
        SwipeMenuListView listView = (SwipeMenuListView)findViewById(R.id.groceries_list_view);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "add" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66,
                        0xff)));
                // set item width
                openItem.setWidth(150);
                // set item title
                openItem.setTitle("Add");
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                openItem.setIcon(R.mipmap.add_grocery);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                // set item title
                deleteItem.setTitle("Delete");
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.mipmap.delete_grocery);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Log.i(TAG,"Add groceries clicked");
                        addGroceriesClick(menu, position);
                        break;
                    case 1:
                        Log.i(TAG,"Delete groceries clicked");
                        deleteGroceriesClick(menu, position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return true;
            }
        });
    }

    public void setGroceryNameToDisplayAndUpdateAdapter(String grocery, int count, int position){

        String m;
        if (!itemsToUnitsMap.containsKey(grocery))
            m = defaultUnit;
        else
            m = itemsToUnitsMap.get(grocery);
        String name = grocery + " ( " + count + " " + m +")";
        stringsToDisplay.remove(position);
        if(count!=0)
            stringsToDisplay.add(position,name);
        List<String> temp = new ArrayList<>(stringsToDisplay);

        //update the list
        SwipeMenuListView listView = (SwipeMenuListView)findViewById(R.id.groceries_list_view);
        adapter.clear();
        adapter.addAll(temp);
        adapter.notifyDataSetChanged();
    }

    public void deleteGroceriesClick(SwipeMenu menu, int position){

        Groceries groceries = GetSaveData.getGroceriesList(this);
        Map<String, Integer> map = groceries.getGroceriesToBuy();

        int i = 0;
        String grocery = "";
        int count = 0;

        for (String k : map.keySet()){

            if(i == position){
                grocery = k;
                count = map.get(k);
            }

            i++;
        }

        //decrease a count;
        count--;

        if (count <= 0 ){
            //strike out the element
//            TextView textView = (TextView)adapter.getView(position,null,null);
//            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            adapter.notifyDataSetChanged();

            //delete entry from groceries and save it
            map.remove(grocery);
            setGroceryNameToDisplayAndUpdateAdapter(grocery,count,position);

        }
        else{
            map.put(grocery,count);
            setGroceryNameToDisplayAndUpdateAdapter(grocery,count,position);

        }
        GetSaveData.saveGroceries(this, groceries);

    }

    public void addGroceriesClick(SwipeMenu menu, int position){

        Groceries groceries = GetSaveData.getGroceriesList(this);
        Map<String, Integer> map = groceries.getGroceriesToBuy();

        int i = 0;
        String grocery = "";
        int count = 0;

        for (String k : map.keySet()){

            if(i == position){
                grocery = k;
                count = map.get(k);
            }

            i++;
        }

        //increase a count;
        count++;

        map.put(grocery,count);
        setGroceryNameToDisplayAndUpdateAdapter(grocery,count,position);
        GetSaveData.saveGroceries(this, groceries);

    }


    public void updateGroceries(){

        stringsToDisplay = new ArrayList<>();
        Groceries groceries = GetSaveData.getGroceriesList(this);
        if (groceries!=null){
            Map<String, Integer> groceriesMap = groceries.getGroceriesToBuy();
            items = getResources().getStringArray(R.array.ingredients);
            units = getResources().getStringArray(R.array.units);

            itemsToUnitsMap = new HashMap<>();
            for (int i = 0 ; i < items.length; i++){
                itemsToUnitsMap.put(items[i],units[i]);
            }

            for(String s : groceries.getGroceriesToBuy().keySet()){

                int count = groceries.getGroceriesToBuy().get(s);
                String m;
                if (!itemsToUnitsMap.containsKey(s))
                    m = defaultUnit;
                else
                    m = itemsToUnitsMap.get(s);
                String grocery = s + " ( " + count + " " + m +")";
                stringsToDisplay.add(grocery);
            }


        }

    }
}
