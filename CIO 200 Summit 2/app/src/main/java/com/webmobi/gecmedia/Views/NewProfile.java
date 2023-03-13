package com.webmobi.gecmedia.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcelable;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.User_Details;
import com.webmobi.gecmedia.CustomViews.ColorFilterTransformation;
import com.webmobi.gecmedia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

import static com.webmobi.gecmedia.Views.EditProfile.EDIT_PROFILE_EXTRAS;

public class NewProfile extends AppCompatActivity {

    ImageView profile_image;
    RoundedImageView profile_banner;
    TextView name_txt, designation_txt, mobile_txt, email_txt, web_txt, desc_txt, url_txt, company_txt;
    float ImgWidth, ImgHeight, lwidth, lheight;
    double cellWidth;
    List<String> savedirs;
    String dir, UserID, profile_pic_str;
    User_Details user_details, user_details1;
    int EDIT_PROFILE_REQ_CODE = 1;
    int EDIT_PROFILE_RES_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels * 0.30F;
        ImgHeight = ImgWidth * 1.7F;
        lwidth = ImgWidth * 0.70F;
        lheight = lwidth * 0.75F;
        dir = getFilesDir() + File.separator + "EventsDownload" + File.separator;
        UserID = Paper.book().read("userId");
        cellWidth = (displayMetrics.widthPixels * 0.20F);
        getprofile();
        setResources();


    }

    private void setResources() {
        name_txt = (TextView) findViewById(R.id.profile_name_txt);
        designation_txt = (TextView) findViewById(R.id.profile_designation_txt);
        mobile_txt = (TextView) findViewById(R.id.profile_mobile_txt);
        email_txt = (TextView) findViewById(R.id.profile_email_txt);
        web_txt = (TextView) findViewById(R.id.profile_weblink_txt);
        desc_txt = (TextView) findViewById(R.id.profile_desc_txt);
        url_txt = (TextView) findViewById(R.id.profile_url_txt);
        company_txt = (TextView) findViewById(R.id.profile_company_txt);

        name_txt.setTypeface(Util.regulartypeface(this));
        designation_txt.setTypeface(Util.regulartypeface(this));
        mobile_txt.setTypeface(Util.regulartypeface(this));
        email_txt.setTypeface(Util.regulartypeface(this));
        web_txt.setTypeface(Util.regulartypeface(this));
        desc_txt.setTypeface(Util.regulartypeface(this));
        url_txt.setTypeface(Util.regulartypeface(this));
        company_txt.setTypeface(Util.regulartypeface(this));

    }

    private void getprofile() {


        final ProgressDialog pDialog = new ProgressDialog(NewProfile.this, R.style.MyAlertDialogStyle);
        pDialog.setMessage("Loading ...");
        pDialog.show();


        String tag_string_req = "Login";
        String url = ApiList.GetProfile + UserID;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                System.out.println(response);
                Gson gson = new Gson();

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                    if (jObj.getBoolean("response")) {
                        JSONArray chatlist = jObj.getJSONArray("Profile");
                        String userdetails = chatlist.getJSONObject(0).toString();
                        user_details = gson.fromJson(userdetails, User_Details.class);
                        setdata(user_details);
                        setImages(user_details.getProfile_pic());
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName("com.webmobi.eventsapp", "com.webmobi.eventsapp.Views.TokenExpireAlertReceived");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), NewProfile.this);
                    }
                } catch (JSONException e) {


                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", NewProfile.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, NewProfile.this), NewProfile.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", NewProfile.this);
                    setdataOffline();
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

    private void setdataOffline() {
        User_Details user_details = Paper.book().read("UserDetailsOffline");
        name_txt.setText(user_details.getFirst_name() + " " + user_details.getLast_name());
        designation_txt.setText(user_details.getDesignation());
        mobile_txt.setText(user_details.getMobile());
        email_txt.setText(user_details.getEmail());
        web_txt.setText(user_details.getWebsite());
        desc_txt.setText(user_details.getDescription());
        url_txt.setText(user_details.getUser_blog());
        company_txt.setText(user_details.getCompany());

    }

    private void setdata(User_Details user_details) {
        Paper.book().write("UserDetailsOffline", user_details);
        name_txt.setText(user_details.getFirst_name() + " " + user_details.getLast_name());
        designation_txt.setText(user_details.getDesignation());
        mobile_txt.setText(user_details.getMobile());
        email_txt.setText(user_details.getEmail());
        web_txt.setText(user_details.getWebsite());
        desc_txt.setText(user_details.getDescription());
        url_txt.setText(user_details.getUser_blog());
        company_txt.setText(user_details.getCompany());
        setImages(user_details.getProfile_pic());

    }

    private void setImages(String profile_pic) {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = layoutInflater.inflate(R.layout.activity_new_profile, null);

        RelativeLayout profile_image_layout = (RelativeLayout) findViewById(R.id.profile_img_relative);

        FrameLayout.LayoutParams profilelayoutparams = (FrameLayout.LayoutParams) profile_image_layout.getLayoutParams();

        profilelayoutparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        profilelayoutparams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        profilelayoutparams.height = (int) (ImgWidth*1.32);

        profile_image_layout.setLayoutParams(profilelayoutparams);
        int clogo_height = (int) (ImgHeight * 1.02);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, clogo_height);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.10), (int) (ImgHeight * 0.65));


        profile_banner = (RoundedImageView) findViewById(R.id.profile_banner);
        profile_image = (ImageView) findViewById(R.id.profile_image);

        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);


        Glide.with(getApplicationContext()).load(profile_pic.equalsIgnoreCase("") ? R.drawable.profile_default : profile_pic)
                .fitCenter()
                .placeholder(R.drawable.profile_default)
                .dontAnimate()
                .error(R.drawable.profile_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new ColorFilterTransformation(this, Color.argb(0, 0, 0, 0)))
                .into(profile_banner);


        profile_banner.setLayoutParams(layoutParams);


        RelativeLayout logo2_layout = (RelativeLayout) findViewById(R.id.profile_layout);
        int margintop = (int) (clogo_height - (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) / 2));
        RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) cellWidth, (int) cellWidth);

