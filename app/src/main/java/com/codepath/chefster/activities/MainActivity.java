package com.codepath.chefster.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.chefster.R;
import com.codepath.chefster.Recipes;
import com.codepath.chefster.adapters.ViewPagerAdapter;
import com.codepath.chefster.fragments.FavoritesFragment;
import com.codepath.chefster.fragments.MainFragment;
import com.codepath.chefster.models.Dish;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        MainFragment.OnMainFragmentInteractionListener, FavoritesFragment.OnFavoritesInteractionListener {
    private static final String ANONYMOUS = "anonymousUser";

    @BindView(R.id.main_viewpager) ViewPager viewPager;
    @BindView(R.id.main_tab_layout) TabLayout tabLayout;

    // Firebase instance variables
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String mUsername;
    private String mPhotoUrl;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<Dish> dishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        handleUserLogIn();

        loadRecipesFromJson();
        setViewPager();
    }

    private void loadRecipesFromJson() {
        try {
            InputStream inputStream = this.getAssets().open("recipes.json");
            dishes = Recipes.fromInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MainFragment.newInstance("", ""), "Categories");
        adapter.addFragment(FavoritesFragment.newInstance("", ""), "Favorites");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void handleUserLogIn() {
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            mUsername = firebaseUser.getDisplayName();
            if (firebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = firebaseUser.getPhotoUrl().toString();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                firebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onFavoritesFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMainFragmentInteraction(Uri uri) {

    }
}