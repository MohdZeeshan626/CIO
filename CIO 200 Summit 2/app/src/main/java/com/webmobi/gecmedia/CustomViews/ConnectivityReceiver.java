package com.webmobi.gecmedia.CustomViews;

/**
 * Created by Lincoln on 18/03/16.
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import com.google.gson.Gson;
import com.singleevent.sdk.model.LocalArraylist.Notes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;


public class ConnectivityReceiver extends BroadcastReceiver {

    private static CountDownTimer myCountDownTimer;
    private static boolean firstConnect = true;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(final Context context, Intent arg1) {
        Paper.init(context);

        if (myCountDownTimer != null)
            myCountDownTimer.cancel();

        myCountDownTimer = new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {

                Gson gson = new Gson();


                if (NetworkUtil.getConnectivityStatusString(context)) {

                    List<String> allkeys = Paper.book("Sync").getAllKeys();
                    if (allkeys.size() > 0) {

                        JSONArray alldata = new JSONArray();
                        for (String appid : allkeys) {

                            if (Paper.book("Sync").read(appid, false)) {
                                HashMap<Integer, Notes> Noteslist = Paper.book(appid).read("AgendaNote", new HashMap<Integer, Notes>());
                                System.out.println(Noteslist.size());
                                final JSONObject json = new JSONObject();


                                JSONArray jarray = new JSONArray();

                                for (Integer n : Noteslist.keySet()) {

                                    String jsonString = gson.toJson(Noteslist.get(n));
                                    try {
                                        JSONObject request = new JSONObject(jsonString);
                                        jarray.put(request);
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                }

                                Noteslist = Paper.book(appid).read("ExhibitorNote", new HashMap<Integer, Notes>());
                                for (Integer n : Noteslist.keySet()) {
                                    String jsonString = gson.toJson(Noteslist.get(n));
                                    try {
                                        JSONObject request = new JSONObject(jsonString);
                                        jarray.put(request);
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }

                                Noteslist = Paper.book(appid).read("SponsorNote", new HashMap<Integer, Notes>());
                                for (Integer n : Noteslist.keySet()) {

                                    String jsonString = gson.toJson(Noteslist.get(n));
                                    try {
                                        JSONObject request = new JSONObject(jsonString);
                                        jarray.put(request);
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                }

                                try {
                                    json.put("notes", jarray);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                jarray = new JSONArray();
                                List<Integer> list = new ArrayList<>();
                                list = Paper.book(appid).read("SCH", new ArrayList<Integer>());
                                for (Integer n : list) {
                                    jarray.put(n);
                                }
                                try {
                                    json.put("schedules", jarray);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                jarray = new JSONArray();
                                list = Paper.book(appid).read("Exhibitor", new ArrayList<Integer>());
                                for (Integer n : list) {
                                    jarray.put(n);
                                }
                                try {
                                    json.put("exhibitor_favorites", jarray);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                jarray = new JSONArray();
                                list = Paper.book(appid).read("Sponsor", new ArrayList<Integer>());
                                for (Integer n : list) {
                                    jarray.put(n);
                                }
                                try {
                                    json.put("sponsor_favorites", jarray);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                JSONObject appdetails = new JSONObject();

                                try {
                                    appdetails.put(appid, json);
                                    alldata.put(appdetails);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        System.out.println(alldata);
                    }


                }
            }
        }.start();


    }


}