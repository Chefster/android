package com.codepath.chefster.client;

import android.content.Context;
import android.content.res.AssetManager;

import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.Recipes;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Review;
import com.codepath.chefster.utils.LocalStorage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FirebaseClient {

    private Context context;
    private InputStream inputStream;
    private static List<Dish> dishesArray;
    private DatabaseReference mDatabase;

    // empty constructor
    public FirebaseClient() {
    }

    public static List<Dish> getDishes(String category) {
        if (category != null) {
            List<Dish> categoryDishes = new ArrayList<>();
            for (Dish dish : dishesArray) {
                if (dish.getCategory().equals(category)) {
                    categoryDishes.add(dish);
                }
            }

            return categoryDishes;
        } else {
            LocalStorage localStorage = ChefsterApplication.getLocalStorage();
            List<Dish> favoriteDishes = new ArrayList<>();
            for (Dish dish : dishesArray) {
                if (localStorage.isDishFavorited(dish.getUid())) {
                    favoriteDishes.add(dish);
                }
            }
            return favoriteDishes;
        }
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

    //upload the reviews to database
    public void uploadDishReviewsToDatabase(Review review, Integer dishId){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("dishes").child(String.valueOf(dishId)).child("reviews").setValue(review);
    }
}
