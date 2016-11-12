package com.codepath.chefster.models;

/**
 * Created by Hezi Eliyahu on 09/11/2016.
 */

public class Instruction {
    private String description;
    private int durationTime; // in minutes
    private String type; // ex: pre prep, prep, cook, serve ...
    private int order;

    public Instruction(String description, int durationTime, String type, int order) {
        this.description = description;
        this.durationTime = durationTime;
        this.type = type;
        this.order = order;
    }

    // Getters And Setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
