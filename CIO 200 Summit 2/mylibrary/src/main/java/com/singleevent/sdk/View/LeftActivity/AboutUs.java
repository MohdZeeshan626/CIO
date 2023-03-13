package com.singleevent.sdk.View.LeftActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

/**
 * Created by Admin on 6/1/2017.
 */

public class AboutUs extends AppCompatActivity {

    AppDetails appDetails;
    int pos;
    private String title;
    private ArrayList<Events> events = new ArrayList<Events>();
    // TextView tv_app_version_name;
    Events e;
    WebView aboutus;
    final String googleDocs = "https://docs.google.com/viewer?url=";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_aboutus);
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //    tv_app_version_name = (TextView) findViewById(R.id.tv_app_version_name);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);


        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        pos = getIntent().getExtras().getInt("pos");
        title = getIntent().getExtras().getString("title");
        events = Paper.book().read("Appevents");
        e = events.get(0);
        aboutus = (WebView)findViewById(R.id.aboutus);

        aboutus.setWebChromeClient(new WebChromeClient());
        aboutus.getSettings().setJavaScriptEnabled(true);
        aboutus.getSettings().setLoadWithOverviewMode(true);
        aboutus.getSettings().setUseWideViewPort(true);
        aboutus.getSettings().setBuiltInZoomControls(true);
        aboutus.getSettings().setDisplayZoomControls(false);
        aboutus.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);

        aboutus.getSettings().setPluginState(WebSettings.PluginState.ON);
        aboutus.loadUrl(String.format("data:text/html;charset=utf-8,<html><body style=\"text-align:left\"> %s </body></Html>", Uri.encode(e.getTabs(pos).getContent())));


        /*loading html content in webview*/
        aboutus.setWebViewClient(new myWebClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //some code

                if (url.endsWith(".pdf")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "application/pdf");
                    /* Check if there is any application capable to process PDF file. */
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        /* If not, show PDF in Google Docs instead. */
                        /* view.loadUrl(googleDocs + url);*/
                        aboutus.loadUrl(String.format("data:text/html;charset=utf-8,<html><body style=\"text-align:left\"> %s </body></Html>", Uri.encode(e.getTabs(pos).getContent())));

                    }
                } else if (url.endsWith(".in")|| url.endsWith(".in/")) {

                    if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                        if (url != null) {
                            //  aboutus.loadUrl(String.format("data:text/html;charset=utf-8,<html><body style=\"text-align:left\"> %s </body></Html>", Uri.encode(e.getTabs(pos).getContent())));
                            view.getContext().startActivity(
                                    new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                            //  aboutus.loadUrl(String.format("data:text/html;charset=utf-8,<html><body style=\"text-align:left\"> %s </body></Html>", Uri.encode(e.getTabs(pos).getContent())));

                        }

                    }
                }

                else if(url.endsWith(".com")){

                    if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                        if (url != null) {
                            //  aboutus.loadUrl(String.format("data:text/html;charset=utf-8,<html><body style=\"text-align:left\"> %s </body></Html>", Uri.encode(e.getTabs(pos).getContent())));
                            view.getContext().startActivity(
                                    new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                            //  aboutus.loadUrl(String.format("data:text/html;charset=utf-8,<html><body style=\"text-align:left\"> %s </body></Html>", Uri.encode(e.getTabs(pos).getContent())));

                        }

                    }
                }
                else{
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
                return true;

            }


        });

    }

    private class myWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //some code
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
            if (url != null) {
                if (validateEmail(url))
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                else
                    view.loadUrl(url);
                return true;

            } else {
                return false;
            }
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler,
                                       SslError error) {


            final AlertDialog.Builder builder = new AlertDialog.Builder(AboutUs.this);
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

    private boolean validateEmail(String url) {
        boolean isValidEmail = false;
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);


        Matcher matcher = pattern.matcher(url);
        isValidEmail = matcher.matches();

        return isValidEmail;
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
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString(title);
        setTitle(Util.applyFontToMenuItem(this, s));
        //tv_app_version_name.setText("Version : " + DataBaseStorage.getAppVersionName(this));

    }
}
