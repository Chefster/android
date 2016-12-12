package com.codepath.chefster;

import android.app.Application;
import android.content.Context;

import com.codepath.chefster.models.Ingredient;
import com.codepath.chefster.utils.LocalStorage;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ChefsterApplication extends Application {
    public static final String SELECTED_DISHES_KEY = "selected_dishes";

    private static Context context;
    private static LocalStorage localStorage;
    public static HashMap<Ingredient, Double> shoppingList;

    public ChefsterApplication() {
        context = this;
        localStorage = new LocalStorage();
    }

    @Override
    public void onCreate() {
        super.onCreate();

/*        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/

        // This instantiates DBFlow
        FlowManager.init(new FlowConfig.Builder(this).build());

        Stetho.initializeWithDefaults(this);
    }

    public static Context getAppContext() {
        return ChefsterApplication.context;
    }

    public static LocalStorage getLocalStorage() {
        return ChefsterApplication.localStorage;
    }
}
