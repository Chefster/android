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
    private Long timeInMillis;
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


}
