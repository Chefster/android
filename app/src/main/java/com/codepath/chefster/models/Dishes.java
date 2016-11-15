package com.codepath.chefster.models;

import android.util.Log;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Dishes {
    private ArrayList<Dish> dishes;

    public Dishes() {
        dishes = new ArrayList<>();
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
    }
}
