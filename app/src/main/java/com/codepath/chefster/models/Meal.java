package com.codepath.chefster.models;


import org.parceler.Parcel;

import java.util.List;

/**
 * This class represents a meal that has one or multiple Dishes.
 */
@Parcel
public class Meal {
    private int id;
    private String name;
    private long cookingTimeInMillis;
    private double rating;
    private List<User> users;
    private List<Dish> dishes;
    private List<Review> reviews;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public long getCookingTimeInMillis() {
        return cookingTimeInMillis;
    }
}
