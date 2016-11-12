package com.codepath.chefster.models;

import java.util.ArrayList;

/**
 * Created by Hezi Eliyahu on 09/11/2016.
 */

public class Recipe {
    private int uid;
    private String title; // Meatloaf
    private boolean favorite;  // like, unlike
    private int totalDurationTime;  // in seconds
    private String description; // This meatloaf is extremely juicy
    private String category;  // Israeli, Indian, Italian ...
    private String subCategory; // breakfast, lunch, dinner
    private int serving; // amount of people.
    private int calories; //  calories
    private Double rating; // 5 stars
    private ArrayList<Ingredient> Ingredients; // onion, cucumber ...
    private ArrayList<Instruction> instructions; // Step 1: pre perp, step 2: prep ...
    private ArrayList<Tool> tools;  // pan, pot ...
    private ArrayList<Recommend> recommendation;// Hugo: that was tasty 5.
    private ArrayList<String> thumbnails;



    private String videoUrl;

    // Getters And Setters
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getTotalDurationTime() {
        return totalDurationTime;
    }

    public void setTotalDurationTime(int totalDurationTime) {
        this.totalDurationTime = totalDurationTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public int getServing() {
        return serving;
    }

    public void setServing(int serving) {
        this.serving = serving;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getVideoUrl() { return videoUrl; }

    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public ArrayList<Ingredient> getIngredients() {
        return Ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        Ingredients = ingredients;
    }

    public ArrayList<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<Instruction> instructions) {
        this.instructions = instructions;
    }

    public ArrayList<Tool> getTools() {
        return tools;
    }

    public void setTools(ArrayList<Tool> tools) {
        this.tools = tools;
    }

    public ArrayList<Recommend> getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(ArrayList<Recommend> recommendation) {
        this.recommendation = recommendation;
    }
    public ArrayList<String> getThumbnails() { return thumbnails; }

    public void setThumbnails(ArrayList<String> thumbnails) {
        this.thumbnails = thumbnails;
    }
}
