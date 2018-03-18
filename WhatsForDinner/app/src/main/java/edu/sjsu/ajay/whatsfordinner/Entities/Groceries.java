package edu.sjsu.ajay.whatsfordinner.Entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by ajay on 3/17/18.
 */

public class Groceries implements Serializable{

    Map<String, Integer> groceriesToBuy;

    public Groceries(){
        groceriesToBuy = new LinkedHashMap<>();
    }

    public Map<String, Integer> getGroceriesToBuy() {
        return groceriesToBuy;
    }

    public void setGroceriesToBuy(Map<String, Integer> groceriesToBuy) {
        this.groceriesToBuy = groceriesToBuy;
    }
}
