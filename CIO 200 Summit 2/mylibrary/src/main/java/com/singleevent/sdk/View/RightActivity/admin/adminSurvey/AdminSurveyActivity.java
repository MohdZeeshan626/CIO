package com.singleevent.sdk.View.RightActivity.admin.adminSurvey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
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
import android.widget.EditText;
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

import butterknife.ButterKnife;
import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

public class AdminSurveyActivity extends AppCompatActivity implements View.OnClickListener, AttendeeUserListAdapter.OnCardClickListner {

    private static final int REQUEST_CODE = 5000;
    private static final String TAG = AdminSurveyActivity.class.getCanonicalName();
    Toolbar toolbar;
    TextView tv_start;
    EditText et_search;
    AppDetails appDetails;
    TextView noitems;
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
    private String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.act_admin_survey);

        appDetails = Paper.book().read("Appdetails");
        ButterKnife.bind(this);

        token = Paper.book().read("token", "");

        res = getResources();
        context = this;
        tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);
        userview = new ArrayList<>();
        tileProvider = new LetterTileProvider(context);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.10F;
        badgewidth = displayMetrics.widthPixels * 0.05F;
        userlist = (RecyclerView) findViewById(R.id.userlist);
        noitems = (TextView) findViewById(R.id.noitems);
        close = (AwesomeText) findViewById(R.id.close);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_start = (TextView) findViewById(R.id.tv_start);
        et_search = (EditText) findViewById(R.id.et_search);


        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        tv_start.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
       /* GradientDrawable bgShape = (GradientDrawable) tv_start.getBackground();
        bgShape.setColor(Color.parseColor(appDetails.getTheme_selected()));*/
        //listeners
        tv_start.setOnClickListener(this);
        et_search.setOnClickListener(this);

        bookListAdapter = new AttendeeUserListAdapter(this, userview);
        userlist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        userlist.setAdapter(bookListAdapter);
        bookListAdapter.setOnCardClickListner(this);

        if (!DataBaseStorage.isInternetConnectivity(this)){
            userview = Paper.book(appDetails.getAppId()).read("AdminAttendees",new ArrayList<User>());
            bookListAdapter = new AttendeeUserListAdapter(AdminSurveyActivity.this,userview);
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


    }

    @Override
    protected void onResume() {
        super.onResume();

        //if token is null call thread to get api
        if (token == null) startThread();
        else getuser();

        SpannableString s = new SpannableString("Admin Survey");
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

    public void filter(String text) {
        List<User> temp = new ArrayList();
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
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        parseuser(jObj.getJSONObject("responseString").getJSONArray("users"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", AdminSurveyActivity.this);


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), AdminSurveyActivity.this);
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
                    Error_Dialog.show("Timeout", AdminSurveyActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), AdminSurveyActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", AdminSurveyActivity.this);
                    if (!DataBaseStorage.isInternetConnectivity(AdminSurveyActivity.this)){
                     //   tv_checkInternet.setVisibility(View.VISIBLE);
                        userview = Paper.book(appDetails.getAppId()).read("AdminAttendees",new ArrayList<User>());
                        bookListAdapter = new AttendeeUserListAdapter(AdminSurveyActivity.this,userview);
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

            Paper.book(appDetails.getAppId()).write("AdminAttendees",userview);

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
        Log.d(TAG, "Position " + position + user.toString());

    }

    private void getAdminSurveyQuestions(final User user) {
        surveylist.clear();
        final ProgressDialog dialog = new ProgressDialog(AdminSurveyActivity.this,
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
                if (dialog.isShowing())
                    dialog.dismiss();
                try {
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

                        //start survey activity
                        startSurvey(user);
                        // initView();

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", AdminSurveyActivity.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), AdminSurveyActivity.this);
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
                    Error_Dialog.show("Timeout", AdminSurveyActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, AdminSurveyActivity.this),
                            AdminSurveyActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", AdminSurveyActivity.this);

                    //store all survey in local storage
                    ArrayList<Items> offlinesur_list = Paper.book(appDetails.getAppId()).read("AdminSurveyList", new ArrayList<Items>());
                    surveylist = offlinesur_list;
                    //start survey activity
                    if (surveylist.size()>0) {

                        int flag=0;

                       ArrayList<String> submitd_sury_usrIds =  Paper.book(appDetails.getAppId()).read("AdminAttendeesUsers",new ArrayList<String>());
                        for (int usr=0;usr<submitd_sury_usrIds.size();usr++){
                            if (user.getUserid().equals(submitd_sury_usrIds.get(usr))){
                                flag=1;
                                break;
                            }
                        }

                        if (flag!=1)
                        startSurvey(user);
                        else {
                            Error_Dialog.show("Survey already submitted.", AdminSurveyActivity.this);
                        }
                    }
                }

            }


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", user.getUserid());
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
        et_search.setText(user.getFirst_name());
        //  Toast.makeText(context,"Attendee Selected ",Toast.LENGTH_SHORT).show();
        //  startSurvey(user);
        getAdminSurveyQuestions(user);
    }

    private void startSurvey(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("AttendeeUser", user);
        bundle.putSerializable("surveyqueslist", surveylist);
        startActivity(new Intent(context, AdminSurveyRoot.class).putExtras(bundle));
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
}
