package com.webmobi.gecmedia.Views;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.singleevent.sdk.utils.DataBaseStorage;
import com.webmobi.gecmedia.R;

public class PrivacyPolicy extends AppCompatActivity {
    WebView webView;
    RelativeLayout v2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        webView = (WebView) findViewById(R.id.privacy_web);
        v2 = (RelativeLayout) findViewById(R.id.view2);
        //checking app is online or offline
        if (DataBaseStorage.isInternetConnectivity(this)) {
            webView.setVisibility(View.VISIBLE);
            v2.setVisibility(View.GONE);
            //setting the privacy policy url
            webView.loadUrl("https://www.webmobi.com/privacy-policy");

        } else {
            webView.setVisibility(View.GONE);
            v2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                break;
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        onBackPressed();
    }
}
