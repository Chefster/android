package com.codepath.chefster.adapters;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.codepath.chefster.models.Step;
import com.codepath.chefster.viewholders.ProgressItemViewHolder;
import com.codepath.chefster.R;
import com.dd.CircularProgressButton;

import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressItemViewHolder> {

    List<Step> steps;
    Context context;
    int activeStep = 0;

    public ProgressAdapter(List<Step> steps, Context context) {
        this.steps = steps;
        this.context = context;
    }

    public void setActiveStep(int activeStep) {
        this.activeStep = activeStep;
    }

    @Override
    public ProgressItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_progress_item, parent, false);
        return new ProgressItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProgressItemViewHolder holder, int position) {
        final Step step = steps.get(position);
        holder.getStepDishTextView().setText(step.getDishName());
        holder.getStepDescriptionTextView().setText(step.getDescription());
        holder.getStepTypeTextView().setText(step.getType());
    }

    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size();
    }

    public interface onStepDoneListener {
        void onStepDone(int position);
    }
}
