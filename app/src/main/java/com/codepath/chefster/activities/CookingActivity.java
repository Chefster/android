package com.codepath.chefster.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codepath.chefster.R;
import com.codepath.chefster.Recipes;
import com.codepath.chefster.adapters.StepsAdapter;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Step;
import com.codepath.chefster.models.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CookingActivity extends AppCompatActivity implements StepsAdapter.OnActiveListInteraction {
    @BindView(R.id.recycler_view_done) RecyclerView doneStepsRecyclerView;
    @BindView(R.id.recycler_view_active) RecyclerView futureStepsRecyclerView;
    @BindView(R.id.view_lists_separator) View separatorView;

    List<Dish> dishes;
    User currentUser;
    List<Step> doneSteps, futureSteps;
    StepsAdapter doneStepsAdapter, futureStepsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);

        loadRecipesFromJson();
        setRecyclerViews();

        startCooking();
    }

    private void startCooking() {
        dishes.remove(0);
//        for ()
    }

    private void loadRecipesFromJson() {
        try {
            InputStream inputStream = this.getAssets().open("recipes.json");
            dishes = Recipes.fromInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setRecyclerViews() {
        doneSteps = new ArrayList<>();
        doneStepsAdapter = new StepsAdapter(doneSteps, this, true, this);
        doneStepsRecyclerView.setAdapter(doneStepsAdapter);

        futureSteps = new ArrayList<>();
        futureStepsAdapter = new StepsAdapter(futureSteps, this, false, this);
        futureStepsRecyclerView.setAdapter(futureStepsAdapter);
    }

    @Override
    public void onStepStarted() {

    }

    @Override
    public void onStepDone() {

    }
}