//        RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45));
        logo2_layout_params.setMargins(0, margintop - 20, 0, 0);
        logo2_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        logo2_layout.setLayoutParams(logo2_layout_params);

//            clogo_center.setCornerRadius(7,7,7,7);
        profile_image.setLayoutParams(new RelativeLayout.LayoutParams((int) cellWidth, (int) cellWidth));
        Glide.with(getApplicationContext())
                .load(profile_pic.equalsIgnoreCase("") ? R.drawable.profile_default : profile_pic)
                .asBitmap()
                .placeholder(R.drawable.profile_default)
                .error(R.drawable.profile_default)
                .into(new BitmapImageViewTarget(profile_image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), Bitmap.createScaledBitmap(resource, (int) cellWidth, (int) cellWidth, false));
                        drawable.setCircular(true);
                        profile_image.setImageDrawable(drawable);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("PROFILE");
        setTitle(Util.applyFontToMenuItem(this, s));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.edit_profile_menu:
                Intent i = new Intent(this, EditProfile.class);
                i.putExtra(EDIT_PROFILE_EXTRAS, (Parcelable) user_details);
                startActivityForResult(i, EDIT_PROFILE_REQ_CODE);
                break;
            case R.id.change_pwd_menu:
                startActivity(new Intent(this, ChangePassword.class));
                break;
            case R.id.logout_menu:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);

    }


    private void logout() {

        final ProgressDialog dialog = new ProgressDialog(NewProfile.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Logout...");
        dialog.show();
        String tag_string_req = "Logout";
        String url = ApiList.Logout;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("response")) {
                        Paper.book().delete("Islogin");
                        Paper.book().delete("email");
                        Paper.book().delete("username");
                        Paper.book().delete("Password");
                        Paper.book().delete("Email");
                        clearalllogin();
                        clearApplicationData();
                        setResult(RESULT_OK);
                        finish();

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {

                            Paper.book().delete("Islogin");
                            Paper.book().delete("email");
                            Paper.book().delete("username");
                            Paper.book().delete("Password");
                            Paper.book().delete("Email");
                            clearalllogin();
                            clearApplicationData();
                            Error_Dialog.show("Session Expired, Please Login", NewProfile.this);


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), NewProfile.this);
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
                    Error_Dialog.show("Timeout", NewProfile.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, NewProfile.this), NewProfile.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", NewProfile.this);
                }
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceType", "android");
                params.put("deviceId", Paper.book().read("regId", ""));
                System.out.println(params);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
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

    private void clearalllogin() {

        savedirs = new ArrayList<>();
        savedirs = getSaveDirs(dir);

        for (String tag : savedirs) {
            System.out.println("" + tag);
            Paper.book(tag).destroy();
        }
    }

    private List<String> getSaveDirs(String dir) {
        List<String> paths = new ArrayList<>();
        File directory = new File(dir);

        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isDirectory())
                paths.add(file.getName());
        }
        return paths;
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(getFilesDir() + File.separator + "EventsDownload" + File.separator);
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }
        }


        File dir = new File(getFilesDir() + "/EventsDownload");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                getprofile();
                System.out.println("Result OK");
            }
        }

    }
}
