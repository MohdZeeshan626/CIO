package com.singleevent.sdk.View.RightActivity.admin.feedback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.model.User;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.adminSurvey.adapter.AttendeeUserListAdapter;
import com.singleevent.sdk.utils.DataBaseStorage;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener, AttendeeUserListAdapter.OnCardClickListner {

    private static final int REQUEST_CODE = 5000;
    private static final String TAG = FeedBackActivity.class.getCanonicalName();
    private static int agendaId;
    Toolbar toolbar;
    TextView tv_start, tv_ques;
    EditText et_search;
    AppDetails appDetails;
    TextView noitems, tvHeadingFeedback;
    List<User> userview;
    AttendeeUserListAdapter bookListAdapter;
    Events e;
    ArrayList<Events> events;
    LetterTileProvider tileProvider;
    private Context context;
    private float dpWidth, badgewidth;
    Bitmap letterTile;
    RecyclerView userlist;
    int tileSize;
    Resources res;
    AwesomeText close;
    ArrayList<Items> surveylist = new ArrayList<>();

    private String action;
    RadioGroup radioSurvey;
    RadioButton radio_anonymous;
    RadioButton radio_user;
    ImageView ivsmiling, ivnetural, iv_sad;

    Spinner spinner;
    int pos = 0;
    HashMap<Integer, String> agenda_set;
    ArrayList<String> spinner_text;
    int spinnerID, agenda_id;
    LinearLayout attendeeView;
    RelativeLayout feedbackView;
    private String dateTime, token;
    private TextView tv_checkInternet;
    private SwipeRefreshLayout swiperefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.act_admin_feedback);
        appDetails = Paper.book().read("Appdetails");

        token = Paper.book().read("token", "");
        res = getResources();
        context = this;
        tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);
        userview = new ArrayList<>();
        tileProvider = new LetterTileProvider(context);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.10F;
        badgewidth = displayMetrics.widthPixels * 0.05F;

        attendeeView = (LinearLayout) findViewById(R.id.attendeeView);
        feedbackView = (RelativeLayout) findViewById(R.id.feedbackView);
        userlist = (RecyclerView) findViewById(R.id.userlist);
        noitems = (TextView) findViewById(R.id.noitems);
        tvHeadingFeedback = (TextView) findViewById(R.id.tvHeadingFeedback);
        close = (AwesomeText) findViewById(R.id.close);
        radioSurvey = (RadioGroup) findViewById(R.id.radioSurvey);
        radio_anonymous = (RadioButton) findViewById(R.id.radio_anonymous);
        radio_user = (RadioButton) findViewById(R.id.radio_user);
        tv_checkInternet = (TextView) findViewById(R.id.tv_checkInternet);
        ivsmiling = (ImageView) findViewById(R.id.ivsmiling);
        ivnetural = (ImageView) findViewById(R.id.ivnetural);
        iv_sad = (ImageView) findViewById(R.id.iv_sad);
        tv_ques = (TextView) findViewById(R.id.tv_ques);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_start = (TextView) findViewById(R.id.tv_start);
        et_search = (EditText) findViewById(R.id.et_search);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        tvHeadingFeedback.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        feedbackView.setVisibility(View.GONE);
        attendeeView.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        tv_start.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        spinner = (Spinner) findViewById(R.id.section_list_spinner);
        agenda_set = new HashMap<Integer, String>();
        spinner_text = new ArrayList<>();
        setSpinnerValues();
        spinnerID = spinner.getSelectedItemPosition();
        getAgendatID(agenda_set.get(spinnerID));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerID = i;
                System.out.println("Agenda Selected Position : " + agenda_set.get(spinnerID));
                //getting agenda id

                agenda_id = getAgendatID(agenda_set.get(spinnerID));
                FeedBackActivity.agendaId = agenda_id;


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        iv_sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (surveylist.size() > 0)
                    sendingfeddback("Not Satisfied");
            }
        });

        ivnetural.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (surveylist.size() > 0)
                    sendingfeddback("Neutral");
            }
        });

        ivsmiling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (surveylist.size() > 0)
                    sendingfeddback("Satisfied");
            }
        });

        //listeners
        tv_start.setOnClickListener(this);
        et_search.setOnClickListener(this);

        bookListAdapter = new AttendeeUserListAdapter(this, userview);
        userlist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        userlist.setAdapter(bookListAdapter);
        bookListAdapter.setOnCardClickListner(this);

        if (!DataBaseStorage.isInternetConnectivity(this)) {
            userview = Paper.book(appDetails.getAppId()).read("AdminAttendees", new ArrayList<User>());
            bookListAdapter = new AttendeeUserListAdapter(FeedBackActivity.this, userview);
            userlist.setAdapter(bookListAdapter);
        }

        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                filter(arg0.toString());
            }
        });


        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        swiperefresh.setRefreshing(true);
                        getuser();
                    }
                }
        );

    }

    public void onRadioButtonClicked(View v) {


        //is the current radio button now checked?
        boolean checked = ((RadioButton) v).isChecked();

        int i = v.getId();
        if (i == R.id.radio_user) {
            if (checked)
                radio_user.setTypeface(null, Typeface.BOLD_ITALIC);
            radio_anonymous.setTypeface(null, Typeface.NORMAL);


        } else if (i == R.id.radio_anonymous) {
            if (checked)

                radio_anonymous.setTypeface(null, Typeface.BOLD_ITALIC);
            radio_user.setTypeface(null, Typeface.NORMAL);
            startSurvey(null);

        }
    }


    private int getAgendatID(String s) {
        int agenda_id = 0;
        for (Map.Entry<Integer, String> entry : agenda_set.entrySet()) {
            String value = entry.getValue();
            Integer key = entry.getKey();
            // do something with key and/or tab
            if (value.equalsIgnoreCase(s)) {
                System.out.println("Agenda Selected Position : " + key + " " + value);
                agenda_id = key;
                break;
            }

        }
        return agenda_id;
    }

    private void setSpinnerValues() {
        events = Paper.book().read("Appevents");
        e = events.get(0);
        for (int i = 0; i < e.getTabsSize(); i++) {
            if (e.getTabs(i).getCheckvalue().equalsIgnoreCase("agenda0")) {
                pos = i;
                break;
            }
        }

        agenda_set.put(0, "Event Checkin");
        spinner_text.add("Event Checkin");
        for (int i = 0; i < e.getTabs(pos).getAgendaSize(); i++) {
            String day_str = "Day " + (i + 1) + " ";
            for (int j = 0; j < e.getTabs(pos).getAgenda(i).getDetailSize(); j++) {
                String temp_str = day_str + e.getTabs(pos).getAgenda(i).getDetail(j).getTopic();
                int agenda_id = e.getTabs(pos).getAgenda(i).getDetail(j).getAgendaId();
                agenda_set.put(agenda_id, temp_str);
                spinner_text.add(temp_str);
                System.out.println("Agenda Details Agenda  :  " + agenda_id + " " + temp_str);
            }
        }

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, spinner_text
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //if token is null call thread to get api
        if (token == null) startThread();
        else getuser();

        SpannableString s = new SpannableString("Feedback");
        setTitle(Util.applyFontToMenuItem(this, s));

        getAdminSurveyQuestions(null, true);

    }

    @Override
    protected void onStop() {
        super.onStop();
        radio_user.setSelected(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;


    }

    public void filter(String text) {
        List temp = new ArrayList();
        for (User d : userview) {

            if (d.getFirst_name().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        bookListAdapter.updateList(temp);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        Log.d(TAG, id + "");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {

                Bundle extras = data.getExtras();
                User user = (User) extras.getSerializable("UserItem");
                Log.d(TAG, user.toString());
                et_search.setText(user.getFirst_name());

            }

        } else {
            Toast.makeText(this, "Unable to get User Details", Toast.LENGTH_SHORT).show();
        }

    }

    private void getuser() {

        final ProgressDialog dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading the User");
        dialog.show();
        String tag_string_req = "getuser";
        String url = ApiList.Users + appDetails.getAppId() + "&admin_flag=attendee";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                tv_checkInternet.setVisibility(View.GONE);
                if (swiperefresh.isRefreshing()) {
                    swiperefresh.setRefreshing(false);
                }
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        parseuser(jObj.getJSONObject("responseString").getJSONArray("users"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", FeedBackActivity.this);


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), FeedBackActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (swiperefresh.isRefreshing()) {
                    swiperefresh.setRefreshing(false);
                }

                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", FeedBackActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), FeedBackActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    // Error_Dialog.show("Please Check Your Internet Connection", FeedBackActivity.this);
                    if (!DataBaseStorage.isInternetConnectivity(FeedBackActivity.this)) {
                        tv_checkInternet.setVisibility(View.VISIBLE);
                        userview = Paper.book(appDetails.getAppId()).read("AdminAttendees", new ArrayList<User>());
                        bookListAdapter = new AttendeeUserListAdapter(FeedBackActivity.this, userview);
                        userlist.setAdapter(bookListAdapter);
                    }
                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", token);
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

    private void parseuser(JSONArray responseString) {

        try {
            userview.clear();
            Gson gson = new Gson();

            Random r = new Random();

            for (int i = 0; i < responseString.length(); i++) {
                String eventString = responseString.getJSONObject(i).toString();
                User obj = gson.fromJson(eventString, User.class);
                obj.setColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));

                if (!Paper.book().read("userId", "").equals(obj.getUserid()))
                    userview.add(obj);
            }

            /*Shorting based in alphabet*/
            Collections.sort(userview, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getFirst_name().compareToIgnoreCase(o2.getFirst_name());
                }
            });

            Paper.book(appDetails.getAppId()).write("AdminAttendees", userview);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (userview.size() > 0) {
            bookListAdapter.notifyDataSetChanged();
        } else
            showview(false);


    }

    private void showview(boolean flag) {

        if (flag) {
            userlist.setVisibility(View.VISIBLE);
            noitems.setVisibility(View.GONE);
        } else {
            userlist.setVisibility(View.GONE);
            noitems.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void OnItemLongClicked(View view, User user, int position) {

        Log.d(TAG, position + user.toString() + "");


    }


    private void getAdminSurveyQuestions(final User user, boolean isAttendee) {
        surveylist.clear();
        final ProgressDialog dialog = new ProgressDialog(FeedBackActivity.this,
                R.style.MyAlertDialogStyle);
        dialog.setMessage("Getting Survey Questions...");
        if (!((Activity) context).isFinishing()) {
            //show dialog
            dialog.show();
        }

        String url = ApiList.GetAdminSurvey;

        String tag_string_req = "Survey";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                try {
                    if (dialog.isShowing())
                        dialog.dismiss();

                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        //  Error_Dialog.show(jObj.getString("responseString"), AdminSurveyRoot.this);
                        Gson gson = new Gson();
                        JSONArray jsonArray = jObj.getJSONArray("responseString");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            /* AdminSurveyModel model = gson.fromJson(obj.toString(),AdminSurveyModel.class);*/
                            Items model = gson.fromJson(obj.toString(), Items.class);
                            Log.d("HII", model.toString());
                            surveylist.add(model);
                        }


                        //store all survey in local storage
                        Paper.book(appDetails.getAppId()).write("AdminSurveyList", surveylist);


                        ArrayList<Items> questions = Paper.book(appDetails.getAppId()).read("AdminSurveyList", new ArrayList<Items>());
                        tv_ques.setText("Ques:- " + questions.get(0).getQuestion());
/*
                        *//*here store all submitted survey's attendee list*//*
                        JSONArray submitted_usr_arry = jObj.getJSONArray("submitted_users");
                        ArrayList<String> userIds = new ArrayList<>();
                        for (int i = 0; i < submitted_usr_arry.length(); i++) {

                            JSONObject object = submitted_usr_arry.getJSONObject(i);
                            userIds.add(object.getString("userid"));

                        }

                        //storing all user ids in paper db
                        if (userIds.size()>0)
                            Paper.book(appDetails.getAppId()).write("AdminAttendeesUsers",userIds);*/

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", FeedBackActivity.this);
                        } else if (!jObj.getBoolean("response")) {
                            Error_Dialog.show(jObj.getString("responseString"), FeedBackActivity.this);

                            ArrayList<Items> questions = Paper.book(appDetails.getAppId()).read("AdminSurveyList", new ArrayList<Items>());
                            if (questions.size() > 0)
                                tv_ques.setText("Ques:- " + questions.get(0).getQuestion());
                            else
                                Error_Dialog.show(jObj.getString("responseString"), FeedBackActivity.this);


                          /*  try {
                                *//*here store all submitted survey's attendee list*//*
                                JSONArray submitted_usr_arry = jObj.getJSONArray("submitted_users");
                                ArrayList<String> userIds = new ArrayList<>();
                                for (int i = 0; i < submitted_usr_arry.length(); i++) {

                                    JSONObject object = submitted_usr_arry.getJSONObject(i);
                                    userIds.add(object.getString("userid"));

                                }

                                //storing all user ids in paper db
                                if (userIds.size()>0)
                                    Paper.book(appDetails.getAppId()).write("AdminAttendeesUsers",userIds);

                            }catch (Exception e){
                                e.printStackTrace();
                            }
*/
                        }
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
                    Error_Dialog.show("Timeout", FeedBackActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, FeedBackActivity.this),
                            FeedBackActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    /*  Error_Dialog.show("Please Check Your Internet Connection", AdminSurveyActivity.this);*/

                    //store all survey in local storage
                    ArrayList<Items> offlinesur_list = Paper.book(appDetails.getAppId()).read("AdminSurveyList", new ArrayList<Items>());
                    surveylist = offlinesur_list;
                }

            }


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", "");
                params.put("appid", appDetails.getAppId());
                params.put("adminsurvey_flag", "1");
                params.put("adminsurvey_type", "attendee");


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
    public void OnItemClick(View view, User user, int position) {
        Log.d(TAG, "Position " + position + user.toString());
        /*et_search.setText(user.getFirst_name());*/
        //  Toast.makeText(context,"Attendee Selected ",Toast.LENGTH_SHORT).show();
        startSurvey(user);
        //getAdminSurveyQuestions(user,true);
    }

    private void startSurvey(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("AttendeeUser", user);
        bundle.putSerializable("surveyqueslist", surveylist);
        bundle.putInt("agenda_id", agenda_id);
        if (surveylist.size() > 0) {
            startActivity(new Intent(context, FeedbackDetails.class).putExtras(bundle));
            finish();
        }
        else
            Error_Dialog.show("No Survey Question Available", FeedBackActivity.this);

    }

    private void startThread() {

        final String[] decryptedString = new String[1];
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // your code here

                try {
                    FileInputStream fis = openFileInput(DataBaseStorage.F_I_L_ENCP2);
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    byte[] decrypted = DataBaseStorage.decryptData((HashMap<String, byte[]>) ois.readObject(),
                            DataBaseStorage.token_pass);
                    if (decrypted != null) {
                        //decryptedString[0] = new String(decrypted);
                        token = new String(decrypted);
                    }
                    ois.close();

                } catch (Exception e) {

                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //getting all the users for api call
                        getuser();

                    }
                });
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    private void sendingfeddback(final String toJson) {

        /*Storing offline */

        HashMap<String, JSONObject> singleUserRecords = new HashMap<>();

        JSONObject object = new JSONObject();
        JSONArray jsonArray1 = new JSONArray();
        try {
            object.put("userid", "anonymous");
            object.put("user_type", "default");
            object.put("agenda_id", agenda_id + "");
            JSONObject ansObj = new JSONObject();

            ansObj.put("question", surveylist.get(0).getQuestion());
            ansObj.put("answer", toJson);

            object.put("ans_data", ansObj);
            System.out.println("JSON Object : " + object.toString());

            jsonArray1 = new JSONArray();
            jsonArray1.put(object);
            singleUserRecords.put(Paper.book().read("userId", ""), object);

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        /*
         * */


        if (Paper.book(appDetails.getAppId()).read("offAdminSurveyAns", new HashMap<String, JSONObject>()).size() == 0) {
            Paper.book(appDetails.getAppId()).write("offAdminSurveyAns", singleUserRecords);

        } else {
            HashMap<String, JSONObject> offvalue =
                    Paper.book(appDetails.getAppId()).read("offAdminSurveyAns", new HashMap<String, JSONObject>());
            HashMap<String, JSONObject> ofLinUsrAnsList = new HashMap<>();
            ofLinUsrAnsList.putAll(offvalue);
            ofLinUsrAnsList.put(Paper.book().read("userId", ""), object);

            Paper.book(appDetails.getAppId()).write("offAdminSurveyAns", ofLinUsrAnsList);
        }

        //getting offline survey answers with user details
        final JSONArray retriveJSONArray;
        retriveJSONArray = new JSONArray();


        HashMap<String, JSONObject> retriveofLinUsrAnsList = new HashMap<>();
        retriveofLinUsrAnsList = Paper.book(appDetails.getAppId()).read("offAdminSurveyAns", new HashMap<String, JSONObject>());
        for (Map.Entry<String, JSONObject> entry : retriveofLinUsrAnsList.entrySet()) {
            //  String value = String.valueOf(entry.getValue());
            String key = entry.getKey();
            JSONObject value = entry.getValue();


            retriveJSONArray.put(value);

            System.out.println("JSON Retrived : " + retriveJSONArray);

        }


        dateTime = String.valueOf(System.currentTimeMillis());
        final ProgressDialog dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialog.setMessage("Sending Feedback...");
        dialog.show();
        String tag_string_req = "Schdule";
        String url = ApiList.PostAdminSurveyFeedBack;
        //  url="http://104.131.76.15:3000/api/event/registration_answers";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), FeedBackActivity.this);
                        /*After submitting all the answers , i am deleting from local storage*/
                        Paper.book(appDetails.getAppId()).write("offAdminSurveyAns", new HashMap<String, JSONObject>());


                     /*   *//*here store all submitted survey's attendee list*//*
                        JSONArray jsonArray = jObj.getJSONArray("submitted_users");
                        ArrayList<String> userIds = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            userIds.add(object.getString("userid"));

                        }

                        //storing all user ids in paper db
                        if (userIds.size()>0)
                            Paper.book(appDetails.getAppId()).write("AdminAttendeesUsers",userIds);
*/


                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", FeedBackActivity.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), FeedBackActivity.this);

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
                    Error_Dialog.show("Timeout", FeedBackActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error,
                            FeedBackActivity.this), FeedBackActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Survey Submitted Offline.", FeedBackActivity.this);

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("appid", appDetails.getAppId());
                params.put("userResponse", retriveJSONArray.toString());
                params.put("adminsurvey_flag", "1");

                System.out.println(params);
                return params;
            }
        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        //submitsurveyans();
    }
}
