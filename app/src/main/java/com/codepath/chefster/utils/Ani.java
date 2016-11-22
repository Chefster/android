package com.codepath.chefster.utils;

import android.view.animation.Animation;
import android.view.animation.Transformation;

class Ani extends Animation
{
    public Ani() {
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;

//        newHeight = (int)(initialHeight * interpolatedTime);

    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
//        initialHeight = actualHeight;
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
};