package com.codepath.chefster.models;

import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;

import org.parceler.Parcel;

/**
 * This class represents one step out of multiple that creates a dish
 */
@Parcel
public class Step {
    private Integer order;
    private String description;
    private String dishName;
    private String type;
    private int preRequisite;
    private Integer durationTime;
    @Nullable private String imageUrl;

    public Step() {
    }

    public Integer getOrder() {
        return order;
    }

    public String getDescription() {
        return description;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getDurationTime() {
        return durationTime;
    }

    public String getDishName() {
        return dishName;
    }

    public int getPreRequisite() {
        return preRequisite;
    }

    public String getType() {
        return type;
    }
}
