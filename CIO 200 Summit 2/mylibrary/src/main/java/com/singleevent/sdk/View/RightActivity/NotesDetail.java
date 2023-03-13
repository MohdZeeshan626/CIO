package com.singleevent.sdk.View.RightActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.LocalArraylist.Notes;
import com.singleevent.sdk.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

/**
 * Created by Admin on 6/16/2017.
 */

public class NotesDetail extends AppCompatActivity implements View.OnClickListener {

    String type;
    int id;
    EditText notes;
    AwesomeText cancel, save;
    ImageView deletenode;
    HashMap<Integer, Notes> Noteslist;
    AppDetails appDetails;
    Context context;
    String action;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_notesdetail);
        appDetails = Paper.book().read("Appdetails");
        context = NotesDetail.this;


        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        id = getIntent().getExtras().getInt("pos");
        type = getIntent().getExtras().getString("type");

        cancel = (AwesomeText) findViewById(R.id.cancel);
        save = (AwesomeText) findViewById(R.id.save);
        notes = (EditText) findViewById(R.id.notes);
        deletenode=(ImageView)findViewById(R.id.deletenode);
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        deletenode.setOnClickListener(this);

        if (type.equalsIgnoreCase("agenda")) {
            action = "AgendaNote";
            Noteslist = Paper.book(appDetails.getAppId()).read("AgendaNote", new HashMap<Integer, Notes>());

        } else if (type.equalsIgnoreCase("exhibitor")) {
            action = "ExhibitorNote";
            Noteslist = Paper.book(appDetails.getAppId()).read("ExhibitorNote", new HashMap<Integer, Notes>());

        } else {
            action = "SponsorNote";
            Noteslist = Paper.book(appDetails.getAppId()).read("SponsorNote", new HashMap<Integer, Notes>());
        }

        Notes n = Noteslist.get(id);

        // setting previous notes

        //notes.setText(n.getNotes());


        // make edittext link clickable
        notes.setMovementMethod(LinkMovementMethod.getInstance());
        // Setup my Spannable with clickable URLs
        Spannable spannable = new SpannableString(n.getNotes());
        Linkify.addLinks(spannable, Linkify.WEB_URLS);
        // The fix: Append a zero-width space to the Spannable
        CharSequence text = TextUtils.concat(spannable, "\u200B");

// Use it!
        notes.setText(n.getNotes());


    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.cancel) {
            onBackPressed();

        } else if (i == R.id.save) {
            /*Addnotes(id, notes.getText().toString(), Noteslist.containsKey(id) ? "update" : "create", Noteslist.get(id).getName());
            Noteslist.put(id, new Notes(id, notes.getText().toString(), type, Noteslist.get(id).getName(), String.valueOf(System.currentTimeMillis())));*/
            String timeinmilli=String.valueOf(System.currentTimeMillis());
            Addnotes(type,id,notes.getText().toString(), Noteslist.containsKey(id) ? "update" : "create",Noteslist.get(id).getName(),timeinmilli);
            Noteslist.put(id, new Notes(id, notes.getText().toString(), type, Noteslist.get(id).getName(), timeinmilli));
            Paper.book(appDetails.getAppId()).write(action, Noteslist);
            new CountDownTimer(2000, 1000) {

                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    finish();
                }
            }.start();
        }
        else if (i == R.id.deletenode) {
            String timeinmilli=String.valueOf(System.currentTimeMillis());
            Addnotes(type,id,notes.getText().toString(), Noteslist.containsKey(id) ? "delete" : "create",Noteslist.get(id).getName(),timeinmilli);
            Noteslist.remove(id);
            Paper.book(appDetails.getAppId()).write(action, Noteslist);
            new CountDownTimer(2000, 1000) {

                public void onTick(long millisUntilFinished) {
    }

                public void onFinish() {
                    finish();
                }
            }.start();


        }

    }
    private void Addnotes(final String type,final int id, final String notes, final String action, final String name,final String time) {

        final ProgressDialog dialog = new ProgressDialog(NotesDetail.this,R.style.MyAlertDialogStyle);
        if(action.equalsIgnoreCase("update"))
        dialog.setMessage("Adding Note");
        else
            dialog.setMessage("deleting Note");
        dialog.show();
        String tag_string_req = "Note";
        String url = ApiList.Notes;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), NotesDetail.this);
                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), NotesDetail.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Paper.book("Sync").write(appDetails.getAppId(), true);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", NotesDetail.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), NotesDetail.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", NotesDetail.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book(appDetails.getAppId()).read("userId", ""));
                params.put("action", action);
                params.put("type", type);
                params.put("notes", notes);
                params.put("type_id", String.valueOf(id));
                params.put("appid", appDetails.getAppId());
                params.put("type_name", name);
                params.put("last_updated", time);
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
}
