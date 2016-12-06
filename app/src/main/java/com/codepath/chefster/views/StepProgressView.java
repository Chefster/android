package com.codepath.chefster.views;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
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
    @BindView(R.id.text_view_running_timer) TextView runningTimerTextView;
    @BindView(R.id.button_step_details) ImageView stepDetailsButton;
    @BindView(R.id.button_play_pause_step) ImageView playPauseStepButton;
    @BindView(R.id.button_finish_step) TextView finishStepButton;
    @BindView(R.id.pause_step_layout) ImageView pauseStepLayout;

    boolean isExpanded;
    boolean isTimerRunning;
    long timeLeftInSeconds;
    CountDownTimer countDownTimer;
    OnStepProgressListener listener;
    Dish dish;
    Step step;
    TextToSpeech tts;
    boolean shouldUseVoiceHelp;

    public StepProgressView(Context context) {
        this(context, null, null, null);
    }

    public StepProgressView(Context context, Dish dish, Step step, TextToSpeech tts) {
        super(context);
        inflate(context, R.layout.widget_step_progress, this);
        ButterKnife.bind(this);

        listener = (OnStepProgressListener) context;
        this.step = step;
        this.dish = dish;
        this.tts = tts;
        shouldUseVoiceHelp = true;
        timeLeftInSeconds = step.getDurationTime() * 60;

        Glide.with(context).load(dish.getThumbnails().get(0)).asBitmap().animate(R.anim.image_zoom_in).into(new SimpleTarget<Bitmap>() {
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
        if (step.getDurationTime() != 0) {
            runningTimerTextView.setText(getTimerFormat(step.getDurationTime() * 60));
            runningTimerTextView.setVisibility(VISIBLE);
        } else {
            runningTimerTextView.setVisibility(GONE);
        }
    }

    @OnClick(R.id.button_play_pause_step)
    public void toggleStepTimer() {
        if (isTimerRunning) {
            isTimerRunning = false;
            pauseStepLayout.setVisibility(VISIBLE);
            playPauseStepButton.setImageResource(R.drawable.ic_play);
            countDownTimer.cancel();
        } else {
            isTimerRunning = true;
            pauseStepLayout.setVisibility(GONE);
            playPauseStepButton.setImageResource(R.drawable.ic_pause);
            countDownTimer = new CountDownTimer(/*timeLeftInSeconds */ 5 * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    timeLeftInSeconds = l / 1000;
                    runningTimerTextView.setText(getTimerFormat(timeLeftInSeconds));
                }

                @Override
                public void onFinish() {
                    runningTimerTextView.setText("Done!");
                    tts.speak(step.getDishName() + " timer is done", TextToSpeech.QUEUE_FLUSH, null);
                    playPauseStepButton.setVisibility(GONE);
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
        String hoursStr = hours > 0 ? String.valueOf(hours) + ":" : "";

        StringBuilder sb = new StringBuilder();
        sb.append(hoursStr).append(minutesStr).append(":").append(secondsStr);
        return sb.toString();
    }

    public void setStepStatus(Step.Status status) {
        step.setStatus(status);

        switch (step.getStatus()) {
            case READY:
                mainLayout.setBackgroundResource(R.color.white_translucent);
                finishStepButton.setVisibility(GONE);
                playPauseStepButton.setVisibility(GONE);
                break;
            case ACTIVE:
                mainLayout.setBackgroundResource(R.color.light_blue_translucent);
                finishStepButton.setVisibility(VISIBLE);
                if (shouldUseVoiceHelp) {
                    String text = "now, for " + step.getDishName() + ", a " + step.getDurationTime() + " minutes step, " + step.getDescription();
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
                if (step.getDurationTime() != 0) {
                    playPauseStepButton.setVisibility(VISIBLE);
                }
                break;
            case DONE:
                mainLayout.setBackgroundResource(R.color.light_orange_translucent);
                playPauseStepButton.setVisibility(GONE);
                finishStepButton.setVisibility(GONE);
        }
    }

    @OnClick(R.id.button_finish_step)
    public void onFinishStepClick() {
        if (isExpanded) {
            expandStepItem();
        }

        // We don't want to show the dialog on every step. Only long cooking steps where
        // the user might be able to keep doing things while a long not busy step is happening
        if (step.getType().equals("Prep") || (step.getType().equals("Cook") && step.getDurationTime() < 8)) {
            setStepStatus(Step.Status.DONE);
            countDownTimer = null;
            listener.showNextStep(step.getDishName(), step.getOrder(), true);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.finish_step_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            setStepStatus(Step.Status.DONE);
                            countDownTimer = null;
                            listener.showNextStep(step.getDishName(), step.getOrder(), true);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            listener.showNextStep(step.getDishName(), step.getOrder(), false);
                        }
                    });
            builder.create().show();
        }
    }

    @OnClick(R.id.button_step_description_audio)
    public void playStepDescription() {
        tts.speak(step.getDescription(), TextToSpeech.QUEUE_FLUSH, null);
    }

    @OnClick(R.id.button_step_details)
    public void expandStepItem() {
        isExpanded = !isExpanded;
        if (isExpanded) {
            stepDetailsButton.setImageResource(R.drawable.ic_less);
            stepDescriptionTextView.setMaxLines(12);
        } else {
            stepDetailsButton.setImageResource(R.drawable.ic_more);
            stepDescriptionTextView.setMaxLines(2);
        }
        listener.expandStepItem(step.getDishName(), isExpanded);
    }

    public void setShouldUseVoiceHelp(boolean shouldUseVoiceHelp) {
        this.shouldUseVoiceHelp = shouldUseVoiceHelp;
    }

    public interface OnStepProgressListener {
        void showNextStep(String dishName, int step, boolean isFinished);
        void pauseStep(String dishName, int step);
        void resumeStep(String dishName, int step);
        void expandStepItem(String dishName, boolean expand);
    }
}
