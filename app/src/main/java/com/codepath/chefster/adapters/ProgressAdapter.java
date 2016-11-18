package com.codepath.chefster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.codepath.chefster.models.Step;
import com.codepath.chefster.viewholders.ProgressItemViewHolder;
import com.codepath.chefster.R;

import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressItemViewHolder> {

    List<Step> steps;
    Context context;
    int index;
    int activeStep = 0;
    boolean isExpanded;

    public ProgressAdapter(List<Step> steps, Context context, int index) {
        this.steps = steps;
        this.context = context;
        this.index = index;
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
        if (position == activeStep) {
            holder.getMainLayout().setBackgroundResource(R.drawable.light_blue_background_black_border);
        } else {
            holder.getMainLayout().setBackgroundResource(R.drawable.white_background_black_border);
        }
        holder.getStepTypeTextView().setText(step.getType());
        holder.getStepDetailsButton().setText(isExpanded ? R.string.hide_details : R.string.show_details);
        holder.getStepDetailsButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((onStepInteractionListener) context).onDetailsButtonClick(index);
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size();
    }

    public interface onStepInteractionListener {
        void onStepDone(String dish, int step);
        void onDetailsButtonClick(int index);
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
