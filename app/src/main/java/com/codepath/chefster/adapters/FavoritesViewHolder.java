package com.codepath.chefster.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.chefster.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.tvTitle) TextView tvMealTitle;
    @BindView(R.id.tvSummary) TextView tvMealSummary;
    @BindView(R.id.tvCookingTime) TextView tvCookingTime;
    @BindView(R.id.tvMealRating) TextView tvMealRating;
    @BindView(R.id.ivMealImage) ImageView ivMealImage;
    @BindView(R.id.cbSelectDish) CheckBox cbSelectDish;

    public FavoritesViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }
}
