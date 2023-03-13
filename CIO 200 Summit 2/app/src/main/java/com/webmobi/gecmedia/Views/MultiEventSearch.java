package com.webmobi.gecmedia.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.webmobi.gecmedia.R;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class MultiEventSearch extends AppCompatActivity {

   ArrayList<String> multieventids=new ArrayList<>();
   ArrayList<String>multieventname=new ArrayList<>();
    ArrayList<String> multieventlogo=new ArrayList<>();
    ArrayList<String>multieventtheme=new ArrayList<>();

    @Override
    protected  void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.multievent_search);

        EditText search_editText=(EditText) findViewById(R.id.search_editText);
        ImageView search_close=(ImageView)findViewById(R.id.search_close);
        final String userid=Paper.book().read("userId", "");
        search_editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //if search is pressed from keypad
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    //checking character length
                    if (textView.length() >= 3)
                        searchMultiEvent(textView.getText().toString(),userid);
                }
                return false;
            }
        });
        search_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }



    private void searchMultiEvent(final String q, final String userid) {

        final ProgressDialog dialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading Details...");
        dialog.show();


        String tag_string_req = "MyltiSearch";
        String url = ApiList.Multi_search;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                     //   parseresult(jObj.getJSONObject("responseString"), appId);
                      //  Toast.makeText(MultiEventSearch.this, jObj.getJSONObject("responseString").toString(), Toast.LENGTH_LONG).show();
                      if(!multieventids.contains(jObj.getJSONObject("data").getString("event_id"))) {
                          String eid=jObj.getJSONObject("data").getString("event_id");
                          String ename=jObj.getJSONObject("data").getString("event_name");
                          String elogo=jObj.getJSONObject("data").getString("multi_event_logo");
                          String etheme=jObj.getJSONObject("data").getString("color_code");


                          multieventids.add(jObj.getJSONObject("data").getString("event_id"));
                          multieventname.add(jObj.getJSONObject("data").getString("event_name"));
                          multieventlogo.add(jObj.getJSONObject("data").getString("multi_event_logo"));
                          multieventtheme.add(jObj.getJSONObject("data").getString("color_code"));
                          Paper.book().write("Multi_event_ids", multieventids);
                          Paper.book().write("Multi_event_name", multieventname);
                          Paper.book().write("Multi_event_logo", multieventlogo);
                          Paper.book().write("Multi_event_theme", multieventtheme);
                          Toast.makeText(MultiEventSearch.this, jObj.getString("responseString"), Toast.LENGTH_LONG).show();
                          callMulti(eid,ename,elogo,etheme);
                      }



                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        Error_Dialog.show(jObj.getString("responseString"), MultiEventSearch.this);

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
                    Error_Dialog.show("Timeout", MultiEventSearch.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(com.webmobi.gecmedia.CustomViews.VolleyErrorLis.handleServerError(error, MultiEventSearch.this), MultiEventSearch.this);
                } else if (com.webmobi.gecmedia.CustomViews.VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", MultiEventSearch.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("q",q);
                params.put("userid",userid);

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
    public void callMulti(String eid,String ename,String elogo,String etheme)
    {

      //  i.putExtra("multie_name",ename);
       Intent i =new Intent(MultiEventSearch.this,HomeScreenMulti.class);
               i.putExtra("Event_ID",eid);
               i.putExtra("Event_Name",ename);
               i.putExtra("Event_Logo",elogo);
               i.putExtra("Event_Theme",etheme);

               startActivity(i);



    }
    private void replaceFragment(Fragment fragment, String popularfragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment, popularfragment);
        transaction.commit();


    }

}
