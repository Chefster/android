package com.codepath.chefster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.codepath.chefster.R;
import com.codepath.chefster.adapters.ProgressAdapter;
import com.codepath.chefster.adapters.RecyclerViewAdapter;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Step;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProgressActivity extends BaseActivity implements ProgressAdapter.onStepInteractionListener {
    @Nullable @BindViews({ R.id.recycler_view_main_1, R.id.recycler_view_main_2, R.id.recycler_view_main_3 })
    List<RecyclerView> recyclerViewsList;

    ProgressAdapter[] adapter;
    List<HashSet<String>> hashSetStepIndexList;
    List<Integer> activeStepsList;
    List<Dish> chosenDishes;
    HashMap<String, Integer> dishToIndexHashMap;
    int numberOfPeople, numberOfPans, numberOfPots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ButterKnife.bind(this);

        chosenDishes = Parcels.unwrap(getIntent().getParcelableExtra("selected_dishes"));
        Intent incomingIntent = getIntent();
        numberOfPeople = incomingIntent.getIntExtra("number_of_people", 1);
        numberOfPans = incomingIntent.getIntExtra("number_of_pans", 1);
        numberOfPots = incomingIntent.getIntExtra("number_of_pots", 1);
        adapter = new ProgressAdapter[chosenDishes.size()];
        hashSetStepIndexList = new ArrayList<>(chosenDishes.size());
        dishToIndexHashMap = new HashMap<>();
//        for (int i = 0; i < chosenDishes.size(); i++) {
//            if (chosenDishes.get(i).getSteps() != null) {
//                for (Step step : chosenDishes.get(i).getSteps()) {
//                    if (step.getPreRequisite() == -1) {
//                        doneSteps.add(step);
//                    } else {
//                        activeSteps.add(step);
//                    }
//                }
//            }
//        }
//        if (doneSteps.size() > 0) {
//            doneStepsTextView.setVisibility(View.VISIBLE);
//        }

        setupRecyclerViews();
    }

    protected void setupRecyclerViews() {
        for (int i = 0; i < chosenDishes.size(); i++) {
            List<Step> currentList = chosenDishes.get(i).getSteps();
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
        recyclerViewsList.get(list).scrollToPosition(step);
        adapter[list].setActiveStep(step);
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


//
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(1000);
//
//                        scrollToStep(0,4);
//
//                        Thread.sleep(1000);
//                        scrollToStep(1,2);
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            thread.run();
    }

    @Override
    public void onStepDone(String dish, int step) {

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
}
