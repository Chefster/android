package com.codepath.chefster.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.codepath.chefster.R;
import com.github.pedrovgs.DraggableListener;
import com.github.pedrovgs.DraggableView;
import com.pierfrancescosoffritti.youtubeplayer.AbstractYouTubeListener;
import com.pierfrancescosoffritti.youtubeplayer.YouTubePlayerView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoActivity extends FragmentActivity {
    @BindView(R.id.draggable_view) DraggableView draggable_view;
    @BindView(R.id.iv_thumbnail) ImageView iv_thumbnail;
    @BindView(R.id.video_view) YouTubePlayerView videoView;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ButterKnife.bind(this);

        intent = getIntent();

        initializeVideoView();

        String image = Parcels.unwrap(intent.getParcelableExtra("image"));
        initializeImage(image);
        hookDraggableViewListener();
    }

    private void initializeVideoView() {
        videoView.initialize(new AbstractYouTubeListener() {
            @Override
            public void onReady() {
                String uri = Parcels.unwrap(intent.getParcelableExtra("uri"));
                String videoUrl = uri;
                videoView.loadVideo(videoUrl, 0);
            }
        }, true);
    }


    /**
     * Hook DraggableListener to draggableView to pause or resume VideoView.
     */
    private void hookDraggableViewListener() {
        draggable_view.setDraggableListener(new DraggableListener() {
            @Override public void onMaximized() {
                startVideo();
            }

            //Empty
            @Override public void onMinimized() {
                //Empty
            }

            @Override public void onClosedToLeft() {
                pauseVideo();
            }

            @Override public void onClosedToRight() {
                pauseVideo();
            }
        });
    }

    /**
     * Pause the VideoView content.
     */
    private void pauseVideo() {
        videoView.pauseVideo();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoView.release();
    }

    /**
     * Resume the VideoView content.
     */
    private void startVideo() {
        videoView.playVideo();
    }

    public void initializeImage(String image){
        Glide.with(this).load(image).asBitmap().into(iv_thumbnail);
    }
}
