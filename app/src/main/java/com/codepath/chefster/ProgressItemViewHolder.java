package com.codepath.chefster;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.dd.CircularProgressButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressItemViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.tvItemTitle)
    TextView tvItemTitle;

    @BindView(R.id.circularButton1)
    CircularProgressButton circularButton1;

    ValueAnimator widthAnimation;


    public TextView getTvItemTitle() {
        return tvItemTitle;
    }

    public CircularProgressButton getCircularButton() {
        return circularButton1;
    }

    public ProgressItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setCircularButton(ProgressItemViewHolder holder){
        // Get The Animator set his Values
        widthAnimation = ValueAnimator.ofInt(1, 100);

        circularButton1.setBackgroundColor(Color.DKGRAY);
        circularButton1.setStrokeColor(Color.RED);
        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int buttonProgress = circularButton1.getProgress();
                if ( buttonProgress == 0) {
                    simulateSuccessProgress(circularButton1);
                }else if (buttonProgress < 100) {
                    if (widthAnimation.isRunning() && ! widthAnimation.isPaused())
                        widthAnimation.pause();
                    else if (widthAnimation.isPaused())
                        widthAnimation.resume();
                }
                else {
                    circularButton1.setProgress(0);
                }
            }
        });
    }

    private void simulateSuccessProgress(final CircularProgressButton button) {
        widthAnimation.setDuration(10000);
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
}
