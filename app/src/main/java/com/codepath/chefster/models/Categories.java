package com.codepath.chefster.models;

/**
 * Created by PRAGYA on 11/12/2016.
 */

public class Categories {

    private String categoryName;
    private String categoryUrl;

    public Categories(String categoryName, String categoryUrl) {
        this.categoryName = categoryName;
        this.categoryUrl = categoryUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }
}
