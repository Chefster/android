package com.codepath.chefster.models;

import android.os.Parcelable;

import org.parceler.Parcel;

import java.util.Date;

/**
 * This class represents a review with its description, author, date, cooking time and rating
 */
@Parcel
public class Review implements Parcelable{
    private User user;
    private String description;
    private Date date;
    private Long cookingTimeInMillis;
    private Double rating;

    public Review() {
    }

    protected Review(android.os.Parcel in) {
        description = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(android.os.Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int i) {
        parcel.writeString(description);
    }
}
