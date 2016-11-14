package com.codepath.chefster.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.chefster.R;
import com.codepath.chefster.Recipes;
import com.codepath.chefster.adapters.FavoritesListAdapter;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Meal;

import org.parceler.Parcels;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MealLaunchActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view_chosen_dishes) RecyclerView dishesRecyclerView;
    @BindView(R.id.pans_spinner) Spinner pansSpinner;
    @BindView(R.id.pots_spinner) Spinner potsSpinner;
    @BindView(R.id.checkbox_oven) CheckBox ovenCheckbox;
    @BindView(R.id.text_view_person) TextView onepersonTextView;
    @BindView(R.id.text_view_persons) TextView morePeopleTextView;


    List<Dish> chosenDishes;
    FavoritesListAdapter dishesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_launch);
        ButterKnife.bind(this);

        chosenDishes = new ArrayList<>();
        dishesAdapter = new FavoritesListAdapter(this, chosenDishes);
        dishesRecyclerView.setAdapter(dishesAdapter);
        // Setup layout manager for items with orientation
        // Also supports `LinearLayoutManager.HORIZONTAL`
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // Attach layout manager to the RecyclerView
        dishesRecyclerView.setLayoutManager(layoutManager);
//        Meal chosenMeal = Parcels.unwrap(getIntent().getParcelableExtra("meal"));
//        chosenDishes = chosenMeal.getDishes();

        try {
            InputStream inputStream = getAssets().open("recipes.json");
            chosenDishes.addAll(Recipes.fromInputStream(inputStream));
            dishesAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.amount_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        pansSpinner.setAdapter(adapter);
        pansSpinner.setSelection(0);
        potsSpinner.setAdapter(adapter);
        potsSpinner.setSelection(0);
    }

}
