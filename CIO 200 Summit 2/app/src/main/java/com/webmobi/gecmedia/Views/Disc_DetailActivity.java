package com.webmobi.gecmedia.Views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.core.app.ActivityCompat;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import com.singleevent.sdk.App;
import com.webmobi.gecmedia.Config.ApiList;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.webmobi.gecmedia.CustomViews.TxtVCustomFonts;
import com.webmobi.gecmedia.Models.Event;
import com.webmobi.gecmedia.Models.User;
import com.webmobi.gecmedia.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;


public class Disc_DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleMap mMap;
    Animation animFadeOut, animFadeIn;
    SupportMapFragment mapFragment;
    public static final String EXTRA_IMAGE = "Disc_DetailActivity:image";
    NestedScrollView nestedscrollview;
    AppBarLayout appbar;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;
    TextView register, locationname, addcalendar, invite;
    Event events;
    TxtVCustomFonts title, des, txtdate, peoplejoined;
    LinearLayout Limages;
    RelativeLayout header;
    private float dpWidth;
    int result = 9004;
    boolean isregistered = false;
    String regId;
    float ImgWidth, ImgHeight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.activity_detail);
        regId = Paper.book().read("regId");
        nestedscrollview = (NestedScrollView) findViewById(R.id.nestedscrollview);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
       // collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        register = (TextView) findViewById(R.id.register);
        header = (RelativeLayout) findViewById(R.id.header);
        Limages = (LinearLayout) findViewById(R.id.l1);
        addcalendar = (TextView) findViewById(R.id.calendar);
        invite = (TextView) findViewById(R.id.invite);
        title = (TxtVCustomFonts) findViewById(R.id.eventtitle);
        peoplejoined = (TxtVCustomFonts) findViewById(R.id.peoplejoined);
        des = (TxtVCustomFonts) findViewById(R.id.des);
        locationname = (TextView) findViewById(R.id.locationname);
        txtdate = (TxtVCustomFonts) findViewById(R.id.date);
        final ImageView image = (ImageView) findViewById(R.id.logo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.07F;
        ImgWidth = displayMetrics.widthPixels;
        ImgHeight = displayMetrics.widthPixels * 0.60F;

        if (getIntent().getStringExtra(EXTRA_IMAGE) == null)
            image.setImageResource(R.drawable.medium_no_image);
        else {

            Glide.with(getApplicationContext())
                    .load(getIntent().getStringExtra(EXTRA_IMAGE))
                    .asBitmap()
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

                if (!isregistered)
                    if ((currentScroll) < 255) {
                        showingbutton(false);

                    } else {
                        showingbutton(true);

                    }
                else
                    showingbutton(true);
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
        invite.setTypeface(type);
        addcalendar.setTypeface(type);
        addcalendar.setOnClickListener(this);
        invite.setOnClickListener(this);

        des.setText(Html.fromHtml(events.getApp_description()));

        if (events.getUsers_length() > 0) {
            Limages.removeAllViews();
            for (int i = 0; i < events.getusersSize(); i++) {
                User users = events.getUsers(i);
                final ImageView images = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) dpWidth, (int) dpWidth);
                if (i != 0)
                    params.setMargins(-10, 0, 0, 0);
                images.setLayoutParams(params);
                Limages.addView(images);


                Glide.with(getApplicationContext())
                        .load((users.getProfile_pic().equalsIgnoreCase("")) ? R.drawable.default_user : users.getProfile_pic())
                        .asBitmap()
                        .placeholder(R.drawable.default_user)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.default_user)
                        .into(new BitmapImageViewTarget(images) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(),
                                        Bitmap.createScaledBitmap(resource, (int) dpWidth, (int) dpWidth, false));
                                drawable.setCircular(true);
                                images.setImageDrawable(drawable);
                            }
                        });


            }

            peoplejoined.setText("" + events.getusersSize() + " People joined");
            header.setVisibility(View.VISIBLE);
            header.setOnClickListener(this);

            checkuser();


        } else
            header.setVisibility(View.GONE);

        register.setOnClickListener(this);


    }

    private void checkuser() {

        if (Paper.book().read("Islogin", false)) {

            List<User> listuser = Arrays.asList(events.getUsers());
            User isthere = new User();
            isthere.setUserid(Paper.book().read("userId", ""));
            isregistered = listuser.contains(isthere);

        }
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

        animFadeOut = AnimationUtils.loadAnimation(Disc_DetailActivity.this, android.R.anim.slide_out_right);
        animFadeIn = AnimationUtils.loadAnimation(Disc_DetailActivity.this, R.anim.slide_in_right);

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
            case R.id.header:
                if (Paper.book().read("Islogin", false)) {
                    sending = new Intent(Disc_DetailActivity.this, DisUser.class);
                    sending.putExtra("Appid", events.getAppid());
                    sending.putExtra("Appname", events.getApp_name());

                    startActivity(sending);
                } else {
                    sending = new Intent(Disc_DetailActivity.this, RegActivity.class);
                    sending.setAction(ApiList.loginaction);
                    startActivityForResult(sending, result);
                }

                break;
            case R.id.register:
                if (Paper.book().read("Islogin", false)) {

                    registeruser();

                } else {

                    sending = new Intent(Disc_DetailActivity.this, RegActivity.class);
                    sending.setAction(ApiList.loginaction);
                    startActivityForResult(sending, result);

                }

                break;

            case R.id.calendar:
                calendarpermission(true);
                break;

            case R.id.invite:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String content = "Event : " + events.getApp_name() + "\n" + "Date : " + txtdate.getText().toString() + "\n " + "Location : " + events.getLocation();
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, events.getApp_name());
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                break;

        }

    }


    public void calendarpermission(boolean b) {
        if (ActivityCompat.checkSelfPermission(Disc_DetailActivity.this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Disc_DetailActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Disc_DetailActivity.this, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, 1);
        } else {
            AdddingTocalendar(b);


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    calendarpermission(true);
                } else {
                    Error_Dialog.show("Without Permission We Couldn't Add to Calendar", Disc_DetailActivity.this);
                }
                break;
        }
    }

    private void AdddingTocalendar(boolean b) {

        HashMap<String, String> getcalendarlist = Paper.book().read("CalendarList", new HashMap<String, String>());

        if (!getcalendarlist.containsKey(events.getAppid())) {
            Date Startdate = null, Enddate = null;
            String Startsplits = events.getStart_date().split("T")[0];
            String Endsplits = events.getEnd_date().split("T")[0];
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Startdate = formatter.parse(Startsplits);
                Enddate = formatter.parse(Endsplits);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long calID = 1;
            long startMillis = 0;
            long endMillis = 0;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Startdate);
            startMillis = calendar.getTimeInMillis();
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(Enddate);
            endMillis = calendar1.getTimeInMillis();
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, events.getApp_title());
            values.put("allDay", 0);
            values.put(CalendarContract.Events.DESCRIPTION, events.getApp_description());
            values.put(CalendarContract.Events.CALENDAR_ID, calID);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Error_Dialog.show("Successfully added to Calendar", Disc_DetailActivity.this);
            getcalendarlist.put(events.getAppid(), events.getAppid());
            Paper.book().write("CalendarList", getcalendarlist);

        } else {
            Error_Dialog.show("Already added to Calendar", Disc_DetailActivity.this);
        }

    }

    private void registeruser() {

        final ProgressDialog dialog = new ProgressDialog(Disc_DetailActivity.this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Register the User");
        dialog.show();
        String tag_string_req = "Login";
        String url = ApiList.discovereventsregister;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {

                        isregistered = true;
                        register.setAnimation(animFadeOut);
                        register.setVisibility(View.GONE);

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), Disc_DetailActivity.this);
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
                params.put("userid", Paper.book().read("userId", ""));
                params.put("appid", events.getAppid());
                params.put("deviceType", "android");
                params.put("deviceId", regId);
                params.put("email", Paper.book().read("Email", ""));
                params.put("username", Paper.book().read("username", ""));
                System.out.println(params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token", ""));
                return headers;
            }
        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == result && resultCode == RESULT_OK) {
            registeruser();
        }
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
