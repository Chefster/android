package com.codepath.chefster.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.codepath.chefster.R;
import com.codepath.chefster.fragments.FavoritesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DishListActivity extends BaseActivity implements FavoritesFragment.OnFavoritesInteractionListener {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_list);

        ButterKnife.bind(DishListActivity.this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Dishes");
        String categoryName = getIntent().getStringExtra("category_name");

        FavoritesFragment favoritesFragment = FavoritesFragment.newInstance();

        getSupportFragmentManager().beginTransaction().add(R.id.rlDishContainer, favoritesFragment).commit();

    }

    @Override
    public void onFavoritesFragmentInteraction(Uri uri) {

    }
}
