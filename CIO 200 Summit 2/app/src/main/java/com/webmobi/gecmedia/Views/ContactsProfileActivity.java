package com.webmobi.gecmedia.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.AppDetails;
import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.Views.fragment.ContactUsFragment;
import com.webmobi.gecmedia.Views.fragment.model.ContactsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

/**
 * Created by webMOBI on 12/12/2017.
 */

public class ContactsProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = ContactsProfileActivity.class.getSimpleName();

    private ImageView expandedImage;
    private EditText et_fName,et_lname,et_con_info;
    private TextView tv_company,tv_designation,tv_address,tv_phone,tv_email,tv_website,tv_fax,
            tv_alt_phone,tv_altEmail,tv_altWebsite,tv_cardType;
    private RelativeLayout rl;
    private String userId,emailId;
    private AppDetails appDetails;
    CoordinatorLayout contactprofile;
    String contactId;
    Button Save;
    private ContactsModel contactsModel;
    private ArrayList<ContactsModel> contactsModels;
    private Menu menu;
    boolean isbol;
    ContactUsFragment contactUsFragment;
    String f_fname,appid,f_lname,f_desc,f_addrs,f_company,f_desig,f_website,f_email,f_dtext,f_uemail,f_flag,f_itemflag,f_uid,f_phone,f_imageUrl,f_altphone,f_altweb,f_altemail;

    private Toolbar toolbar;
    private CollapsingToolbarLayout toolbar_layout;
    ContactUsFragment cf = new ContactUsFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile );

        Paper.init(this);
        try {
            appDetails = Paper.book().read("Appdetails", null);
        }catch (Exception e){

        }
        userId = Paper.book().read("userId","");
        emailId = Paper.book().read("Email","");

        //initialize views
        expandedImage = (ImageView) findViewById(R.id.expandedImage);
        et_fName = (EditText) findViewById(R.id.et_fName);
        et_lname = (EditText) findViewById(R.id.et_lname);
        et_con_info = (EditText) findViewById(R.id.et_con_info);
        tv_company = (TextView) findViewById(R.id.tv_company);
        tv_designation = (TextView) findViewById(R.id.tv_designation);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_website = (TextView) findViewById(R.id.tv_website);
        tv_fax = (TextView) findViewById(R.id.tv_fax);
        tv_alt_phone = (TextView) findViewById(R.id.tv_alt_phone);
        tv_altEmail = (TextView) findViewById(R.id.tv_altEmail);
        tv_altWebsite = (TextView) findViewById(R.id.tv_altWebsite);
        tv_cardType = (TextView) findViewById(R.id.tv_cardType);
        Save = (Button) findViewById(R.id.tv_save);
        try {
           Save.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        }catch (Exception e){}
        contactprofile=(CoordinatorLayout)findViewById(R.id.contactprofile);
        Save.setOnClickListener(this);


        Intent intent = getIntent();
        if (intent==null){
            finish();

        }else {
            Gson gS = new Gson();
            String target = getIntent().getStringExtra("ContactsModel");
            ContactsModel contactsModel = gS.fromJson(target, ContactsModel.class);
            contactId=contactsModel.getContactId();
          try {
              if (appDetails != null){
                  appid = appDetails.getAppId();
          }
              else{
                  appid="";
              }
          }catch (Exception e){}


            //setting profile pic using glide if url is available
            if (contactsModel.getImageUrl()!=null){

                Glide
                        .with(getApplicationContext())
                        .load(contactsModel.getImageUrl())
                        .centerCrop()
                        .placeholder(R.drawable.default_logo)
                        .into(expandedImage);


            } /*if(!contactsModel.getName().equals("")){
              expandedImage.setBackground(DrawableUtil.genBackgroundDrawable(this,contactsModel.getName()));
              expandedImage.setImageResource(DrawableUtil.getDrawableForName(contactsModel.getName()));
            }*/


            et_fName.setText(contactsModel.getContact_first_name());
            et_lname.setText(contactsModel.getContact_last_name());
            et_con_info.setText(contactsModel.getContact_info());
            tv_company.setText( contactsModel.getCompany());
            tv_designation.setText( contactsModel.getDesignation());
            tv_address.setText( contactsModel.getAddress());
            tv_phone.setText( contactsModel.getContact_phone());
            tv_email.setText( contactsModel.getContact_email());
            tv_website.setText( contactsModel.getWebsite());
            tv_fax.setText( contactsModel.getFax());
            tv_alt_phone.setText(  contactsModel.getContact_phone_1());
            tv_altEmail.setText( contactsModel.getContact_email_1());
            tv_altWebsite.setText( contactsModel.getWebsite_1());
            tv_cardType.setText( contactsModel.getCard_type());
            f_imageUrl=contactsModel.getImageUrl();


            Log.v( TAG,contactsModel.toString() );
        }

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_layout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        TextView tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(Util.applyFontToMenuItem(this, new SpannableString("Scan Card Details")));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;

                } else if (isShow) {

                    //toolbar_layout.setTitle("Contact details");
                    isShow = false;

                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:

                try {
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }catch(NullPointerException e)
                {
                    e.printStackTrace();
                }
                uploadData(true, "", "", "", "", "", "", "", "", "", "", "", "");

                break;
        }
    }


    private void replaceFragment(Fragment fragment, String popularfragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_container, fragment, popularfragment);

        transaction.commit();
    }
