package com.codepath.chefster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.codepath.chefster.R;
import com.codepath.chefster.adapters.ProgressAdapter;
import com.codepath.chefster.adapters.RecyclerViewAdapter;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Step;
import com.codepath.chefster.viewholders.RecyclerViewViewHolder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProgressActivity extends BaseActivity implements ProgressAdapter.onStepDoneListener {
    @BindView(R.id.recycler_view_main) RecyclerView mainRecyclerView;

    List<List<Step>> stepLists;
    RecyclerViewAdapter mainAdapter;
    List<HashSet<String>> hashSetStepIndexList;
    List<Integer> activeStepsList;
    List<Dish> chosenDishes;
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
        hashSetStepIndexList = new ArrayList<>(chosenDishes.size());
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
        stepLists = new ArrayList<>();
        mainAdapter = new RecyclerViewAdapter(stepLists, this);

        for (int i = 0; i < chosenDishes.size(); i++) {
            List<Step> currentList = chosenDishes.get(i).getSteps();
            stepLists.add(currentList);
        }
        mainAdapter.notifyDataSetChanged();
//        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, false);
//        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setAdapter(mainAdapter);
    }

    public void scrollToStep(int list, int step) {
        mainRecyclerView.smoothScrollToPosition(list);
        mainAdapter.setActiveStep(list, step);
    }

    @OnClick(R.id.text_view_meals_in_progress)
    public void startScrolling() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    scrollToStep(0,4);

                    Thread.sleep(1000);
                    scrollToStep(1,2);

                    Thread.sleep(2000);
                    scrollToStep(2,1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.run();
    }

    @Override
    public void onStepDone(int position) {

    }
}
