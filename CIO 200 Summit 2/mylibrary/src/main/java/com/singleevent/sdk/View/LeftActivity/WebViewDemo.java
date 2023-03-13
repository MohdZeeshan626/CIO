package com.singleevent.sdk.View.LeftActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.singleevent.sdk.Custom_View.MyWebViewClient;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.R;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.utils.DataBaseStorage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.paperdb.Paper;

public class WebViewDemo extends AppCompatActivity {
    private ProgressBar progressBar;
    private WebView webView;
    AppDetails appDetails;
    private String title, url;
    private TextView tv_nointernet, tv_refresh;
    private SwipeRefreshLayout swiperefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.youtube);
        /*appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_nointernet = (TextView) findViewById(R.id.tv_nointernet);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient(this, progressBar));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);


        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewDemo.myWebClient());*/

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view1);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "1lbnBPOUteM";
                youTubePlayer.loadVideo(videoId, 0);
            }
        });

        /////

       /* swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        tv_refresh = (TextView) findViewById(R.id.tv_refresh);

        progressBar.setVisibility(View.GONE);

        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        url = getIntent().getExtras().getString("url");
        title = getIntent().getExtras().getString("title");


        //on refresh loading content
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiperefresh.setRefreshing(true);
                //loading content
                loadHttpUrl();

                swiperefresh.setRefreshing(false);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//loading content
        loadHttpUrl();


        SpannableString s = new SpannableString(title);
        setTitle(Util.applyFontToMenuItem(this, s));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    private class myWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //some code
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //some code
            return false;
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler,
                                       SslError error) {


            final AlertDialog.Builder builder = new AlertDialog.Builder(WebViewDemo.this);
            builder.setMessage("SSL Certificate Error");
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void loadHttpUrl() {

        if (DataBaseStorage.isInternetConnectivity(this)) {
            tv_nointernet.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl(url);
            tv_refresh.setVisibility(View.GONE);
        } else {
            tv_nointernet.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            tv_refresh.setVisibility(View.VISIBLE);
        }
    }*/
    }
}
