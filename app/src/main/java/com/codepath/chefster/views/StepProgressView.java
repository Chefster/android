package com.codepath.chefster.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepProgressView extends CardView {
    @BindView(R.id.card_view_progress_item) CardView mainCardView;
    @BindView(R.id.relative_layout_step_item) RelativeLayout mainLayout;
    @BindView(R.id.text_view_dish_title) TextView stepDishTextView;
    @BindView(R.id.text_view_step_description) TextView stepDescriptionTextView;
    @BindView(R.id.text_view_step_type) TextView stepTypeTextView;
    @BindView(R.id.text_view_time_duration) TextView estTimeTextView;
    @BindView(R.id.button_step_details) Button stepDetailsButton;
    @BindView(R.id.button_play_pause_step) Button playPauseStepButton;
    @BindView(R.id.button_finish_step) Button finishStepButton;

    boolean isTimerRunning;
    OnStepProgressListener listener;
    Dish dish;
    Step step;

    public StepProgressView(Context context, Dish dish, Step step) {
        super(context);
        inflate(context, R.layout.widget_step_progress, this);
        ButterKnife.bind(this);
        listener = (OnStepProgressListener) context;
        this.step = step;
        this.dish = dish;

        Glide.with(context).load(dish.getThumbnails().get(0)).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mainCardView.setBackground(drawable);
                }
            }
        });

        stepDishTextView.setText(step.getDishName());
        stepDescriptionTextView.setText(step.getDescription());
        stepTypeTextView.setText(step.getType());
        estTimeTextView.setText("" + step.getDurationTime() + "mins");

    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        View parentScrollView = ((View)(getParent().getParent()));
//
//        if (parentScrollView!=null) {
//            // check the container of the container is an HorizontallScrollView
//            if (parentScrollView instanceof HorizontalScrollView) {
//                // Yes it is, so change width to HSV's width
//                widthMeasureSpec=parentScrollView.getMeasuredWidth();
//            }
//        }
//        setMeasuredDimension(300, heightMeasureSpec);
//    }

    public interface OnStepProgressListener {
        void finishStep(String dishName, int step);
        void pauseStep(String dishName, int step);
        void resumeStep(String dishName, int step);
    }
}
