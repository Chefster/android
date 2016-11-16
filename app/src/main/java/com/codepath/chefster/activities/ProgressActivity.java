package com.codepath.chefster.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.chefster.R;
import com.codepath.chefster.adapters.ProgressAdapter;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Step;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressActivity extends BaseActivity implements ProgressAdapter.onStepDoneListener {
    @BindView(R.id.recycler_view_main) RecyclerView mainRecyclerView;

    List<List<Step>> stepLists;
    List<ProgressAdapter> adaptersList;
    List<HashSet<String>> hashSetStepIndexList;
    List<Dish> chosenDishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ButterKnife.bind(this);

        chosenDishes = Parcels.unwrap(getIntent().getParcelableExtra("selected_dishes"));
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
        adaptersList = new ArrayList<>();



        for (int i = 0; i < chosenDishes.size(); i++) {
            List<Step> currentList = chosenDishes.get(i).getSteps();
            ProgressAdapter currentAdapter = new ProgressAdapter(currentList, this);
            RecyclerView recyclerView = new RecyclerView(this);
            // We want to have the recycler view with equal size for each row
            recyclerView.setLayoutParams(
                    new RecyclerView.LayoutParams(mainFrameLayout.getWidth(), mainFrameLayout.getHeight() / chosenDishes.size()));
            LinearLayoutManager llm = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(currentAdapter);
            mainFrameLayout.addView(recyclerView);

            stepLists.add(currentList);
            adaptersList.add(currentAdapter);
        }
    }

    @Override
    public void onStepDone(int position) {

    }
}
