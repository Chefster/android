package com.codepath.chefster.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.codepath.chefster.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hezi Eliyahu on 16/11/2016.
 */

public class ReviewViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvReviewName)
    TextView tvReviewName;

    @BindView(R.id.tvReview)
    TextView tvReview;

    @BindView(R.id.rbReview)
    RatingBar rbReview;

    @BindView(R.id.ivReviewProfile)
    ImageView ivReviewProfile;

    @BindView(R.id.tvReviewDate)
    TextView tvReviewDate;

    public TextView getTvReviewName() {
        return tvReviewName;
    }

    public TextView getTvReview() {
        return tvReview;
    }

    public RatingBar getRbReview() {
        return rbReview;
    }

    public ImageView getIvReviewProfile() {
        return ivReviewProfile;
    }

    public TextView getTvReviewDate() {
        return tvReviewDate;
    }

    public ReviewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}