////////////////////////////////////

    private void uploadData( boolean isFromScan, String fname, String lname, String
            company, String conphone, String website, String descrip, String e_mail, String
                                     u_id, String dtext, String uemail, String flag, String item_flag )
    {
        //api call to upload the contact
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        // progressDialog.dismiss();
        boolean ff = isFromScan;
        String uid = Paper.book().read("userId", "");
        if (isFromScan) {
          /*  f_name = fname;
            l_name = lname;
            desc = descrip;
            extension = "";
            fileExtensionName = "";
            photo_data = "";
            email = Paper.book().read("Email_ID", "");
            uid = Paper.book().read("userId", "");
            detect_text = "1";
            item_flag = "contact";*/
            try {
                f_fname = et_fName.getText().toString();
                f_lname = et_lname.getText().toString();
                f_desc = et_con_info.getText().toString();
                f_company = tv_company.getText().toString();
                f_website = tv_website.getText().toString();
                f_addrs = tv_address.getText().toString();
                f_email = tv_email.getText().toString();
                f_desig = tv_designation.getText().toString();
                f_uemail = Paper.book().read("Email", "");
                f_uid = Paper.book().read("userId", "");
                f_flag = "update";

                f_itemflag = "contact";
                f_phone = tv_phone.getText().toString();
                f_altphone=tv_alt_phone.getText().toString();
                f_altweb=tv_altWebsite.getText().toString();
                f_altemail=tv_altEmail.getText().toString();

            }catch (Exception e)
            {
                e.printStackTrace();
            }

            //f_imageUrl=contactsModel.getCard_image_url();
            // Adding APi to send scan data

            String tag_string_req = "upload_contact";
            String url = ApiList.Post_Contacts;

            final String finalF_name = f_fname;
            final String finalL_name = f_lname;
            final String finalDesc = f_desc;
            // final String finalPhoto_data = photo_data;
            final String finalItem_flag = f_itemflag;
            final String finalemail = f_email;
            final String finaluemail = f_uemail;
            final String finaluid = f_uid;
            final String finaltextdetect_text = "false";
            final String finalPhone = f_phone;
            final String finalaltemail = f_altemail;
            final String finalaltphone = f_altphone;
            final String finalaltweb = f_altweb;
            final String finalcompany = f_company;
            final String finalwebsite = f_website;
            final String finaldesignation = f_desig;
            final String finaladdrs = f_addrs;
            final String finalappid = appid;


            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {
                    try {
                        progressDialog.hide();

                        System.out.println(response);


                        JSONObject jObj = new JSONObject(response);


                        if (jObj.getBoolean("response")) {
                            setContentView(R.layout.lactivity_main3);
                            if (contactUsFragment == null)

                                contactUsFragment = new ContactUsFragment();
                            replaceFragment(contactUsFragment, "contactusfragment");
                            //  toolbar_layout.setVisibility(View.GONE);
                            contactprofile.setVisibility(View.GONE);


                            //  onBackPressed();

                        } else if (!jObj.getBoolean("response")) {
                            Error_Dialog.show(jObj.getString("responseString"), ContactsProfileActivity.this);
                        }

                    } catch (JSONException e) {
                        if (progressDialog.isShowing())
                            progressDialog.hide();

                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog.isShowing()) {
                        progressDialog.hide();
                    }
                    Log.v(TAG, error.toString());
                    if (this != null)
                        if (error instanceof TimeoutError) {
                            Error_Dialog.show("Timeout", ContactsProfileActivity.this);
                        } else if (VolleyErrorLis.isServerProblem(error)) {
                            Error_Dialog.show(VolleyErrorLis.handleServerError(error, getApplicationContext()), ContactsProfileActivity.this);
                        } else if (VolleyErrorLis.isNetworkProblem(error)) {
                            Error_Dialog.show("Please Check Your Internet Connection", ContactsProfileActivity.this);
                        }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("first_name", finalF_name);
                    params.put("last_name", finalL_name);
                    params.put("email", finaluemail);
                    params.put("contact_email", finalemail);
                    params.put("userid", finaluid);
                    params.put("appid", finalappid);
                    params.put("contact_info", finalDesc);
                    params.put("contact_phone", finalPhone);
                    params.put("contact_email_1", finalaltemail);
                    params.put("contact_phone_1", finalaltphone);
                    params.put("website_1", finalaltweb);
                    params.put("company", finalcompany);
                    params.put("website", finalwebsite);
                    params.put("contact_id", contactId);

                    params.put("designation", finaldesignation);
                    params.put("address", finaladdrs);

                    params.put("flag", "update");
                    params.put("item_flag", finalItem_flag);
                    params.put("image_url", f_imageUrl);
                    params.put("detect_text", finaltextdetect_text);
                    System.out.println("Upload Contacts Param : " + params.toString());
                    return params;

                }
            };

//        AppNew.getInstance().getRequestQueue().getCache().clear();
            App.getInstance().addToRequestQueue(strReq, tag_string_req);
            strReq.setRetryPolicy(new DefaultRetryPolicy(
                    100000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));


        }
    }
}

