package com.singleevent.sdk.View.LeftActivity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;

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
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.R;
import com.singleevent.sdk.model.AppDetails;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.paperdb.Paper;

public class GalleryVideo extends AppCompatActivity implements View.OnClickListener {
    SimpleExoPlayerView galleryvideo;
    SimpleExoPlayer cexoPlayer;
    Toolbar gvtoolbar;
    AppDetails appDetails;
    String url,title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.videogallery);
        galleryvideo=(SimpleExoPlayerView)findViewById(R.id.galleryvideo);
        gvtoolbar=(Toolbar)findViewById(R.id.gvtoolbar);
        appDetails = Paper.book().read("Appdetails");

        //    tv_app_version_name = (TextView) findViewById(R.id.tv_app_version_name);
        gvtoolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        gvtoolbar.setTitle(Util.applyFontToMenuItem(GalleryVideo.this,
                new SpannableString("Gallery")));

        //  setSupportActionBar(toolbar);
        gvtoolbar.setOnClickListener(this);
        if (getIntent().getExtras() == null)
            finish();
        url=getIntent().getExtras().getString("imageurl");
        title = getIntent().getExtras().getString("title");
        try {

            // bandwisthmeter is used for
            // getting default bandwidth


            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

            // track selector is used to navigate between
            // video using a default seekbar.
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

            // we are adding our track selector to exoplayer.
            cexoPlayer = ExoPlayerFactory.newSimpleInstance(GalleryVideo.this, trackSelector);

            // we are parsing a video url
            // and parsing its video uri.
            Uri videouri = Uri.parse(url);

            // we are creating a variable for datasource factory
            // and setting its user agent as 'exoplayer_view'
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");

            // we are creating a variable for extractor factory
            // and setting it to default extractor factory.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            // we are creating a media source with above variables
            // and passing our event handler as null,
            MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);

            // inside our exoplayer view
            // we are setting our player
            galleryvideo.setPlayer(cexoPlayer);

            // we are preparing our exoplayer
            // with media source.
            cexoPlayer.prepare(mediaSource);

            // we are setting our exoplayer
            // when it is ready.
            cexoPlayer.setPlayWhenReady(true);

            // holder.exoPlayerView.buildDrawingCache();

            cexoPlayer.addListener(new ExoPlayer.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playWhenReady && playbackState == cexoPlayer.STATE_READY) {
                        // media actually playing
                    } else if (playWhenReady) {
                        if(cexoPlayer.getDuration()==cexoPlayer.getCurrentPosition()){
                            cexoPlayer.seekTo(0);
                        }
                        // might be idle (plays after prepare()),
                        // buffering (plays when data available)
                        // or ended (plays when seek away from end)
                    } else {
                        // player paused in any state
                    }
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity() {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }
            });



        } catch (Exception e) {
            // below line is used for
            // handling our errors.
            Log.e("TAG", "Error : " + e.toString());
        }

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.gvtoolbar) {
            onBackPressed();
        }
    }
}
