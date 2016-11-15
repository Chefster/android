package com.codepath.chefster.models;


import org.parceler.Parcel;

import java.util.List;

/**
 * This class represents a meal that has one or multiple Dishes.
 */
@Parcel
public class Meal {
    private Long id;
    private String name;
    private Long cookingTimeInMillis;
    private Double rating;
    private List<User> users;
    private List<Dish> dishes;
    private List<Review> reviews;

    public Meal() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getRating() {
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

    public Long getCookingTimeInMillis() {
        return cookingTimeInMillis;
    }
}
