package com.codepath.chefster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.chefster.R;
import com.codepath.chefster.adapters.FavoritesListAdapter;
import com.codepath.chefster.models.Dish;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MealLaunchActivity extends BaseActivity {
    @BindView(R.id.recycler_view_chosen_dishes) RecyclerView dishesRecyclerView;
    @BindView(R.id.pans_spinner) Spinner pansSpinner;
    @BindView(R.id.pots_spinner) Spinner potsSpinner;
    @BindView(R.id.checkbox_oven) CheckBox ovenCheckbox;
    @BindView(R.id.text_view_person) TextView onePersonTextView;
    @BindView(R.id.text_view_persons) TextView morePeopleTextView;

    @BindColor(android.R.color.black) int chosenColor;
    @BindColor(android.R.color.white) int unchosenColor;

    List<Dish> chosenDishes;
    FavoritesListAdapter dishesAdapter;
    int numberOfPeople = 1;
    int numberOfPans = 1;
    int numberOfPots = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_launch);
        ButterKnife.bind(this);

        chosenDishes = Parcels.unwrap(getIntent().getParcelableExtra("selected_dishes"));
        dishesAdapter = new FavoritesListAdapter(this, chosenDishes);
        dishesRecyclerView.setAdapter(dishesAdapter);
        // Setup layout manager for items with orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // Attach layout manager to the RecyclerView
        dishesRecyclerView.setLayoutManager(layoutManager);

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

        onePersonTextView.setBackgroundColor(chosenColor);
        onePersonTextView.setTextColor(unchosenColor);
    }

    private void calculateCookingTime() {
        Toast.makeText(this, "Recalculate time necessary for cooking!", Toast.LENGTH_SHORT).show();
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
        intent.putExtra("selected_dishes", Parcels.wrap(chosenDishes));
        intent.putExtra("number_of_people", numberOfPeople);
        intent.putExtra("number_of_pans", numberOfPans);
        intent.putExtra("number_of_pots", numberOfPots);
        startActivity(intent);
    }
}
