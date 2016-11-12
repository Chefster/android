package com.codepath.chefster.models;

/**
 * Created by Hezi Eliyahu on 09/11/2016.
 */

public class Ingredient {
    private String name;
    private String description;
    private int amount;
    private String amountType;
    private String thumbnail;

    public Ingredient(String name, String description, int amount, String amountType, String thumbnail) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.amountType = amountType;
        this.thumbnail = thumbnail;
    }

    // Getters And Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAmountType() {
        return amountType;
    }

    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    public String getThumbnail() { return thumbnail; }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
}