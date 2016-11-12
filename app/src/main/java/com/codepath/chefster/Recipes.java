package com.codepath.chefster;

import com.codepath.chefster.models.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Hezi Eliyahu on 11/11/2016.
 */

public class Recipes {

    public static ArrayList<Recipe> fromInputStream(InputStream inputStream) {
        ArrayList<Recipe> recipes = new ArrayList<>();

        String json = null;
        try {

            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            recipes = gson.fromJson(json,new TypeToken<ArrayList<Recipe>>(){}.getType());

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return  recipes;
    }
}
