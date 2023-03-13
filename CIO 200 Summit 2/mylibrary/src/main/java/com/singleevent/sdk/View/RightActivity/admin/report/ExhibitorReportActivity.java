package com.singleevent.sdk.View.RightActivity.admin.report;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.adapter.ExhibitorReportAdapter;
import com.singleevent.sdk.View.RightActivity.admin.leadGeneration.ScanQrCodeActivity;
import com.singleevent.sdk.View.RightActivity.admin.model.ExhibitorReportModel;
import com.singleevent.sdk.utils.DataBaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by webMOBI on 12/19/2017.
 */

public class ExhibitorReportActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = ReportActivity.class.getCanonicalName();
    private static final int SCAN_QR_REQUEST = 2000;
    private static final int ZBAR_CAMERA_PERMISSION = 7;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ExhibitorReportAdapter reportAdapter;
    private ArrayList<ExhibitorReportModel> reportModelArrayList;
    private ExhibitorReportModel reportModel;
    private LinearLayout view1,view2;
    private Button btnGenLead;
    private float dpWidth;
    AppDetails appDetails;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibitor_report);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        view1 = (LinearLayout) findViewById(R.id.view1);
        view2 = (LinearLayout) findViewById(R.id.view2);
        btnGenLead = (Button)findViewById(R.id.btnGenLead);
         appDetails = Paper.book().read("Appdetails");
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor( appDetails.getTheme_color()));


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.10F;



        reportModelArrayList = new ArrayList<>();
        reportAdapter = new ExhibitorReportAdapter(this,dpWidth, reportModelArrayList );
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(reportAdapter);

        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.GONE);

        btnGenLead.setOnClickListener(this);
        if(Build.VERSION.SDK_INT < 21){
            btnGenLead.setBackgroundColor(Color.parseColor(String.valueOf(R.color.green_transparent)));
        }


        getExhibitorReports();
    }

    private void getExhibitorReports() {

        final String url= ApiList.GET_Leads;
        String str_rq = "getReports";
        reportModelArrayList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("response")){

                        JSONArray jsonArray = jsonObject.getJSONArray("leads");
                        for (int count=0;count<jsonArray.length();count++){
                            JSONObject jObject = jsonArray.getJSONObject(count);
                            reportModel = new ExhibitorReportModel(jObject.getString("table_id"),
                                    jObject.getString("userid"),jObject.getString("appid"),
                                    jObject.getString("username"),jObject.getString("lead_id"),
                                    jObject.getString("lead_name"), jObject.getString("admin_flag"));

                            reportModelArrayList.add(reportModel);
                        }

                        reportAdapter = new ExhibitorReportAdapter(ExhibitorReportActivity.this,dpWidth,reportModelArrayList);
                        recyclerView.setAdapter(reportAdapter);
                        reportAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    view1.setVisibility( View.GONE );
                    view2.setVisibility(View.VISIBLE);
                    Log.v( TAG , e.toString() );
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                Log.v(TAG,error.toString());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param=new HashMap<>();
                param.put("appid",appDetails.getAppId());
                param.put("userid", Paper.book().read("userId",""));
                param.put("admin_flag", Paper.book().read("admin_flag").toString());

                return param;
            }
        };


        App.getInstance().addToRequestQueue(stringRequest,str_rq);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(Util.applyFontToMenuItem(ExhibitorReportActivity.this,
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

    //checking camera
    private void checkCameraPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            // only for marsemellow and newer versions
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
            } else {

                //Do something
                startActivityForResult(new Intent(this, ScanQrCodeActivity.class),SCAN_QR_REQUEST);
            }
        }else{

            //Do something
            startActivityForResult(new Intent(this, ScanQrCodeActivity.class),SCAN_QR_REQUEST);
        }

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnGenLead) {

            startActivityForResult(new Intent(this, ScanQrCodeActivity.class), SCAN_QR_REQUEST);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SCAN_QR_REQUEST){
            if (resultCode==RESULT_OK){
                if (data.getExtras()!=null){
                    String  leadid =  data.getExtras().getString("content");
                    String decoded_qrcode_userid= DataBaseStorage.decrypt(leadid);
                    Log.d(TAG,leadid);
                    if (leadid != null && ( ! leadid.equals(Paper.book().read("userId" ) )))
                        checkUserIdToCheckin( decoded_qrcode_userid );
                    else{
                        Error_Dialog.show(   "You can't add this user.",ExhibitorReportActivity.this);
                    }
                }

            }
        }

    }
    private void checkUserIdToCheckin(final String leadId ) {

        final String url= ApiList.ADD_Leads;
        String str_rq = "check_User_Id_To_Checkin";

        // reportModelArrayList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.v( TAG , response );
                    if(jsonObject.getBoolean("response")){

                        Error_Dialog.show(   jsonObject.getString("responseString"),ExhibitorReportActivity.this);

                    }else{
                        Error_Dialog.show(   jsonObject.getString("responseString"),ExhibitorReportActivity.this);
                    }

                } catch (JSONException e) {
                    Log.v( TAG , e.toString() );
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.v(TAG,error.toString());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param=new HashMap<>();
                param.put("appid", appDetails.getAppId());
                param.put("userid", Paper.book().read("userId",""));
                param.put("username", Paper.book().read("username",""));
                param.put("leadid",leadId);
                param.put("action","create");

                return param;
            }
        };


        App.getInstance().addToRequestQueue(stringRequest,str_rq);

    }

}
