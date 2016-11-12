package com.codepath.chefster.models;

/**
 * Created by Hezi Eliyahu on 09/11/2016.
 */

public class Tool {
    private String name;
    private int amount;

    public Tool(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
