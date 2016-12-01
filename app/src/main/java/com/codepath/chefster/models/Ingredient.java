package com.codepath.chefster.models;

import android.os.Parcelable;

import org.parceler.Parcel;

/**
 * This class represents an ingredient that's a part of a dish with its price and amount needed
 */
@Parcel
public class Ingredient implements Comparable {
    private String name;
    private String type;
    private String description;
    private Double amount;
    private Double price;
    private String amountType;
    private String thumbnail;
    private String category;

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

    public Double getAmount() {
        return amount;
    }

    public Double getPrice() {
        return price;
    }

    public String getAmountType() {
        return amountType;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public int compareTo(Object o) {
        Ingredient other = (Ingredient) o;
        return category.compareTo(other.category);
    }
}

