package com.codepath.chefster.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
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
import butterknife.OnClick;

public class StepProgressView extends CardView {
    @BindView(R.id.card_view_progress_item) CardView mainCardView;
    @BindView(R.id.relative_layout_step_item) RelativeLayout mainLayout;
    @BindView(R.id.text_view_dish_title) TextView stepDishTextView;
    @BindView(R.id.text_view_step_description) TextView stepDescriptionTextView;
    @BindView(R.id.text_view_step_type) TextView stepTypeTextView;
    @BindView(R.id.text_view_time_duration) TextView estTimeTextView;
    @BindView(R.id.button_step_details) TextView stepDetailsButton;
    @BindView(R.id.button_play_pause_step) TextView playPauseStepButton;
    @BindView(R.id.button_finish_step) TextView finishStepButton;
    @BindView(R.id.pause_step_layout) ImageView pauseStepLayout;

    boolean isTimerRunning;
    long timeLeftInSeconds;
    CountDownTimer countDownTimer;
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
        timeLeftInSeconds = step.getDurationTime() * 60;

        Glide.with(context).load(dish.getThumbnails().get(0)).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mainCardView.setBackground(drawable);
                }
            }
        });

        setViewsText();

    }

    private void setViewsText() {
        stepDishTextView.setText(step.getDishName());
        stepDescriptionTextView.setText(step.getDescription());
        stepTypeTextView.setText(step.getType());
        estTimeTextView.setText("" + step.getDurationTime() + "m");
    }

    @OnClick(R.id.button_play_pause_step)
    public void toggleStepTimer() {
        if (isTimerRunning) {
            isTimerRunning = false;
            pauseStepLayout.setVisibility(VISIBLE);
            playPauseStepButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_play, 0, 0, 0);
            countDownTimer.cancel();
        } else {
            isTimerRunning = true;
            pauseStepLayout.setVisibility(GONE);

            playPauseStepButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause, 0, 0, 0);
            countDownTimer = new CountDownTimer(timeLeftInSeconds * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    timeLeftInSeconds = l / 1000;
                    playPauseStepButton.setText(getTimerFormat(timeLeftInSeconds));
                }

                @Override
                public void onFinish() {
                    playPauseStepButton.setText("Done!");
                    playPauseStepButton.setCompoundDrawables(null, null, null, null);
                }
            }.start();
        }
    }

    public String getTimerFormat(long totalSeconds) {
        int seconds = (int) (totalSeconds % 60);
        int minutes = (int) ((totalSeconds / 60) % 60);
        int hours   = (int) ((totalSeconds / 3600) % 24);
        String secondsStr = seconds < 10 ? "0" + seconds : String.valueOf(seconds);
        String minutesStr = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
        String hoursStr = hours < 10 ? "0" + hours : String.valueOf(hours);

        StringBuilder sb = new StringBuilder();
        sb.append(hoursStr).append(":").append(minutesStr).append(":").append(secondsStr);
        return sb.toString();
    }

    public interface OnStepProgressListener {
        void finishStep(String dishName, int step);
        void pauseStep(String dishName, int step);
        void resumeStep(String dishName, int step);
    }
}
