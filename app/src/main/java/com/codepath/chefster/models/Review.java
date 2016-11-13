package com.codepath.chefster.models;

import org.parceler.Parcel;

import java.util.Date;

/**
 * This class represents a review with its description, author, date, cooking time and rating
 */
@Parcel
public class Review {
    private User user;
    private String description;
    private Date date;
    private long cookingTimeInMillis;
    private double rating;

    public User getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public long getCookingTimeInMillis() {
        return cookingTimeInMillis;
    }

    public double getRating() {
        return rating;
    }
}
