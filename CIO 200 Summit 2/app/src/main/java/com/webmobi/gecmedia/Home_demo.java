package com.webmobi.gecmedia;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import io.paperdb.Paper;

/**
 * Created by  on 4/12/2018.
 */

public class Home_demo extends AppCompatActivity {


    public static ArrayList<Events> eventsToDisplay = new ArrayList<Events>();

    EditText appname;
    File eventDir, jsonFile;
    private String dir, appID;
    RelativeLayout preview;
    String baseUrl = "https://webmobi.s3.amazonaws.com/nativeapps/";
    String filename = "app.json";
    File myFolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_home_demo);
        Paper.init(this);
        dir = getFilesDir() + File.separator + "Demo" + File.separator;
        myFolder = new File(getFilesDir() + "/Demo");
        if (!myFolder.exists()) myFolder.mkdir();
        appname = (EditText) findViewById(R.id.appname);
        preview = (RelativeLayout) findViewById(R.id.preview);


        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!appname.getText().toString().isEmpty()) {
                    appID = appname.getText().toString();
                    String query="";
                    try {
                        query = URLEncoder.encode( appname.getText().toString(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String jsonUrl = baseUrl +query + "/temp/appData.json";
                    //String jsonUrl = baseUrl + appID + "/temp/appData.json";
                    loadjson(jsonUrl);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter the AppNew Name", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public String loadStaticjson(){

        String returnJSONString="";
        JSONArray jsonobject = new JSONArray();
        JSONObject jsonObj = new JSONObject();
//

        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = getResources().getAssets()
                    .open("App_json.json", Context.MODE_WORLD_READABLE);
//                fIn = stream;
            isr = new InputStreamReader(fIn, "UTF-8");
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                returnString.append(line);
//                    System.out.println(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        try {

//            jsonobject = new JSONArray(returnString.toString());
            returnJSONString = returnString.toString();
            jsonObj = new JSONObject(returnString.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(jsonObj);
        return returnJSONString;

    }




    private void loadjson(String jsonUrl) {

        final ProgressDialog pDialog = new ProgressDialog(Home_demo.this,R.style.MyAlertDialogStyle);
        pDialog.setMessage("Preview the AppNew ...");
        pDialog.show();
        String tag_string_req = "Updating";
        System.out.println("Url " + jsonUrl);
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, jsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                JSONObject res = null;
                try {
                    res = new JSONObject(response);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                String jsonResponse = response;


                try {
                    eventDir = new File(dir + appID);
                    System.out.println(eventDir);
                    if (!eventDir.exists()) {
                        eventDir.mkdir();
                        jsonFile = new File(eventDir, filename);
                        Files.write(jsonResponse, jsonFile, Charset.defaultCharset());
                        loadevent(appID);
                    } else {
                        eventDir.mkdir();
                        jsonFile = new File(eventDir, filename);
                        Files.write(jsonResponse, jsonFile, Charset.defaultCharset());
                        loadevent(appID);
                       // loadevent(appID);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", Home_demo.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, Home_demo.this), Home_demo.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", Home_demo.this);
                }

            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(
                    NetworkResponse response) {

                String strUTF8 = null;
                try {
                    strUTF8 = new String(response.data, "UTF-8");

                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }
                return Response.success(strUTF8,
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        ;

        App.getInstance().addToRequestQueue(jsonRequest, tag_string_req);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }

    private void callingmain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                appname.setText("");
                Intent sending;
                sending = new Intent(Home_demo.this, MainActivity.class);
                startActivity(sending);

            }
        }, 1 * 1000); // wait for 5 seconds

    }


    private void loadevent(String appID) {

        eventDir = new File(dir + appID);
        jsonFile = new File(eventDir, filename);

        try {
            showjs(jsonFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showjs(File jsonFile) throws IOException {

        Paper.book().delete("Islogin");

        String contents = Files.toString(jsonFile, Charset.defaultCharset());
       // String contents = loadStaticjson();
        System.out.println("File Contents : " + contents);
        eventsToDisplay = new ArrayList<>();
        Gson gson = new Gson();


        JSONObject args = null;
        try {
            args = new JSONObject(contents);
            AppDetails obj = gson.fromJson(args.toString(), AppDetails.class);
            Paper.book().write("Appdetails", obj);
            JSONArray eventslist = args.getJSONArray("events");
            String eventString = eventslist.getJSONObject(0).toString();
            Events eobj = gson.fromJson(eventString, Events.class);
            eventsToDisplay.add(eobj);
            Paper.book().write("Appevents", eventsToDisplay);
            callingmain();
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.print("EXCEPTION IS IN Events Class or IN APPDETAILS class. "
                    +e.getMessage());
        }catch (Exception e){
            System.out.print("EXCEPTION IS IN Events Class or IN APPDETAILS class. "
                    +e.getMessage());
        }


    }


}
