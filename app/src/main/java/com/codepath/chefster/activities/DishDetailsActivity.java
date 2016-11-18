package com.codepath.chefster.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.chefster.R;
import com.codepath.chefster.fragments.IngredientsFragment;
import com.codepath.chefster.fragments.ReviewFragment;
import com.codepath.chefster.models.Dish;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DishDetailsActivity extends BaseActivity {
    @BindView(R.id.vpDish)
    ViewPager viewPagerDish;

    @BindView(R.id.tlDish)
    TabLayout tabLayoutDish;

    @BindView(R.id.ivDishDetails)
    ImageView ivDishDetails;

    final static public String DISH_KEY = "selected_dish";
    final static private int FIRST = 0;
    final static private int SECOND = 1;

    private Dish dish;
    private IngredientsFragment ingredientsFragment;
    private ReviewFragment reviewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_details);

        ButterKnife.bind(this);

        ingredientsFragment = new IngredientsFragment();
        reviewFragment = new ReviewFragment();

        dish = Parcels.unwrap(getIntent().getParcelableExtra(DISH_KEY));

        Glide.with(this).load(dish.getThumbnails().get(0)).into(ivDishDetails);

        setViewPager();
    }

    private void setViewPager() {
        DishDetailsPagerAdapter adapter = new DishDetailsPagerAdapter(getSupportFragmentManager());
        viewPagerDish.setAdapter(adapter);

        tabLayoutDish.setupWithViewPager(viewPagerDish);
    }

    // This Class is for Fragments Adapter.
    public class DishDetailsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {getString(R.string.ingredients), getString(R.string.reviews)};

        public DishDetailsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            if (position == FIRST) {
                bundle.putParcelableArrayList(getString(R.string.ingredients), (ArrayList) dish.getIngredients());
                ingredientsFragment.setArguments(bundle);
                return ingredientsFragment;
            } else if (position == SECOND) {
                bundle.putParcelableArrayList(getString(R.string.reviews), (ArrayList) dish.getReviews());
                reviewFragment.setArguments(bundle);
                return reviewFragment;
            } else
                return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}