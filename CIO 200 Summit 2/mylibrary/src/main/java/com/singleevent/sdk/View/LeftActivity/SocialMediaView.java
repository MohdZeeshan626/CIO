package com.singleevent.sdk.View.LeftActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;


import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.utils.DataBaseStorage;

import io.paperdb.Paper;

public class SocialMediaView extends AppCompatActivity {


    private WebView webView;
    AppDetails appDetails;
    private String title, url;
    private TextView tv_nointernet;
    Button tv_refresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_social_media_details);
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_nointernet = (TextView) findViewById(R.id.tv_nointernet);
        tv_refresh = (Button) findViewById(R.id.tv_refresh);

        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        tv_refresh.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new myWebClient());



        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        url = getIntent().getExtras().getString("url");
        title = getIntent().getExtras().getString("title");

        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadHttpUrl();
            }
        });



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
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
            webView.loadUrl(url);
            return false;
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler,
                                       SslError error) {


            final AlertDialog.Builder builder = new AlertDialog.Builder(SocialMediaView.this);
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

    private void loadHttpUrl(){

        if (DataBaseStorage.isInternetConnectivity(this)){
            tv_nointernet.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl(url);
            tv_refresh.setVisibility(View.GONE);
        } else {
            tv_nointernet.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            tv_refresh.setVisibility(View.VISIBLE);
        }
    }
}
