package com.webmobi.gecmedia.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Letter.LetterSectionListAdapter;
import com.singleevent.sdk.Custom_View.Letter.LetterSectionListItem;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.webmobi.gecmedia.Config.ApiList;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.webmobi.gecmedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;

/**
 * Created by Admin on 5/11/2017.
 */

public class DisUser extends AppCompatActivity {


    String APPID, Appname;
    private Context context;
    Bitmap letterTile;
    int tileSize;
    Resources res;
    LetterTileProvider tileProvider;
    private float dpWidth, badgewidth;
    TextView noitems;
    ListView userlist;
    Toolbar toolbar;
    int pos;
    private String title;
    List<LetterSectionListItem> userview;
    boolean isaction = false;
    Bundle extras;
    LetterSectionListAdapter bookListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_attendee);
        if (getIntent().getExtras() == null)
            finish();

        APPID = getIntent().getStringExtra("Appid");
        Appname = getIntent().getStringExtra("Appname");
        res = getResources();
        context = this;
        tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);
        noitems = (TextView) findViewById(R.id.noitems);
        userlist = (ListView) findViewById(R.id.userlist);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#0a6a99"));
        setSupportActionBar(toolbar);

        tileProvider = new LetterTileProvider(context);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels * 0.10F;
        badgewidth = displayMetrics.widthPixels * 0.05F;
        userview = new ArrayList<>();

        getuser();

    }


    private void getuser() {

        final ProgressDialog dialog = new ProgressDialog(DisUser.this,R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading the User");
        dialog.show();
        String tag_string_req = "Login";
        String url = ApiList.DiscoveryUsers + APPID;
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
                            Error_Dialog.show("Session is Expired Please Login", DisUser.this);


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), DisUser.this);
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
                    Error_Dialog.show("Timeout", DisUser.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context), DisUser.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", DisUser.this);
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", Paper.book().read("token", ""));
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
                com.singleevent.sdk.model.User obj = gson.fromJson(eventString, com.singleevent.sdk.model.User.class);
                obj.setColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));
                userview.add(obj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (userview.size() > 0) {


            for (LetterSectionListItem book : userview) {

                book.calculateSortString();
            }
            settinguser();
        } else
            showview(false);


    }

    private void settinguser() {


        //Using custom booklist_row view that the header will be wrapped around
        bookListAdapter = new LetterSectionListAdapter(this, com.singleevent.sdk.R.layout.s_userlist_row, userview) {
            @Override
            public View getView(int position, View v, ViewGroup parent) {
                //Must call this before to wrap the header around the view
                v = super.getView(position, v, parent);
                final com.singleevent.sdk.model.User book = (com.singleevent.sdk.model.User) this.getItem(position);

                TextView bookTitle = (TextView) v.findViewById(com.singleevent.sdk.R.id.book_title);
                final TextView badge = (TextView) v.findViewById(com.singleevent.sdk.R.id.counter);
                RelativeLayout.LayoutParams badgeParams = (RelativeLayout.LayoutParams) badge.getLayoutParams();
                badgeParams.width = (int) badgewidth;
                badgeParams.height = (int) badgewidth;
                badge.setLayoutParams(badgeParams);


                TextView authorName = (TextView) v.findViewById(com.singleevent.sdk.R.id.author_name);
                final RoundedImageView profilepic = (RoundedImageView) v.findViewById(com.singleevent.sdk.R.id.profilepic);
                RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) profilepic.getLayoutParams();

                imageParams.width = (int) dpWidth;
                imageParams.height = (int) dpWidth;
                profilepic.setLayoutParams(imageParams);


//                if (newmessages.containsKey(book.getUserid())) {
//                    badge.setText("" + newmessages.get(book.getUserid()));
//                    badge.setVisibility(View.VISIBLE);
//                } else
//                    badge.setVisibility(View.GONE);


                authorName.setTypeface(Util.regulartypeface(context));
                bookTitle.setTypeface(Util.regulartypeface(context));
                bookTitle.setText(book.getFirst_name());
                if (!book.getCompany().equalsIgnoreCase("") && !book.getDesignation().equalsIgnoreCase("")) {
                    authorName.setText(book.getDesignation() + ", " + book.getCompany());
                    authorName.setVisibility(View.VISIBLE);
                } else
                    authorName.setVisibility(View.GONE);


                // setting profilepic its there

                if (book.getProfile_pic().equalsIgnoreCase("")) {
                    letterTile = tileProvider.getLetterTile(book.getFirst_name(), "key", (int) dpWidth, (int) dpWidth, book.getColor());
                    profilepic.setImageBitmap((letterTile));
                    profilepic.setCornerRadius(8, 8, 8, 8);

                } else {

                    Glide.with(getApplicationContext())

                            .load(book.getProfile_pic())
                            .asBitmap()
                            .placeholder(com.singleevent.sdk.R.drawable.round_user)
                            .error(com.singleevent.sdk.R.drawable.round_user)
                            .into(profilepic);
                    profilepic.setCornerRadius(8, 8, 8, 8);
                    /*new BitmapImageViewTarget(profilepic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(),
                                            Bitmap.createScaledBitmap(resource, (int) dpWidth, (int) dpWidth, false));
                                    drawable.setCircular(true);
                                    profilepic.setImageDrawable(drawable);
                                }
                            });*/
                }


                v.setTag(book.getUserid());


                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Bundle args = new Bundle();
                        args.putSerializable("UserItem", book);
                        args.putString("appid",APPID);
                        args.putString("appname",Appname);
                        Intent i = new Intent(DisUser.this, MyAttendeeProfile.class);
                        i.putExtras(args);

                        startActivity(i);
                    }
                });

                return v;
            }
        };


        userlist.setAdapter(bookListAdapter);

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
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString("Attendees");
        setTitle(Util.applyFontToMenuItem(this, s));

    }
}
