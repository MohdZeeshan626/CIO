package com.singleevent.sdk.View.LeftActivity;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Left_Adapter.ChatAdapter;
import com.singleevent.sdk.Left_Adapter.GroupMessageAdapter;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.LocalArraylist.ChatMSG;
import com.singleevent.sdk.model.User_Details;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.model.EventUser;
import com.singleevent.sdk.agora.openvcall.model.GroupChat;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

public class ZoomMain  extends AppCompatActivity {

    AppDetails appDetails;
    String url;
    private String title;
    private ArrayList<Events> events = new ArrayList<Events>();
    // TextView tv_app_version_name;
    Events e;
    WebView aboutus;
    final String googleDocs = "https://docs.google.com/viewer?url=";
    private ListView listView;
    EditText sendchat;
    ImageView sendmsg;
    String group_id;
    User_Details userDetails;
    ChatAdapter adapter;
    private Context context;
    private float dpWidth, badgewidth;
    HashMap<String, JSONObject> newmessages;
    private GroupMessageAdapter chatArrayAdapter;
    String UserID,username,profile_pic;
    private io.socket.client.Socket mSocket;
    GroupChat user;
    String gid[];
    String target;
    ChatMSG user1;
    ArrayList<EventUser> filelist;
    List<com.singleevent.sdk.model.User> userview;


    ArrayList<GroupChat> messages;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        context = this;
        setContentView(R.layout.zoom_main);

        appDetails = Paper.book().read("Appdetails");
        username=Paper.book().read("username");
        userview = new ArrayList<>();
        profile_pic=Paper.book().read("profile_pic");
        UserID = Paper.book().read("userId");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //    tv_app_version_name = (TextView) findViewById(R.id.tv_app_version_name);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        setSupportActionBar(toolbar);




        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        url = getIntent().getExtras().getString("url");
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
        aboutus.getSettings().setAllowContentAccess(true);
        aboutus.getSettings().setDomStorageEnabled(true);
        aboutus.loadUrl(url);
        //https://global.gotomeeting.com/join/468866973


        /*loading html content in webview*/
        aboutus.setWebViewClient(new ZoomMain.myWebClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //some code


                if (url != null) {
                    if (url != null) {
                        //  aboutus.loadUrl(String.format("data:text/html;charset=utf-8,<html><body style=\"text-align:left\"> %s </body></Html>", Uri.encode(e.getTabs(pos).getContent())));
                        view.getContext().startActivity(
                                new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                        //  aboutus.loadUrl(String.format("data:text/html;charset=utf-8,<html><body style=\"text-align:left\"> %s </body></Html>", Uri.encode(e.getTabs(pos).getContent())));

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

    public  class myWebClient extends WebViewClient {

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


            final AlertDialog.Builder builder = new AlertDialog.Builder(ZoomMain.this);
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
