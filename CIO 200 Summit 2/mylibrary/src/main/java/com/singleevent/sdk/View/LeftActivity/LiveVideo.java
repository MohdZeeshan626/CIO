package com.singleevent.sdk.View.LeftActivity;

import android.graphics.Color;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;

import java.net.URISyntaxException;

import io.paperdb.Paper;
import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class LiveVideo extends AppCompatActivity   {
    private ProgressBar progressBar;
    private WebView webView;
    AppDetails appDetails;
    private String title, url;
    private TextView tv_nointernet, tv_refresh;
    private SwipeRefreshLayout swiperefresh;
    android.widget.VideoView videoView;
    ImageButton mPlayButton;
    private io.socket.client.Socket mSocket;
    int count=0;
    double stime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livevideo);
        appDetails = Paper.book().read("Appdetails");
        videoView = (VideoView) findViewById(R.id.video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        MediaController mediaController = new MediaController(this);
        //mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);//https://webmobi.s3.amazonaws.com/nativeapps/webmobitechconference2018/1590642052582_WhatsAppVideoPM.mp4
        Uri video = Uri.parse("https://webmobisecure.s3.ap-south-1.amazonaws.com/keynotesession1.mp4");
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(video);
        videoView.requestFocus();


        try {
            mSocket = IO.socket("http://3.80.243.211:3030/socket/con");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        mSocket.on("confirm_connection", onConnect);
          mSocket.on("synctimeupdate",onNewTime);
        // mSocket.on("msg_ack",onConfirmMessage);
        mSocket.connect();

       // videoView.seekTo(6000);

        mPlayButton = (ImageButton) findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!videoView.isPlaying()) {
                    playVideo(stime);
                    mPlayButton.setVisibility(View.GONE);

                } else {
                    // playVideo(videoUrl, mVideoView.getHolder());
                    // show the media controls
                    //   mController.show();
                    // hide button once playback starts
                   // mSocket.emit("synctimeupdate",2000.0);

                    mPlayButton.setVisibility(View.GONE);
                   // playVideo(stime);
                }
            }
        });
/*
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // do nothing here......
                return true;
            }
        });
*/
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    };
    private Emitter.Listener onNewTime = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                   try {
                       double time = (Double) args[0];
                       stime=time;
                       System.out.print(stime);



                   }catch (Exception e)
                   {

                   }


                }
            });
        }
    };


    private Emitter.Listener onConfirm = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Error_Dialog.show("Connected", CallActivity.this);
                }
            });
        }
    };
    @Override
    public boolean onTouchEvent (MotionEvent ev){
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            if(videoView.isPlaying()){
                videoView.pause();
            } else {
              //  videoView.seekTo(12000);
                playVideo(stime);

              //  videoView.start();
            }
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
//loading content


        SpannableString s = new SpannableString("Live Video");
        setTitle(Util.applyFontToMenuItem(this, s));

    }
public  void playVideo(Double time)
{
   try {
       int l=time.intValue();
       int s=l*1000;
       videoView.seekTo(s);
   }catch (Exception e)
   {

       videoView.start();
   }
}

}


