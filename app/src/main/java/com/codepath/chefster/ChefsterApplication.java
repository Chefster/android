package com.codepath.chefster;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ChefsterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-CondLight.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
