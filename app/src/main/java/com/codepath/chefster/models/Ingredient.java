package com.codepath.chefster.models;

import org.parceler.Parcel;

/**
 * This class represents an ingredient that's a part of a dish with its price and amount needed
 */
@Parcel
public class Ingredient {
    private String name;
    private String type;
    private double amount;
    private double price;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }
}
