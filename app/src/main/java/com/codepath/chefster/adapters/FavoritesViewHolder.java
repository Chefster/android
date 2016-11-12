package com.codepath.chefster.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.chefster.R;

/**
 * Created by PRAGYA on 11/12/2016.
 */

public class FavoritesViewHolder extends RecyclerView.ViewHolder{

    public TextView tvMealTitle;
    public TextView tvMealSummary;
    public ImageView ivMealImage;
    public TextView tvCookingTime;
    public TextView tvMealRating;

    public FavoritesViewHolder(View itemView) {
        super(itemView);
        tvMealTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvMealSummary = (TextView) itemView.findViewById(R.id.tvSummary);
        tvCookingTime = (TextView) itemView.findViewById(R.id.tvCookingTime);
        tvMealRating = (TextView) itemView.findViewById(R.id.tvMealRating);
        ivMealImage = (ImageView) itemView.findViewById(R.id.ivMealImage);
    }
}
