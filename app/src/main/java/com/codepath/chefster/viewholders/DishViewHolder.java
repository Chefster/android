package com.codepath.chefster.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.chefster.R;
import com.like.LikeButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DishViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.tvTitle) public TextView tvMealTitle;
    @BindView(R.id.tvSummary) public TextView tvMealSummary;
    @BindView(R.id.tvCookingTime) public TextView tvCookingTime;
    @BindView(R.id.tvMealRating) public TextView tvMealRating;
    @BindView(R.id.ivMealImage) public ImageView ivMealImage;
    @BindView(R.id.ivfavorite) public LikeButton ivfavorite;
    @BindView(R.id.btnAddToMenu) public Button btnAddToMenu;

    public DishViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }
}
