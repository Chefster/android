package com.codepath.chefster.models;

import org.parceler.Parcel;

@Parcel
public class Tool {
    private String name;
    private Integer amount;

    public Tool() {
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
