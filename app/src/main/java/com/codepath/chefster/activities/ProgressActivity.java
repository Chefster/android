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
    @BindView(R.id.recycler_view_done) RecyclerView doneStepsRecyclerView;
    @BindView(R.id.recycler_view_active) RecyclerView activeStepsRecyclerView;
    @BindView(R.id.text_view_done_steps_label) TextView doneStepsTextView;

    List<Step> doneSteps;
    ProgressAdapter doneStepsAdapter;
    List<Step> activeSteps;
    ProgressAdapter activeStepsAdapter;
    List<HashSet<String>> hashSetStepIndexList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ButterKnife.bind(this);

        setupRecyclerViews();

        List<Dish> chosenDishes = Parcels.unwrap(getIntent().getParcelableExtra("selected_dishes"));
        hashSetStepIndexList = new ArrayList<>(chosenDishes.size());
        for (int i = 0; i < chosenDishes.size(); i++) {
          if ( chosenDishes.get(i).getSteps() != null ) {
                for (Step step : chosenDishes.get(i).getSteps()) {
                    if (step.getPreRequisite() == -1) {
                        doneSteps.add(step);
                    } else {
                        activeSteps.add(step);
                    }
                }
            }
        }
        if (doneSteps.size() > 0) {
            doneStepsTextView.setVisibility(View.VISIBLE);
            doneStepsAdapter.notifyDataSetChanged();
        }
        activeStepsAdapter.notifyDataSetChanged();
    }

    protected void setupRecyclerViews() {
        doneSteps = new ArrayList<>();
        doneStepsAdapter = new ProgressAdapter(doneSteps, this, true);
        doneStepsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        doneStepsRecyclerView.setAdapter(doneStepsAdapter);

        activeSteps = new ArrayList<>();
        activeStepsAdapter = new ProgressAdapter(activeSteps, this, false);
        activeStepsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        activeStepsRecyclerView.setAdapter(activeStepsAdapter);
    }

    @Override
    public void onStepDone(int position) {
        Step newDoneStep = activeSteps.remove(position);
        activeStepsAdapter.notifyItemRemoved(position);
        doneSteps.add(0, newDoneStep);
        doneStepsAdapter.notifyItemInserted(0);
    }
}
