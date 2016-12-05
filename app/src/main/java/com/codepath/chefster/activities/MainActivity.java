package com.codepath.chefster.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.chefster.R;
import com.codepath.chefster.adapters.DishesAdapter;
import com.codepath.chefster.adapters.ViewPagerAdapter;
import com.codepath.chefster.client.FirebaseClient;
import com.codepath.chefster.fragments.ContainerFragment;
import com.codepath.chefster.fragments.DishesFragment;
import com.codepath.chefster.fragments.MainFragment;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Dish_Table;
import com.codepath.chefster.models.Dishes;
import com.codepath.chefster.models.User;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.parceler.Parcels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        DishesAdapter.DishesInteractionListener,
        MainFragment.CategoryClickedListener {
    private static final String ANONYMOUS = "anonymousUser";
    final static public String DISH_KEY = "selected_dish";
    private static final String CATEGORY_KEY = "category_name";

    @BindView(R.id.main_viewpager) ViewPager viewPager;
    @BindView(R.id.main_tab_layout) TabLayout tabLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.button_items_on_list) Button itemsOnListButton;
    @BindView(R.id.autoComplete) AutoCompleteTextView autocompleteView;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;

    // Firebase instance variables
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String mUsername;
    private String mPhotoUrl;
    private GoogleApiClient mGoogleApiClient;

    ViewPagerAdapter adapter;
    private DatabaseReference mDatabase;
    private Dishes dishes;
    List<Dish> search_result;
    public List<Dish> selectedDishes;
    private FirebaseClient firebaseClient;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
     //   getSupportActionBar().setIcon(R.mipmap.ic_launcher);
 /*       mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this *//* FragmentActivity *//*, this /* OnConnectionFailedListener *//*)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
                */
        handleUserLogIn();


        drawerInit();
        // Use this call only if you have new Data stored in .json and you want to update the DB.
      //   loadDataToDatabase();
        // Get all dishes from database.
        loadDishes();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
            requestPermissions(permissions, 0);
        }
    }

    public void drawerInit(){

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("user", Parcels.wrap(user));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplication().startActivity(intent);
                break;
         }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // using this Method will upload new .json to DataBase and Overwrite the tree.
    private void loadDataToDatabase() {
        firebaseClient = new FirebaseClient(this);
        firebaseClient.uploadNewDataToDatabase();
    }

    public void loadDishes() {
        dishes = new Dishes();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("dishes");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dishSnapshot : dataSnapshot.getChildren()) {
                            Dish dish = dishSnapshot.getValue(Dish.class);
                            dish.save();
                            dishes.addDish(dish);
                        }
                        FirebaseClient.setDishes(dishes.getDishes());
                        setViewPager();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

        selectedDishes = new ArrayList<>();
    }

    private void setViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        adapter.setFragment(ContainerFragment.newInstance(), 0);
        adapter.setFragment(DishesFragment.newInstance(null), 1);
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
                LoginManager.getInstance().logOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            case R.id.action_search:
                autocompleteView.setVisibility(View.VISIBLE);
                autocompleteView.requestFocus();
                autocompleteView.setCursorVisible(true);
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(autocompleteView ,
                        InputMethodManager.SHOW_IMPLICIT);
                searchViewHandler("");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);

        user = FirebaseClient.getUserInformation();
        userHandler(user);

        return true;
    }

    /*
    *   This Method Handle the user profile
    */
    private void userHandler(User user) {
        if ( user != null) {
            final ImageView ivHeader = (ImageView) findViewById(R.id.ivHeader);
            TextView tvHeaderName = (TextView) findViewById(R.id.tvHeaderName);
            TextView tvHeaderEmail = (TextView) findViewById(R.id.tvHeaderEmail);

            if ( user.getFirstName() != null)
                tvHeaderName.setText(user.getFirstName());
            tvHeaderEmail.setText(user.getEmail());

            if (user.getImageUrl() != "" ) {
                Glide.with(this).load(user.getImageUrl()).asBitmap().centerCrop()
                        .placeholder(R.drawable.profile_avatar_placeholder_large)
                        .into(new BitmapImageViewTarget(ivHeader) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivHeader.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            }
        }
    }

    /*
    * this method handle the request from the searchView,
    * getting the query from the database and displaying the result.
    */
    public void searchViewHandler(String newText) {

        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<Dish> selectedDish = SQLite.select().from(Dish.class)
                        .where(Dish_Table.title.like("%" + adapterView.getItemAtPosition(i) + "%")).queryList();
                if (!selectedDish.isEmpty()) {
                    Dish currentDish = selectedDish.get(0);

                    // TODO - Add Lists to Table - DBFlow don't support Lists.
                    for (Dish dish : dishes.getDishes()) {
                        if (dish.getUid() == currentDish.getUid()) {
                            currentDish = dish;
                        }
                    }

                    autocompleteView.setText("");
                    autocompleteView.setVisibility(View.GONE);

                    Intent intent = new Intent(getApplication(), DishDetailsActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(DISH_KEY, Parcels.wrap(currentDish));
                    getApplication().startActivity(intent);

                }
            }
        });


        autocompleteView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                autocompleteView.setCursorVisible(true);
                autocompleteView.setSelection(charSequence.length());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                autocompleteView.setCursorVisible(true);
                autocompleteView.setSelection(charSequence.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        autocompleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autocompleteView.setVisibility(View.GONE);
                autocompleteView.setText("");
            }
        });

        search_result = SQLite.select().from(Dish.class)
                .where(Dish_Table.title.like("%" + newText + "%")).queryList();
        String[] strings = new String[search_result.size()];
        for (int i = 0; i < search_result.size(); i++) {
            strings[i] = search_result.get(i).getTitle();
        }
        int layoutItemId = android.R.layout.simple_dropdown_item_1line;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, layoutItemId, Arrays.asList(strings));

        autocompleteView.setAdapter(adapter);

        autocompleteView.setFocusable(true);
        autocompleteView.requestFocus();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnClick(R.id.button_items_on_list)
    public void startCooking() {
        if (selectedDishes == null || selectedDishes.isEmpty()) {
            Toast.makeText(this, "There are 0 dishes on your list!", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, MealLaunchActivity.class);
            intent.putExtra("selected_dishes", Parcels.wrap(selectedDishes));
            startActivity(intent);
        }
    }

    @Override
    public void onDishLiked() {
        adapter.replaceFragment(DishesFragment.newInstance(null), 1);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDishAdded(Dish dish) {
        if (selectedDishes.size() == 3) {
            Toast.makeText(this, "Can't cook more than 3 dishes with this version", Toast.LENGTH_SHORT).show();
        } else {
            if (!selectedDishes.contains(dish)) {
                selectedDishes.add(dish);
            }
            itemsOnListButton.setText("Continue (" + selectedDishes.size() + " items)");
            adapter.replaceFragment(DishesFragment.newInstance(null), 1);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDishRemoved(Dish dish) {
        selectedDishes.remove(dish);
        itemsOnListButton.setText("Dishes On List (" + selectedDishes.size() + ")");
        adapter.replaceFragment(DishesFragment.newInstance(null), 1);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCategoryclicked(String category) {
        adapter.replaceFragment(DishesFragment.newInstance(category), 0);
        adapter.notifyDataSetChanged();
    }
}