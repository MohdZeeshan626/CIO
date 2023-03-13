package com.singleevent.sdk.View.LeftActivity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;

import io.paperdb.Paper;

public class Video_Stream extends AppCompatActivity {
    private ProgressBar progressBar;
    private WebView webView;
    AppDetails appDetails;
    private String title, url;
    private TextView tv_nointernet, tv_refresh;
    private SwipeRefreshLayout swiperefresh;
    android.widget.VideoView videoView;
    ImageButton mPlayButton;
    private io.socket.client.Socket mSocket;
    int count = 0;
    double stime;
    String surl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_stream);
        appDetails = Paper.book().read("Appdetails");
        videoView = (VideoView) findViewById(R.id.video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        if (getIntent().getExtras() == null)
            finish();


        surl=getIntent().getExtras().getString("stream_url");
        title=getIntent().getExtras().getString("title");
        MediaController mediaController = new MediaController(this);
        //mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);//https://webmobi.s3.amazonaws.com/nativeapps/webmobitechconference2018/1590642052582_WhatsAppVideoPM.mp4
       // Uri video = Uri.parse("https://webmobisecure.s3.ap-south-1.amazonaws.com/keynotesession1.mp4");
        /*videoView.setMediaController(mediaController);
        videoView.setVideoURI(video);
        videoView.requestFocus();
        videoView.start();*/
        videoView.setVideoPath("https://webmobisecure.s3.ap-south-1.amazonaws.com/keynotesession1.mp4");
        videoView.start();
    }
    @Override
    protected void onResume() {
        super.onResume();
//loading content


        SpannableString s = new SpannableString(title);
        setTitle(Util.applyFontToMenuItem(this, s));

    }
}
