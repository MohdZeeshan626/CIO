package com.singleevent.sdk.View.Fragment.Left_Fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;

import io.paperdb.Paper;


public class floorimageview extends AppCompatActivity {

    AppDetails appDetails;
    String imageurl;
    WebView txtimage;

    TextView appVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_aboutus);
        appDetails = Paper.book().read("Appdetails");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);


        if (getIntent().getExtras() == null)
            finish();

        appVersion = (TextView) findViewById(R.id.tv_app_version_name);
        appVersion.setVisibility(View.GONE);
        imageurl = getIntent().getExtras().getString("imageurl");
        txtimage = (WebView) findViewById(R.id.aboutus);
        WebSettings settings = txtimage.getSettings();
        txtimage.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        txtimage.getSettings().setBuiltInZoomControls(true);
        txtimage.getSettings().setLoadWithOverviewMode(true);
        txtimage.getSettings().setUseWideViewPort(true);
        txtimage.getSettings().setLoadWithOverviewMode(true);
        txtimage.getSettings().setUseWideViewPort(true);
        txtimage.getSettings().setBuiltInZoomControls(true);
        txtimage.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(floorimageview.this, R.style.MyAlertDialogStyle);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    super.onPageFinished(view, url);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    progressDialog.dismiss();
                }
            }

        });

        txtimage.loadUrl(imageurl.trim());

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

        SpannableString s = new SpannableString("");
        setTitle(Util.applyFontToMenuItem(this, s));

    }


}
