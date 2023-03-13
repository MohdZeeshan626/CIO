package com.webmobi.gecmedia.Views;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.io.Files;
import com.singleevent.sdk.App;
import com.webmobi.gecmedia.Config.ApiList;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.webmobi.gecmedia.CustomViews.TxtVCustomFonts;
import com.webmobi.gecmedia.CustomViews.VolleyErrorLis;
import com.webmobi.gecmedia.Models.Event;
import com.webmobi.gecmedia.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;


public class Enter_DetailActivity extends AppCompatActivity implements View.OnClickListener {
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    Animation animFadeOut, animFadeIn;
    public static final String EXTRA_IMAGE = "Disc_DetailActivity:image";
    NestedScrollView nestedscrollview;
    AppBarLayout appbar;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;
    TextView register, locationname;
    Event events;
    TxtVCustomFonts title, des, txtdate;
    RelativeLayout header;
    LinearLayout h3;
    int pos;
    String action;
    String regId;
    float ImgWidth, ImgHeight;

    Context context;
    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    static ProgressDialog pDialog;

    File eventDir, jsonFile, descFile;
    private String dir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.activity_detail);
        context = this;
        regId = Paper.book().read("regId");
        nestedscrollview = (NestedScrollView) findViewById(R.id.nestedscrollview);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        // collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        register = (TextView) findViewById(R.id.register);
        register.setText("Download");
        dir = getFilesDir() + File.separator + "EventsDownload" + File.separator;
        header = (RelativeLayout) findViewById(R.id.header);
        h3 = (LinearLayout) findViewById(R.id.h3);
        header.setVisibility(View.GONE);
        h3.setVisibility(View.GONE);
        title = (TxtVCustomFonts) findViewById(R.id.eventtitle);
        des = (TxtVCustomFonts) findViewById(R.id.des);
        locationname = (TextView) findViewById(R.id.locationname);
        txtdate = (TxtVCustomFonts) findViewById(R.id.date);
        final ImageView image = (ImageView) findViewById(R.id.logo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        ImgWidth = displayMetrics.widthPixels;
        ImgHeight = displayMetrics.widthPixels * 0.70F;

        action = getIntent().getAction();
        pos = getIntent().getIntExtra("pos", 0);

        if (getIntent().getStringExtra(EXTRA_IMAGE) == null)
            image.setImageResource(R.drawable.medium_no_image);
        else {
            Glide.with(getApplicationContext())
                    .load(getIntent().getStringExtra(EXTRA_IMAGE))
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.medium_no_image)
                    .error(R.drawable.medium_no_image)
                    .into(new BitmapImageViewTarget(image) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            image.setImageBitmap(scaleBitmap(resource, (int) ImgWidth, (int) ImgHeight));

                        }
                    });

        }

        register.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();

        events = (Event) bundle.getSerializable("events");


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                MarkerOptions marker = new MarkerOptions().position(new LatLng(events.getLatitude(), events.getLongitude())).title(events.getVenue());
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                mMap.addMarker(marker);

                LatLng latlng = new LatLng(events.getLatitude(), events.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(8).build();
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });


        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int totalScroll = appBarLayout.getTotalScrollRange();
                int currentScroll = totalScroll + verticalOffset;

                if ((currentScroll) < 255) {
                    showingbutton(false);

                } else {
                    showingbutton(true);

                }

            }
        });

        title.setText(events.getApp_name());

        locationname.setSelected(true);
        locationname.setText(events.getLocation());
        String Startsplits = events.getStart_date().split("T")[0];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date date = formatter.parse(Startsplits);
            txtdate.setText(formatter1.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Mark Simonson - Proxima Nova Alt Regular.otf");
        locationname.setTypeface(type);


        des.setText(Html.fromHtml(events.getApp_description()));

        register.setOnClickListener(this);


    }


    @Override
    protected void onResume() {
        super.onResume();
        setTitle("");


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


    private void showingbutton(boolean flag) {

        animFadeOut = AnimationUtils.loadAnimation(Enter_DetailActivity.this, android.R.anim.slide_out_right);
        animFadeIn = AnimationUtils.loadAnimation(Enter_DetailActivity.this, R.anim.slide_in_right);

        if (flag) {
            if (register.getVisibility() != View.GONE) {
                register.setAnimation(animFadeOut);
                register.setVisibility(View.GONE);
            }
        } else {
            if (register.getVisibility() != View.VISIBLE) {
                register.setAnimation(animFadeIn);
                register.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    public void onClick(View view) {
        Intent sending;
        switch (view.getId()) {
            case R.id.register:
                savedeviceapp(events.getAppid());
                break;

        }

    }

    private void savedeviceapp(final String appid) {

        final ProgressDialog dialog = new ProgressDialog(Enter_DetailActivity.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("DownLoading...");
        dialog.show();


        String tag_string_req = "DeviceApp";
        String url = ApiList.DeviceApp;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {

                        downloadjson(pos);

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), Enter_DetailActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appid", appid);
                params.put("device_id", regId);
                params.put("device_type", "android");
                params.put("userid", Paper.book().read("Islogin", false) ? Paper.book().read("userId", "") : "");
                System.out.println(params);
                return params;
            }

        };

        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }


    private void downloadjson(final int downpos) {

        String jsonUrl = baseUrl + events.getApp_url() + "/appData.json";
        pDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Downloading ... ...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();


        String tag_string_req = "Downloading";
        System.out.println("Url " + jsonUrl);
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, jsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                System.out.println("JSON Response " + response);

                eventDir = new File(dir + events.getAppid());
                if (!eventDir.exists())
                    eventDir.mkdir();
                jsonFile = new File(eventDir, filename);
                descFile = new File(eventDir, "description.txt");
                try {
                    Files.write(response, jsonFile, Charset.defaultCharset());
                    Files.append(events.getApp_name() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_category() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getStart_date() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getAppid() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getLocation() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_title() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getVenue() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_logo() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_url() + "\n", descFile, Charset.defaultCharset());
                    Files.append(events.getApp_image() + "\n", descFile, Charset.defaultCharset());

                    if (action.equalsIgnoreCase(ApiList.EventCategory))
                        update(events.getAppid(), downpos);
                    else if (action.equalsIgnoreCase(ApiList.Eventrecom))
                        update(events.getAppid(), downpos);


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", Enter_DetailActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), Enter_DetailActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("No Internet Connection", Enter_DetailActivity.this);
                }

            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {

                String strUTF8 = null;
                try {
                    strUTF8 = new String(response.data, "UTF-8");

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }
                return Response.success(strUTF8,
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        App.getInstance().addToRequestQueue(jsonRequest, tag_string_req);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }

    private void update(String appid, int pos) {

        Intent i = new Intent();
        i.putExtra("pos", pos);
        i.putExtra("appid", appid);
        setResult(RESULT_OK, i);
        finish();

    }

    private void update(int pos) throws IOException {

        Intent i = new Intent();
        i.putExtra("pos", pos);
        setResult(RESULT_OK, i);
        finish();


    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }


}

