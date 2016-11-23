package com.codepath.chefster;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ChefsterApplication extends Application {
    public static final String SELECTED_DISHES_KEY = "selected_dishes";

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-CondLight.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        // This instantiates DBFlow
        FlowManager.init(new FlowConfig.Builder(this).build());

        Stetho.initializeWithDefaults(this);
    }
}
