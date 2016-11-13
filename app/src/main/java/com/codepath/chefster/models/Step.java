package com.codepath.chefster.models;

import android.support.annotation.Nullable;

import org.parceler.Parcel;

/**
 * This class represents one step out of multiple that creates a dish
 */
@Parcel
public class Step {
    private String description;
    private Integer order;
    private Integer durationTime;
    private Integer prerequisite;
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

    public Integer getPrerequisite() {
        return prerequisite;
    }

    public boolean hasPreRequisite() {
        return prerequisite != -1;
    }
}
