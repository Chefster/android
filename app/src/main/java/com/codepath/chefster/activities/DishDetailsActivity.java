package com.codepath.chefster.activities;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.codepath.chefster.R;
import com.codepath.chefster.fragments.IngredientsFragment;
import com.codepath.chefster.fragments.ReviewFragment;
import com.codepath.chefster.fragments.StepsFragment;
import com.codepath.chefster.models.Dish;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.pierfrancescosoffritti.youtubeplayer.AbstractYouTubeListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView;

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

    @BindView(R.id.ivPlayer)
    ImageView ivPlayer;

    @BindView(R.id.videoPlayer)
    YouTubePlayerView videoPlayer;

    @BindView(R.id.detailsToolbar)
    Toolbar toolbar;

    @BindView(R.id.sliderDetail)
    SliderLayout sliderDetail;

    final static public String DISH_KEY = "selected_dish";
    final static private int FIRST = 0;
    final static private int SECOND = 1;
    final static private int THIRD = 2;

    private Dish dish;
    private IngredientsFragment ingredientsFragment;
    private ReviewFragment reviewFragment;
    private StepsFragment stepsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_details);

        ButterKnife.bind(this);

        ingredientsFragment = new IngredientsFragment();
        reviewFragment = new ReviewFragment();
        stepsFragment = new StepsFragment();

        dish = Parcels.unwrap(getIntent().getParcelableExtra(DISH_KEY));

        setSliderDetails();

        //Add support For ActionBar And collapsingToolbar
        toolbar.setTitle(dish.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(dish.getTitle());


        Glide.with(this).load(dish.getThumbnails().get(0)).into(ivDishDetails);

        if (!dish.getVideoUrl().isEmpty()) {
            videoManager();
        } else {
            Glide.with(this).load("").asBitmap().into(ivPlayer);
        }

        setViewPager();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void videoManager() {
        ivDishDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo();
            }
        });
        ivPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //playVideo();
                Intent intent = new Intent(getBaseContext(),VideoActivity.class);
                intent.putExtra("uri",Parcels.wrap(dish.getVideoUrl()));
                intent.putExtra("image",Parcels.wrap(dish.getThumbnail()));
                startActivity(intent);
            }
        });
    }

    public void playVideo() {
        videoPlayer.setVisibility(View.VISIBLE);
        ivPlayer.setVisibility(View.INVISIBLE);
        sliderDetail.setVisibility(View.INVISIBLE);
        videoPlayer.initialize(new AbstractYouTubeListener() {
            @Override
            public void onReady() {
                String videoUrl = dish.getVideoUrl();
                videoPlayer.loadVideo(videoUrl, 0);
            }
        }, true);
    }

    private void setViewPager() {
        DishDetailsPagerAdapter adapter = new DishDetailsPagerAdapter(getSupportFragmentManager());
        viewPagerDish.setAdapter(adapter);

        tabLayoutDish.setupWithViewPager(viewPagerDish);
    }

    // This Class is for Fragments Adapter.
    public class DishDetailsPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = {"STEPS", "INGREDIENTS", "REVIEWS"};

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
                bundle.putParcelableArrayList(getString(R.string.steps), (ArrayList) dish.getSteps());
                stepsFragment.setArguments(bundle);
                return stepsFragment;
            } else if (position == SECOND) {
                bundle.putParcelableArrayList(getString(R.string.ingredients), (ArrayList) dish.getIngredients());
                ingredientsFragment.setArguments(bundle);
                return ingredientsFragment;
            } else if (position == THIRD) {
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


    private void setSliderDetails() {
        if (dish.getThumbnails() != null) {
            for (String str : dish.getThumbnails()) {
                DefaultSliderView sliderView = new DefaultSliderView(this);
                //Glide.with(this).load(str).asBitmap().into((ImageView) sliderView.getView());
                sliderView.image(str);
                sliderDetail.addSlider(sliderView);
            }
            sliderDetail.startAutoCycle();
        }

    }
}