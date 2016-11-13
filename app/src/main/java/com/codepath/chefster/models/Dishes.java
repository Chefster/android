package com.codepath.chefster.models;

import org.parceler.Parcel;
import java.util.List;

@Parcel
public class Dishes {
    private List<Dish> dishes;

    public Dishes() {
    }

    public List<Dish> getDishes() {
        return dishes;
    }
}
