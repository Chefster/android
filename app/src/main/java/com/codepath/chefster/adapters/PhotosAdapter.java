package com.codepath.chefster.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.codepath.chefster.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<String> photosPathList;
    Context context;

    public PhotosAdapter(List<String> photoUrisList, Context context) {
        this.photosPathList = photoUrisList;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View photoView = layoutInflater.inflate(R.layout.photo_item, parent, false);
        //Return the a new Holder instance
        PhotosViewHolder viewHolder = new PhotosViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PhotosViewHolder photosViewHolder = (PhotosViewHolder) holder;
        configurePhotosViewHolder(photosViewHolder, position);
    }

    private void configurePhotosViewHolder(PhotosViewHolder photosViewHolder, int position) {
        if (position == photosPathList.size()) {
            photosViewHolder.getPhotoTakenImageView().setImageResource(R.drawable.ic_take_photo);
        } else {

            Bitmap bitmap = BitmapFactory.decodeFile(photosPathList.get(position));
            Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true);
            photosViewHolder.getPhotoTakenImageView().setImageBitmap(bMapScaled);

        }
    }

    @Override
    public int getItemCount() {
        return 1 + photosPathList.size();
    }

    public class PhotosViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_view_photo_taken) ImageView photoTakenImageView;

        public PhotosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public ImageView getPhotoTakenImageView() {
            return photoTakenImageView;
        }

    }
}
