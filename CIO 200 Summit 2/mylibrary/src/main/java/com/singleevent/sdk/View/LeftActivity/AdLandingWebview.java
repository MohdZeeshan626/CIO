package com.singleevent.sdk.View.LeftActivity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;
import android.webkit.WebView;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;

import io.paperdb.Paper;

public class AdLandingWebview extends AppCompatActivity {
    AppDetails appDetails;
    public static final String BANNER_NAME = "banner_name";
    public static final String BANNER_URL = "banner_url";
    String banner_name, banner_url;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appDetails = Paper.book().read("Appdetails");
        setContentView(R.layout.activity_ad_landing_webview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.webview_toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras == null)
            return;
        banner_name = extras.getString(BANNER_NAME);
        banner_url = extras.getString(BANNER_URL);
        System.out.println("Banner URL : " + banner_url);

        webView = (WebView) findViewById(R.id.webView_landing);

        webView.loadUrl(banner_url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDomStorageEnabled(true);


        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);




    }

    @Override
    protected void onResume() {
        super.onResume();
        SpannableString s = new SpannableString(banner_name);
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

   /* private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }*/
}
