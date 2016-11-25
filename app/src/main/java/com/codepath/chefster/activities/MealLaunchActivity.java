package com.codepath.chefster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.R;
import com.codepath.chefster.adapters.CompactLayoutDishAdapter;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Tool;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MealLaunchActivity extends BaseActivity {
    private static final int OPTIMIZED_MULTIPLE_PEOPLE_PORTION = 7;
    private static final int AGGREGATED_MULTIPLE_PEOPLE_PORTION = 5;
    @BindView(R.id.recycler_view_chosen_dishes) RecyclerView dishesRecyclerView;
    @BindView(R.id.pans_spinner) Spinner pansSpinner;
    @BindView(R.id.pots_spinner) Spinner potsSpinner;
    @BindView(R.id.text_view_person) TextView onePersonTextView;
    @BindView(R.id.text_view_persons) TextView morePeopleTextView;
    @BindView(R.id.text_view_regular_time_calc) TextView regularTimeTextView;
    @BindView(R.id.text_view_app_time_calc) TextView appTimeTextView;
    @BindView(R.id.text_view_list_tools_needed) TextView listOfToolsNeededTextView;
    @BindView(R.id.text_view_tools_warning) TextView toolsWarningTextView;

    @BindColor(android.R.color.black) int chosenColor;
    @BindColor(android.R.color.white) int unchosenColor;

    List<Dish> chosenDishes;
    CompactLayoutDishAdapter dishesAdapter;

    HashMap<String, Integer> toolsNeededHashMap;
    int numberOfPeople = 1;
    int numberOfPans = 1;
    int numberOfPots = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_launch);
        ButterKnife.bind(this);

        setupRecyclerView();
        countToolsNeeded();
        setupSpinners();

        onePersonTextView.setBackgroundColor(chosenColor);
        onePersonTextView.setTextColor(unchosenColor);

        calculateCookingTime();
    }

    private void countToolsNeeded() {
        toolsNeededHashMap = new HashMap<>();
        for (Dish dish : chosenDishes) {
            for (Tool tool : dish.getTools()) {
                Integer amountOfToolNeeded = toolsNeededHashMap.get(tool.getName());
                toolsNeededHashMap.put(tool.getName(),
                        amountOfToolNeeded == null ? tool.getAmount() : amountOfToolNeeded + tool.getAmount());
            }
        }

        StringBuilder toolsNeededStr = new StringBuilder();
        for (String key : toolsNeededHashMap.keySet()) {
            toolsNeededStr.append(toolsNeededHashMap.get(key)).append(" ").append(key).append(", ");
        }
        toolsNeededStr.setLength(toolsNeededStr.length() - 2);

        listOfToolsNeededTextView.setText(toolsNeededStr.toString());
    }

    private void setupRecyclerView() {
        chosenDishes = Parcels.unwrap(getIntent().getParcelableExtra(ChefsterApplication.SELECTED_DISHES_KEY));
        dishesAdapter = new CompactLayoutDishAdapter(this, chosenDishes);
        dishesRecyclerView.setAdapter(dishesAdapter);
        // Setup layout manager for items with orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // Attach layout manager to the RecyclerView
        dishesRecyclerView.setLayoutManager(layoutManager);
    }

    private void setupSpinners() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.amount_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        pansSpinner.setAdapter(adapter);
        pansSpinner.setSelection(numberOfPans - 1);
        pansSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numberOfPans = i + 1;
                calculateCookingTime();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        potsSpinner.setAdapter(adapter);
        potsSpinner.setSelection(numberOfPots - 1);
        potsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numberOfPots = i + 1;
                calculateCookingTime();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * This method calculates the time it will take to cook this meal sequentially (without the app)
     * and how much it will take 1/2 people to cook it with the help of the app
     */
    private void calculateCookingTime() {
        // Prep time is for steps that demand preparation are defined as "Busy" steps, and Cooking time
        // is for steps that demands only a partial attention, like paste on stove or baking something
        // in the oven
        int totalPrepTime = 0, totalCookingTime = 0, totalOptimizedTime = 0, totalAggregatedTime = 0;
        for (Dish dish : chosenDishes) {
            totalOptimizedTime += Math.max(dish.getCookingTime(), (dish.getPrepTime() / numberOfPeople));
            // If you cook sequentially, add 10 minutes between dishes to breathe a little :)
            totalAggregatedTime += (dish.getCookingTime() + dish.getPrepTime() + 10);
        }
        // Remove the last 10 minutes that were added because it's a wrong calculation
        totalAggregatedTime -= 10;
        // These two are estimated assumptions for how productive 2 people can be in comparison to 1
        totalAggregatedTime -= (numberOfPeople > 1 ? (totalAggregatedTime / AGGREGATED_MULTIPLE_PEOPLE_PORTION) : 0);
        totalOptimizedTime -= (numberOfPeople > 1 ? (totalOptimizedTime / OPTIMIZED_MULTIPLE_PEOPLE_PORTION) : 0);

        regularTimeTextView.setText(getBetterFormat(totalAggregatedTime));
        appTimeTextView.setText(getBetterFormat(totalOptimizedTime));

        toolsWarningTextView.setVisibility(View.GONE);
        for (String key : toolsNeededHashMap.keySet()) {
            if ((key.equals("Pan") && toolsNeededHashMap.get(key) > numberOfPans)
                || (key.equals("Pot") && toolsNeededHashMap.get(key) > numberOfPots)) {
                toolsWarningTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    private CharSequence getBetterFormat(int timeInMinutes) {
        return "" + (timeInMinutes / 60) + "h" + (timeInMinutes % 60) + "m";
    }

    @OnClick(R.id.text_view_person)
    public void chooseOnePerson() {
        onePersonTextView.setBackgroundColor(chosenColor);
        onePersonTextView.setTextColor(unchosenColor);
        morePeopleTextView.setBackgroundColor(unchosenColor);
        morePeopleTextView.setTextColor(chosenColor);
        numberOfPeople = 1;
        calculateCookingTime();
    }

    @OnClick(R.id.text_view_persons)
    public void chooseMorePeople() {
        onePersonTextView.setBackgroundColor(unchosenColor);
        onePersonTextView.setTextColor(chosenColor);
        morePeopleTextView.setBackgroundColor(chosenColor);
        morePeopleTextView.setTextColor(unchosenColor);
        numberOfPeople = 2;
        calculateCookingTime();
    }

    @OnClick(R.id.button_start_cooking)
    public void startCooking() {
        Intent intent = new Intent(this, ProgressActivity.class);
        intent.putExtra(ChefsterApplication.SELECTED_DISHES_KEY, Parcels.wrap(chosenDishes));
        intent.putExtra("number_of_people", numberOfPeople);
        intent.putExtra("number_of_pans", numberOfPans);
        intent.putExtra("number_of_pots", numberOfPots);
        startActivity(intent);
    }
}
