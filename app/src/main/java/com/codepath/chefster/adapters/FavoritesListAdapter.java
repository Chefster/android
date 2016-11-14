package com.codepath.chefster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Dish;

import java.util.List;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

    private Context context;
    private List<Dish> dishesList;

    public FavoritesListAdapter(Context context, List<Dish> recipes) {
        this.context = context;
        this.dishesList = recipes;
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
        Dish dish = dishesList.get(position);
        holder.tvMealTitle.setText(dish.getTitle());
        holder.tvMealSummary.setText(dish.getDescription());
        holder.tvCookingTime.setText("Est." + String.valueOf(dish.getPrep_time() + dish.getCooking_time()) + " mins");
        holder.tvMealRating.setText(String.valueOf(dish.getRating()));
        if (dish.getThumbnails() != null && !dish.getThumbnails().isEmpty()) {
            Glide.with(context).load(dish.getThumbnails().get(0)).into(holder.ivMealImage);
        }
    }

    @Override
    public int getItemCount() {
        if (dishesList != null && !dishesList.isEmpty()) {
            return dishesList.size();
        }
        return 0;
    }
}
