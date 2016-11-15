package com.codepath.chefster.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.codepath.chefster.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hezi Eliyahu on 12/11/2016.
 */

public class ProgressHeaderViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvHeaderTitle)
    TextView tvHeaderTitle;

    public TextView getTvHeaderTitle() {
        return tvHeaderTitle;
    }

    public ProgressHeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
