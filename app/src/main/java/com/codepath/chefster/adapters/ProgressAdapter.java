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
    boolean isDoneList;

    public ProgressAdapter(List<Step> steps, Context context, boolean isDoneList) {
        this.steps = steps;
        this.context = context;
        this.isDoneList = isDoneList;
    }

    @Override
    public ProgressItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_progress_item, parent, false);
        return new ProgressItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProgressItemViewHolder holder, final int position) {
        final Step step = steps.get(position);
        holder.getStepDishTextView().setText(step.getDishName());
        holder.getStepDescriptionTextView().setText(step.getDescription());
        holder.getStepTypeTextView().setText(step.getType());
        String estTime = "Est.\n" + step.getDurationTime() + " mins";
        if (isDoneList) {
            holder.getCircularProgressButton().setVisibility(View.GONE);
        } else {
            holder.getCircularProgressButton().setText(estTime);
            holder.getCircularProgressButton().setVisibility(View.VISIBLE);
            // Get The Animator set his Values
            final ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);

            holder.getCircularProgressButton().setBackgroundColor(Color.DKGRAY);
            holder.getCircularProgressButton().setStrokeColor(Color.RED);
            holder.getCircularProgressButton().setIndeterminateProgressMode(true);
            holder.getCircularProgressButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int buttonProgress = holder.getCircularProgressButton().getProgress();
                    if (buttonProgress == 0) {
                        simulateSuccessProgress(widthAnimation, holder.getCircularProgressButton(), step.getDurationTime());
                    } else if (buttonProgress < 100) {
                        if (widthAnimation.isRunning() && ! widthAnimation.isPaused())
                            widthAnimation.pause();
                        else if (widthAnimation.isPaused())
                            widthAnimation.resume();
                    }
                    else {
                        ((onStepDoneListener) context).onStepDone(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
//        if (isDoneList) {
//            return steps.size() > 3 ? 3 : steps.size();
//        }
        return steps.size();
    }

    private void simulateSuccessProgress(ValueAnimator widthAnimation, final CircularProgressButton button, int durationInMinutes) {
        widthAnimation.setDuration(durationInMinutes * 60 * 1000);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
            }
        });
        widthAnimation.start();
    }

    public interface onStepDoneListener {
        void onStepDone(int position);
    }
}
