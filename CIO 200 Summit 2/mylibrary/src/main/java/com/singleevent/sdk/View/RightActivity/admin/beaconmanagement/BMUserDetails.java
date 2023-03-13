package com.singleevent.sdk.View.RightActivity.admin.beaconmanagement;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
//import com.google.android.gms.vision.text.Line;
//import com.google.android.gms.vision.text.Text;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.adminSurvey.UsersModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class BMUserDetails extends AppCompatActivity implements View.OnClickListener {
    public static final String USER_DETAILS = "User_Details";
    public static final String RESULT_DETAILS = "result_details";
    public static final int REQUEST_DEVICE_ID = 100;
    public static final int REQUEST_BT = 10;
    UsersModel users;
    Toolbar toolbar;
    AppDetails appDetails;
    String token, theme_color;
    TextView user_name, user_company, user_designation, user_beaconid, assignbeacon_btn, savebeacon_btn, changebeacon_btn;
    private double CellWidth;
    ImageView userpic;
    LinearLayout assignbeacon_ll, changebeacon_ll;
    String user_name_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmuser_details);
        appDetails = Paper.book().read("Appdetails");
        theme_color = appDetails.getTheme_color();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(theme_color));

        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();

        //getting the user details from intent
        users = (UsersModel) extras.getSerializable(USER_DETAILS);

        //setting the resources
        setResources();
        //setting the user values
        setValues();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //title
        SpannableString s = new SpannableString("Beacon Management");
        setTitle(Util.applyFontToMenuItem(this, s));
    }

    private void setValues() {
        String beaconid = users.getBeacon_id();
        //check beacon is assigned to the user
        if (beaconid.equalsIgnoreCase("")) {
            //if not assigned set empty value to beacon id field
            user_beaconid.setText("");
            assignBeaconView(true);
        } else {
            //if assigned set beacon id to the field
            user_beaconid.setText(beaconid);
            assignBeaconView(false);
        }
        //user name
        user_name_str = users.getFirst_name() + " " + users.getLast_name();
        user_name.setText(user_name_str);
        //company name
        user_company.setText(users.getCompany());
        //designation
        user_designation.setText(users.getDesignation());
        String bm = users.getProfile_pic();
        //profile pic
        Glide.with(getApplicationContext())

                .load(bm.equalsIgnoreCase("") ? R.drawable.round_user : bm)
                .asBitmap()
                .placeholder(R.drawable.round_user)
                .error(R.drawable.round_user)
                .into(new BitmapImageViewTarget(userpic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(),
                                Bitmap.createScaledBitmap(resource, (int) CellWidth, (int) CellWidth, false));
                        drawable.setCircular(true);
                        userpic.setImageDrawable(drawable);
                    }
                });

    }

    private void setResources() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        CellWidth = (displayMetrics.widthPixels * 0.20F);
        token = Paper.book().read("token", "");
        user_name = (TextView) findViewById(R.id.bm_ud_name);
        user_company = (TextView) findViewById(R.id.bm_ud_company);
        user_designation = (TextView) findViewById(R.id.bm_ud_designation);
        user_beaconid = (TextView) findViewById(R.id.bm_ud_beaconID);
        userpic = (ImageView) findViewById(R.id.bm_ud_img);
        assignbeacon_btn = (TextView) findViewById(R.id.bm_ud_assignbeacon);
        savebeacon_btn = (TextView) findViewById(R.id.bm_ud_save);
        changebeacon_btn = (TextView) findViewById(R.id.bm_ud_changebeacon);
        changebeacon_ll = (LinearLayout) findViewById(R.id.changebeacon_ll);
        assignbeacon_ll = (LinearLayout) findViewById(R.id.assignbeacon_ll);

        assignbeacon_btn.setBackgroundColor(Color.parseColor(theme_color));
        savebeacon_btn.setBackgroundColor(Color.parseColor(theme_color));
        changebeacon_btn.setBackgroundColor(Color.parseColor(theme_color));


        assignbeacon_btn.setOnClickListener(this);
        changebeacon_btn.setOnClickListener(this);
        savebeacon_btn.setOnClickListener(this);

        assignBeaconView(true);
    }

    private void assignBeaconView(boolean b) {
        //if beacon is not assigned -  assign beacon button will be visible
        //if beacon is assigned - save and change beacon button will be visible
        if (b) {
            assignbeacon_ll.setVisibility(View.VISIBLE);
            changebeacon_ll.setVisibility(View.GONE);
        } else {
            assignbeacon_ll.setVisibility(View.GONE);
            changebeacon_ll.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bm_ud_assignbeacon || i == R.id.bm_ud_changebeacon) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_BT);
            } else {
                getRequiredPermissions();
                /*Intent intent = new Intent(BMUserDetails.this, DeviceList.class);
                intent.putExtra(DeviceList.USER_NAME, user_name_str);
                startActivityForResult(intent, REQUEST_DEVICE_ID);*/
            }

        } else if (i == R.id.bm_ud_save) {
            savebeacon();
        }

    }

    private void savebeacon() {

        //save beacon to the user
        final ProgressDialog dialog = new ProgressDialog(BMUserDetails.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Adding Beacon...");
        dialog.show();
        String tag_string_req = "Rating";
        String url = ApiList.ASSIGN_BEACON;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Toast.makeText(BMUserDetails.this, "Beacon Added", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), BMUserDetails.this);
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
                    Error_Dialog.show("Timeout", BMUserDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, BMUserDetails.this), BMUserDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", BMUserDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", users.getUserid());
                params.put("beacon_id", user_beaconid.getText().toString());
                params.put("appid", appDetails.getAppId());
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DEVICE_ID) {
            if (resultCode == RESULT_OK) {
                assignBeaconView(false);
                String beaconid = data.getStringExtra(RESULT_DETAILS);
                //assign beacon
                if (!(beaconid == null || beaconid.equalsIgnoreCase("")))
                    user_beaconid.setText(beaconid);
            }
        } else if (requestCode == REQUEST_BT) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(BMUserDetails.this, "BT ON", Toast.LENGTH_SHORT);
            }
        }
    }

    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATIONS = 11;

    private void getRequiredPermissions() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != 0 && ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, MY_PERMISSIONS_REQUEST_READ_LOCATIONS);

        } else {
            Intent intent = new Intent(BMUserDetails.this, DeviceList.class);
            intent.putExtra(DeviceList.USER_NAME, user_name_str);
            startActivityForResult(intent, REQUEST_DEVICE_ID);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    Intent intent = new Intent(BMUserDetails.this, DeviceList.class);
                    intent.putExtra(DeviceList.USER_NAME, user_name_str);
                    startActivityForResult(intent, REQUEST_DEVICE_ID);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }

    }
}

