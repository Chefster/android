package com.codepath.chefster.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.codepath.chefster.ChefsterApplication;

import java.util.HashSet;
import java.util.Set;

public class LocalStorage {
    private static final String FAVORITED_DISHES = "favoritedDishes";
    private Context context = ChefsterApplication.getAppContext();
    private SharedPreferences sharedPreferences;

    private void putInt(String key, int value) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private int getInt(String key, int defaultValue) {
        return getPrefs().getInt(key, defaultValue);
    }

    /**
     * Memoization of retrieving the default SharedPreferences from the preference manager
     * @return The default SharedPreferences
     */
    private SharedPreferences getPrefs() {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sharedPreferences;
    }

    @NonNull
    private Set<String> getStringSet(String key) {
        // We create a new HashSet object because the original Set returned by getStringSet
        // MUST NOT be modified, as per the docs below:
        // http://developer.android.com/reference/android/content/SharedPreferences.html
        return new HashSet<>(getPrefs().getStringSet(key, new HashSet<String>()));
    }

    /**
     * @return The set after being modified
     */
    private synchronized Set<String> addStringToSet(@NonNull String key, @NonNull String s) {
        Set<String> stringSet = getStringSet(key);
        stringSet.add(s);
        getPrefs().edit().putStringSet(key, stringSet).apply();
        return stringSet;
    }

    /**
     * @return The set after being modified
     */
    private synchronized Set<String> removeStringFromSet(@NonNull String key, @NonNull String s) {
        Set<String> stringSet = getStringSet(key);
        stringSet.remove(s);
        getPrefs().edit().putStringSet(key, stringSet).apply();
        return stringSet;
    }

    public boolean isDishFavorited(int dishId) {
        Set<String> favoritedSet = getStringSet(FAVORITED_DISHES);
        return favoritedSet.contains(String.valueOf(dishId));
    }

    public void favoriteDish(Integer uid) {
        addStringToSet(FAVORITED_DISHES, String.valueOf(uid));
    }

    public void unfavoriteDish(Integer uid) {
        removeStringFromSet(FAVORITED_DISHES, String.valueOf(uid));
    }
}
