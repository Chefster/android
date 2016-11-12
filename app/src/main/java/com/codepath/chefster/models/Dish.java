package com.codepath.chefster.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * This class represents a dish with the amount of steps needed and estimated time to cook
 */
@Parcel
public class Dish {
    private int id;
    private List<Step> steps;
    private List<Ingredient> ingredients;
    private List<Review> reviews;
    private long timeInMillis;
    private String imageUrl;

    public int getId() {
        return id;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
