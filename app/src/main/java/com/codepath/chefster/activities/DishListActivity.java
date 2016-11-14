package com.codepath.chefster.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.codepath.chefster.R;
import com.codepath.chefster.fragments.FavoritesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DishListActivity extends AppCompatActivity implements FavoritesFragment.OnFavoritesInteractionListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
//    @BindView(R.id.rvDishContainer)
//    RecyclerView rvDishContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_list);

        ButterKnife.bind(DishListActivity.this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Dishes");
        String categoryName = getIntent().getStringExtra("category_name");

        FavoritesFragment favoritesFragment = FavoritesFragment.newInstance("","");

        getSupportFragmentManager().beginTransaction().add(R.id.rlDishContainer, favoritesFragment).commit();

    }

    @OnClick(R.id.fab)
    public void setFabOnClickListner(View v){
        Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onFavoritesFragmentInteraction(Uri uri) {

    }
}
