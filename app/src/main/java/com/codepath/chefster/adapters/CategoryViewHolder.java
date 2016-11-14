package com.codepath.chefster.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.chefster.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PRAGYA on 11/12/2016.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.ivCategoryImage)
    ImageView ivCategory;
    @BindView(R.id.tvCategoryName) TextView tvCategoryName;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
