package com.webmobi.gecmedia.Views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.webmobi.gecmedia.Config.ApiList;
import com.webmobi.gecmedia.CustomViews.VolleyErrorLis;
import com.webmobi.gecmedia.Models.Event;
import com.webmobi.gecmedia.Models.OnItemClickListener;
import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.SingleEventHome;
import com.webmobi.gecmedia.Views.Adapter.DisEventAdapter;
import com.webmobi.gecmedia.Views.Adapter.SearchAdapter;

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

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

/**
 * Created by Admin on 5/15/2017.
 */

public class DisCover_Search extends Activity implements View.OnClickListener, OnItemClickListener {

    static ProgressDialog pDialog;
    String dir, dir_prev;
    LinearLayout header;
    private float navdpWidth;
    TextView txtmsg;
    EditText searchevent;
    AwesomeText close;
    Context context;
    ArrayList<Event> Searchevnets;
    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    Handler handler = new Handler(Looper.getMainLooper() /*UI thread*/);
    Runnable workRunnable;
    private List<String> savedirs;
    private List<String> savePrevdirs;
    RecyclerView filmlists;
    DisEventAdapter f_adapter;
    RelativeLayout v2;
    LinearLayoutManager mLayoutManager;
    float ImgWidth, ImgHeight;
    File eventDir, jsonFile, descFile;
    private ArrayList<Events> eventsToDisplay;
    String regId;
    String searchstr;
    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.act_privatesearch);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        regId = Paper.book().read("regId");
        context = this;
        header = (LinearLayout) findViewById(R.id.header);
        v2 = (RelativeLayout) findViewById(R.id.v2);
        searchevent = (EditText) findViewById(R.id.pguide);
        searchevent.setImeActionLabel("Search Model", EditorInfo.IME_ACTION_DONE);

        close = (AwesomeText) findViewById(R.id.backward);
        txtmsg = (TextView) findViewById(R.id.msg);
        txtmsg.setTypeface(Util.boldtypeface(this));
        txtmsg.setText("Search text is too short(min 3 characters)");

        Searchevnets = new ArrayList<>();
        filmlists = (RecyclerView) findViewById(R.id.filmlist);

        navdpWidth = (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) * 2) + 10;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) header.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = (int) navdpWidth;
        header.setLayoutParams(params);
        searchevent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 3)
                    txtmsg.setVisibility(View.GONE);
                else {
                    v2.setVisibility(View.GONE);
                    txtmsg.setVisibility(View.VISIBLE);
                    txtmsg.setText("Search text is too short(min 3 characters)");
                }

            }

            @Override
            public void afterTextChanged(final Editable editable) {

                if (editable.length() >= 3) {
                    handler.removeCallbacks(workRunnable);
                    workRunnable = new Runnable() {
                        @Override
                        public void run() {
                            doSmth(editable.toString());
                        }
                    };
                    handler.postDelayed(workRunnable, 500 /*delay*/);
                }
            }
        });

        close.setOnClickListener(this);

        dir = getFilesDir() + File.separator + "EventsDownload" + File.separator;
        dir_prev = getFilesDir() + File.separator + "PreviewDownloaded" + File.separator;
        savedirs = new ArrayList<>();
        savePrevdirs = new ArrayList<>();
        savedirs = getSaveDirs(dir);
        savePrevdirs = getSaveDirs(dir_prev);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels;
        ImgHeight = displayMetrics.widthPixels * 0.50F;

        mLayoutManager = new LinearLayoutManager(context);
        Searchevnets = new ArrayList<>();
        filmlists.setLayoutManager(mLayoutManager);
        searchAdapter = new SearchAdapter(this, R.layout.search_row, Searchevnets);
        f_adapter = new DisEventAdapter(DisCover_Search.this, Searchevnets, ImgWidth, ImgHeight, DisCover_Search.this);
        filmlists.setAdapter(f_adapter);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backward:
                finish();
                break;
        }

    }


    private void doSmth(final String searchstring) {

        final ProgressDialog dialog = new ProgressDialog(DisCover_Search.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading..");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        String tag_string_req = "events";

        String url;
        if (searchstring.endsWith("_preview")) {
            if (!Paper.book().read("Islogin", false)) {
                dialog.dismiss();
                Error_Dialog.show("Please Login to view Preview of AppNew", DisCover_Search.this);
                return;
            } else {

            }
            String prevSearch = searchstring.substring(0, searchstring.lastIndexOf("_"));
            url = ApiList.SearchEvents + prevSearch + "&preview_flag=1" + "&userid=" + Paper.book().read("userId", "");
        } else
            url = ApiList.SearchEvents + searchstring + "&preview_flag=0";
        // url = "https://api.webmobi.com/api/event/event_search?q=demopreview&preview_flag=1";
        searchstr = searchstring;

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                dialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("response")) {
                        parseResult(json.getJSONArray("events"), searchstring);
                    } else
                        showitems(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", DisCover_Search.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), DisCover_Search.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                }

            }
        });


        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


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


    private void parseResult(JSONArray events, final String searchstring) {

        try {

            if (searchstring.endsWith("_preview")) {
                String admin_flag = events.getJSONObject(0).getString("admin_flag");
                if (!admin_flag.equals("admin")) {
                    Error_Dialog.show("Only Admin can Preview the AppNew", DisCover_Search.this);

                    showitems(false);
                    return;
                }
            }
            Searchevnets.clear();
            Gson gson = new Gson();

            for (int i = 0; i < events.length(); i++) {
                String eventString = events.getJSONObject(i).toString();
                Event obj = gson.fromJson(eventString, Event.class);

                String appurl = events.getJSONObject(i).getString("appid");

                if (!searchstring.endsWith("_preview")) {
                    if (savedirs.contains(appurl)) {
                        obj.setDownloaded(true);
                    } else
                        obj.setDownloaded(false);
                    Searchevnets.add(obj);


                } else {
                    if (savePrevdirs.contains(appurl)) {
                        obj.setDownloaded(true);
                    } else
                        obj.setDownloaded(false);
                    Searchevnets.add(obj);

                }

            }


            if (Searchevnets.size() > 0) {
                f_adapter.notifyDataSetChanged();
                showitems(true);
            } else
                showitems(false);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void showitems(boolean flag) {
        if (flag) {
            v2.setVisibility(View.VISIBLE);
            txtmsg.setVisibility(View.GONE);
        } else {
            v2.setVisibility(View.GONE);
            txtmsg.setVisibility(View.VISIBLE);
            txtmsg.setText("No Events");
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
                    Intent intent = new Intent(DisCover_Search.this, Enter_DetailActivity.class);
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("events", events);
                Intent intent = new Intent(DisCover_Search.this, Disc_DetailActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("Disc_DetailActivity:image", events.getApp_logo());
                startActivity(intent);
            }


        }/*else if(viewId == R.id.preview){
            Event events = (Event) view.getTag();


            String query="";
            try {
               String appUrl = events.getApp_url();
                query = URLEncoder.encode( appUrl, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String jsonUrl = baseUrl +query + "/temp/appData.json";
            //String jsonUrl = baseUrl + appID + "/temp/appData.json";
            //loadjson(jsonUrl);
        }*/
    }


    private void savedeviceapp(final Event events, final int downpos) {

        final ProgressDialog dialog = new ProgressDialog(DisCover_Search.this, R.style.MyAlertDialogStyle);
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

                        downloadjson(events, downpos);

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), DisCover_Search.this);
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

        String jsonUrl;
        if (searchstr.endsWith("_preview")) {
            jsonUrl = baseUrl + events.getApp_url() + "/temp/appData.json";

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

                    eventDir = new File(dir_prev + events.getAppid());
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
                        Event update = Searchevnets.get(downpos);
                        update.setDownloaded(true);
                        f_adapter.notifyDataSetChanged();
                         openevent(events.getAppid());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    if (error instanceof TimeoutError) {
                        Error_Dialog.show("Timeout", DisCover_Search.this);
                    } else if (VolleyErrorLis.isServerProblem(error)) {
                        Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), DisCover_Search.this);
                    } else if (VolleyErrorLis.isNetworkProblem(error)) {
                        Error_Dialog.show("No Internet Connection", DisCover_Search.this);
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


        } else {

            jsonUrl = baseUrl + events.getApp_url() + "/appData.json";
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
                        Event update = Searchevnets.get(downpos);
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
                        Error_Dialog.show("Timeout", DisCover_Search.this);
                    } else if (VolleyErrorLis.isServerProblem(error)) {
                        Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), DisCover_Search.this);
                    } else if (VolleyErrorLis.isNetworkProblem(error)) {
                        Error_Dialog.show("No Internet Connection", DisCover_Search.this);
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


    }


    private void openevent(String appid) throws IOException {

        boolean sended = LocalBroadcastManager.getInstance(DisCover_Search.this).sendBroadcast(new
                Intent("com.reloadsavedevents"));


        f_adapter.notifyDataSetChanged();

        if (!searchstr.endsWith("_preview")) {
            eventDir = new File(dir + appid);

        } else {
            eventDir = new File(dir_prev + appid);

        }
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
            Intent sending = new Intent(DisCover_Search.this, SingleEventHome.class);
            sending.putExtra("Engagement","");
            sending.putExtra("back","APPS");
            if (searchstr.endsWith("_preview")) {
                Paper.book(obj.getAppId()).write("isPreview", true);
            } else {
                Paper.book(obj.getAppId()).write("isPreview", false);
            }
            startActivity(sending);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
