package com.codepath.chefster.Database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = ChefsterDatabase.NAME, version = ChefsterDatabase.VERSION)
public class ChefsterDatabase {

    public static final String NAME = "ChefsterDatabase";

    public static final int VERSION = 2;
}
