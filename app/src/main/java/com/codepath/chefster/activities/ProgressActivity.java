package com.codepath.chefster.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.codepath.chefster.R;
import com.codepath.chefster.adapters.ProgressAdapter;
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view_done) RecyclerView finishedStepsRecyclerView;
    @BindView(R.id.recycler_view_active) RecyclerView toDoStepsRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        ButterKnife.bind(this);

        setupRecycler();
    }


    protected void setupRecycler() {

    }
}
