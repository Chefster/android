package com.codepath.chefster.models;

import android.os.Parcelable;

import com.codepath.chefster.Database.ChefsterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import org.parceler.Parcel;

import java.util.Date;

/**
 * This class represents a review with its description, author, date, cooking time and rating
 */
@Table(database = ChefsterDatabase.class)
@Parcel(analyze = {Review.class})
public class Review {
    @Column
    @PrimaryKey(autoincrement = true)
    private Long reviewId;
    @Column
    @ForeignKey
    private User user;
    @Column
    private String description;
    @Column
    private Date date;
    @Column
    private Long cookingTimeInMillis;
    @Column
    private Double rating;
    @Column
    private Long mealId;

    public Review() {
    }

    public Long getReviewId() {
        return reviewId;
    }

    public Long getMealId() {
        return mealId;
    }

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

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCookingTimeInMillis(Long cookingTimeInMillis) {
        this.cookingTimeInMillis = cookingTimeInMillis;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }
}
