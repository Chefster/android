package com.codepath.chefster.models;

public class Category {

    private String name;
    private String image;

    public Category(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
