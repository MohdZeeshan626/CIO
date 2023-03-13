package com.singleevent.sdk.View.LeftActivity.linkedInModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.singleevent.sdk.R;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;

import java.util.ArrayList;

import io.paperdb.Paper;

public class LinkedInMainActivity extends AppCompatActivity {
    TextView linkedin_login,message_Text;
    String id = "", token = "";
    WebView webView;
    Toolbar toolbar;
    AppDetails appDetails;
    int pos;
    String hashtag="",default_msg="";
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedin);
//        linkedin_login = findViewById(R.id.linkedin_login);

        pos = getIntent().getExtras().getInt("pos");
//        title = getIntent().getExtras().getString("title");
        events = Paper.book().read("Appevents");
        e = events.get(0);
        hashtag = e.getTabs(pos).getHashtag();
        default_msg = e.getTabs(pos).getDefaultmsg();
        Log.d("check_the_tabs", "onCreate: "+default_msg+"\n"+hashtag);

        appDetails = Paper.book().read("Appdetails");
        toolbar=findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        webView = findViewById(R.id.webView);
        message_Text = findViewById(R.id.message_Text);
        SharedPreferences sharedPreferences = getSharedPreferences("LinkedIn", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Chrome/56.0.0.0 Mobile");
        webView.setWebChromeClient(new WebChromeClient());
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                Log.d("check_webview", "onCreate: " + url);

            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:(function() { " +
                        "document.querySelector('[role=\"toolbar\"]').remove();})()");

                view.loadUrl("javascript:(function() { " +
                        "document.getElementById('header-text-nav-container').style.display='none'; " +
                        "})()");
                progressDialog.dismiss();

            }
        });
        webView.loadUrl("https://www.linkedin.com/company/webmobi/posts/?feedView=all");
//        webView.loadUrl("https://www.linkedin.com/in/vivek-prajapati-a6912621a/detail/recent-activity/shares/");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!id.isEmpty() && !token.isEmpty()) {
//            linkedin_login.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            message_Text.setVisibility(View.GONE);

        } else {
//            linkedin_login.setVisibility(View.VISIBLE);
            message_Text.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }
    }

    public void back(View view) {
        onBackPressed();
        finish();
    }

    public void LinkedIn_login(View view) {
        startActivity(new Intent(this, LinkedInLoginActivity.class));
    }

    public void gotoPost(View view) {
        if (!id.isEmpty() && !token.isEmpty()) {
            Intent i=new Intent(this,LinkedInPostsActivity.class);
            i.putExtra("pos",pos);
            i.putExtra("hashtag",hashtag);
            i.putExtra("default_msg",default_msg);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(LinkedInMainActivity.this, "please connect with LinkedIn", Toast.LENGTH_SHORT).show();
        }
    }
}