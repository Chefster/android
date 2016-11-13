package com.codepath.chefster.models;

import android.support.annotation.Nullable;

import org.parceler.Parcel;

/**
 * This class represents one step out of multiple that creates a dish
 */
@Parcel
public class Step {
    private int order;
    private String description;
    private int durationTime;
    @Nullable private String imageUrl;

    public int getOrder() {
        return order;
    }

    public String getDescription() {
        return description;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    public int getDurationTime() {
        return durationTime;
    }
}
