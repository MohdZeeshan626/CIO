package com.singleevent.sdk.View.RightActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
//import com.google.android.gms.vision.text.Line;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.Left_Adapter.AgendaAdapter;
import com.singleevent.sdk.View.LeftActivity.AgendaRoot;
import com.singleevent.sdk.model.Agenda.Agenda;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.DateComparator;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.AgendaDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

/**
 * Created by Admin on 6/12/2017.
 */

public class MySchedule extends AppCompatActivity implements View.OnClickListener, AgendaAdapter.OnCardClickListner {

    AppDetails appDetails;
    int pos;
    private ArrayList<Events> events = new ArrayList<Events>();
    static ArrayList<Agenda> agendalist = new ArrayList<>();
    RelativeLayout noschd;
    Button myschbut;
    Events e;
    LinearLayout contents;
    TextView nosch;
    List<Integer> list;
    LinkedHashMap<Long, ArrayList<Agendadetails>> timelisting = new LinkedHashMap<>();
    private double ImgWidth, padwidth;
    SimpleDateFormat myFormat;
    String token;
    String stitle;
    HashMap<String, Integer> categorylist = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_mysch);
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);

        token = Paper.book().read("token", "");
        agendalist.clear();
        events = Paper.book().read("Appevents");
        e = events.get(0);

        for (int i = 0; i < e.getTabs().length; i++) {
            if (e.getTabs(i).getType().compareTo("agenda") == 0) {
                pos = i;
                stitle=e.getTabs(i).getTitle().toString();
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

        contents = (LinearLayout) findViewById(R.id.contains);
        nosch = (TextView) findViewById(R.id.noitems);
        noschd=(RelativeLayout) findViewById(R.id.noschd);
        myschbut=(Button)findViewById(R.id.myschbut);
        myschbut.setBackground(Util.setdrawable(MySchedule.this, com.singleevent.sdk.R.drawable.healthpostbut,
                Color.parseColor(appDetails.getTheme_color())));

        nosch.setText("No Schedule");

        nosch.setOnClickListener(this);
        myschbut.setOnClickListener(this);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = (displayMetrics.widthPixels) * 0.20;

        padwidth = ImgWidth * 0.50;

        settingview();


    }

    private void settingview() {
        list = Paper.book(appDetails.getAppId()).read("SCH", new ArrayList<Integer>() {
        });
        if (list.size() > 0) {
            timelisting = new LinkedHashMap<>();
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
            showview();

        } else {
            noschd.setVisibility(View.VISIBLE);
            //nosch.setVisibility(View.VISIBLE);
            contents.setVisibility(View.GONE);
        }
    }

    private void showview() {
        contents.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (final Long key : timelisting.keySet()) {
            View child = inflater.inflate(R.layout.s_agendagroup, null);
            final TextView tvTitle = (TextView) child.findViewById(R.id.theatrename);
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tvTitle.setPadding(0, 8, 8, 8);
            LinearLayout screenlist = (LinearLayout) child.findViewById(R.id.screenlist);
            tvTitle.setText(converlontostring(key));
            ArrayList<Agendadetails> details = timelisting.get(key);
            for (int i = 0; i < details.size(); i++) {
                final Agendadetails item = details.get(i);
                View child1 = inflater.inflate(R.layout.s_agenda_view, null);
                final LinearLayout time = (LinearLayout) child1.findViewById(R.id.time);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) time.getLayoutParams();
                params.width = (int) ImgWidth;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                TextView fromtime = (TextView) child1.findViewById(R.id.fromtime);
                TextView totime = (TextView) child1.findViewById(R.id.totime);
                TextView title = (TextView) child1.findViewById(R.id.title);
                AwesomeText dots = (AwesomeText) child1.findViewById(R.id.dots);
                AwesomeText isch = (AwesomeText) child1.findViewById(R.id.isch);
                TextView venue = (TextView) child1.findViewById(R.id.venue);
                RelativeLayout loc = (RelativeLayout) child1.findViewById(R.id.loc);
                LinearLayout time_layout = (LinearLayout) child1.findViewById(R.id.time);
                LinearLayout linearLayout = (LinearLayout) child1.findViewById(R.id.linearlayout);

                fromtime.setText(converlontotime(item.getFromtime()));
                totime.setText(converlontotime(item.getTotime()));
                title.setText(item.getTopic());
                isch.setTextColor(Color.parseColor(appDetails.getTheme_color()));
                try {
                    dots.setTextColor(categorylist.get(item.getCategory()));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (!item.getLocation().equalsIgnoreCase("")) {
                    venue.setText(item.getLocation());
                    loc.setVisibility(View.VISIBLE);
                } else {
                    loc.setVisibility(View.GONE);
                }

                isch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Paper.book().read("Islogin", false)) {
                            list = Paper.book(appDetails.getAppId()).read("SCH", new ArrayList<Integer>());
                            if (list.contains(item.getAgendaId())) {
                                list.remove(Integer.valueOf(item.getAgendaId()));
                                addtosch("Removing from Schedules", "unmark");
                            } else {
                                list.add(item.getAgendaId());
                                addtosch("Adding to Schedules", "mark");
                            }
                            Paper.book(appDetails.getAppId()).write("SCH", list);
                            settingview();

                        }
                    }
                });

                time_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        agendaDetails(key, item);
                    }
                });
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        agendaDetails(key, item);
                    }
                });
               /* child1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bundle args = new Bundle();
                        args.putLong("Date", key);
                        args.putSerializable("Agendaview", item);
                        args.putSerializable("category_colorList", categorylist);
                        Intent i = new Intent(MySchedule.this, AgendaDetails.class);
                        i.setAction("com.agenda");
                        i.putExtras(args);
                        //startActivityForResult(i, 1);
                        startActivity(i);


                    }
                });*/

                screenlist.addView(child1);
            }
            contents.addView(child);
        }


    }

    private void agendaDetails(Long key, Agendadetails item) {
        Bundle args = new Bundle();
        args.putLong("Date", key);
        args.putSerializable("Agendaview", item);
        args.putSerializable("category_colorList", categorylist);
        Intent i = new Intent(MySchedule.this, AgendaDetails.class);
        i.setAction("com.agenda");
        i.putExtras(args);
        //startActivityForResult(i, 1);
        startActivity(i);
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

        SpannableString s = new SpannableString("My Schedule");
        setTitle(Util.applyFontToMenuItem(this, s));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            settingview();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.myschbut){
            Intent i=new Intent(MySchedule.this, AgendaRoot.class);
            i.putExtra("pos", pos);
            i.putExtra("title", stitle);
            startActivity(i);
        }

    }

    @Override
    public void OnItemLongClicked(View view, Agendadetails user, int position) {
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


        final ProgressDialog dialog = new ProgressDialog(MySchedule.this, R.style.MyAlertDialogStyle);
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
                        Error_Dialog.show(jObj.getString("responseString"), MySchedule.this);

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(MySchedule.this.getPackageName(), MySchedule.this.getPackageName() + ".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), MySchedule.this);
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
                    Error_Dialog.show("Timeout", MySchedule.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, MySchedule.this), MySchedule.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", MySchedule.this);
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

    @Override
    public void OnItemClick(View view, Agendadetails user, int position) {

    }
}
