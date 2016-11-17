package com.codepath.chefster.models;

import android.os.Parcelable;

import org.parceler.Parcel;

/**
 * This class represents an ingredient that's a part of a dish with its price and amount needed
 */
@Parcel
public class Ingredient implements Parcelable{
    private String name;
    private String type;
    private String description;
    private Double amount;
    private Double price;
    private String amountType;
    private String thumbnail;

    public Ingredient() {
    }

    protected Ingredient(android.os.Parcel in) {
        name = in.readString();
        type = in.readString();
        description = in.readString();
        amountType = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(android.os.Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(description);
        parcel.writeString(amountType);
        parcel.writeString(thumbnail);
    }
}

