package com.codepath.chefster.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.chefster.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hezi Eliyahu on 16/11/2016.
 */

public class IngredientsViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvIngredient)
    TextView tvIngredient;

    @BindView(R.id.tvIngredientAmount)
    TextView tvIngredientAmount;

    @BindView(R.id.ivIngredients)
    ImageView ivIngredients;

    public TextView getTvIngredient() {
        return tvIngredient;
    }

    public TextView getTvIngredientAmount() { return tvIngredientAmount; }

    public ImageView getIvIngredients() {
        return ivIngredients;
    }

    public IngredientsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}