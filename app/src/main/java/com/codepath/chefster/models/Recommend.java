package com.codepath.chefster.models;

/**
 * Created by Hezi Eliyahu on 09/11/2016.
 */

public class Recommend {
    String name;
    int recipeUid;
    String review;
    int stars;

    public Recommend(String name, int recipeUid, String review, int stars) {
        this.name = name;
        this.recipeUid = recipeUid;
        this.review = review;
        this.stars = stars;
    }

    // Getters And Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRecipeUid() {
        return recipeUid;
    }

    public void setRecipeUid(int recipeUid) {
        this.recipeUid = recipeUid;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
