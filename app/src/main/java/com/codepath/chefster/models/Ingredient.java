package com.codepath.chefster.models;

import org.parceler.Parcel;

/**
 * This class represents an ingredient that's a part of a dish with its price and amount needed
 */
@Parcel
public class Ingredient {
    private String name;
    private String type;
    private String description;
    private Double amount;
    private Double price;
    private String amountType;
    private String thumbnail;

    public Ingredient() {
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public String getAmountType() {
        return amountType;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}

