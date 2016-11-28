package com.codepath.chefster.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.codepath.chefster.R;
import com.codepath.chefster.adapters.PhotosAdapter;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Review;
import com.codepath.chefster.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * This view represents a share box at the end of a cooking session, and it's per dish
 */
public class ShareDishView extends RelativeLayout {
    @BindView(R.id.relative_layout_share_dish_item) RelativeLayout mainLayout;
    @BindView(R.id.share_dish_name) TextView dishNameTextView;
    @BindView(R.id.text_view_post_review) TextView postReviewTextView;
    @BindView(R.id.share_dish_recycler_view) RecyclerView dishPhotoRecyclerView;
    @BindView(R.id.share_dish_rating_bar) MaterialRatingBar ratingBar;
    @BindView(R.id.edit_text_dish_review) EditText reviewEditText;

    List<Uri> imagesList;
    PhotosAdapter photosAdapter;
    OnLaunchCameraListener listener;


    public ShareDishView(Context context, Dish dish) {
        super(context);
        init(context, dish);
    }

    public ShareDishView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }

    public ShareDishView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, null);
    }

    protected void init(Context context, Dish dish) {
        inflate(context, R.layout.widget_share_dish, this);
        ButterKnife.bind(this);
        listener = (OnLaunchCameraListener) context;

        Glide.with(context).load(dish.getThumbnails().get(0)).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mainLayout.setBackground(drawable);
                }
            }
        });

        dishNameTextView.setText(dish.getTitle());

        imagesList = new ArrayList<>();
        photosAdapter = new PhotosAdapter(imagesList, context);
        dishPhotoRecyclerView.setAdapter(photosAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        dishPhotoRecyclerView.setLayoutManager(layoutManager);

        ItemClickSupport.addTo(dishPhotoRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        if (imagesList.isEmpty()) {
                            listener.launchCamera(dishNameTextView.getText().toString());
                        } else {
                            if (position == imagesList.size()) {
                                listener.launchCamera(dishNameTextView.getText().toString());
                            } else {
                                // for now do nothing, but later, show big image
                            }
                        }
                    }
                }
        );
    }

    public MaterialRatingBar getRatingBar() {
        return ratingBar;
    }

    public List<Uri> getImagesList() {
        return imagesList;
    }

    public void addToImagesList(Uri uri) {
        imagesList.add(uri);
        photosAdapter.notifyItemInserted(imagesList.size() - 1);
        dishPhotoRecyclerView.smoothScrollToPosition(imagesList.size() - 1);
    }

    public EditText getReviewEditText() {
        return reviewEditText;
    }

    public void setReviewEditText(EditText reviewEditText) {
        this.reviewEditText = reviewEditText;
    }

    @OnClick(R.id.text_view_post_review)
    public void postReview() {
        Review review = new Review();
        // fill review details
        //persist review to database
        Toast.makeText(getContext(), "Review posted!", Toast.LENGTH_SHORT).show();
    }

    public interface OnLaunchCameraListener {
        void launchCamera(String dishName);
    }
}