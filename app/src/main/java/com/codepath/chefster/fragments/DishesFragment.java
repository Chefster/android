package com.codepath.chefster.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.R;
import com.codepath.chefster.adapters.DishesAdapter;
import com.codepath.chefster.client.FirebaseClient;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.utils.LocalStorage;
import com.codepath.chefster.utils.RecyclerViewVerticalSpacing;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DishesFragment extends BaseFragment {
    private static final String CATEGORY_KEY = "category";

    @BindView(R.id.recycler_view_dishes) RecyclerView dishesRecyclerView;

    private List<Dish> dishList;
    private DishesAdapter adapter;
    LocalStorage localStorage;
    String category;

    public DishesFragment() {
        // Required empty public constructor
    }

    public static DishesFragment newInstance(String category) {
        DishesFragment fragment = new DishesFragment();
        Bundle args = new Bundle();
        args.putString(CATEGORY_KEY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(CATEGORY_KEY);
        }
        localStorage = ChefsterApplication.getLocalStorage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dishes, container, false);

        ButterKnife.bind(this, view);

        dishList = new ArrayList<>();
        dishList.clear();
        dishList.addAll(FirebaseClient.getDishes(category));
        initializeRecyclerView();
        return view;
    }

    private void initializeRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());

        dishesRecyclerView.setLayoutManager(manager);
        adapter = new DishesAdapter(getActivity(), dishList, category);
        dishesRecyclerView.setAdapter(adapter);
        dishesRecyclerView.addItemDecoration(new RecyclerViewVerticalSpacing(30, false));
        adapter.notifyDataSetChanged();
    }
}
