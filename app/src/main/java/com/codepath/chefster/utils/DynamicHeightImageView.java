package com.codepath.chefster.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by PRAGYA on 10/20/2016.
 * Class to create staggered grid view to display the news articles
 */

public class DynamicHeightImageView extends ImageView {

    private double heightRatio;

    public DynamicHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightImageView(Context context) {
        super(context);
    }

    public double getHeightRatio() {
        return heightRatio;
    }

    public void setHeightRatio(double ratio) {
        if (ratio != heightRatio) {
            heightRatio = ratio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (heightRatio > 0.0) {
            //set the image view size
            int width =  MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * heightRatio);
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
