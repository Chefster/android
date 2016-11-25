package com.codepath.chefster.models;

import com.codepath.chefster.Database.ChefsterDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;
import java.util.List;

/**
 * This class represents a dish with the amount of steps needed and estimated time to cook
 */
@Table(database = ChefsterDatabase.class)
@Parcel(analyze={Dish.class})
public class Dish extends BaseModel {
    @PrimaryKey
    @Column
    private Integer uid;
    @Column
    private Integer serving; // amount of people.
    @Column
    private Integer calories; //  calories
    @Column
    private Integer prepTime;
    @Column
    private Integer cookingTime;
    private List<Step> steps;
    private List<Ingredient> ingredients;
    private List<Review> reviews;
    private List<Tool> tools;  // pan, pot ...
    private List<String> thumbnails;
    @Column
    private String thumbnail;
    @Column
    private String title; // Meatloaf
    @Column
    private String description; // This meatloaf is extremely juicy
    @Column
    private String category;  // Israeli, Indian, Italian ...
    @Column
    private String subCategory; // breakfast, lunch, dinner
    @Column
    private String videoUrl;
    @Column
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

    public String getThumbnail() { return thumbnail; }

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


    // Setters

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public void setServing(Integer serving) {
        this.serving = serving;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public void setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    public void setThumbnails(List<String> thumbnails) {
        this.thumbnails = thumbnails;
        if ( ! thumbnails.isEmpty() ) {
            setThumbnail(thumbnails.get(0));
        }
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
