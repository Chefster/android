package com.codepath.chefster.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * This class represents a dish with the amount of steps needed and estimated time to cook
 */
@Parcel
public class Dish {
    private Integer uid;
    private Integer serving; // amount of people.
    private Integer calories; //  calories
    private Integer prepTime;
    private Integer cookingTime;
    private List<Step> steps;
    private List<Ingredient> ingredients;
    private List<Review> reviews;
    private List<Tool> tools;  // pan, pot ...
    private List<String> thumbnails;
    private String title; // Meatloaf
    private String description; // This meatloaf is extremely juicy
    private String category;  // Israeli, Indian, Italian ...
    private String subCategory; // breakfast, lunch, dinner
    private String videoUrl;
    private Double rating; // 5 stars

    public Dish() {
    }

    public Integer getUid() {
        return uid;
    }

    public Integer getServing() {
        return serving;
    }

    public Integer getCalories() {
        return calories;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public Integer getCookingTime() {
        return cookingTime;
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

    public List<Tool> getTools() {
        return tools;
    }

    public List<String> getThumbnails() {
        return thumbnails;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Double getRating() {
        return rating;
    }
}
