package com.codepath.chefster.models;

import android.util.Log;

import com.codepath.chefster.Database.ChefsterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import org.parceler.Parcel;

import java.util.List;

/**
 * This class represents a user and his/her activities in the app
 */
@Table(database = ChefsterDatabase.class)
@Parcel(analyze = {User.class})
public class User {
    @Column
    @PrimaryKey(autoincrement = true)
    private Long id;
    @Column
    private String imageUrl;
    @Column
    private String firstName;
    @Column
    private String lastName;
    private List<Meal> pastMeals;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getNumberOfMealsCooked() {
        return pastMeals.size();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPastMeals(List<Meal> pastMeals) {
        this.pastMeals = pastMeals;
    }
}
