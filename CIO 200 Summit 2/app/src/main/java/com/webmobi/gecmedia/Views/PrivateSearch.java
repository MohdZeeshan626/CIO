package com.webmobi.gecmedia.Views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
import com.singleevent.sdk.model.LocalArraylist.Notes;
import com.singleevent.sdk.model.LocalArraylist.Rating;
import com.webmobi.gecmedia.Config.ApiList;
import com.webmobi.gecmedia.CustomViews.VolleyErrorLis;
import com.webmobi.gecmedia.Models.Event;
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

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

/**
 * Created by Admin on 7/11/2017.
 */

public class PrivateSearch extends Activity implements View.OnClickListener, OnItemClickListener {


    static ProgressDialog pDialog;
    String dir;
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
    RecyclerView filmlists;
    DisEventAdapter f_adapter;
    RelativeLayout v2;
    LinearLayoutManager mLayoutManager;
    float ImgWidth, ImgHeight;
    File eventDir, jsonFile, descFile;
    private ArrayList<Events> eventsToDisplay;
    String regId;
    SharedPreferences spf;
    private String passPhrase = "";
    String Event_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.act_privatesearch);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        spf = getSharedPreferences(ApiList.LOCALSTORAGE, MODE_PRIVATE);
        regId = Paper.book().read("regId");
        context = this;
        header = (LinearLayout) findViewById(R.id.header);
        v2 = (RelativeLayout) findViewById(R.id.v2);
        searchevent = (EditText) findViewById(R.id.pguide);
        close = (AwesomeText) findViewById(R.id.backward);
        txtmsg = (TextView) findViewById(R.id.msg);
        txtmsg.setTypeface(Util.boldtypeface(this));
        txtmsg.setText(R.string.privatemsg);

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

                if (charSequence.length() >= 3)
                    txtmsg.setVisibility(View.GONE);
                else {
                    v2.setVisibility(View.GONE);
                    txtmsg.setVisibility(View.VISIBLE);
                    txtmsg.setText(R.string.privatemsg);
                }

            }

            @Override
            public void afterTextChanged(final Editable editable) {

                if (editable.length() > 3) {
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
        savedirs = new ArrayList<>();
        savedirs = getSaveDirs(dir);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels;
        ImgHeight = displayMetrics.widthPixels * 0.50F;

        mLayoutManager = new LinearLayoutManager(context);
        Searchevnets = new ArrayList<>();
        filmlists.setLayoutManager(mLayoutManager);

        f_adapter = new DisEventAdapter(PrivateSearch.this, Searchevnets, ImgWidth, ImgHeight, PrivateSearch.this);
        filmlists.setAdapter(f_adapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            Event_id = extras.getString("Event_id");
        }
    }

    private void doSmth(final String searchstring) {

        final ProgressDialog dialog = new ProgressDialog(PrivateSearch.this, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("Loading..");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        String tag_string_req = "events";
        passPhrase = searchstring;
        String url = ApiList.PSearchEvents1 + searchstring +"&event_id="+Event_id+ "&userid=" + Paper.book().read("userId");

        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                dialog.dismiss();
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("response")) {
                        parseResult(json.getJSONArray("events"), searchstring);
                    } else {
                        txtmsg.setText(json.getString("responseString"));
                        System.out.println("Event Response : " + json.getString("responseString"));
                        showitems(false, "You are not registered to this event, Please contact the event organizer for registration");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", PrivateSearch.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), PrivateSearch.this);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backward:
                finish();
                break;
        }
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

    private void parseResult(JSONArray events, String searchString) {


        try {

            Searchevnets.clear();
            Gson gson = new Gson();

            for (int i = 0; i < events.length(); i++) {
                String eventString = events.getJSONObject(i).toString();
                Event obj = gson.fromJson(eventString, Event.class);

                String app_id = events.getJSONObject(i).getString("appid");

//                Paper.book(app_id).write("PrivateKey", searchevent.getText().toString());
                Paper.book(app_id).write("PrivateKey", obj.getPrivate_key());
                Paper.book(app_id).write("InfoPrivacy", obj.getInfo_privacy());

                if (savedirs.contains(app_id))
                    obj.setDownloaded(true);
                else
                    obj.setDownloaded(false);


                Searchevnets.add(obj);
            }


            if (Searchevnets.size() > 0) {
                f_adapter.notifyDataSetChanged();
                showitems(true, "");
            } else
                showitems(false, "No Events");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void showitems(boolean flag, String s) {
        if (flag) {
            v2.setVisibility(View.VISIBLE);
            txtmsg.setVisibility(View.GONE);
        } else {
            v2.setVisibility(View.GONE);
            txtmsg.setVisibility(View.VISIBLE);
            txtmsg.setText(s);
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
                    Intent intent = new Intent(PrivateSearch.this, Enter_DetailActivity.class);
                    intent.setAction(ApiList.EventCategory);
                    intent.putExtras(bundle);
                    intent.putExtra("pos", position);
                    intent.putExtra("Disc_DetailActivity:image", events.getApp_logo());
                    startActivityForResult(intent, 2);

                } else {
                    try {
                        openevent(events.getAppid(), events.getInfo_privacy());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {

                Bundle bundle = new Bundle();
                bundle.putSerializable("events", events);
                Intent intent = new Intent(PrivateSearch.this, Disc_DetailActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("Disc_DetailActivity:image", events.getApp_image());
                startActivity(intent);

            }


        }
    }


    private void savedeviceapp(final Event events, final int downpos) {

        final ProgressDialog dialog = new ProgressDialog(PrivateSearch.this);
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
                        Error_Dialog.show(jObj.getString("responseString"), PrivateSearch.this);
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
        pDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
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
                Paper.book(Searchevnets.get(downpos).getAppid()).write("PrivateKey", Searchevnets.get(downpos).getPrivate_key());
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
                    //openevent(events.getAppid(),update.getInfo_privacy());
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", PrivateSearch.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), PrivateSearch.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("No Internet Connection", PrivateSearch.this);
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


    private void openevent(String appid, int privacy) throws IOException {

        boolean sended = LocalBroadcastManager.getInstance(PrivateSearch.this).sendBroadcast(new Intent("com.reloadsavedevents"));


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
            checkPrivacyId(appid, privacy);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkPrivacyId(final String appId, final int privacy) {

        pDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        pDialog.setMessage("Please wait...");
        pDialog.show();


        String tag_string_req = "Checking";
        String url = ApiList.Single_event_login;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        JSONObject details = jObj.getJSONObject("responseString");
                      /*  Paper.book().write("Islogin", true);*/
                        Paper.book().write("profile_pic", details.getString("profile_pic"));
                        Paper.book(appId).write("token", details.getJSONObject("token").getString("token"));
                        Paper.book().write("userId", details.getJSONObject("token").getString("userId"));
                        Paper.book(appId).write("userId", details.getJSONObject("token").getString("userId"));
                        Paper.book().write("username", details.getJSONObject("token").getString("username"));
                        Paper.book().write("email", Paper.book().read("Email", ""));

                        Paper.book(appId).write("admin_flag", details.getString("admin_flag"));

                        SharedPreferences.Editor editor = spf.edit();
                        editor.putString("username", Paper.book().read("username").toString());
                        editor.putString("user_id", Paper.book().read("userId").toString());
                        editor.apply();
                        // adding the sch list
                        List<Integer> list = new ArrayList<>();


                        JSONArray jsonArr = new JSONArray(details.getString("schedules"));

                        for (int i = 0; i < jsonArr.length(); i++)
                            list.add(jsonArr.getInt(i));

                        Paper.book().write("SCH", list);

                        // adding exhibitor list

                        list = new ArrayList<>();
                        if (!details.getString("exhibitor_favorites").equalsIgnoreCase("")) {
                            jsonArr = new JSONArray(details.getString("exhibitor_favorites"));
                            for (int i = 0; i < jsonArr.length(); i++)
                                list.add(jsonArr.getInt(i));
                        }

                        Paper.book().write("Exhibitor", list);

                        //adding schedule list

                        list = new ArrayList<>();

                        if (!details.getString("sponsor_favorites").equalsIgnoreCase("")) {
                            jsonArr = new JSONArray(details.getString("sponsor_favorites"));
                            for (int i = 0; i < jsonArr.length(); i++)
                                list.add(jsonArr.getInt(i));
                        }


                        Paper.book().write("Sponsor", list);

                        JSONObject notesdetails = details.getJSONObject("notes");

                        // Adding Note of Agenda
                        JSONArray agenda = notesdetails.getJSONArray("agenda");
                        Gson gson = new Gson();
                        HashMap<Integer, Notes> noteslist = new HashMap<>();
                        for (int i = 0; i < agenda.length(); i++) {
                            String eventString = agenda.getJSONObject(i).toString();
                            Notes eobj = gson.fromJson(eventString, Notes.class);
                            noteslist.put(eobj.getId(), eobj);
                        }

                        Paper.book().write("AgendaNote", noteslist);


                        // Adding Note of Exhibitor
                        JSONArray exh = notesdetails.getJSONArray("exhibitors");

                        noteslist = new HashMap<>();
                        for (int i = 0; i < exh.length(); i++) {
                            String eventString = exh.getJSONObject(i).toString();
                            Notes eobj = gson.fromJson(eventString, Notes.class);
                            noteslist.put(eobj.getId(), eobj);
                        }

                        Paper.book().write("ExhibitorNote", noteslist);

                        // Adding Note of Sponsor

                        JSONArray sponsor = notesdetails.getJSONArray("sponsors");

                        noteslist = new HashMap<>();
                        for (int i = 0; i < sponsor.length(); i++) {
                            String eventString = sponsor.getJSONObject(i).toString();
                            Notes eobj = gson.fromJson(eventString, Notes.class);
                            noteslist.put(eobj.getId(), eobj);
                        }

                        Paper.book().write("SponsorNote", noteslist);


                        // Adding Rating of agenda
                        JSONObject ratingdetails = details.getJSONObject("ratings");

                        JSONArray AgendaRating = ratingdetails.getJSONArray("agenda");

                        HashMap<Integer, Rating> ratinglist = new HashMap<>();

                        for (int i = 0; i < AgendaRating.length(); i++) {
                            String eventString = AgendaRating.getJSONObject(i).toString();
                            Rating eobj = gson.fromJson(eventString, Rating.class);
                            ratinglist.put(eobj.getType_id(), eobj);
                        }

                        Paper.book().write("AgendaRating", ratinglist);


                        //Adding Rating of Speaker

                        JSONArray SpeakerRating = ratingdetails.getJSONArray("speakers");

                        ratinglist = new HashMap<>();
                        for (int i = 0; i < SpeakerRating.length(); i++) {
                            String eventString = SpeakerRating.getJSONObject(i).toString();
                            Rating eobj = gson.fromJson(eventString, Rating.class);
                            ratinglist.put(eobj.getType_id(), eobj);
                        }

                        Paper.book().write("SpeakerRating", ratinglist);

                       /* setResult(RESULT_OK);
                        finish();*/
                        Intent sending = new Intent(PrivateSearch.this, SingleEventHome.class);sending.putExtra("Engagement","");sending.putExtra("back","APPS");
                        startActivity(sending);
                        finish();

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), PrivateSearch.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceType", "android");
                params.put("deviceId", regId);
                params.put("appid", appId);
                params.put("userid_flag", "0");
                if (privacy==0) {
                    params.put("info_privacy", "false");
                } else
                    params.put("info_privacy", "true");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                // add headers <key,value>
                String credentials;
                if (privacy==0) {
                    credentials = Paper.book().read("Email", "") + ":" + Paper.book().read("Password", "");
                } else {
                    credentials = Paper.book().read("Email", "") + ":" + passPhrase;
                }

                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


}
