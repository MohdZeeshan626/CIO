package com.singleevent.sdk.View.LeftActivity;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.singleevent.sdk.R;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.EmojiItem;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Items;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.paperdb.Paper;

public class Moodometer extends AppCompatActivity implements View.OnClickListener  {

    ArrayList<String> store = new ArrayList<>();
    ArrayList<String[]> mood = new ArrayList<>();
    ArrayList<Items> moodlist = new ArrayList<>();
    ArrayList<EmojiItem> emoji = new ArrayList<>();

    int pos;
    String title;
    String MainTitle;
    String question;
    String q_id;
    String tempresult;
    int default_id;
    TextView textque,moodt1,moodt2,moodt3,taverag;
    AppDetails appDetails;
    Events e;
    Button moodometersub;
    ImageView happy,sad,excited,normal,resultimage;
    LinearLayout horizontal;
    private ArrayList<Events> events = new ArrayList<Events>();
    HashMap<String,Integer> imgm=new HashMap<String, Integer>();
    ArrayList<String> temp=new ArrayList<>();
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);

        appDetails = Paper.book().read("Appdetails");
        setContentView(R.layout.mood_ometer);
        horizontal=(LinearLayout)findViewById(R.id.horizontal);
        textque=(TextView) findViewById(R.id.textque);
        moodometersub=(Button)findViewById(R.id.moodometersub);
        moodometersub.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

         toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
       // setSupportActionBar(toolbar);
        toolbar.setOnClickListener(this);
        moodometersub.setOnClickListener(this);



        if (getIntent().getExtras() == null)
            finish();
        pos = getIntent().getExtras().getInt("pos");
        title = getIntent().getExtras().getString("title");
        events = Paper.book().read("Appevents");
        e = events.get(0);

        for(int i=0; i< e.getTabs(pos).getItemsSize(); i++)
        {
            moodlist.add(e.getTabs(pos).getItems(i));
            //emoji.add(e.getTabs(pos).getEmoji_info(i));
        }



       /* store.clear();
        imgm.clear();
        store.add("Happy");
        store.add("Sad");
        store.add("Excited");
        store.add("Normal");*/

        imgm.put("happy", R.id.happy);
        imgm.put("sad", R.id.sad);
        imgm.put("excited", R.id.excited);
        imgm.put("normal", R.id.average);

        for (int i = 0; i < e.getTabs(pos).getItemsSize(); i++) {
            for(int j=0; j<e.getTabs(pos).getItems(i).getEmoji_info(); j++) {
                if(e.getTabs(pos).getItems(i).getActive()!=0) {
                    temp.add(e.getTabs(pos).getItems(i).getEmoji_info(j).getEmoji_name());
                    question = e.getTabs(pos).getItems(i).getQuestion();
                    q_id = String.valueOf(e.getTabs(pos).getItems(i).getQuestion_id());
                }
            }
            MainTitle=e.getTabs(pos).getItems(0).getQuestion();
            textque.setText(MainTitle);

        }
        for (int i=0;i<temp.size();i++)
        {

            if(temp.get(i).equalsIgnoreCase("happy"))
            {
                happy=(ImageView)findViewById(R.id.happy);
                moodt1=(TextView)findViewById(R.id.moodt1);
                happy.setImageResource(R.drawable.smiling);
                moodt1.setText("Happy");
                happy.setOnClickListener(this);
            }


            if(temp.get(i).equalsIgnoreCase("sad"))
            {
                sad=(ImageView)findViewById(R.id.sad);
                moodt2=(TextView)findViewById(R.id.moodt2);
                moodt2.setText("Sad");
                sad.setImageResource(R.drawable.sad);
                sad.setOnClickListener(this);
            }

            if ((temp.get(i).equals("normal")) || (temp.get(i).equals("neutral")))
            {

                excited=(ImageView)findViewById(R.id.excited);
                moodt3=(TextView)findViewById(R.id.moodt3);
                moodt3.setText("Neutral");
                excited.setImageResource(R.drawable.confused);
                excited.setOnClickListener(this);



            }

            if(temp.get(i).equalsIgnoreCase("excited"))
            {
               // excited=(ImageView)findViewById(R.id.excited);
             //   moodt3=(TextView)findViewById(R.id.moodt3);
               // moodt3.setText("Excited");
               // excited.setImageResource(R.drawable.smiling);
              //  excited.setOnClickListener(this);
            }


        }
    }

    @Override
    public void onClick(View view) {

        if(view.getId()== R.id.moodometersub)
        {
            sendMoodFeedback(tempresult);
        }

        else if(view.getId()== R.id.toolbar){
            onBackPressed();
        }
       else if (view.getId()== R.id.happy){

            changeUI("happy");
          /*  Intent i = new Intent(this,MoodFeedback.class);
            i.putExtra("pos",pos);
            i.putExtra("emojiname","happy" );
            startActivity(i);*/

            /*Error_Dialog.show("Thank you for your valuable feedback",Moodometer.this);
            new CountDownTimer(2000, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    finish();
                }
            }.start();*/

        }
      else  if (view.getId()== R.id.sad){
            changeUI("sad");
           /* Intent i = new Intent(this,MoodFeedback.class);
            i.putExtra("pos",pos);
            i.putExtra("emojiname","sad" );
            startActivity(i);*/

        }
      else  if (view.getId()== R.id.excited){
            changeUI("normal");
          //  changeUI("excited");
           /* Intent i = new Intent(this,MoodFeedback.class);
            i.putExtra("pos",pos);
            i.putExtra("emojiname","excited" );
            startActivity(i);*/
        }
      else  if (view.getId()== R.id.average){
            changeUI("normal");
          /*  Intent i = new Intent(this,MoodFeedback.class);
            i.putExtra("pos",pos);
            i.putExtra("emojiname","average");
            startActivity(i);*/
        }



    }

    public void changeUI(String image)
    {

        if(image.equalsIgnoreCase("happy"))
        {
            horizontal.setVisibility(View.GONE);
            resultimage =(ImageView)findViewById(R.id.resultimage);
            resultimage.setImageResource(R.drawable.smiling);
            tempresult="happy";
        }
        if(image.equalsIgnoreCase("sad"))
        {
            horizontal.setVisibility(View.GONE);
            resultimage =(ImageView)findViewById(R.id.resultimage);
            resultimage.setImageResource(R.drawable.sad);
            tempresult="sad";

        }
        if(image.equalsIgnoreCase("normal")|| image.equalsIgnoreCase("neutral"))
        {
            horizontal.setVisibility(View.GONE);
            resultimage =(ImageView)findViewById(R.id.resultimage);
            resultimage.setImageResource(R.drawable.confused);
            tempresult="normal";

        }
    }

    ////ApI
    private void sendMoodFeedback(final String emojiname) {

        final ProgressDialog dialog = new ProgressDialog(Moodometer.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Sending Feedback...");
        dialog.show();
        String tag_string_req = "Schdule";
        String url = ApiList.MoodFeedback;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), Moodometer.this);
                        Paper.book().write("Mood_survey",1);

                        new CountDownTimer(2000, 1000) {

                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                finish();
                            }
                        }.start();


                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", Moodometer.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), Moodometer.this);
                        new CountDownTimer(2000, 1000) {

                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                finish();
                            }
                        }.start();
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
                    Error_Dialog.show("Timeout", Moodometer.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, Moodometer.this), Moodometer.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Survey Couldn't submit.", Moodometer.this);


                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("flag","update");
                params.put("default_id","moodometer0");
                params.put("appid", appDetails.getAppId());
                params.put("question_id",q_id);
                params.put("question",question);
                params.put("emoji_name",emojiname);
                params.put("tags","[]");
                params.put("details","");
                System.out.println(params);
                return params;
            }


        };


        strReq.setShouldCache(false);
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
        toolbar.setTitle(Util.applyFontToMenuItem(Moodometer.this,
                new SpannableString(title)));

    }

}
