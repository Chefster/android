package com.codepath.chefster.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.R;
import com.codepath.chefster.adapters.ProgressAdapter;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Step;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProgressActivity extends BaseActivity implements ProgressAdapter.OnStepInteractionListener {
    @Nullable @BindViews({ R.id.recycler_view_main_1, R.id.recycler_view_main_2, R.id.recycler_view_main_3 })
    List<RecyclerView> recyclerViewsList;

    ProgressAdapter[] adapter;

    List<HashSet<String>> hashSetStepIndexList;
    List<Dish> chosenDishes;
    List<List<Step>> stepsLists;
    List<Integer> activeStepPerDish;
    // A HashMap that points on an index for each dish name
    HashMap<String,Integer> dishNameToIndexHashMap;
    int numberOfPeople, numberOfPans, numberOfPots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ButterKnife.bind(this);

        chosenDishes = Parcels.unwrap(getIntent().getParcelableExtra(ChefsterApplication.SELECTED_DISHES_KEY));
        stepsLists = new ArrayList<>(chosenDishes.size());
        for (Dish dish : chosenDishes) {
            stepsLists.add(dish.getSteps());
        }

        Intent incomingIntent = getIntent();
        numberOfPeople = incomingIntent.getIntExtra("number_of_people", 1);
        numberOfPans = incomingIntent.getIntExtra("number_of_pans", 1);
        numberOfPots = incomingIntent.getIntExtra("number_of_pots", 1);
        adapter = new ProgressAdapter[chosenDishes.size()];
        hashSetStepIndexList = new ArrayList<>(chosenDishes.size());
        dishNameToIndexHashMap = new HashMap<>();

        setupRecyclerViews();
        markInitialActiveSteps();
    }

    private void markInitialActiveSteps() {
        activeStepPerDish = new ArrayList<>(chosenDishes.size());
        PriorityQueue<Step> potentialInitialSteps = new PriorityQueue<>(chosenDishes.size());
        for (int i = 0; i < stepsLists.size(); i++) {
            activeStepPerDish.add(0);
            potentialInitialSteps.add(stepsLists.get(i).get(0));
        }

        for (int i = 0; i < numberOfPeople; i++) {
            Step thisStep = potentialInitialSteps.poll();
            if (thisStep != null) {
                int dishIndex = dishNameToIndexHashMap.get(thisStep.getDishName());
                thisStep.setStatus(Step.Status.ACTIVE);
                adapter[dishIndex].notifyDataSetChanged();
            }
        }
    }

    protected void setupRecyclerViews() {
        for (int i = 0; i < stepsLists.size(); i++) {
            dishNameToIndexHashMap.put(stepsLists.get(i).get(0).getDishName(), i);
            List<Step> currentList = stepsLists.get(i);
            for (Step step : currentList) {
                step.setStatus(Step.Status.READY);
            }
            adapter[i] = new ProgressAdapter(currentList, this, i);
            final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false);
            layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
            recyclerViewsList.get(i).setAdapter(adapter[i]);
            recyclerViewsList.get(i).setLayoutManager(layoutManager);
            recyclerViewsList.get(i).setHasFixedSize(true);
            recyclerViewsList.get(i).addOnScrollListener(new CenterScrollListener());
            recyclerViewsList.get(i).setVisibility(View.VISIBLE);
        }
    }

    public void scrollToStep(int list, int step) {
        recyclerViewsList.get(list).smoothScrollToPosition(step);
    }

    @OnClick(R.id.text_view_meals_in_progress)
    public void startScrolling() {
        try {
            Thread.sleep(1000);
            scrollToStep(0,4);
            Thread.sleep(1000);
            scrollToStep(1,2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStepDone(String dish, int step) {
        int index = dishNameToIndexHashMap.get(dish);
        List<Step> currentStepsList = stepsLists.get(index);
        currentStepsList.get(step).setStatus(Step.Status.DONE);
        adapter[index].notifyItemChanged(step);
        if (step == currentStepsList.size() - 1) {
            Toast.makeText(this, "Congrats for finishing to cook " + dish + "!", Toast.LENGTH_LONG).show();
        } else {
            currentStepsList.get(step + 1).setStatus(Step.Status.ACTIVE);
            adapter[index].notifyItemChanged(step + 1);
            scrollToStep(index, step + 1);
        }
    }

    @Override
    public void onDetailsButtonClick(int index) {
        boolean isExpanded = adapter[index].isExpanded();
        expandIndex(index, !isExpanded);
        adapter[index].setExpanded(!isExpanded);
        adapter[index].notifyDataSetChanged();
    }

    public void expandIndex(int index, boolean expand) {
        if (expand) {
            for (int i = 0; i < chosenDishes.size(); i++) {
                recyclerViewsList.get(i).setVisibility(i == index ? View.VISIBLE : View.GONE);
            }
        } else {
            for (int i = 0; i < chosenDishes.size(); i++) {
                recyclerViewsList.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.button_finish_cooking)
    public void finishCooking() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.finish_cooking_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getBaseContext(), ShareActivity.class);
                        intent.putExtra(ChefsterApplication.SELECTED_DISHES_KEY, Parcels.wrap(chosenDishes));
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
       builder.create().show();
    }
}
