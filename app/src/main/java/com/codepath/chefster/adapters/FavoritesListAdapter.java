package com.codepath.chefster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Recipe;

import java.util.List;

/**
 * Created by PRAGYA on 11/12/2016.
 */

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesViewHolder>{

    private Context context;
    private List<Recipe> recipeList;

    public FavoritesListAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipeList = recipes;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View favView = layoutInflater.inflate(R.layout.favorite_item, parent, false);
        //Return the a new Holder instance
        FavoritesViewHolder viewHolder = new FavoritesViewHolder(favView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.tvMealTitle.setText(recipe.getTitle());
        holder.tvMealSummary.setText(recipe.getDescription());
        holder.tvCookingTime.setText(String.valueOf(recipe.getTotalDurationTime()) + "");
        holder.tvMealRating.setText(String.valueOf(recipe.getRating()));
        if (recipe.getThumbnails() != null && !recipe.getThumbnails().isEmpty()) {
            Glide.with(context).load(recipe.getThumbnails().get(0)).into(holder.ivMealImage);
        }
    }

    @Override
    public int getItemCount() {
        if (recipeList != null && !recipeList.isEmpty()) {
            return recipeList.size();
        }
        return 0;
    }
}
