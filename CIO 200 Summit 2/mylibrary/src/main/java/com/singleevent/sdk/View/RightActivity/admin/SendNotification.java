package com.singleevent.sdk.View.RightActivity.admin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.View.RightActivity.admin.checkin.EventUsersActivity;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.model.ResponseString;
import com.singleevent.sdk.View.RightActivity.admin.model.SegmentModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by webMOBI on 11/29/2017.
 */

public class SendNotification extends AppCompatActivity {

    private static final int REQUEST_FOR_CHOOSE_ONE_DEVICE = 11;
    private Toolbar toolbar;
    private EditText etContent, etTitle;
    private TextView tv_oneDevice, tv_device_bySegment, tvDate, tvHour, tvMinute;
    private LinearLayout ll_Button, linearlayout_schedule;
    private Button btnSend;
    AppDetails appDetails;
    Spinner spinner, spinner_time;
    RadioButton radio_one_device, radio_device_by_segment, radio_immediately, radio_schedule, radio_broadcast;
    RadioGroup platformAndReceptsRadioGroup, deliveryScheduleRadioGroup;
    private static String[] minuteArray = new String[60];
    int day, month, year1, hour, minutes;
    private ArrayList<String> notifUserArrayList;
    long millisec;

    private int mYear, mMonth, mDay;

