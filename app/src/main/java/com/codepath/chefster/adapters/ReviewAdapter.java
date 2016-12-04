package com.codepath.chefster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Review;
import com.codepath.chefster.models.User;
import com.codepath.chefster.viewholders.ReviewViewHolder;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private Context context;
    private List<Review> reviewList;

    public Context getContext() {
        return context;
    }

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        List<User> reviewUser = review.getUser();
        User user = null;
        if ( reviewUser != null )
            user = reviewUser.get(0);

        holder.getTvReviewName().setText("Anonymous");
        holder.getTvReview().setText(review.getDescription());
        if (review.getRating() != null)
            holder.getRbReview().setRating(review.getRating().floatValue());
        if ( user != null ) {
            String userPhoto = user.getImageUrl();
            holder.getTvReviewName().setText(user.getFirstName());
            if (userPhoto != "") {
                Glide.with(getContext()).load(user.getImageUrl()).asBitmap()
                        .into(holder.getIvReviewProfile());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (reviewList != null && ! reviewList.isEmpty()) {
            return reviewList.size();
        }
        return 0;
    }
}
