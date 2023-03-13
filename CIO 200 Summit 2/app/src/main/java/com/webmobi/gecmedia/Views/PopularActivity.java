package com.webmobi.gecmedia.Views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
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
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.webmobi.gecmedia.Config.ApiList;
import com.webmobi.gecmedia.CustomViews.ConnectionDetector;
import com.webmobi.gecmedia.CustomViews.DilatingDotsProgressBar;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.webmobi.gecmedia.CustomViews.VolleyErrorLis;
import com.webmobi.gecmedia.Models.Event;
import com.webmobi.gecmedia.Models.NearbyInterface;
import com.webmobi.gecmedia.Models.OnItemClickListener;
import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.SingleEventHome;
import com.webmobi.gecmedia.Views.Adapter.DisEventAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by  on 12/6/2017.
 */

public class PopularActivity extends AppCompatActivity implements OnItemClickListener {

    NearbyInterface listener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    DisEventAdapter f_adapter;
    ProgressDialog pDialog;
    boolean type = false;
    LinearLayoutManager mLayoutManager;
    ArrayList<Event> popularevents;
    private DilatingDotsProgressBar mDilatingDotsProgressBar;
    Context context;
    RecyclerView filmlists;
    TextView noitems, filter, tryagain;
    RelativeLayout v1, v2;
    float ImgWidth, ImgHeight;
    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    String dir;
    private List<String> savedirs;
    File eventDir, jsonFile, descFile;
    private ArrayList<Events> eventsToDisplay;
    String regId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Paper.init(this);
        regId = Paper.book().read("regId");
        context = PopularActivity.this;
        setContentView(R.layout.act_disceventlist);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels;
        ImgHeight = displayMetrics.widthPixels * 0.60F;
        filmlists = (RecyclerView) findViewById(R.id.filmlist);
        noitems = (TextView) findViewById(R.id.noitems);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        filter = (TextView) findViewById(R.id.filter_button);
        tryagain = (TextView) findViewById(R.id.tryagain);
        v1 = (RelativeLayout) findViewById(R.id.view1);
        v2 = (RelativeLayout) findViewById(R.id.view2);

        mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);

        filter.setVisibility(View.GONE);
        noitems.setText("No Event");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setEnabled(false);
        mLayoutManager = new LinearLayoutManager(context);
        popularevents = new ArrayList<>();
        filmlists.setLayoutManager(mLayoutManager);
        f_adapter = new DisEventAdapter(this, popularevents, ImgWidth, ImgHeight, PopularActivity.this);
        filmlists.setAdapter(f_adapter);


        dir = getFilesDir() + File.separator + "EventsDownload" + File.separator;
        savedirs = new ArrayList<>();
        savedirs = getSaveDirs(dir);


        if (new ConnectionDetector(this).isConnectingToInternet()) {
            showview(true);
            fetchpopularevents();
        } else {
            showview(false);
        }

        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showview(true);
                fetchpopularevents();
            }
        });
    }

    private List<String> getSaveDirs(String dir) {
        List<String> paths = new ArrayList<>();
        File directory = new File(dir);

        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isDirectory())
                paths.add(file.getName());
        }
        return paths;
    }

    @Override
    protected void onResume() {
        super.onResume();
        f_adapter.notifyDataSetChanged();

        SpannableString s = new SpannableString("Popular");
        setTitle(Util.applyFontToMenuItem(this, s));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    private void openevent(String appid) throws IOException {
        f_adapter.notifyDataSetChanged();

        eventDir = new File(dir + appid);
        jsonFile = new File(eventDir, filename);

        String contents = Files.toString(jsonFile, Charset.defaultCharset());
        System.out.println("File Contents : " + contents);


        eventsToDisplay = new ArrayList<>();
        Gson gson = new Gson();


        JSONObject args = null;
        try {
            args = new JSONObject(contents);
            AppDetails obj = gson.fromJson(args.toString(), AppDetails.class);
            Paper.book().write("Appdetails", obj);
            JSONArray eventslist = args.getJSONArray("events");
            String eventString = eventslist.getJSONObject(0).toString();
            Events eobj = gson.fromJson(eventString, Events.class);
            eventsToDisplay.add(eobj);
            Paper.book().write("Appevents", eventsToDisplay);
            Intent sending = new Intent(this, SingleEventHome.class);sending.putExtra("Engagement","");sending.putExtra("back","APPS");
            startActivity(sending);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void fetchpopularevents() {
        //listener.disabletab(2);

        mDilatingDotsProgressBar.showNow();

        final String tag_string_req = "Popular Event";

        final StringRequest strReq = new StringRequest(Request.Method.GET,
                ApiList.Popular, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                mDilatingDotsProgressBar.hideNow();
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("response")) {
                        parseResult(json.getJSONArray("events"));
                    } else
                        showitems(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                // listener.enableall();

                mDilatingDotsProgressBar.hideNow();

                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", PopularActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), PopularActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    showview(false);
                }

            }
        });

        App.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void parseResult(JSONArray jobj) {
        try {

            popularevents.clear();
            Gson gson = new Gson();

            for (int i = 0; i < jobj.length(); i++) {
                String eventString = jobj.getJSONObject(i).toString();
                Event obj = gson.fromJson(eventString, Event.class);

                String appurl = jobj.getJSONObject(i).getString("appid");

                if (savedirs.contains(appurl))
                    obj.setDownloaded(true);
                else
                    obj.setDownloaded(false);

                popularevents.add(obj);
            }


            if (popularevents.size() > 0) {
                f_adapter.notifyDataSetChanged();
                showitems(true);
            } else
                showitems(false);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void showitems(boolean flag) {
        // listener.enableall();
        if (flag) {
            filmlists.setVisibility(View.VISIBLE);
            noitems.setVisibility(View.GONE);
        } else {
            filmlists.setVisibility(View.GONE);
            noitems.setVisibility(View.VISIBLE);
        }
    }


    private void showview(boolean flag) {
        if (flag) {
            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.GONE);
        } else {
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View view, int position, View Imageview) {

        long viewId = view.getId();

        if (viewId == R.id.download) {
            Event events = (Event) view.getTag();
            savedeviceapp(events, position);


        } else if (viewId == R.id.moredetails) {
            Event events = (Event) view.getTag();
            if (events.getAccesstype().equalsIgnoreCase("dashboard")) {
                if (!events.isDownloaded()) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("events", events);
                    Intent intent = new Intent(PopularActivity.this, Enter_DetailActivity.class);
                    intent.setAction(ApiList.EventCategory);
                    intent.putExtras(bundle);
                    intent.putExtra("pos", position);
                    intent.putExtra("Disc_DetailActivity:image", events.getApp_logo());
                    startActivityForResult(intent, 2);

                } else {
                    try {
                        openevent(events.getAppid());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {

               /* Bundle bundle = new Bundle();
                bundle.putSerializable("events", events);
                Intent intent = new Intent(PopularActivity.this, Disc_DetailActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("Disc_DetailActivity:image", events.getApp_logo());
                PopularActivity.this.startActivity(intent);*/
                Intent intent = new Intent(PopularActivity.this, DiscoveryEventDetails.class);
                intent.putExtra(DiscoveryEventDetails.DISCOVERY_DETAILS_APPID, events.getAppid());
                PopularActivity.this.startActivity(intent);
            }


        }


    }


    private void savedeviceapp(final Event events, final int position) {


        final ProgressDialog dialog = new ProgressDialog(PopularActivity.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Downloading...");
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


                        downloadjson(events, position);

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), PopularActivity.this);
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
                params.put("appid", events.getAppid());
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


    private void downloadjson(final Event events, final int downpos) {

        String jsonUrl = baseUrl + events.getApp_url() + "/appData.json";
        pDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Downloading ...");
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

                    Event update = popularevents.get(downpos);
                    update.setDownloaded(true);
                    f_adapter.notifyDataSetChanged();
                    // openevent(events.getAppid());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", PopularActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), PopularActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("No Internet Connection", PopularActivity.this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getExtras() != null) {
                        int pos = data.getIntExtra("pos", 0);
                        // listener.enableall();
                        Event update = popularevents.get(pos);
                        update.setDownloaded(true);

                        try {
                            openevent(update.getAppid());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }
}
