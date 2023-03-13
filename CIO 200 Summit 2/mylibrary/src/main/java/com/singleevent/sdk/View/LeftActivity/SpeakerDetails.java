package com.singleevent.sdk.View.LeftActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.TxtVCustomFonts;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.View.RightActivity.MyAgendaDetails;
import com.singleevent.sdk.model.Agenda.Agenda;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.LocalArraylist.Rating;
import com.singleevent.sdk.model.LocalArraylist.agendaspeakerlist;
import com.singleevent.sdk.model.speakerssocialmedia;
import com.singleevent.sdk.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.paperdb.Paper;

/**
 * Created by Admin on 5/29/2017.
 */

public class SpeakerDetails extends AppCompatActivity implements View.OnClickListener {

    private static final int LOGIN_REQ_CODE = 108;
    AppDetails appDetails;
    ImageView speakerimage;
    RelativeLayout ratingbar;
    private double ImgWidth, Imgheight;
    TextView speakername, profession, txtrate, s1, s2,des,txtpdf, filesize;
    LinearLayout socialdetails, sessiondetails, sessionlist, socialist;
    String action;
    agendaspeakerlist item;
    ArrayList<speakerssocialmedia> socialinks = new ArrayList<>();
    private ArrayList<Events> events = new ArrayList<Events>();
    ArrayList<Agenda> speakersession = new ArrayList<>();
    Events e;
    int pos;
    ArrayList<Agendadetails> agendalist = new ArrayList<>();
    private SimpleDateFormat myFormat;
    Context context;
    RatingBar ratinBar;
    HashMap<Integer, Rating> Ratinglist;
    Runnable runnable;
    RequestQueue queue;
    LinearLayout pdfdetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_agendaspeaker);

        appDetails = Paper.book().read("Appdetails");
        context = SpeakerDetails.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);


        speakerimage = (ImageView) findViewById(R.id.speakerimage);
        socialdetails = (LinearLayout) findViewById(R.id.socialdetails);
        sessiondetails = (LinearLayout) findViewById(R.id.sessiondetails);
        sessionlist = (LinearLayout) findViewById(R.id.sessionlist);
        socialist = (LinearLayout) findViewById(R.id.sociallinks);
        ratingbar = (RelativeLayout) findViewById(R.id.ratingbar);
        ratinBar = (RatingBar) findViewById(R.id.ratingBar);
        speakername = (TextView) findViewById(R.id.speakername);
        profession = (TextView) findViewById(R.id.profession);
        txtrate = (TextView) findViewById(R.id.txtrate);
        s1 = (TextView) findViewById(R.id.s1);
        s2 = (TextView) findViewById(R.id.s2);
        des = (TextView) findViewById(R.id.des);
        pdfdetails = (LinearLayout) findViewById(R.id.pdfdetails);
        txtpdf = (TextView) findViewById(R.id.txtpdf);
        filesize = (TextView) findViewById(R.id.filesize);

        queue = Volley.newRequestQueue(this);

        // setting the fonts
        profession.setTypeface(Util.regulartypeface(this));
        des.setTypeface(Util.regulartypeface(this));
        txtrate.setTypeface(Util.regulartypeface(this));
        speakername.setTypeface(Util.boldtypeface(this));
        s1.setTypeface(Util.boldtypeface(this));
        s2.setTypeface(Util.boldtypeface(this));
        txtpdf.setTypeface(Util.regulartypeface(this));
        filesize.setTypeface(Util.regulartypeface(this));


        //calculating the speaker image size

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = (displayMetrics.widthPixels) * 0.40;
        Imgheight = (displayMetrics.widthPixels) * 0.40;


        // getting the action
        action = getIntent().getAction();


        //getting speakers details
        events = Paper.book().read("Appevents");
        e = events.get(0);

        for (int i = 0; i < e.getTabs().length; i++) {
            if (e.getTabs(i).getType().compareTo("agenda") == 0) {
                pos = i;
                break;
            }

        }

        speakersession = new ArrayList<>();
        for (int j = 0; j < e.getTabs(pos).getAgendaSize(); j++) {
            speakersession.add(e.getTabs(pos).getAgenda(j));
        }


        item = (agendaspeakerlist) getIntent().getSerializableExtra("SpeakerDetails"); //Obtaining data
        ratingbar.setOnClickListener(this);
        ratinBar.setVisibility(View.INVISIBLE);
        // setting data

        settingdata();
        startthread();

    }

    private void startthread() {
        runnable = new Runnable() {
            public void run() {

                getrating();


            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();

    }

    private void getrating() {

        String tag_string_req = "Login";
        String url = ApiList.GetRating + appDetails.getAppId() + "&type=speaker" + "&type_id=" + item.getSpeakerid();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Message message = messageHandler.obtainMessage();
                message.obj = 1;

                Bundle bundle = new Bundle();
                bundle.putString("response", response);

                message.setData(bundle);


                messageHandler.sendMessage(message);


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Message message = messageHandler.obtainMessage();
                message.obj = 2;
                Bundle bundle = new Bundle();
                if (error instanceof TimeoutError) {
                    bundle.putString("error", "Timeout");
                    message.setData(bundle);
                    messageHandler.sendMessage(message);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    bundle.putString("error", VolleyErrorLis.handleServerError(error, context));
                    message.setData(bundle);
                    messageHandler.sendMessage(message);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    bundle.putString("error", "Please Check Your Internet Connection");
                    message.setData(bundle);
                    messageHandler.sendMessage(message);
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



    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            if ((int) msg.obj == 1) {
                Bundle bundle = msg.getData();
                String response = bundle.getString("response");
                System.out.println(response);

                try {
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("response")) {
                        double ratingvalue = jObj.getDouble("responseString");
                        ratinBar.setRating((float) ratingvalue);
                        ratinBar.setVisibility(View.VISIBLE);

                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), SpeakerDetails.this);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            } else if ((int) msg.obj == 2) {
                Bundle bundle = msg.getData();
                String error = bundle.getString("error");
                Error_Dialog.show(error, SpeakerDetails.this);
            }

        }
    };
    private void settingdata() {


      try {
          if (item.getSpeaker_document_url().equalsIgnoreCase(""))
              pdfdetails.setVisibility(View.GONE);
          else {
              if(item.getSpeaker_document_hide()!=1){
              pdfdetails.setVisibility(View.VISIBLE);
              txtpdf.setText(item.getSpeaker_document_name());
              pdfdetails.setOnClickListener(this);}
              else{
                  pdfdetails.setVisibility(View.GONE);
              }
          }
      }catch (Exception e)
      {

      }

        //setting title
        speakername.setText(item.getName());

        //setting profession
        profession.setText(Html.fromHtml(item.getProf()));

        //setting description

        if (!item.getDetails().equalsIgnoreCase("")) {
            des.setText(Html.fromHtml(item.getDetails()));
            des.setVisibility(View.VISIBLE);
            des.setMovementMethod(LinkMovementMethod.getInstance());

        } else
            des.setVisibility(View.GONE);


        // setting speaker image

        Glide.with(getApplicationContext())

                .load((item.getImage().equalsIgnoreCase("")) ? R.drawable.default_user : item.getImage())
                .asBitmap()
                .placeholder(R.drawable.default_speaker)
                .error(R.drawable.default_speaker)
                .into(new BitmapImageViewTarget(speakerimage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        speakerimage.setImageBitmap(scaleBitmap(resource, (int) ImgWidth, (int) Imgheight));
                    }
                });


        // setting the contacts

        if (!item.getFacebook().equalsIgnoreCase("") && !item.getLinkedin().equalsIgnoreCase("")) {
            socialinks.add(new speakerssocialmedia("Facebook", item.getFacebook()));
            socialinks.add(new speakerssocialmedia("Linkedin", item.getLinkedin()));


        } else if (!item.getFacebook().equalsIgnoreCase("")) {
            socialinks.add(new speakerssocialmedia("Facebook", item.getFacebook()));


        } else if (!item.getLinkedin().equalsIgnoreCase("")) {
            socialinks.add(new speakerssocialmedia("Linkedin", item.getLinkedin()));

        }


        //checking the social links
        if (socialinks.size() > 0) {
            socialdetails.setVisibility(View.VISIBLE);
            adddata(socialinks);
        } else
            socialdetails.setVisibility(View.GONE);


        //setting speaker session

        // fetching the speakers attending this session

        int[] agendaid = item.getagendaid();

        agendalist = new ArrayList<>();

        for (int k = 0; k < agendaid.length; k++) {
            for (int j = 0; j < speakersession.size(); j++) {
                Agenda a = speakersession.get(j);
                if (a.getDetailSize() > 0) {
                    for (int i = 0; i < a.getDetailSize(); i++) {
                        if (agendaid[k] == speakersession.get(j).getDetail(i).getAgendaId()) {
                            agendalist.add(new Agendadetails(speakersession.get(j).getName(), speakersession.get(j).getDetail(i).getActivity(), speakersession.get(j).getDetail(i).getLocation(), speakersession.get(j).getDetail(i).getTime(), speakersession.get(j).getDetail(i).getTopic(), speakersession.get(j).getDetail(i).getDescription(), speakersession.get(j).getDetail(i).getCategory(), speakersession.get(j).getDetail(i).getSpeakerId(), speakersession.get(j).getDetail(i).getAgendaId(), speakersession.get(j).getDetail(i).getAttachment_type(), speakersession.get(j).getDetail(i).getAttachment_name(), speakersession.get(j).getDetail(i).getAttachment_url(), speakersession.get(j).getDetail(i).getFromtime(), speakersession.get(j).getDetail(i).getTotime()
							,speakersession.get(j).getDetail(i).getSurvey_checkvalue(),speakersession.get(j).getDetail(i).getMap_checkvalue()
                            ,speakersession.get(j).getDetail(i).getFloor_id(),speakersession.get(j).getDetail(i).getStream_link(),speakersession.get(j).getDetail(i).getEnable_agora_stream()
                            ));
                            break;
                        }

                    }
                }
            }
        }

        //checking the session details
        if (agendalist.size() > 0) {
            sessiondetails.setVisibility(View.VISIBLE);
            addsection(agendalist);
        } else
            sessiondetails.setVisibility(View.GONE);


    }

    private void addsection(ArrayList<Agendadetails> agendalist) {

        sessionlist.removeAllViews();
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < agendalist.size(); i++) {
            final Agendadetails item = agendalist.get(i);
            View child = vi.inflate(R.layout.s_speakersessions, null);
            TxtVCustomFonts sessioname = (TxtVCustomFonts) child.findViewById(R.id.sessioname);
            TxtVCustomFonts sessiondetails = (TxtVCustomFonts) child.findViewById(R.id.sessiondetails);
            TxtVCustomFonts sname=(TxtVCustomFonts)child.findViewById(R.id.sname);

            sname.setText(item.getTopic());
            sessioname.setText(converlontostring(item.getName(), true));
            sessiondetails.setText(converlontostring(item.getFromtime(), false) + " - " + converlontostring(item.getTotime(), false));

            if (!action.equalsIgnoreCase("com.agendaspeaker"))
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle args = new Bundle();
                        args.putLong("Date", item.getName());
                        args.putSerializable("Agendaview", item);

                        Intent i = new Intent(SpeakerDetails.this, AgendaDetails.class);
                        i.setAction("com.speakeragenda");
                        i.putExtras(args);
                        startActivity(i);
                    }
                });


            sessionlist.addView(child);
        }

    }


    private void adddata(ArrayList<speakerssocialmedia> socialinks) {
        socialist.removeAllViews();
        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < socialinks.size(); i++) {
            final speakerssocialmedia item = socialinks.get(i);
            View child = vi.inflate(R.layout.s_speakersocialmediaview, null);
            TextView txtmedianame = (TextView) child.findViewById(R.id.medianame);
            TextView txtmediaurl = (TextView) child.findViewById(R.id.mediaurl);
            ImageView logo = (ImageView) child.findViewById(R.id.logo);
            if (item.getType().compareTo("Facebook") == 0)
            logo.setBackground(context.getResources().getDrawable(R.drawable.fb2));
            else if (item.getType().compareTo("Linkedin") == 0)
                logo.setBackground(context.getResources().getDrawable(R.drawable.linkedin2));
            else if (item.getType().compareTo("Instagram") == 0)
                logo.setBackground(context.getResources().getDrawable(R.drawable.insta2));
            else if (item.getType().compareTo("Twitter") == 0)
                logo.setBackground(context.getResources().getDrawable(R.drawable.twitter2));
            txtmedianame.setText(item.getType());
            txtmediaurl.setTypeface(Util.lighttypeface(SpeakerDetails.this));
            if(item.getLink()!=null&& !item.getLink().equalsIgnoreCase("")){
                txtmediaurl.setText(item.getLink());
                txtmediaurl.setVisibility(View.VISIBLE);
            }


            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = item.getLink();
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }
                   /* Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i2);*/
                    Intent i3 = new Intent(context, SocialMediaView.class);
                    i3.putExtra("url",item.getLink());
                    i3.putExtra("title",item.getType());
                    context.startActivity(i3);
                }
            });

            socialist.addView(child);

        }


    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    private String converlontostring(Long key, boolean flag) {

        if (flag) {
            myFormat = new SimpleDateFormat("E, MMM dd, yyyy");
            myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            myFormat.format(key);
            return myFormat.format(key);
        } else {

            myFormat = new SimpleDateFormat("h:mm a");
            myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            myFormat.format(key);
            return myFormat.format(key);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString("Details");
        setTitle(Util.applyFontToMenuItem(this, s));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {

        int i1 = view.getId();

        if (i1 == R.id.pdfdetails) {
            Uri path = Uri.parse(item.getSpeaker_document_url());
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(SpeakerDetails.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }

        }


        if (i1 == R.id.ratingbar) {
            if (Paper.book().read("Islogin", false)) {

                Ratinglist = Paper.book(appDetails.getAppId()).read("SpeakerRating", new HashMap<Integer, Rating>());
                int temp = item.getSpeakerid();
                if (Ratinglist.size() > 0) {
                    if(Ratinglist.containsKey(temp)) {
                        Toast.makeText(SpeakerDetails.this, "You Already submitted Rating", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        showratingbar();
                    }
                } else {

                    showratingbar();
                }
            }
            else {
                Error_Dialog.show("Please Login For Rating", SpeakerDetails.this);

                Intent i = new Intent(this, LoginActivity.class);
                startActivityForResult(i, 1);

            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == 1 && resultCode == RESULT_OK ){
            showratingbar();
        }
    }

    private void showratingbar() {

        Ratinglist = Paper.book(appDetails.getAppId()).read("SpeakerRating", new HashMap<Integer, Rating>());


        final Dialog mBottomSheetDialog = new Dialog(SpeakerDetails.this, R.style.MaterialDialogSheet);

        mBottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBottomSheetDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        mBottomSheetDialog.setContentView(R.layout.s_rating_dialog);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.show();

        TextView heading = (TextView) mBottomSheetDialog.findViewById(R.id.h1);
        TextView rating = (TextView) mBottomSheetDialog.findViewById(R.id.rating);
        TextView cancel = (TextView) mBottomSheetDialog.findViewById(R.id.cancel);
        final TextView warning = (TextView) mBottomSheetDialog.findViewById(R.id.h2);
        warning.setVisibility(View.GONE);
        final RatingBar ratingbar = (RatingBar) mBottomSheetDialog.findViewById(R.id.ratingbar);
        heading.setTypeface(Util.boldtypeface(context));
        rating.setTypeface(Util.regulartypeface(context));
        warning.setTypeface(Util.lighttypeface(context));
        cancel.setTypeface(Util.regulartypeface(context));
        rating.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        cancel.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        warning.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        // set the previous rating if it present

        if (Ratinglist.containsKey(item.getSpeakerid())) {
            Rating r = Ratinglist.get(item.getSpeakerid());
            ratingbar.setRating((float) r.getRating());
        }

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (b && v > 0) {
                    warning.setVisibility(View.GONE);
                } else if (b && v <= 0)
                    warning.setVisibility(View.VISIBLE);

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingbar.getRating() > 0) {
                    mBottomSheetDialog.dismiss();
                    sendRating(ratingbar.getRating(), Ratinglist.containsKey(item.getSpeakerid()) ? "update" : "create");
                    Ratinglist.put(item.getSpeakerid(), new Rating(ratingbar.getRating(), item.getSpeakerid()));
                    Paper.book(appDetails.getAppId()).write("SpeakerRating", Ratinglist);
                } else
                    warning.setVisibility(View.VISIBLE);

            }
        });


    }


    // send the rating to server

    private void sendRating(final float rating, final String s) {

        final ProgressDialog dialog = new ProgressDialog(SpeakerDetails.this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Submitting...");
        dialog.show();
        String tag_string_req = "Rating";
        String url = ApiList.SetRating;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        ratinBar.setRating((float) jObj.getDouble("rating_average"));
                        Error_Dialog.show("Thank you for your feedback", SpeakerDetails.this);
                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), SpeakerDetails.this);
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
                    Error_Dialog.show("Timeout", SpeakerDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), SpeakerDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", SpeakerDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("action", s);
                params.put("type", "speaker");
                params.put("rating", String.valueOf(rating));
                params.put("type_id", String.valueOf(item.getSpeakerid()));
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


}
