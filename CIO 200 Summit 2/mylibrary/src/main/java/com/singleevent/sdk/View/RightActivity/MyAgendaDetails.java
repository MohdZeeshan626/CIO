package com.singleevent.sdk.View.RightActivity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.TxtVCustomFonts;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.model.LocalArraylist.Notes;
import com.singleevent.sdk.model.LocalArraylist.Rating;
import com.singleevent.sdk.model.LocalArraylist.agendaspeakerlist;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.SpeakerDetails;
import com.singleevent.sdk.View.LeftActivity.askAquestion.AskAQuestionActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;
import io.paperdb.Paper;

/**
 * Created by Admin on 5/26/2017.
 */

public class MyAgendaDetails extends AppCompatActivity implements View.OnClickListener {

    TextView etitle, date, time, category, venue, s1, txtnotes, txtsch, txtrate, p1, txtpdf, filesize;
    RelativeLayout loc, addnotes, addsch, ratingbar;
    ArrayList<Items> speakerslist = new ArrayList<>();
    LinearLayout speakerdetails, speakers, pdfdetails;
    TxtVCustomFonts /*des,*/idownload;
    TextView des;
    private double ImgWidth, Imgheight;
    Agendadetails item;
    long Agendadate;
    private SimpleDateFormat myFormat;
    private ArrayList<Events> events = new ArrayList<Events>();
    ArrayList<agendaspeakerlist> speakername = new ArrayList<>();
    Events e;
    int pos;
    AppDetails appDetails;
    AwesomeText inote, isch, ipdf;
    String action;
    List<Integer> list;
    HashMap<Integer, Notes> Noteslist;
    Context context;
    HashMap<Integer, Rating> Ratinglist;
    RatingBar ratinBar;
    Runnable runnable;
    private RelativeLayout ask_question_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_agendadetails);
        appDetails = Paper.book().read("Appdetails");
        context = MyAgendaDetails.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ask_question_layout = (RelativeLayout)findViewById(R.id.ask_question_layout);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);


        Bundle extras = getIntent().getExtras();
        if (extras == null)
            finish();

        // getting the action
        action = getIntent().getAction();

        // getting agenda details from fragment
        item = (Agendadetails) getIntent().getSerializableExtra("Agendaview"); //Obtaining data
        Agendadate = extras.getLong("Date");

        //getting speakers details

        events = Paper.book().read("Appevents");
        e = events.get(0);

        for (int i = 0; i < e.getTabs().length; i++) {
            if (e.getTabs(i).getType().compareTo("speakersData") == 0) {
                pos = i;
                break;
            }

        }
        speakerslist = new ArrayList<>();
        for (int j = 0; j < e.getTabs(pos).getItemsSize(); j++) {
            speakerslist.add(e.getTabs(pos).getItems(j));
        }

        //intializing all view

        speakerdetails = (LinearLayout) findViewById(R.id.speakerdetails);
        pdfdetails = (LinearLayout) findViewById(R.id.pdfdetails);
        speakers = (LinearLayout) findViewById(R.id.speakers);
        etitle = (TextView) findViewById(R.id.etitle);
        date = (TextView) findViewById(R.id.date);
        txtnotes = (TextView) findViewById(R.id.txtnote);
        txtsch = (TextView) findViewById(R.id.txtsch);
        time = (TextView) findViewById(R.id.day);
        s1 = (TextView) findViewById(R.id.s1);
        p1 = (TextView) findViewById(R.id.p1);
        category = (TextView) findViewById(R.id.category);
        venue = (TextView) findViewById(R.id.venue);
        txtrate = (TextView) findViewById(R.id.txtrate);
        txtpdf = (TextView) findViewById(R.id.txtpdf);
        filesize = (TextView) findViewById(R.id.filesize);
        loc = (RelativeLayout) findViewById(R.id.loc);
        addnotes = (RelativeLayout) findViewById(R.id.addnotes);
        ratingbar = (RelativeLayout) findViewById(R.id.ratingbar);
        ratinBar = (RatingBar) findViewById(R.id.ratingBar);
        addsch = (RelativeLayout) findViewById(R.id.addschedule);
        des = (TextView) findViewById(R.id.des);
        inote = (AwesomeText) findViewById(R.id.inote);
        isch = (AwesomeText) findViewById(R.id.isch);
        idownload = (TxtVCustomFonts) findViewById(R.id.idownload);

        // setting the fonts
        etitle.setTypeface(Util.regulartypeface(this));
        date.setTypeface(Util.regulartypeface(this));
        category.setTypeface(Util.regulartypeface(this));
        venue.setTypeface(Util.regulartypeface(this));
        txtnotes.setTypeface(Util.regulartypeface(this));
        txtsch.setTypeface(Util.regulartypeface(this));
        txtrate.setTypeface(Util.regulartypeface(this));
        time.setTypeface(Util.boldtypeface(this));
        s1.setTypeface(Util.boldtypeface(this));
        p1.setTypeface(Util.boldtypeface(this));
        txtpdf.setTypeface(Util.regulartypeface(this));
        filesize.setTypeface(Util.regulartypeface(this));


        //setting the texcolor

        txtnotes.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        txtsch.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        inote.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        isch.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        idownload.setTextColor(Color.parseColor(appDetails.getTheme_color()));

        ask_question_layout.setOnClickListener(this);

        addnotes.setOnClickListener(this);
        addsch.setOnClickListener(this);
        ratingbar.setOnClickListener(this);
        //calculating the speaker image size

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = (displayMetrics.widthPixels) * 0.20;
        Imgheight = (displayMetrics.widthPixels) * 0.15;
        ratinBar.setVisibility(View.INVISIBLE);

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

    private void settingdata() {

        //setting title
        etitle.setText(item.getTopic());

        //setting date
        date.setText(converlontostring(Agendadate, true));

        //setting fromtime and totime

        time.setText(converlontostring(item.getFromtime(), false) + " - " + converlontostring(item.getTotime(), false));

        // setting catgory

        category.setText(item.getCategory());

        //setting location

        if (!item.getLocation().equalsIgnoreCase("")) {
            venue.setText(item.getLocation());
            loc.setVisibility(View.VISIBLE);
        } else {
            loc.setVisibility(View.GONE);

        }

        //setting description

        if (!item.getDescription().equalsIgnoreCase("")) {
            des.setMovementMethod(LinkMovementMethod.getInstance());
            des.setText(Html.fromHtml(item.getDescription()));
            des.setVisibility(View.VISIBLE);
        } else
            des.setVisibility(View.GONE);


        // fetching the speakers attending this session

        int[] speakerid = item.getSpeakerId();

        speakername = new ArrayList<>();
        for (int k = 0; k < speakerid.length; k++) {
            for (int m = 0; m < speakerslist.size(); m++) {
                if (speakerid[k] == speakerslist.get(m).getSpeakerId()) {
                    speakername.add(new agendaspeakerlist(speakerslist.get(m).getSpeakerId(), speakerslist.get(m).getName(), speakerslist.get(m).getDescription(), speakerslist.get(m).getImage(), speakerslist.get(m).getDetails(), speakerslist.get(m).getFacebook(), speakerslist.get(m).getLinkedin(), speakerslist.get(m).getAgendaId(),speakerslist.get(m).getSpeaker_document_url()
                    ,speakerslist.get(m).getSpeaker_document_hide(),speakerslist.get(m).getSpeaker_document_name()));
                    break;
                }
            }

        }

        if (speakername.size() > 0) {
            speakerdetails.setVisibility(View.VISIBLE);
            Addthespeaker();
        } else
            speakerdetails.setVisibility(View.GONE);


        //setting the pdf if its present

        if (item.getAttachment_url().equalsIgnoreCase(""))
            pdfdetails.setVisibility(View.GONE);
        else {
            pdfdetails.setVisibility(View.VISIBLE);
            txtpdf.setText(item.getAttachment_name());
            pdfdetails.setOnClickListener(this);
        }


    }


    private void getrating() {

        String tag_string_req = "agendrating";
        String url = ApiList.GetRating + appDetails.getAppId() + "&type=agenda" + "&type_id=" + item.getAgendaId();
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
                        Error_Dialog.show(jObj.getString("responseString"), MyAgendaDetails.this);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            } else if ((int) msg.obj == 2) {
                Bundle bundle = msg.getData();
                String error = bundle.getString("error");
                Error_Dialog.show(error, MyAgendaDetails.this);
            }

        }
    };


    private void Addthespeaker() {
        speakers.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < speakername.size(); i++) {
            final agendaspeakerlist item = speakername.get(i);
            View child = inflater.inflate(R.layout.s_agendaspeakerview, null);
            LinearLayout speakerimage = (LinearLayout) child.findViewById(R.id.speakerview);
            final RoundedImageView simage = (RoundedImageView) child.findViewById(R.id.speakerimage);
            RelativeLayout.LayoutParams sparams = (RelativeLayout.LayoutParams) speakerimage.getLayoutParams();
            sparams.width = (int) ImgWidth;
            sparams.height = (int) Imgheight;
          //  speakerimage.setLayoutParams(sparams);

            Glide.with(getApplicationContext())

                    .load((item.getImage().equalsIgnoreCase("")) ? R.drawable.default_user : item.getImage())
                    .asBitmap()
                    .placeholder(R.drawable.default_speaker)
                    .error(R.drawable.default_speaker)
                    .into(simage);
            simage.setCornerRadius(12,12,12,12);
        /*new BitmapImageViewTarget(simage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            simage.setImageBitmap(scaleBitmap(resource, (int) ImgWidth, (int) Imgheight));
                        }
                    });*/

            TextView sname = (TextView) child.findViewById(R.id.title);
            TextView sdes = (TextView) child.findViewById(R.id.des);
            sname.setTypeface(Util.regulartypeface(this));
            sdes.setTypeface(Util.regulartypeface(this));

            sname.setText(item.getName());
            sdes.setText(Html.fromHtml(item.getProf()));

            if (!action.equalsIgnoreCase("com.speakeragenda"))

                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Bundle args = new Bundle();
                        args.putSerializable("SpeakerDetails", item);
                        Intent i = new Intent(MyAgendaDetails.this, SpeakerDetails.class);
                        i.setAction("com.agendaspeaker");
                        i.putExtras(args);
                        startActivity(i);
                    }
                });

            speakers.addView(child);

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

        settingsch();


    }

    private void settingsch() {

try {
    list = Paper.book(appDetails.getAppId()).read("SCH", new ArrayList<Integer>() {
    });
    if (list.contains(item.getAgendaId())) {
        txtsch.setText("REMOVE FROM SCHEDULE");
        isch.setPixedenStrokeIcon(FontCharacterMaps.Pixeden.PE_CHECK);
    } else {
        txtsch.setText("ADD TO MY SCHEDULE");
        isch.setPixedenStrokeIcon(FontCharacterMaps.Pixeden.PE_PLUS);
    }
}catch (Exception e){}
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
        int i = view.getId();
        if (i == R.id.pdfdetails) {
            Uri path = Uri.parse(item.getAttachment_url());
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MyAgendaDetails.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }

        } else if (i == R.id.addnotes) {
            if (Paper.book().read("Islogin",false))
                shownotes();
            else
                Error_Dialog.show("Please Login", MyAgendaDetails.this);

        } else if (i == R.id.addschedule) {
            if (Paper.book().read("Islogin",false)) {
                list = Paper.book(appDetails.getAppId()).read("SCH", new ArrayList<Integer>());
                if (list.contains(item.getAgendaId())) {
                    list.remove(new Integer(item.getAgendaId()));
                    addtosch("Removing from Schedules", "unmark");
                } else {
                    list.add(item.getAgendaId());
                    addtosch("Adding to Schedules", "mark");
                }
                Paper.book(appDetails.getAppId()).write("SCH", list);
                settingsch();

            } else
                Error_Dialog.show("Please Login", MyAgendaDetails.this);


        } else if (i == R.id.ratingbar) {
            if (Paper.book().read("Islogin",false))
            showratingbar();

        }
        if (i==R.id.ask_question_layout){
            if (Paper.book().read("Islogin",false )) {
                Bundle args = new Bundle();
                args.putSerializable("Agendaview", item);

                Intent intent = new Intent(this, AskAQuestionActivity.class);
                intent.setAction("com.agendadetails");
                intent.putExtras(args);
                startActivity(intent);
            }else {
                Error_Dialog.show("Please Login", MyAgendaDetails.this);
            }
        }
    }

    private void showratingbar() {

        Ratinglist = Paper.book(appDetails.getAppId()).read("AgendaRating", new HashMap<Integer, Rating>());

        final Dialog mBottomSheetDialog = new Dialog(MyAgendaDetails.this, R.style.MaterialDialogSheet);

        mBottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBottomSheetDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        mBottomSheetDialog.setContentView(R.layout.s_rating_dialog);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.show();
        final RatingBar ratingbar = (RatingBar) mBottomSheetDialog.findViewById(R.id.ratingbar);

        TextView heading = (TextView) mBottomSheetDialog.findViewById(R.id.h1);
        TextView rating = (TextView) mBottomSheetDialog.findViewById(R.id.rating);
        TextView cancel = (TextView) mBottomSheetDialog.findViewById(R.id.cancel);
        final TextView warning = (TextView) mBottomSheetDialog.findViewById(R.id.h2);
        warning.setVisibility(View.GONE);
        heading.setTypeface(Util.boldtypeface(context));
        warning.setTypeface(Util.lighttypeface(context));
        rating.setTypeface(Util.regulartypeface(context));
        cancel.setTypeface(Util.regulartypeface(context));
        rating.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        cancel.setTextColor(Color.parseColor(appDetails.getTheme_color()));
        warning.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });

        // set the previous rating if it present

        if (Ratinglist.containsKey(item.getAgendaId())) {
            Rating r = Ratinglist.get(item.getAgendaId());
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

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingbar.getRating() > 0) {
                    mBottomSheetDialog.dismiss();
                    sendRating(ratingbar.getRating(), Ratinglist.containsKey(item.getAgendaId()) ? "update" : "create");
                    Ratinglist.put(item.getAgendaId(), new Rating(ratingbar.getRating(), item.getAgendaId()));
                    Paper.book(appDetails.getAppId()).write("AgendaRating", Ratinglist);
                } else
                    warning.setVisibility(View.VISIBLE);
            }
        });


    }

    private void shownotes() {

        Noteslist = Paper.book(appDetails.getAppId()).read("AgendaNote", new HashMap<Integer, Notes>());


        final Dialog mBottomSheetDialog = new Dialog(MyAgendaDetails.this, R.style.MaterialDialogSheet);

        mBottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBottomSheetDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        mBottomSheetDialog.setContentView(R.layout.s_note);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.show();

        final LinearLayout cancel = (LinearLayout) mBottomSheetDialog.findViewById(R.id.cancel);
        LinearLayout save = (LinearLayout) mBottomSheetDialog.findViewById(R.id.save);
        Button canceltxt = (Button) mBottomSheetDialog.findViewById(R.id.canceltxt);
        Button savetxt = (Button) mBottomSheetDialog.findViewById(R.id.savetxt);

        canceltxt.setTypeface(Util.regulartypeface(context));
        savetxt.setTypeface(Util.regulartypeface(context));
        canceltxt.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        savetxt.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        final EditText notes = (EditText) mBottomSheetDialog.findViewById(R.id.notes);
        notes.setTypeface(Util.regulartypeface(context));

        if (Noteslist.containsKey(item.getAgendaId())) {
            Notes n = Noteslist.get(item.getAgendaId());
            notes.setText(n.getNotes());
        }


        canceltxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });

        savetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!notes.getText().toString().isEmpty()) {
                    mBottomSheetDialog.dismiss();
                    Addnotes(notes.getText().toString(), Noteslist.containsKey(item.getAgendaId()) ? "update" : "create");
                    Noteslist.put(item.getAgendaId(), new Notes(item.getAgendaId(), notes.getText().toString(), "agenda", item.getTopic(), String.valueOf(System.currentTimeMillis())));
                    Paper.book(appDetails.getAppId()).write("AgendaNote", Noteslist);
                }
            }
        });


    }

    private void Addnotes(final String notes, final String action) {

        final ProgressDialog dialog = new ProgressDialog(MyAgendaDetails.this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Adding Note");
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
                        Error_Dialog.show("Saved", MyAgendaDetails.this);
                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), MyAgendaDetails.this);
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
                    Error_Dialog.show("Timeout", MyAgendaDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, getApplicationContext()), MyAgendaDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", MyAgendaDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book(appDetails.getAppId()).read("userId", ""));
                params.put("action", action);
                params.put("type", "agenda");
                params.put("notes", notes);
                params.put("type_name", item.getTopic());
                params.put("type_id", String.valueOf(item.getAgendaId()));
                params.put("appid", appDetails.getAppId());
                params.put("last_updated", String.valueOf(System.currentTimeMillis()));
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


    private void addtosch(String msg, final String mark) {

        // converting arraylist to jsonarray

        Gson gson = new GsonBuilder().create();
        final JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();


        final ProgressDialog dialog = new ProgressDialog(MyAgendaDetails.this,R.style.MyAlertDialogStyle);
        dialog.setMessage(msg);
        dialog.show();
        String tag_string_req = "Schdule";
        String url = ApiList.Schdule;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("response")) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", MyAgendaDetails.this);


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), MyAgendaDetails.this);
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
                    Error_Dialog.show("Timeout", MyAgendaDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), MyAgendaDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", MyAgendaDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book(appDetails.getAppId()).read("userId", ""));
                params.put("action", mark);
                params.put("schedules", myCustomArray.toString());
                params.put("appid", appDetails.getAppId());
                System.out.println(params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book(appDetails.getAppId()).read("token", ""));
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


    // send the rating to server

    private void sendRating(final float rating, final String s) {

        final ProgressDialog dialog = new ProgressDialog(MyAgendaDetails.this,R.style.MyAlertDialogStyle);
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
                        Error_Dialog.show("Thank you for your feedback", MyAgendaDetails.this);
                    } else {
                        Error_Dialog.show(jObj.getString("responseString"), MyAgendaDetails.this);
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
                    Error_Dialog.show("Timeout", MyAgendaDetails.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), MyAgendaDetails.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", MyAgendaDetails.this);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book(appDetails.getAppId()).read("userId", ""));
                params.put("action", s);
                params.put("type", "agenda");
                params.put("rating", String.valueOf(rating));
                params.put("type_id", String.valueOf(item.getAgendaId()));
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
