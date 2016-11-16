package com.codepath.chefster.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codepath.chefster.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewViewHolder  extends RecyclerView.ViewHolder {
    @BindView(R.id.recycler_view_horizontal) RecyclerView horizontalRecyclerView;

    public RecyclerViewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
