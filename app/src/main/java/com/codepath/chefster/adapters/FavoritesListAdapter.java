package com.codepath.chefster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.viewholders.FavoritesViewHolder;

import java.util.ArrayList;
import java.util.List;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

    private Context context;
    private List<Dish> dishesList;
    private List<Dish> selectedDishesList;

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
    public void onBindViewHolder(final FavoritesViewHolder holder, int position) {
        final Dish dish = dishesList.get(position);

        selectedDishesList = new ArrayList<>();
        holder.tvMealTitle.setText(dish.getTitle());
        holder.tvMealSummary.setText(dish.getDescription());
        holder.tvCookingTime.setText("Est." + String.valueOf(dish.getPrep_time() + dish.getCooking_time()) + " mins");
        holder.tvMealRating.setText(String.valueOf(dish.getRating()));
        if (dish.getThumbnails() != null && !dish.getThumbnails().isEmpty()) {
            Glide.with(context).load(dish.getThumbnails().get(0)).into(holder.ivMealImage);
        }


        holder.cbSelectDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.cbSelectDish.isChecked()){
                    selectedDishesList.add(dish);
                }else {
                    selectedDishesList.remove(dish);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (dishesList != null && !dishesList.isEmpty()) {
            return dishesList.size();
        }
        return 0;
    }

    public List<Dish> getSelectedDishesList() {
        return selectedDishesList;
    }
}
