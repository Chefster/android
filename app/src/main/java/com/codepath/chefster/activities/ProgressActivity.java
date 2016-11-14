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

    final static private int NUMBER_OF_TILES = 1;

    @BindView(R.id.rvProgressActivity)
    RecyclerView rvProgressActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        ButterKnife.bind(this);
        setupRecycler();
    }


    protected void setupRecycler() {
        ProgressAdapter adapter = new ProgressAdapter();
        rvProgressActivity.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_TILES);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(adapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        rvProgressActivity.setLayoutManager(layoutManager);
    }
}
