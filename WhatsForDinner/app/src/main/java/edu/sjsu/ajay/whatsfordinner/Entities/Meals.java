package edu.sjsu.ajay.whatsfordinner.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ajay on 3/17/18.
 */

public class Meals implements Serializable{

    public enum Days{
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIdAY, SATURDAY, SUNDAY
    }

    public enum DaysMeal{
        BREAKFAST, LUNCH, DINNER
    }

    public static class Tuple implements Serializable{
        Days d;
        DaysMeal m;

        public Tuple(Days d, DaysMeal m){
            this.d =d;
            this.m = m;
        }

        public Days getD() {
            return d;
        }

        public void setD(Days d) {
            this.d = d;
        }

        public DaysMeal getM() {
            return m;
        }

        public void setM(DaysMeal m) {
            this.m = m;
        }
    }

    public static String defaultMeal = "Eating Out";
    List<Tuple> mealsData;
    Map<Tuple, String> map;

    public Meals(){
        mealsData = new ArrayList<>();
        init();
    }

    public void init(){
        for(Days d : Days.values()){
            for(DaysMeal m : DaysMeal.values()){
                mealsData.add(new Tuple(d,m));
            }
        }

        map = new LinkedHashMap<>();

        for(Tuple t:mealsData){
            map.put(t,defaultMeal);
        }
    }

    public List<Tuple> getMealsData() {
        return mealsData;
    }

    public String getDefaultMeal() {
        return defaultMeal;
    }

    public void setDefaultMeal(String defaultMeal) {
        this.defaultMeal = defaultMeal;
    }

    public Map<Tuple, String> getMap() {
        return map;
    }

    public void setMap(Map<Tuple, String> map) {
        this.map = map;
    }

    public void setMealsData(List<Tuple> mealsData) {
        this.mealsData = mealsData;
    }
}
