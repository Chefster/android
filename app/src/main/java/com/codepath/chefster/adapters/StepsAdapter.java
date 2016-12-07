package com.codepath.chefster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alorma.timeline.TimelineView;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Step;
import com.codepath.chefster.viewholders.StepViewHolder;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepViewHolder> {
    final static private int MIN_HEIGHT = 380;

    private Context context;
    private List<Step> stepList;
    private int timelineLimit;

    public StepsAdapter(Context context, List<Step> stepArrayList) {
        this.context = context;
        this.stepList = stepArrayList;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.step_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {

        Step step = stepList.get(position);

        holder.getTextViewStep().setText(step.getDescription());

        // Determine the Height of The TimeLine To be Like The Height of the textView
        int tvHeight = getHeight(holder.getTextViewStep(),step.getDescription(),18,850,5);
        ViewGroup.LayoutParams tlParams =  holder.getTimelineView().getLayoutParams();
        tlParams.height = tvHeight;
        holder.getTimelineView().setLayoutParams(tlParams);

        // the last position will change to Timeline End Point
        if (position == stepList.size() -1 )
            holder.getTimelineView().determineTimelineType(position,stepList.size());
    }

    // This Method return the Height of The TextView after setText.
    public static int getHeight(TextView textView, CharSequence text, int textSize, int deviceWidth, int padding) {
        textView.setPadding(padding,0,padding,padding);
        textView.setText(text, TextView.BufferType.SPANNABLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        if (textView.getMeasuredHeight() < MIN_HEIGHT)
            return MIN_HEIGHT;
        else
            return textView.getMeasuredHeight();
    }

    @Override
    public int getItemCount() {
        if (stepList != null && !stepList.isEmpty()) {
            return stepList.size();
        }
        return 0;
    }
}
