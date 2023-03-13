package com.singleevent.sdk.View.RightActivity.admin.leadGeneration;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
import com.singleevent.sdk.utils.DataBaseStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by webMOBI on 12/14/2017.
 */

public class AdminLeadGenerationActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int SCAN_QR_REQUEST_CODE = 5000;
    private static final String TAG = AdminLeadGenerationActivity.class.getSimpleName();
    private static final int ZBAR_CAMERA_PERMISSION = 444;
    private Toolbar toolbar;
    private AppDetails appDetails;
    private Button btn_scan_qr_code, btn_add_lead_manually;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        appDetails = Paper.book().read("Appdetails");
        setContentView(R.layout.activity_admin_lead_generation );

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_scan_qr_code = (Button) findViewById(R.id.btn_scan_qr_code);
        btn_add_lead_manually =(Button) findViewById(R.id.btn_add_lead_manually);

        //setting background color of button and
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));


        btn_scan_qr_code.setOnClickListener(this);
        btn_add_lead_manually.setOnClickListener(this);

        if(Build.VERSION.SDK_INT < 21){
            btn_add_lead_manually.setBackgroundColor(Color.parseColor(String.valueOf(R.color.green_transparent)));
            btn_scan_qr_code.setBackgroundColor(Color.parseColor(String.valueOf(R.color.green_transparent)));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(Util.applyFontToMenuItem(AdminLeadGenerationActivity.this,
                new SpannableString("Lead Generation")));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();


            if (id== R.id.btn_add_lead_manually){
                //start activity for adding contact as a lead manually
                startActivity(new Intent(this,AddLeadManuallyActivity.class));

            }
            if (id== R.id.btn_scan_qr_code){
                //start activity for result for qr scan
                checkCameraPermission();
              /*  startActivityForResult(new Intent(this,ScanQrCodeActivity.class),SCAN_QR_REQUEST_CODE);*/

            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SCAN_QR_REQUEST_CODE){
            if (resultCode==RESULT_OK){
                if (data.getExtras()!=null){
                   String  leadid =  data.getExtras().getString("content");
                    String decoded_qrcode_userid= DataBaseStorage.decrypt(leadid);

                    Log.d(TAG,leadid);
                    if (leadid != null  &&  ( ! leadid.equals(Paper.book().read("userId") )))
                    checkUserIdToCheckin( decoded_qrcode_userid );
                    else {
                        Error_Dialog.show(   "You can't add this user.",AdminLeadGenerationActivity.this);
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

                        Error_Dialog.show(   jsonObject.getString("responseString"),AdminLeadGenerationActivity.this);

                    }else{
                        Error_Dialog.show(   jsonObject.getString("responseString"),AdminLeadGenerationActivity.this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ZBAR_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Do something

                    startActivityForResult( new Intent(AdminLeadGenerationActivity.this,
                            ScanQrCodeActivity.class), SCAN_QR_REQUEST_CODE);

                } else {
                    Toast.makeText(this, "Please grant camera permission to use QR SCAN", Toast.LENGTH_SHORT).show();

                }
        }
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
                startActivityForResult(new Intent(AdminLeadGenerationActivity.this,
                        ScanQrCodeActivity.class), SCAN_QR_REQUEST_CODE);
            }
        }else{

            //Do something
            startActivityForResult( new Intent(AdminLeadGenerationActivity.this,
                    ScanQrCodeActivity.class), SCAN_QR_REQUEST_CODE);
        }

    }
}
