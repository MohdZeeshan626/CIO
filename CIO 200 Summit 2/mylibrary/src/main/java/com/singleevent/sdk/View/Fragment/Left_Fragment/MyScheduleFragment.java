package com.singleevent.sdk.View.Fragment.Left_Fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.Left_Adapter.AgendaAdapter;
import com.singleevent.sdk.model.Agenda.Agenda;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.DateComparator;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.R;
import com.singleevent.sdk.utils.DataBaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import io.paperdb.Paper;
import timber.log.Timber;

public class MyScheduleFragment extends Fragment implements View.OnClickListener, AgendaAdapter.OnCardClickListner {

    AppDetails appDetails;
    int pos;
    private ArrayList<Events> events = new ArrayList<Events>();
    static ArrayList<Agenda> agendalist = new ArrayList<>();
    static List<Agendadetails> agendaSchedulelist = new ArrayList<>();
    Events e;
    LinearLayout contents;
    TextView nosch;
    List<Integer> list;
    Agenda agendaview;
    long clickeddate;
    LinkedHashMap<Long, ArrayList<Agendadetails>> timelisting = new LinkedHashMap<>();
    private double ImgWidth, padwidth;
    SimpleDateFormat myFormat;
    private SwipeRefreshLayout agenda_refresh;
    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    File eventDir, jsonFile, descFile;
    String dir, token = "";
    AgendaAdapter agendaAdapter;
    HashMap<String, Integer> categorylist = new HashMap<>();
    private RecyclerView mrecyclerview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.s_mysch_fragment, container, false);

        //If the fragment was created by the TabHost, return empty view
        if (getArguments() == null)
            return view;


        appDetails = Paper.book().read("Appdetails");
        agendalist.clear();
        agendaSchedulelist.clear();

        agendaview = (Agenda) getArguments().getSerializable("Agendaview");
        clickeddate = getArguments().getLong("Date");
        token = getArguments().getString("token");


        events = Paper.book().read("Appevents");

        e = events.get(0);


        for (int i = 0; i < e.getTabs().length; i++) {
            if (e.getTabs(i).getType().compareTo("agenda") == 0) {
                pos = i;
                break;
            }

        }
        for (int j = 0; j < e.getTabs(this.pos).getAgendaSize(); j++) {
            agendalist.add(e.getTabs(this.pos).getAgenda(j));
        }


        //for setting category color in all agenda list

        try {

            if (e.getTabs(pos).getcatSize() > 0) {
                for (int j = 0; j < e.getTabs(pos).getcatSize(); j++) {
                    try {
                        categorylist.put(e.getTabs(pos).getCategories(j).getCategory(),
                                Color.parseColor(e.getTabs(pos).getCategories(j).getColor_code()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // setting view

        contents = (LinearLayout) view.findViewById(R.id.contains);
        agenda_refresh = (SwipeRefreshLayout) view.findViewById(R.id.agenda_refresh);
        mrecyclerview = (RecyclerView) view.findViewById(R.id.mrecyclerview);
        nosch = (TextView) view.findViewById(R.id.noitems);
        nosch.setText("No Schedule");


        /*Recycler view */

        agendaAdapter = new AgendaAdapter(getActivity(), agendaSchedulelist, categorylist, clickeddate, "time");
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false));
        mrecyclerview.setAdapter(agendaAdapter);
        agendaAdapter.setOnCardClickListner(this);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = (displayMetrics.widthPixels) * 0.20;

        padwidth = ImgWidth * 0.50;

        agenda_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                agenda_refresh.setRefreshing(true);
                getLatestJson();

            }
        });

        settingview();


        return view;
    }

    private void settingview() {
        list = Paper.book(appDetails.getAppId()).read("SCH", new ArrayList<Integer>() {
        });
        if (list.size() > 0) {
            timelisting = new LinkedHashMap<>();
            agendaSchedulelist.clear();

            for (int i = 0; i < agendalist.size(); i++) {
                Agenda agenda = agendalist.get(i);
                for (int j = 0; j < agenda.getDetailSize(); j++) {
                    Agendadetails details = agenda.getDetail(j);
                    if (list.contains(details.getAgendaId())) {
                        if (!timelisting.containsKey(agenda.getName())) {
                            ArrayList<Agendadetails> films = new ArrayList<>();
                            films.add(details);
                            timelisting.put(agenda.getName(), films);
                        } else {
                            ArrayList<Agendadetails> films = timelisting.get(agenda.getName());
                            films.add(details);
                            Collections.sort(films, new DateComparator());
                            timelisting.put(agenda.getName(), films);
                        }

                    }
                }
            }

            showMyScheduleAgendaist();

        } else {
            nosch.setVisibility(View.VISIBLE);
            contents.setVisibility(View.GONE);
            mrecyclerview.setVisibility(View.GONE);
        }
    }

    private void showMyScheduleAgendaist() {
        for (final Long key : timelisting.keySet()) {
            if (clickeddate == key) {
                // ArrayList<Agendadetails> details = timelisting.get(key);
                agendaSchedulelist = timelisting.get(key);
            }
        }

        agendaAdapter = new AgendaAdapter(getActivity(), agendaSchedulelist, categorylist, clickeddate, "time");
        mrecyclerview.setAdapter(agendaAdapter);
        agendaAdapter.notifyDataSetChanged();
    }

    private String converlontostring(Long key) {

        myFormat = new SimpleDateFormat("yyyy-MM-dd");
        myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        myFormat.format(key);
        return myFormat.format(key);
    }

    private String converlontotime(Long key) {

        myFormat = new SimpleDateFormat("h:mm a");
        myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        myFormat.format(key);
        return myFormat.format(key);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        dir = getContext().getFilesDir().toString();
        dir = getActivity().getFilesDir() + File.separator + "EventsDownload" + File.separator;
    }


    private void getLatestJson() {
        String query = "";
        try {
            query = URLEncoder.encode(appDetails.getAppUrl(), "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
//        query = appDetails.getAppUrl();
        String jsonUrl = baseUrl + query + "/appData.json";
        loadjson(jsonUrl);
        agenda_refresh.setRefreshing(false);
    }

    private void loadjson(String jsonUrl) {
        final ProgressDialog pDialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
        pDialog.setMessage("Updating ...");
        pDialog.show();

        StringRequest jsonRequest = new StringRequest(com.android.volley.Request.Method.GET, jsonUrl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        pDialog.dismiss();

                        JSONObject res = null;
                        try {
                            res = new JSONObject(response);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        String jsonResponse = response;
                        System.out.println("JSON Response " + jsonResponse);
                        try {

                            eventDir = new File(dir + appDetails.getAppId());
                            jsonFile = new File(eventDir, filename);
                            jsonFile.delete();
                            descFile = new File(eventDir, "description.txt");

                            Files.write(jsonResponse, jsonFile, Charset.defaultCharset());


                            try {
                                showjsupdate(jsonFile);
                            }catch (Exception e){

                            }

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, getActivity()),
                            getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection",
                            getActivity());
                }

            }
        }) {
            @Override
            protected com.android.volley.Response<String> parseNetworkResponse(
                    NetworkResponse response) {

                String strUTF8 = null;
                try {
                    strUTF8 = new String(response.data, "UTF-8");

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }
                return com.android.volley.Response.success(strUTF8,
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        jsonRequest.setShouldCache(false);
        requestQueue.add(jsonRequest);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }

    private void showjsupdate(File jsonFile) {

        try {
            ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
            dialog.setMessage("Please wait...");
            dialog.show();

        String contents = null;
        try {
            contents = Files.toString(jsonFile, Charset.defaultCharset());
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        ArrayList<Object> eventsToDisplay = new ArrayList<>();

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
            dialog.dismiss();


            if (agenda_refresh.isRefreshing())
                agenda_refresh.setRefreshing(false);


            } catch (JSONException e) {
                e.printStackTrace();
                dialog.dismiss();
            }
        }catch (Exception e){}
    }

    //start theread to get latest json
    ProgressDialog pDialog;

    public void startThread(final byte[] bytes) {
        final Handler handler = new Handler();  //Optional. Define as a variable in your activity.
        final String[] decryptedString = new String[1];
        pDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        pDialog.setMessage("Please wait ...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // your code here

                try {
                    HashMap<String, byte[]> map = DataBaseStorage.encryptJsonData(bytes, DataBaseStorage.json_pass,
                            getActivity());

                    FileOutputStream fos = getActivity().openFileOutput(DataBaseStorage.F_I_L_ENCP1, Context.MODE_PRIVATE);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(map);
                    oos.close();


                    FileInputStream fis = getActivity().openFileInput(DataBaseStorage.F_I_L_ENCP1);
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    byte[] decrypted = DataBaseStorage.decryptJsonData((HashMap<String, byte[]>) ois.readObject(),
                            DataBaseStorage.json_pass, getActivity());
                    if (decrypted != null) {
                        decryptedString[0] = new String(decrypted);

                    }
                    ois.close();


                    if (pDialog.isShowing())
                        pDialog.dismiss();


                } catch (Exception e) {
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    e.printStackTrace();
                }


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Code to update the UI
                        try {
                            showjs(decryptedString[0]);
                        } catch (IOException e1) {
                            if (pDialog.isShowing())
                                pDialog.dismiss();

                            e1.printStackTrace();
                        }
                    }
                });
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    private void showjs(String values) throws IOException {
        String contents = values;

        try {
            ArrayList<Events> eventsToDisplay = new ArrayList<>();
            Gson gson = new Gson();
            JSONObject args = null;

            args = new JSONObject(contents);
            AppDetails obj = gson.fromJson(args.toString(), AppDetails.class);
            Paper.book().write("Appdetails", obj);
            JSONArray eventslist = args.getJSONArray("events");
            String eventString = eventslist.getJSONObject(0).toString();
            Events eobj = gson.fromJson(eventString, Events.class);
            eventsToDisplay.add(eobj);
            Paper.book().write("Appevents", eventsToDisplay);

           /* if (Paper.book().read("activefragment","").equals("myschedule")){
                performMySchedule();
            }else {
                performFullAgenda();
            }*/
            /*Intent intent = getIntent();

            finish();
            startActivity(intent);*/
        } catch (JSONException e) {

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUi(intent);
        }
    };

    private void updateUi(Intent intent) {

        if (intent != null && intent.getStringExtra("searched_text") != null)
            filter(intent.getStringExtra("searched_text"));
    }

    public void filter(String text) {
        List<Agendadetails> temp = new ArrayList();
        for (Agendadetails d : agendaSchedulelist) {

            if (d.getTopic() != null)
                if (d.getTopic().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(d);
                } else if (d.getLocation() != null && d.getLocation().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(d);
                } else if (d.getAttachment_name() != null && d.getAttachment_name().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(d);
                } else if (d.getCategory() != null && d.getCategory().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(d);
                }
        }
        //update recyclerview
        agendaAdapter.updateList(temp);

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getContext().registerReceiver(mNotificationReceiver, new IntentFilter("SEARCH_KEY"));


        } catch (NullPointerException e) {
            Timber.e(MyScheduleFragment.class.getCanonicalName(), e.getMessage());

        }

    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            getContext().unregisterReceiver(mNotificationReceiver);
        } catch (NullPointerException e) {
            Timber.e(MyScheduleFragment.class.getCanonicalName(), e.getMessage());

        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void OnItemLongClicked(View view, Agendadetails user, int position) {

    }

    @Override
    public void OnItemClick(View view, Agendadetails user, int position) {

        if (view.getId() == R.id.isch)
        // Toast.makeText(getContext(),"item clicked",3).show();
        {
            if (Paper.book().read("Islogin", false)) {
                list = Paper.book(appDetails.getAppId()).read("SCH", new ArrayList<Integer>());
                if (list.contains(user.getAgendaId())) {
                    list.remove(Integer.valueOf(user.getAgendaId()));
                    addtosch("Removing from Schedules", "unmark");
                } else {
                    list.add(user.getAgendaId());
                    addtosch("Adding to Schedules", "mark");
                }
                Paper.book(appDetails.getAppId()).write("SCH", list);
                settingview();

            }


        }


    }

    public void addtosch(String msg, final String mark) {

        // converting arraylist to jsonarray

        Gson gson = new GsonBuilder().create();
        final JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();


        final ProgressDialog dialog = new ProgressDialog(getContext(), R.style.MyAlertDialogStyle);
        dialog.setMessage(msg);
        dialog.show();
        String tag_string_req = "Schdule";
        String url = ApiList.Schdule;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), getActivity());

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(getContext().getPackageName(), getContext().getPackageName() + ".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), getActivity());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Paper.book().write("Sync", true);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", getActivity());
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, getContext()), getActivity());
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", getActivity());
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("action", mark);
                params.put("schedules", myCustomArray.toString());
                params.put("appid", appDetails.getAppId());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", token);
                return headers;
            }
        };


        strReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }
}
