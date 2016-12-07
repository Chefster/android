package com.codepath.chefster.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.R;
import com.codepath.chefster.adapters.CompactLayoutDishAdapter;
import com.codepath.chefster.adapters.DishesAdapter;
import com.codepath.chefster.fragments.MealLaunchSettingsFragment;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Ingredient;
import com.codepath.chefster.models.Tool;
import com.codepath.chefster.utils.RecyclerViewVerticalSpacing;
import com.codepath.chefster.views.ShoppingInredientView;

import org.parceler.Parcels;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MealLaunchActivity extends BaseActivity implements
        MealLaunchSettingsFragment.OnSettingsDialogCloseListener {
    private static final int OPTIMIZED_MULTIPLE_PEOPLE_PORTION = 7;
    private static final int AGGREGATED_MULTIPLE_PEOPLE_PORTION = 5;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view_chosen_dishes) RecyclerView dishesRecyclerView;
    @BindView(R.id.text_view_regular_time_calc) TextView regularTimeTextView;
    @BindView(R.id.text_view_app_time_calc) TextView appTimeTextView;
    @BindView(R.id.text_view_list_tools_needed) TextView listOfToolsNeededTextView;
    @BindView(R.id.text_view_list_tools_using) TextView listOfToolsUsingTextView;
    @BindView(R.id.text_view_tools_warning) TextView toolsWarningTextView;
    @BindView(R.id.linear_layout_shopping_list) LinearLayout shoppingListLinearLayout;

    List<Dish> chosenDishes;
    List<Ingredient> ingredients;
    StringBuilder shoppingListStr;
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

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.left_arrow);
            getSupportActionBar().setTitle("Meal Summary");
        }

        launchSettingsDialog();

        setupRecyclerView();
        countToolsNeeded();
        calculateCookingTime();
        fillShoppingList();
        // Slide in from right
        //overridePendingTransition(R.anim.transition_from_right, R.anim.transition_to_left);
    }

    private void launchSettingsDialog() {
        FragmentManager fm = getFragmentManager();
        MealLaunchSettingsFragment mealLaunchSettingsFragment = MealLaunchSettingsFragment.newInstance(numberOfPans, numberOfPots, numberOfPeople);
        mealLaunchSettingsFragment.show(fm, "fragment_meal_launch_settings");
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
        String toolsUsingStr = numberOfPeople + " " + getQuant(R.plurals.people, numberOfPeople) +
                ", using " + numberOfPans + " " + getQuant(R.plurals.pans, numberOfPans) + ", " +
                numberOfPots + " " + getQuant(R.plurals.pots, numberOfPots) + ".";
        listOfToolsUsingTextView.setText(toolsUsingStr);
    }

    private void setupRecyclerView() {
        chosenDishes = Parcels.unwrap(getIntent().getParcelableExtra(ChefsterApplication.SELECTED_DISHES_KEY));
        dishesAdapter = new CompactLayoutDishAdapter(this, chosenDishes);
        dishesRecyclerView.setAdapter(dishesAdapter);
        // Setup layout manager for items with orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // Attach layout manager to the RecyclerView
        dishesRecyclerView.setLayoutManager(layoutManager);
        dishesRecyclerView.addItemDecoration(new RecyclerViewVerticalSpacing(20, false));
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

    private void fillShoppingList() {
        ingredients = new ArrayList<>();
        shoppingListStr = new StringBuilder();
        for (Ingredient ingredient : ChefsterApplication.shoppingList.keySet()) {
            boolean found = false;
            for (int i = 0; i < ingredients.size(); i++) {
                if (ingredient.getName().equals(ingredients.get(i).getName())) {
                    ingredients.get(i).setAmount(ingredients.get(i).getAmount() + ingredient.getAmount());
                    ingredients.get(i).setPrice(ingredients.get(i).getPrice() + ingredient.getPrice());
                    found = true;
                    break;
                }
            }
            if (!found) ingredients.add(ingredient);
        }
        Collections.sort(ingredients);
        String lastCategory = "jibrish";
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getCategory() != null) {
                if (!ingredient.getCategory().equals(lastCategory)) {
                    lastCategory = ingredient.getCategory();
                    TextView categoryTextView = new TextView(this);
                    categoryTextView.setTextSize(23f);
                    categoryTextView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    SpannableString content = new SpannableString(lastCategory);
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    categoryTextView.setText(content);
                    categoryTextView.setPadding(0, 4, 0, 0);
                    shoppingListLinearLayout.addView(categoryTextView);
                    shoppingListStr.append('\n').append(lastCategory).append('\n');
                }
            }
            double amount = ingredient.getAmount();
            String amountStr = amount + " " + ingredient.getAmountType();
            DecimalFormat df2 = new DecimalFormat(".##");
            ingredient.setPrice(Double.valueOf(df2.format(ingredient.getPrice())));
            ShoppingInredientView shoppingInredientView = new ShoppingInredientView(this);
            shoppingInredientView.setTextViews(amountStr, ingredient.getName(), ingredient.getPrice());
            shoppingListLinearLayout.addView(shoppingInredientView);
            shoppingListStr.append(amountStr).append(" ").append(ingredient.getName()).append(" = $").append(ingredient.getPrice());
        }
    }

    private CharSequence getBetterFormat(int timeInMinutes) {
        return "" + (timeInMinutes / 60) + "h" + (timeInMinutes % 60) + "m";
    }

    @OnClick(R.id.image_view_share)
    public void shareShoppingList() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Shopping List");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shoppingListStr.toString());
        startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_meal_launch, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_settings:
                launchSettingsDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onToolsSet(int pans, int pots, int people) {
        numberOfPans = pans;
        numberOfPots = pots;
        numberOfPeople = people;
        countToolsNeeded();
        calculateCookingTime();
    }

    public String getQuant(int id, int quantity) {
        return getResources().getQuantityString(id, quantity);
    }
}
