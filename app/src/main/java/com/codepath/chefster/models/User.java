package com.codepath.chefster.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * This class represents a user and his/her activities in the app
 */
@Parcel
public class User {
    private int id;
    private String imageUrl;
    private String firstName;
    private String lastName;
    private List<Meal> pastMeals;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Meal> getPastMeals() {
        return pastMeals;
    }

    public int getNumberOfMealsCooked() {
        return pastMeals.size();
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
