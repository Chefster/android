package com.codepath.chefster.models;

import android.os.Parcelable;

import org.parceler.Parcel;

import java.util.Date;

/**
 * This class represents a review with its description, author, date, cooking time and rating
 */
@Parcel
public class Review{
    private User user;
    private String description;
    private Date date;
    private Long cookingTimeInMillis;
    private Double rating;

    public User getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public Long getCookingTimeInMillis() {
        return cookingTimeInMillis;
    }

    public Double getRating() {
        return rating;
    }
}
