package com.codepath.chefster.client;

import android.content.Context;
import android.content.res.AssetManager;
import com.codepath.chefster.Recipes;
import com.codepath.chefster.models.Dish;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Hezi Eliyahu on 15/11/2016.
 */

public class FirebaseClient {

    private Context context;
    private InputStream inputStream;
    private static ArrayList<Dish> dishesArray;
    private DatabaseReference mDatabase;

    // empty constructor
    public FirebaseClient() {
    }

    public static ArrayList<Dish> getDishes() {
        return dishesArray;
    }

    public static void setDishes(ArrayList<Dish> dishesArray) {
        FirebaseClient.dishesArray = dishesArray;
    }

    // constructor
    public FirebaseClient(Context context) {
        this.context = context;

        AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open("recipes.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.dishesArray = new ArrayList<>();
    }

    // Load data From .json To ArrayList
    private ArrayList<Dish> loadRecipesFromJson() {
        return Recipes.fromInputStream(inputStream);
    }

    // Upload the ArrayList into Database.
    public void uploadNewDataToDatabase(){
        dishesArray = loadRecipesFromJson();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        for (Dish dish : dishesArray ) {
            mDatabase.child("dishes").child(String.valueOf(dish.getUid())).setValue(dish);
        }
    }
}
