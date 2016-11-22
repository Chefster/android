package com.codepath.chefster.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.chefster.R;
import com.codepath.chefster.adapters.ReviewAdapter;
import com.codepath.chefster.adapters.StepsAdapter;
import com.codepath.chefster.models.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsFragment extends BaseFragment {
    @BindView(R.id.rvSteps)
    RecyclerView rvSteps;

    final static private String ARGUMENT = "steps";

    private List<Step> stepsList;
    private StepsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stepsList = (List) getArguments().getParcelableArrayList(ARGUMENT);
        getArguments().remove(ARGUMENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout
        final View view = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, view);

        initializeRecyclerView();

        return view;
    }

    private void initializeRecyclerView() {
        adapter = new StepsAdapter(getActivity(),stepsList);
        rvSteps.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rvSteps.setLayoutManager(manager);
    }
}
