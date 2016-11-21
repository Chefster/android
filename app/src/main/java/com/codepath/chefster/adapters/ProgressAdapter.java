package com.codepath.chefster.adapters;

import android.content.Context;
import android.os.CountDownTimer;
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
    boolean isExpanded;
    long[] timeLeft;
    boolean[] isRunning;

    public ProgressAdapter(List<Step> steps, Context context, int index) {
        this.steps = steps;
        this.context = context;
        this.index = index;
        timeLeft = new long[steps.size()];
        isRunning = new boolean[steps.size()];
        for (int i = 0; i < steps.size(); i++) {
            timeLeft[i] = steps.get(i).getDurationTime();
        }
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
        switch (step.getStatus()) {
            case ACTIVE:
                holder.getMainLayout().setBackgroundResource(R.drawable.light_blue_background_black_border);
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
                holder.getMainLayout().setBackgroundResource(R.drawable.light_orange_background_black_border);
                holder.getPlayPauseStepButton().setVisibility(View.GONE);
                holder.getFinishStepButton().setVisibility(View.GONE);
                break;
            default:
                holder.getMainLayout().setBackgroundResource(R.drawable.white_background_black_border);
                holder.getPlayPauseStepButton().setVisibility(View.GONE);
                holder.getFinishStepButton().setVisibility(View.GONE);
        }

        holder.getStepTypeTextView().setText(step.getType());
        holder.getEstTimeTextView().setText("" + step.getDurationTime() + "mins");
        holder.getStepDetailsButton().setText(isExpanded ? R.string.show_less : R.string.show_more);
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
