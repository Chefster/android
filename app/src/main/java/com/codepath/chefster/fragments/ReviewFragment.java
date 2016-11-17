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
import com.codepath.chefster.models.Review;
import com.codepath.chefster.utils.SpacesItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 *  Review Fragment - for Displaying all the reviews of specific dish.
 */

public class ReviewFragment extends BaseFragment {
    @BindView(R.id.rvReviews)
    RecyclerView rvReviews;

    final static private String ARGUMENT = "reviews";

    private List<Review> reviewList;
    private ReviewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        reviewList = getArguments().getParcelableArrayList("reviews");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout
        final View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        ButterKnife.bind(this, view);

        initializeRecyclerView();

        return view;
    }

    private void initializeRecyclerView() {
        adapter = new ReviewAdapter(getActivity(),reviewList);
        rvReviews.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rvReviews.setLayoutManager(manager);
    }
}
