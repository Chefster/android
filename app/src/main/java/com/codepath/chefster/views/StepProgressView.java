package com.codepath.chefster.views;

import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.chefster.R;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Step;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepProgressView extends CardView {
    @BindView(R.id.card_view_progress_item) CardView mainCardView;
    @BindView(R.id.relative_layout_step_item) RelativeLayout mainLayout;
    @BindView(R.id.text_view_step_description) TextView stepDescriptionTextView;
    @BindView(R.id.text_view_step_type) TextView stepTypeTextView;
    @BindView(R.id.text_view_running_timer) TextView runningTimerTextView;
    @BindView(R.id.button_play_pause_step) ImageView playPauseStepButton;
    @BindView(R.id.button_finish_step) TextView finishStepButton;
    @BindView(R.id.pause_step_layout) ImageView pauseStepLayout;

    @BindColor(R.color.colorPrimary) int activeStepColor;
    @BindColor(R.color.colorPrimaryDark) int finishedStepColor;

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

        setViewsText();
    }

    private void setViewsText() {
        stepDescriptionTextView.setText(step.getDescription());
        stepDescriptionTextView.setMaxLines(4);
        stepTypeTextView.setText(step.getType());
        runningTimerTextView.setText(getTimerFormat(step.getDurationTime() * 60));
    }

    @OnClick(R.id.button_play_pause_step)
    public void toggleStepTimer() {
        if (isTimerRunning) {
            isTimerRunning = false;
            pauseStepLayout.setVisibility(VISIBLE);
            runningTimerTextView.setVisibility(GONE);
            playPauseStepButton.setImageResource(R.drawable.ic_play);
            countDownTimer.cancel();
        } else {
            isTimerRunning = true;
            pauseStepLayout.setVisibility(GONE);
            runningTimerTextView.setVisibility(VISIBLE);
            playPauseStepButton.setImageResource(R.drawable.ic_pause);
            countDownTimer = new CountDownTimer(/*timeLeftInSeconds */ 20 * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    timeLeftInSeconds = l / 1000;
                    runningTimerTextView.setText(getTimerFormat(timeLeftInSeconds));
                }

                @Override
                public void onFinish() {
                    runningTimerTextView.setText("Done!");
                    tts.speak(step.getDishName() + " timer is done. I repeat, " + step.getDishName() + " timer is done.", TextToSpeech.QUEUE_FLUSH, null);
                    playPauseStepButton.setImageResource(R.drawable.ic_play);
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
                finishStepButton.setVisibility(GONE);
                playPauseStepButton.setVisibility(GONE);
                break;
            case ACTIVE:
                mainLayout.setBackgroundResource(R.drawable.light_orange_translucent_background);
                finishStepButton.setVisibility(VISIBLE);
                if (step.getDurationTime() != 0) {
                    playPauseStepButton.setVisibility(VISIBLE);
                }
                break;
            case DONE:
                mainLayout.setBackgroundResource(R.drawable.round_white_background_primarydark_border);
                playPauseStepButton.setVisibility(GONE);
                finishStepButton.setVisibility(GONE);
                stepTypeTextView.setText("DONE");
                pauseStepLayout.setImageResource(R.drawable.ic_added_dish);
                pauseStepLayout.setBackground(null);
                pauseStepLayout.setVisibility(VISIBLE);
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
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }
            runningTimerTextView.setVisibility(GONE);
            listener.showNextStep(step.getDishName(), step.getOrder(), true);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.finish_step_message)
                    .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            setStepStatus(Step.Status.DONE);
                            countDownTimer = null;
                            listener.showNextStep(step.getDishName(), step.getOrder(), true);
                        }
                    })
                    .setNegativeButton(R.string.moving_on, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (!isTimerRunning) {
                                toggleStepTimer();
                            }
                            listener.showNextStep(step.getDishName(), step.getOrder(), false);
                        }
                    });
            builder.create().show();
        }
    }

    @OnClick(R.id.button_step_description_audio)
    public void playStepDescription() {
        String text = "A " + step.getDurationTime() + " minutes step, " + step.getDescription();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @OnClick(R.id.relative_layout_step_item)
    public void expandStepItem() {
        isExpanded = !isExpanded;
        if (isExpanded) {
            stepDescriptionTextView.setMaxLines(12);
            stepDescriptionTextView.setTextSize(24f);
        } else {
            stepDescriptionTextView.setMaxLines(4);
            stepDescriptionTextView.setTextSize(18f);
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
