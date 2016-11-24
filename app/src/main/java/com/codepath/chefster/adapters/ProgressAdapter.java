package com.codepath.chefster.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Step;
import com.codepath.chefster.viewholders.ProgressItemViewHolder;
import com.codepath.chefster.R;

import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressItemViewHolder> {
    Dish dish;
    List<Step> steps;
    Context context;
    int index;
    boolean isExpanded;
    long[] timeLeft;
    boolean[] isRunning;

    public ProgressAdapter(List<Step> steps, Context context, int index, Dish dish) {
        this.steps = steps;
        this.context = context;
        this.index = index;
        this.dish = dish;
        timeLeft = new long[steps.size()];
        isRunning = new boolean[steps.size()];
        for (int i = 0; i < steps.size(); i++) {
            timeLeft[i] = steps.get(i).getDurationTime();
        }
    }

    @Override
    public ProgressItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.widget_step_progress, parent, false);
        return new ProgressItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProgressItemViewHolder holder, int position) {
        final Step step = steps.get(position);
        Glide.with(context).load(dish.getThumbnails().get(0)).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.getMainCardView().setBackground(drawable);
                }
            }
        });

        holder.getStepDishTextView().setText(step.getDishName());
        holder.getStepDescriptionTextView().setText(step.getDescription());
        switch (step.getStatus()) {
            case ACTIVE:
                holder.getPlayPauseStepButton().setVisibility(View.VISIBLE);
                holder.getFinishStepButton().setVisibility(View.VISIBLE);
                if (isRunning[position]) {
                    if (timeLeft[position] == 0) {
                        holder.getPlayPauseStepButton().setText("Done!");
                    } else {
                        String timeLeftStr = "" + timeLeft[position] + "m left";
                        holder.getPlayPauseStepButton().setText(String.valueOf(timeLeftStr));
                        holder.getPlayPauseStepButton().setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause, 0, 0, 0);
                    }
                } else {
                    holder.getPlayPauseStepButton().setText(R.string.start);
                    holder.getPlayPauseStepButton().setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play, 0, 0, 0);
                }
                break;
            case DONE:
                holder.getPlayPauseStepButton().setVisibility(View.GONE);
                holder.getFinishStepButton().setVisibility(View.GONE);
                break;
            default:
                holder.getPlayPauseStepButton().setVisibility(View.GONE);
                holder.getFinishStepButton().setVisibility(View.GONE);
        }

        holder.getStepTypeTextView().setText(step.getType());
        holder.getEstTimeTextView().setText("" + step.getDurationTime() + "mins");
        holder.getStepDetailsButton().setText(isExpanded ? R.string.less : R.string.more);
        holder.getStepDetailsButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OnStepInteractionListener) context).onDetailsButtonClick(index);
            }
        });
        holder.getFinishStepButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OnStepInteractionListener) context).onStepDone(step.getDishName(), step.getOrder());
            }
        });
        final int stepPosition = position;
        holder.getPlayPauseStepButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OnStepInteractionListener) context).onPausePlayButtonClick(step.getDishName(),
                        step.getOrder(),
                        timeLeft[stepPosition]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size();
    }

    public void setTimeLeftForStep(int step, long timeLeftForStep) {
        timeLeft[step] = timeLeftForStep;
    }

    public void setStepTimerRunning(int step, boolean isRunning) {
        this.isRunning[step] = isRunning;
    }

    public interface OnStepInteractionListener {
        void onStepDone(String dish, int step);
        void onPausePlayButtonClick(String dish, int step, long timeLeft);
        void onDetailsButtonClick(int index);
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
