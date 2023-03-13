package com.singleevent.sdk.View.LeftActivity.linkedInModule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.LinkedinProfile;
import com.singleevent.sdk.View.RightActivity.MyProfile;
import com.singleevent.sdk.View.RightActivity.Profile;
import com.singleevent.sdk.model.LinkedinUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LinkedInLoginActivity extends AppCompatActivity {
    WebView webView;
    private String TAG = "Linkedin Integration";
    private String code;
    private String accessToken;
    private String id;
    String F_name, L_name,directed_from;
    private String redirect_uri = "https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=77s8qs6oqrmcqi&redirect_uri=https://www.google.com/&state=foobar&scope=r_liteprofile%20r_emailaddress%20w_member_social";
    private StringRequest request;
    private LinkedinUser linkedInUser = new LinkedinUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_in_login);
        webView = findViewById(R.id.webView);
        directed_from=getIntent().getStringExtra("directed_from");
        webView.loadUrl(redirect_uri);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                auth1();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                SharedPreferences sharedPreferences = getSharedPreferences("LinkedIn_Authenticated", MODE_PRIVATE);
                String id = sharedPreferences.getString("id", "");
                String token = sharedPreferences.getString("token", "");
                if (!id.isEmpty() && !token.isEmpty()) {
                    super.onPageFinished(view, url);
//                    startActivity(new Intent(AuthActivityLi.this,Linkedin.class));
//                    finish();

                } else {
                    auth1();
                }

            }
        });
    }


    public void auth1() {
        String url1 = webView.getUrl();
        String str[] = url1.split("=", 2);
        Log.d("awsedrtg", "auth1: "+str+"\n"+url1);
        if(!url1.equals("https://www.linkedin.com/checkpoint/lg/login-submit")) {
            String str2[] = str[1].split("&", 2);
            code = str2[0];
            auth2();
        }
    }

    public void auth2() {
        ProgressDialog progress = new ProgressDialog(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request1 = new StringRequest(Request.Method.POST, "https://www.linkedin.com/oauth/v2/accessToken", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    webView.setVisibility(View.INVISIBLE);
                    progress.show();
                    webView.setVisibility(View.INVISIBLE);
                    JSONObject object = new JSONObject(response);
                    accessToken = object.get("access_token").toString();
                    Log.d(TAG, response);
                    queue.add(request);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<String, String>();
                map.put("grant_type", "authorization_code");
                map.put("code", code);
                map.put("redirect_uri", "https://www.google.com/");
                map.put("client_id", "77s8qs6oqrmcqi");
                map.put("client_secret", "MtqzP0woROUIVmDu");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Content-Type", "application/x-www-form-urlencoded");
                return map;
            }
        };

        request = new StringRequest(Request.Method.GET, "https://api.linkedin.com/v2/me", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d(TAG, response);
                    JSONObject object = new JSONObject(response);
                    id = object.get("id").toString();
                    F_name = object.get("localizedFirstName").toString();
                    L_name = object.get("localizedLastName").toString();
                    linkedInUser.setId(id);
                    linkedInUser.setFirstName(F_name);
                    linkedInUser.setLastName(L_name);
                    Toast.makeText(LinkedInLoginActivity.this, F_name + "\n" + L_name, Toast.LENGTH_SHORT).show();
                    auth3(id,F_name,L_name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                progress.dismiss();
                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", "Bearer " + accessToken);
                return map;
            }
        };

        queue.add(request1);
    }
    public void auth3(String id,String linkedInUserFirstName,String linkedInUserLastName) {
//        Intent intent = new Intent(this, MyProfile.class);
        SharedPreferences sharedPreferences = getSharedPreferences("LinkedIn_Authenticated", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.putString("token", accessToken);
        editor.putString("firstName", F_name);
        editor.putString("lastName", L_name);
        editor.commit();
//        startActivity(intent);
//        finish();
        Intent i=new Intent();
        if(directed_from.equals("SettingProfile")) {
            i = new Intent(this, MyProfile.class);
        }else if(directed_from.equals("AttendeeProfile")){
            i = new Intent(this, Profile.class);
        }
        i.putExtra("fname", linkedInUserFirstName);
        i.putExtra("lname", linkedInUserLastName);
        i.putExtra("image", "sfsssf");
        startActivity(i);
        finish();
    }



}