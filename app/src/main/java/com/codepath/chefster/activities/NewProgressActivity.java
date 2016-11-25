package com.codepath.chefster.activities;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Step;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewProgressActivity extends AppCompatActivity {
    @BindView(R.id.list_view_main_1) LinearLayout listView1;
    @BindView(R.id.list_view_main_2) LinearLayout listView2;
    @BindView(R.id.list_view_main_3) LinearLayout listView3;

    List<Dish> chosenDishes;
    List<List<Step>> stepsLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_progress);
        ButterKnife.bind(this);

        chosenDishes = Parcels.unwrap(getIntent().getParcelableExtra(ChefsterApplication.SELECTED_DISHES_KEY));
        stepsLists = new ArrayList<>(chosenDishes.size());
        for (Dish dish : chosenDishes) {
            stepsLists.add(dish.getSteps());
        }

    }
}
