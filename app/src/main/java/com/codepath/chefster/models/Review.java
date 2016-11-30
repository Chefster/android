package com.codepath.chefster.models;

import com.codepath.chefster.Database.ChefsterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import org.parceler.Parcel;

import java.util.Date;

/**
 * This class represents a review with its description, author, date, cooking time and rating
 */
@Table(database = ChefsterDatabase.class)
@Parcel(analyze={Dish.class})
public class Review{
    @PrimaryKey
    @Column
    private Integer id;
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
    private Integer name;

    public Review() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
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
}
