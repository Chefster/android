package com.codepath.chefster.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alorma.timeline.TimelineView;
import com.codepath.chefster.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tlStep)
    TimelineView timelineView;

/*    @BindView(R.id.tlStepEnd)
    TimelineView timelineViewEnd;*/

    @BindView(R.id.tvStep)
    TextView textViewStep;

  /*  public TimelineView getTimelineViewEnd() {
        return timelineViewEnd;
    }*/

    public TimelineView getTimelineView() {
        return timelineView;
    }

    public TextView getTextViewStep() {
        return textViewStep;
    }

    public StepViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }
}
