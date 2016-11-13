package com.codepath.chefster.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.codepath.chefster.R;
import com.codepath.chefster.models.Step;

import java.util.List;

import butterknife.BindDrawable;

public class StepsAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @BindDrawable(R.drawable.transparent_background_blue_border) Drawable activeBackground;
    @BindDrawable(R.drawable.transparent_background_red_border) Drawable pastBackground;

    List<Step> steps;
    Context context;
    OnActiveListInteraction listener;
    int colorId;
    boolean isDoneStepsAdapter;

    public StepsAdapter(List<Step> steps, Context context, boolean isDoneStepsAdapter, OnActiveListInteraction listener) {
        this.steps = steps;
        this.context = context;
        this.listener = listener;
        this.isDoneStepsAdapter = isDoneStepsAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        // For the done tasks list, don't show more than 3 last steps, and otherwise, show as many
        // steps possible
        if (isDoneStepsAdapter && steps.size() > 3) {
            return 3;
        } else {
            return steps.size();
        }
    }

    public interface OnActiveListInteraction {
        void onStepStarted();
        void onStepDone();
    }
}
