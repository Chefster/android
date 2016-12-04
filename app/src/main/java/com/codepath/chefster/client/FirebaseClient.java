package com.codepath.chefster.client;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.Recipes;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Review;
import com.codepath.chefster.models.User;
import com.codepath.chefster.utils.LocalStorage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
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
    private static DatabaseReference mDatabase;

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
    public void uploadNewDataToDatabase() {
        dishesArray = loadRecipesFromJson();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        for (Dish dish : dishesArray) {
            mDatabase.child("dishes").child(String.valueOf(dish.getUid())).setValue(dish);
        }
    }

    // Upload the ArrayList into Database.
    public static void uploadDishReviewToFireBase(Review review, int dishUid) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("dishes").child(String.valueOf(dishUid)).
                child("reviews").child(String.valueOf(review.getReviewId())).
                setValue(review);
    }

    /*
    *   getting the user information from firebase.
    */
    public static User getUserInformation(){
        User myUser = new User();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                myUser.setFirstName(name);
                String email = profile.getEmail();
                myUser.setEmail(email);
                Uri photoUrl = profile.getPhotoUrl();
                if ( photoUrl != null)
                    myUser.setImageUrl(photoUrl.toString());
                return myUser;
            };
            return myUser;
        }
        return null;
    }


    /*
    *  Updating the information of a user in Firebase
     */
    public static void updateUserInformation(String displayName, String photoUri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(Uri.parse(photoUri))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("FirebaseClient", "User profile updated.");
                        }
                    }
                });
    }
}