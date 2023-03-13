package com.singleevent.sdk.View.RightActivity.admin.report;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.adapter.ReportAdapter;
import com.singleevent.sdk.View.RightActivity.admin.model.ReportModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by webMOBI on 12/18/2017.
 */

public class ReportActivity extends AppCompatActivity {
    private static final String TAG = ReportActivity.class.getCanonicalName();
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ReportAdapter reportAdapter;
    private ArrayList<ReportModel> reportModelArrayList;
    private ReportModel reportModel;
    private LinearLayout view1,view2,view3;
    private Button btn_send_reminder;
    AppDetails appDetails;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report );

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        view1 = (LinearLayout) findViewById(R.id.view1);
        view2 = (LinearLayout) findViewById(R.id.view2);
        view3 = (LinearLayout) findViewById(R.id.view3);
        btn_send_reminder = (Button)findViewById(R.id.btn_send_reminder);

        appDetails = Paper.book().read("Appdetails");
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor( appDetails.getTheme_color()));


        reportModelArrayList = new ArrayList<>();
        reportAdapter = new ReportAdapter(this,reportModelArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(reportAdapter);

        /*progressbar visible*/
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);

        if(Build.VERSION.SDK_INT < 21){
            btn_send_reminder.setBackgroundColor(Color.parseColor(String.valueOf(R.color.green_transparent)));
        }


        getReports();
    }



    private void getReports() {

        final String url= ApiList.GET_Leads;
        String str_rq = "getReports";
        reportModelArrayList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                /*progressbar hide*/
                view1.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("response")){

                        JSONArray jsonArray = jsonObject.getJSONArray("leads");
                        for (int count=0;count<jsonArray.length();count++){
                            reportModel = new ReportModel( jsonArray.getJSONObject(count).getString("exhibitor_name"),
                                    jsonArray.getJSONObject(count).getString("exhibitor_id"),
                                            jsonArray.getJSONObject(count).getInt("lead_count"));

                            reportModelArrayList.add(reportModel);
                        }

                        reportAdapter = new ReportAdapter(ReportActivity.this,reportModelArrayList);
                        recyclerView.setAdapter(reportAdapter);
                        reportAdapter.notifyDataSetChanged();

                        if (reportModelArrayList.size()>0){
                            view2.setVisibility(View.VISIBLE);
                        }else {
                            view3.setVisibility(View.VISIBLE);
                            view2.setVisibility(View.GONE);
                        }
                    }else{
                        //show text ,there is no content in report
                        view3.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    /*if error,progressbar visibility gone*/
                    view1.setVisibility( View.GONE );
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                /*if error progressbar  visibility gone*/
                view1.setVisibility(View.GONE);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", ReportActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, ReportActivity.this),
                            ReportActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", ReportActivity.this);
                }


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param=new HashMap<>();
                param.put("appid", appDetails.getAppId());
                param.put("userid", Paper.book().read("userId",""));
                param.put("admin_flag", Paper.book().read("admin_flag").toString());

                return param;
            }
        };


        stringRequest.setShouldCache(false);
        App.getInstance().addToRequestQueue(stringRequest,str_rq);

    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(Util.applyFontToMenuItem(ReportActivity.this,
                new SpannableString("Lead Retrieval Report")));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                finish();

        }
        return true;
    }
}
