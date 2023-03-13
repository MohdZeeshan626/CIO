package com.singleevent.sdk.Left_Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.model.Polling.Report;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.PollingQuestions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by Admin on 10/20/2016.
 */
public class PollingAdapter extends BaseAdapter {


    private int lastPosition = -1;
    int tabpos,polltypeid;
    Context context;
    private ArrayList<Items> pollingCategories = new ArrayList<>();
    String title,polltype;

    AppDetails appDetails;

    public PollingAdapter(Context context, ArrayList<Items> pollingCategories, int tabpos, String title,String polltype,int polltypeid) {
        // TODO Auto-generated constructor stub
        this.pollingCategories = pollingCategories;
        this.context = context;
        this.tabpos = tabpos;
        this.title = title;
        this.polltype=polltype;
        this.polltypeid=polltypeid;
        Paper.init(context);
        appDetails = Paper.book().read("Appdetails");

    }

    @Override
    public int getCount() {
        return pollingCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));
        if (convertView == null) {

            LayoutInflater vi = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.s_pollview, null);
            holder = new ViewHolder();
            holder.txtmedianame = (TextView) convertView.findViewById(R.id.medianame);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Items c = pollingCategories.get(position);
        holder.txtmedianame.setText(c.getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle arg = new Bundle();
                arg.putInt("Tabpos", tabpos);
                arg.putInt("Itempos", position);
                arg.putString("pollName", c.getName());
                arg.putString("title", title);
                arg.putString("polltype",polltype);
                arg.putInt("polltypeid",polltypeid);

                Intent i = new Intent(context,/* PollingItems.class*/PollingQuestions.class);
                i.putExtras(arg);
                getUserFeedbackDetails(i,c);



            }
        });
        return convertView;
    }


    private class ViewHolder {
        TextView txtmedianame;
        ImageView logo;
    }

    private void getUserFeedbackDetails(final Intent i, final Items items) {

        final ProgressDialog dialog = new ProgressDialog(this.context, R.style.MyAlertDialogStyle);
        dialog.setMessage(" Updating...");
        dialog.show();
        String tag_string_req = "check / update";
        String url = ApiList.PollingFeedback;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response") ) {

                        Gson gson = new Gson();
                        ArrayList<Report> reportArrayList = new ArrayList<>();
                        if (jObj.getBoolean("submitted_answer")){
                            JSONArray reportList = jObj.getJSONArray("report");

                            for (int i=0; i< reportList.length();i++){
                                String reoortString = reportList.getJSONObject(i).toString();
                                Report report = gson.fromJson(reoortString,Report.class);
                                Log.d("TAG",report.toString());
                                reportArrayList.add(report);
                            }

                        }
                        i.putExtra("ReportList",reportArrayList);
                        i.putExtra("submitted_answer",jObj.getBoolean("submitted_answer"));
                        context.startActivity(i);

                        Error_Dialog.show(jObj.getString("responseString"), (Activity)context);

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(context.getPackageName(), context.getPackageName() + ".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), (Activity)context);
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
                    Error_Dialog.show("Timeout", (Activity)context);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, (Activity)context), (Activity)context);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", (Activity)context);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId",""));
                params.put("username", Paper.book().read("username", ""));
                params.put("email", Paper.book().read("email", ""));
                params.put("appId", appDetails.getAppId());
                params.put("flag","check");
                params.put("pollName", items.getName());
                params.put("submissiondate", String.valueOf(System.currentTimeMillis()));
                params.put("eventdate", converlontostring(appDetails.getStartdate()));



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

    private String converlontostring(Long key) {

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(key);
        return myFormat.format(cal.getTime());
    }
}
