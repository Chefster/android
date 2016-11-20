package com.codepath.chefster.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.chefster.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.tvTitle) public TextView tvMealTitle;
    @BindView(R.id.tvSummary) public TextView tvMealSummary;
    @BindView(R.id.tvCookingTime) public TextView tvCookingTime;
    @BindView(R.id.tvMealRating) public TextView tvMealRating;
    @BindView(R.id.ivMealImage) public ImageView ivMealImage;
    @BindView(R.id.ivfavorite) public ImageView ivfavorite;

    public FavoritesViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }
}
