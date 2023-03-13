package com.singleevent.sdk.Left_Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.singleevent.sdk.R;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class MediaAdapter extends PagerAdapter {
    ArrayList<Image> imageList = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;

    public MediaAdapter(ArrayList<Image> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageList == null ? 0 : imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = layoutInflater.inflate(R.layout.feed_media_view_row, container, false);

        ImageView imageView = view.findViewById(R.id.image_view);
        CardView card_image = view.findViewById(R.id.card4);
        CardView card_video = view.findViewById(R.id.card2);
        CardView play_custom_card = view.findViewById(R.id.play_custom_card);
        SimpleExoPlayerView exoPlayerView = view.findViewById(R.id.idExoPlayerVIew);
        SimpleExoPlayer exoPlayer;

        if (imageList.get(position).getType().contains("image")) {
//            String imageURl = imageList.get(position).getPath();
            card_image.setVisibility(View.VISIBLE);
            Log.d("check_list_image", "instantiateItem: " + imageList.get(position).getPath());
            Picasso.get().load(imageList.get(position).getPath()).fit().centerInside().placeholder(R.drawable.progress_animation_picasso).into(imageView);
        } else if (imageList.get(position).getType().contains("video")) {
            card_image.setVisibility(View.GONE);
            card_video.setVisibility(View.VISIBLE);
            Log.d("check_list_video", "instantiateItem: " + imageList.get(position).getPath());

            String result = imageList.get(position).getPath().substring(0, imageList.get(position).getPath().indexOf("?"));
            try {

                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));


                exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

                Uri videouri = Uri.parse(result);


                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");


                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();


                MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);


                exoPlayerView.setPlayer(exoPlayer);


                exoPlayer.prepare(mediaSource);

                play_custom_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        play_custom_card.setVisibility(View.GONE);
                        exoPlayer.setPlayWhenReady(true);
                        exoPlayerView.setUseController(true);
                        setVideoPlayer(view,exoPlayer);
                    }
                });

            } catch (Exception e) {

                Log.e("TAG", "Error : " + e);
            }


        }
        Objects.requireNonNull(container).addView((view));
        return view;
    }

    private void setVideoPlayer(View view, SimpleExoPlayer exoPlayer) {
        CardView play_custom_card = view.findViewById(R.id.play_custom_card);
        SimpleExoPlayerView exoPlayerView = view.findViewById(R.id.idExoPlayerVIew);
        exoPlayer.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
                Log.d("check78", "onTimelineChanged: " + "onTimelineChanged");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.d("check78", "onTimelineChanged: " + "onTracksChanged");

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.d("check78", "onTimelineChanged: " + "onLoadingChanged");

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.d("check78", "onTimelineChanged: " + "onPlayerStateChanged" + playWhenReady);
                if (!playWhenReady) {
                    play_custom_card.setVisibility(View.VISIBLE);
                }
                exoPlayerView.setUseController(playWhenReady);

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.d("check78", "onTimelineChanged: " + "onPlayerError");

            }

            @Override
            public void onPositionDiscontinuity() {
                Log.d("check78", "onTimelineChanged: " + "onPositionDiscontinuity");

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                Log.d("check78", "onTimelineChanged: " + "onPlaybackParametersChanged");

            }
        });
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
