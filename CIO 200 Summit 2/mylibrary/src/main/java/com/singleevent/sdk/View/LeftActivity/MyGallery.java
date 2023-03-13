package com.singleevent.sdk.View.LeftActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.DefaultRetryPolicy;
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
import com.singleevent.sdk.Left_Adapter.ImageAdapter;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.paperdb.Paper;

public class MyGallery extends AppCompatActivity   implements View.OnClickListener{



    AppDetails appDetails;
    int pos;
    private String title="Photo Gallery";
    String appid;
    String userid;
    ArrayList<String> imgurl =new ArrayList<String>();

    ArrayList<String> imgurl1 =new ArrayList<String>();
    GridView gridView;
    Toolbar toolbar;
    private ArrayList<String> glist = new ArrayList<>();
    private ArrayList<String> demo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.gallery);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        gridView=(GridView)findViewById(R.id.grid_view);
        appDetails = Paper.book().read("Appdetails");
        appid=appDetails.getAppId();
        userid =  Paper.book().read("userId","");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarg);
        //    tv_app_version_name = (TextView) findViewById(R.id.tv_app_version_name);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        toolbar.setTitle(Util.applyFontToMenuItem(MyGallery.this,
                new SpannableString(title)));

        //  setSupportActionBar(toolbar);
        toolbar.setOnClickListener(this);

        if (getIntent().getExtras() == null)
            finish();
        title = getIntent().getExtras().getString("title");

        getGallery(appid,userid);







    }


    private void getGallery(String appid,String userid){



        final ProgressDialog pDialog = new ProgressDialog(MyGallery.this,
                R.style.MyAlertDialogStyle );
        pDialog.setMessage("Loading ...");
        pDialog.show();

        String tag_string_req="my_gallery";
        String url = ApiList.GetGallery + appid +"&userid=" +userid;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                System.out.println(response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        JSONArray nlp = jObj.getJSONArray("data");
                        for(int i=0; i<nlp.length(); i++)
                        {
                            imgurl.add(nlp.getJSONObject(i).getString("res_url"));
                            if((nlp.getJSONObject(i).getString("thumbnail_res_url")!=null) && (!nlp.getJSONObject(i).getString("thumbnail_res_url").equalsIgnoreCase("")))
                            { imgurl1.add(nlp.getJSONObject(i).getString("thumbnail_res_url"));}
                            else{
                                imgurl1.add(nlp.getJSONObject(i).getString("res_url"));}
                        }
                        Paper.book().write("galleryurl",imgurl);
                        Paper.book().write("gallerythumbnail",imgurl1);

                        try{
                            gridView.setAdapter(new ImageAdapter(MyGallery.this,imgurl1));}
                        catch (Exception e){


                        }


                        try{
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




                                 if(imgurl.get(position).contains(".mp4")){
                                     Intent intent = new Intent(getApplicationContext(), GalleryVideo.class);
                                     intent.putExtra("imageurl", imgurl.get(position));
                                     intent.putExtra("title",title);
                                     startActivity(intent);
                                 }
                                 else {
                                     Intent intent = new Intent(getApplicationContext(), FullScreenActivity.class);
                                     intent.putExtra("id", position);
                                     startActivity(intent);
                                 }

                                }
                            });}catch (Exception e)
                        {
                        }


                    } else {

                        Error_Dialog.show(jObj.getString("responseString"),
                                MyGallery.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", MyGallery.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error,
                            MyGallery.this),
                            MyGallery.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                }
            }
        });


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }
    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString(title);
        setTitle(Util.applyFontToMenuItem(this, s));
        //tv_app_version_name.setText("Version : " + DataBaseStorage.getAppVersionName(this));

    }
    @Override
    public void onClick(View v){
        if(v.getId()==R.id.toolbarg) {
            onBackPressed();
        }

    }


}