    private static String broadcastType = "broadcast", delivery_schedule = "no";
    private static String timePm = "PM", eventTime = "";
    private String segment_id = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notfication);

        Paper.init(this);
        notifUserArrayList = new ArrayList<>();

        appDetails = Paper.book().read("Appdetails");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etContent = (EditText) findViewById(R.id.etContent);
        ll_Button = (LinearLayout) findViewById(R.id.ll_button);
        platformAndReceptsRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_PR);
        deliveryScheduleRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_DS);
        radio_one_device = (RadioButton) findViewById(R.id.radio_one_device);
        radio_device_by_segment = (RadioButton) findViewById(R.id.radio_device_by_segment);
        radio_broadcast = (RadioButton) findViewById(R.id.radio_broadcast);
        radio_immediately = (RadioButton) findViewById(R.id.radio_immediately);
        radio_schedule = (RadioButton) findViewById(R.id.radio_schedule);

        spinner = (Spinner) findViewById(R.id.spinner);

        spinner_time = (Spinner) findViewById(R.id.spinner_time);
        tv_oneDevice = (TextView) findViewById(R.id.tv_oneDevice);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvHour = (TextView) findViewById(R.id.tvHour);
        tvMinute = (TextView) findViewById(R.id.tvMinute);
        linearlayout_schedule = (LinearLayout) findViewById(R.id.linearlayout_schedule);
        tv_device_bySegment = (TextView) findViewById(R.id.tv_device_bySegment);
        btnSend = (Button) findViewById(R.id.btnSend);

        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        tv_oneDevice.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        tv_device_bySegment.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);


        if (Build.VERSION.SDK_INT >= 21) {

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{

                            new int[]{-android.R.attr.state_enabled}, //disabled
                            new int[]{android.R.attr.state_enabled} //enabled
                    },
                    new int[]{

                            Color.parseColor(appDetails.getTheme_color()) //disabled
                            , Color.parseColor(appDetails.getTheme_selected()) //enabled

                    }
            );


            radio_one_device.setButtonTintList(colorStateList);//set the color tint list
            radio_one_device.invalidate(); //could not be necessary
            radio_device_by_segment.setButtonTintList(colorStateList);
            radio_device_by_segment.invalidate();
            radio_schedule.setButtonTintList(colorStateList);
            radio_schedule.invalidate();
            radio_immediately.setButtonTintList(colorStateList);
            radio_immediately.invalidate();
            radio_broadcast.setButtonTintList(colorStateList);
            radio_broadcast.invalidate();
        }


        btnSend.setBackground(Util.setdrawable(SendNotification.this, R.drawable.healthpostbut, Color.parseColor(appDetails.getTheme_color())));


        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor(appDetails.getTheme_selected()), Color.parseColor(appDetails.getTheme_color())});
        gd.setCornerRadius(0f);
        gd.setStroke(2, Color.parseColor(appDetails.getTheme_strips()));

     //   btnSend.setBackground(gd);

        Context wrapper = new ContextThemeWrapper(this, R.style.MyAlertDialogStyle);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(wrapper,
                R.array.platform_array, R.layout.spinner_item_layout);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        String compareValue = "Android";
        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spinner.setSelection(spinnerPosition);

        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, R.layout.spinner_item_layout);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_time.setAdapter(timeAdapter);

        if (!timePm.equals(null)) {
            int spinnerPosition = adapter.getPosition(timePm);
            spinner_time.setSelection(spinnerPosition);
        }
        spinner_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_time.setSelection(position);
                if (position == 0) {
                    timePm = "PM";
                } else {
                    timePm = "AM";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tv_oneDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendNotification.this, ChooseDeviceIdActivity.class);
                startActivityForResult(intent, REQUEST_FOR_CHOOSE_ONE_DEVICE);
            }
        });

        tv_device_bySegment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> seg_names = new ArrayList<>();
                arr = new String[responseStringList.size()];
                for (int i = 0; i < responseStringList.size(); i++) {
                    seg_names.add(responseStringList.get(i).getSegmentName());
                    arr[i] = seg_names.get(i);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(SendNotification.this, R.style.CalendarDialog);
                builder.setTitle("Select Segment Name")
                        .setItems(arr, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                tv_device_bySegment.setText(arr[which]);
                                segment_id = responseStringList.get(which).getSegmentId();
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        tvHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] arr = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                        "12"};
                AlertDialog.Builder builder = new AlertDialog.Builder(SendNotification.this, R.style.CalendarDialog);
                builder.setTitle("Hours")
                        .setItems(arr, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                hour = Integer.parseInt(arr[which]);
                                tvHour.setText(arr[which]);
                            }
                        });
                builder.create();
                builder.show();

            }
        });

        tvMinute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < 60; i++) {
                    minuteArray[i] = String.format("%02d", i);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(SendNotification.this, R.style.MyAlertDialogStyle);
                builder.setTitle("Minutes")
                        .setItems(minuteArray, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                minutes = Integer.parseInt(minuteArray[which]);
                                tvMinute.setText(minuteArray[which]);
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call api to send notification
                convertMilliSec();
                if (userInputIsValid()) {
                    String temp_email = Paper.book().read("Email");
                    if (appDetails.getAppId().equalsIgnoreCase("e51674d0e7f74adc0936a69a834be9912ed4") && temp_email.equalsIgnoreCase("frank12@webmobi.in")) {
                        Toast.makeText(SendNotification.this, "Notification Send Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        sendNotifictionApiCall();
                    }
                }
            }
        });
    }

    private void convertMilliSec() {
        Calendar cal = Calendar.getInstance();
        cal.set(year1, (1 + month), day, hour, minutes);
        eventTime = year1 + "-" + (1 + month) + "-" + day + " " + tvHour.getText() + ":" + tvMinute.getText() + " " + timePm.toUpperCase();

        millisec = cal.getTimeInMillis();


    }

    private boolean userInputIsValid() {


        if (etTitle.getText().toString().length() < 2 || etTitle.getText().toString().equals("")) {
            Toast.makeText(this, "Title length is short .", Toast.LENGTH_SHORT).show();
        } else if (etContent.getText().toString().length() < 2) {
            Toast.makeText(this, "Message length is short .", Toast.LENGTH_SHORT).show();
        } else if (etTitle.getText().toString().length() >= 2 && etContent.getText().length() >= 2) {

            if (!delivery_schedule.equals("yes"))
                return true;
            else if (!tvDate.getText().equals("DD-MM-YYYY") && !tvMinute.getText().equals("M") && !tvHour.getText().equals("H")) {
                return true;
            } else {

                Toast.makeText(this, "Please, Check Fields.", Toast.LENGTH_SHORT).show();

                return false;
            }

        } else {
            Toast.makeText(this, "Please, Check Fields.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        int id = view.getId();
        if (id == R.id.radio_broadcast) {
            if (checked) {
                tv_oneDevice.setVisibility(View.GONE);
                tv_device_bySegment.setVisibility(View.GONE);
                broadcastType = "broadcast";
            }
        } else if (id == R.id.radio_one_device) {
            if (checked) {
                tv_oneDevice.setVisibility(View.VISIBLE);
                tv_device_bySegment.setVisibility(View.GONE);
                broadcastType = "one_user";
            }
        } else if (id == R.id.radio_device_by_segment) {
            if (checked) {
                tv_oneDevice.setVisibility(View.GONE);
                tv_device_bySegment.setVisibility(View.VISIBLE);
                broadcastType = "segment";
                getSegmentByCallApi();
            }
        }
    }

    public void onDeliveryScheduleRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        int id = view.getId();
        if (id == R.id.radio_immediately) {
            if (checked) {
                delivery_schedule = "no";
                linearlayout_schedule.setVisibility(View.GONE);

            }
        } else if (id == R.id.radio_schedule) {
            if (checked) {
                delivery_schedule = "yes";
                eventTime = "";
                linearlayout_schedule.setVisibility(View.VISIBLE);


            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Send Notification");
        setTitle(Util.applyFontToMenuItem(this, s));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;

    }

    public void calendarClick(View view) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.CalendarDialog,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        day = dayOfMonth;
                        month = monthOfYear;
                        year1 = year;

                        tvDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }


                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void sendNotifictionApiCall() {

        String tag = "send_notification_to_device";


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ApiList.SendNotification, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("response")) {

                        Toast.makeText(SendNotification.this,
                                jsonObject.getString("responseString"), Toast.LENGTH_SHORT).show();
                    } else if (!jsonObject.getBoolean("response")) {
                        Toast.makeText(SendNotification.this,
                                jsonObject.getString("responseString"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SendNotification.this,
                        "Sorry try again.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", "46029");
                params.put("appid", appDetails.getAppId());
                params.put("app_name", appDetails.getAppName());
                params.put("msg_type", "text");
                params.put("msg_title", etTitle.getText().toString());
                params.put("message", etContent.getText().toString());
                params.put("notification_type", broadcastType);
                params.put("broadcast_type", spinner.getSelectedItem().toString().toLowerCase());
                if (notifUserArrayList.size() > 0) {
                    params.put("notify_userid", event_user_id);
                } else {
                    params.put("notify_userid", "");
                }
                params.put("segment_id", segment_id);
                params.put("schedule_flag", delivery_schedule);
                if (delivery_schedule.equals("yes")) {
                    params.put("scheduled_date", eventTime);
                } else {
                    params.put("scheduled_date", "");
                }


                return params;
            }
        };
        stringRequest.setShouldCache(false);
        App.getInstance().addToRequestQueue(stringRequest, tag);

    }

    //Declaring variables for segments
    List<ResponseString> responseStringList;
    String[] arr;

    private void getSegmentByCallApi() {
        final ProgressDialog pDialog = new ProgressDialog(SendNotification.this, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                ApiList.GetSegment + appDetails.getAppId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        responseStringList = new ArrayList<>();
                        try {
//                            JSONObject jsonObject = new JSONObject(response);

                            SegmentModel model = new Gson().fromJson(response, SegmentModel.class);
                            if (model.getResponse()) {
                                List<ResponseString> resp = model.getResponseString();

                                responseStringList.addAll(resp);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
            }
        });

        App.getInstance().addToRequestQueue(stringRequest);

    }

    String event_user_id;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_CHOOSE_ONE_DEVICE) {

            /*if(resultCode==11){

            }*/
            notifUserArrayList = new ArrayList<>();
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras != null)
                    notifUserArrayList = (ArrayList<String>) extras.getSerializable("user_details");
                event_user_id = extras.getString("event_user_id", "");
            }


        }
    }
}
