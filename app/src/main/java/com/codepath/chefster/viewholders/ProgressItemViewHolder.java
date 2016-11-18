package com.codepath.chefster.viewholders;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.codepath.chefster.R;
import com.dd.CircularProgressButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressItemViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.text_view_dish_title) TextView stepDishTextView;
    @BindView(R.id.text_view_step_description) TextView stepDescriptionTextView;
    @BindView(R.id.text_view_step_type) TextView stepTypeTextView;

    ValueAnimator widthAnimation;

    public ProgressItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getStepDishTextView() {
        return stepDishTextView;
    }

    public TextView getStepDescriptionTextView() {
        return stepDescriptionTextView;
    }

    public TextView getStepTypeTextView() {
        return stepTypeTextView;
    }

    public ValueAnimator getWidthAnimation() {
        return widthAnimation;
    }
}
