package com.codepath.chefster.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.codepath.chefster.R;
import com.codepath.chefster.models.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewViewHolder  extends RecyclerView.ViewHolder {
    @BindView(R.id.recycler_view_horizontal) public RecyclerView horizontalRecyclerView;

    public RecyclerViewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
