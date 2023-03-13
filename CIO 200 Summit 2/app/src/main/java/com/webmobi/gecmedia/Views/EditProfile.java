package com.webmobi.gecmedia.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.User_Details;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.webmobi.gecmedia.CustomViews.ColorFilterTransformation;
import com.webmobi.gecmedia.LinkedinSDK.APIHelper;
import com.webmobi.gecmedia.LinkedinSDK.LISessionManager;
import com.webmobi.gecmedia.LinkedinSDK.errors.LIApiError;
import com.webmobi.gecmedia.LinkedinSDK.errors.LIAuthError;
import com.webmobi.gecmedia.LinkedinSDK.listeners.ApiListener;
import com.webmobi.gecmedia.LinkedinSDK.listeners.ApiResponse;
import com.webmobi.gecmedia.LinkedinSDK.listeners.AuthListener;
import com.webmobi.gecmedia.LinkedinSDK.utils.Scope;
import com.webmobi.gecmedia.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    ImageView profile_image, edit_image;
    RoundedImageView profile_banner;
    RelativeLayout linkedin;
    float ImgWidth, ImgHeight, lwidth, lheight;
    double cellWidth;
    public static final String EDIT_PROFILE_EXTRAS = "edit_extras";
    User_Details user_details;
    EditText f_name_et, l_name_et, desig_et, mobile_et, email_et, web_et, desc_et, url_et, company_et;
    TextView save_txt;
    String f_name_str, l_name_str, desig_str, mobile_str, email_str, web_str, desc_str, url_str, company_str;

    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://" + host + "/v1/people/~:" +
            "(id,first-name,email-address,last-name,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras == null)
            return;
        user_details = (User_Details) extras.getParcelable(EDIT_PROFILE_EXTRAS);

        setResources();
        setEditTextValues();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = displayMetrics.widthPixels * 0.30F;
        ImgHeight = ImgWidth * 1.7F;
        lwidth = ImgWidth * 0.70F;
        lheight = lwidth * 0.75F;
        /*dir = getFilesDir() + File.separator + "EventsDownload" + File.separator;
        UserID = Paper.book().read("userId");*/
        cellWidth = (displayMetrics.widthPixels * 0.20F);
        setImages(user_details.getProfile_pic());
    }

    private void setEditTextValues() {
        f_name_et.setText(user_details.getFirst_name());
        l_name_et.setText(user_details.getLast_name());
        desig_et.setText(user_details.getDesignation());
        mobile_et.setText(user_details.getMobile());
        email_et.setText(user_details.getEmail());
        web_et.setText(user_details.getWebsite());
        desc_et.setText(user_details.getDescription());
        url_et.setText(user_details.getUser_blog());
        company_et.setText(user_details.getCompany());


    }

    private void setResources() {
        f_name_et = (EditText) findViewById(R.id.edit_profile_fname_et);
        l_name_et = (EditText) findViewById(R.id.edit_profile_lname_et);
        desig_et = (EditText) findViewById(R.id.edit_profile_designation_et);
        mobile_et = (EditText) findViewById(R.id.edit_profile_mobile_et);
        email_et = (EditText) findViewById(R.id.edit_profile_email_et);
        web_et = (EditText) findViewById(R.id.edit_profile_web_et);
        desc_et = (EditText) findViewById(R.id.edit_profile_desc_et);
        url_et = (EditText) findViewById(R.id.edit_profile_url_et);
        company_et = (EditText) findViewById(R.id.edit_profile_company_et);
        linkedin = (RelativeLayout) findViewById(R.id.edit_profile_linkedin);
        edit_image = (ImageView) findViewById(R.id.edit_profile_edit_image);
        save_txt = (TextView) findViewById(R.id.edit_profile_save);
        save_txt.setOnClickListener(this);
        edit_image.setOnClickListener(this);
        linkedin.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        setTitle("");
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setImages(String profile_pic_str) {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        RelativeLayout profile_image_layout = (RelativeLayout) findViewById(R.id.edit_profile_img_relative);

        FrameLayout.LayoutParams profilelayoutparams = (FrameLayout.LayoutParams) profile_image_layout.getLayoutParams();

        profilelayoutparams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        profilelayoutparams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        profilelayoutparams.height = (int) (ImgWidth*1.32);

        profile_image_layout.setLayoutParams(profilelayoutparams);
        int clogo_height = (int) (ImgHeight * 1.02);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, clogo_height);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (ImgWidth * 1.10), (int) (ImgHeight * 0.65));


        profile_banner = (RoundedImageView) findViewById(R.id.edit_profile_banner);
        profile_image = (ImageView) findViewById(R.id.edit_profile_image);

        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        Glide.with(getApplicationContext())
                .load(profile_pic_str.equalsIgnoreCase("") ? R.drawable.profile_default : profile_pic_str)
                .fitCenter()
                .placeholder(R.drawable.profile_default)
                .dontAnimate()
                .error(R.drawable.profile_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new ColorFilterTransformation(this, Color.argb(0, 0, 0, 0)))
                .into(profile_banner);


        profile_banner.setLayoutParams(layoutParams);


        RelativeLayout logo2_layout = (RelativeLayout) findViewById(R.id.edit_profile_layout);
        int margintop = (int) (clogo_height - (getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material) / 2));
        RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) cellWidth, (int) cellWidth);

//        RelativeLayout.LayoutParams logo2_layout_params = new RelativeLayout.LayoutParams((int) (ImgWidth * 0.45), (int) (ImgWidth * 0.45));
        logo2_layout_params.setMargins(0, margintop - 20, 0, 0);
        logo2_layout_params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        logo2_layout.setLayoutParams(logo2_layout_params);

//            clogo_center.setCornerRadius(7,7,7,7);
        profile_image.setLayoutParams(new RelativeLayout.LayoutParams((int) cellWidth, (int) cellWidth));
        Glide.with(getApplicationContext())

                .load(profile_pic_str.equalsIgnoreCase("") ? R.drawable.profile_default : profile_pic_str)
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_profile_save:
                getValues();
                break;
            case R.id.edit_profile_edit_image:
                onSelectImageClick(view);
                break;
            case R.id.edit_profile_linkedin:
                linkedinlogin();
                break;
        }
    }

    private void getValues() {
        f_name_str = f_name_et.getText().toString();
        l_name_str = l_name_et.getText().toString();
        desig_str = desig_et.getText().toString();
        mobile_str = mobile_et.getText().toString();
        email_str = email_et.getText().toString();
        web_str = web_et.getText().toString();
        desc_str = desc_et.getText().toString();
        url_str = url_et.getText().toString();
        company_str = company_et.getText().toString();
        UpdatingProfile();
    }

    public void onSelectImageClick(View view) {

        Rect square6 = new Rect();
        square6.set(0, 0, 0, 0);

        CropImage.activity(null)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setRequestedSize((int) cellWidth, (int) cellWidth)
                .setMinCropWindowSize((int) cellWidth, (int) cellWidth)
                .start(this);
    }

    private void UpdatingProfile() {

        final ProgressDialog dialog = new ProgressDialog(EditProfile.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Updating Profile...");
        dialog.show();
        String tag_string_req = "Profile";
        String url = ApiList.SetProfile;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), EditProfile.this);
                        Intent i = new Intent();
                        setResult(RESULT_OK, i);
                        finish();
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session Expired, Please Login", EditProfile.this);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), EditProfile.this);
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
                    Error_Dialog.show("Timeout", EditProfile.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, EditProfile.this), EditProfile.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", EditProfile.this);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("first_name", f_name_str);
                params.put("last_name", l_name_str);
                params.put("phone", mobile_str);
                params.put("designation", desig_str);
                params.put("company", company_str);
                params.put("profile_pic_url", Paper.book().read("ProfilePIC", ""));
                params.put("website", web_str);
                params.put("user_blog", url_str);
                params.put("description", desc_str);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(result.getUri());
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    String encodedImage = encodeImage(selectedImage);
                    updateprofilepic(encodedImage);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        } else {
            LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
            linkededinApiHelper();
        }

    }

    public void linkededinApiHelper() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(EditProfile.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    System.out.println(result.getResponseDataAsJson());
                    JSONObject jobject = result.getResponseDataAsJson();
                    f_name_et.setText(jobject.getString("firstName"));
                    l_name_et.setText(jobject.getString("lastName"));
                    Paper.book().write("ProfilePIC", jobject.getString("pictureUrl"));
                    setImages(jobject.getString("pictureUrl"));
                    // UpdatingProfile();
                    getValues();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onApiError(LIApiError error) {
                // ((TextView) findViewById(R.id.error)).setText(error.toString());

            }
        });
    }


    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void updateprofilepic(final String encodedImage) {

        final ProgressDialog dialog = new ProgressDialog(EditProfile.this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Uploading Profile Pic...");
        dialog.show();
        String tag_string_req = "Profile";
        String url = ApiList.UpdateProfilePic;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Paper.book().write("ProfilePIC", jObj.getString("responseString"));
                        setImages(jObj.getString("responseString"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session Expired, Please Login ", EditProfile.this);
                        } else
                            Error_Dialog.show(jObj.getString("responseString"), EditProfile.this);
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
                    Error_Dialog.show("Timeout", EditProfile.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, EditProfile.this), EditProfile.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", EditProfile.this);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId", ""));
                params.put("appurl", "");
                params.put("user_type", "eventuser");
                params.put("image", encodedImage);
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

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    private void linkedinlogin() {

        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {

                // Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAuthError(LIAuthError error) {

                Toast.makeText(getApplicationContext(), "failed " + error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }, true);

    }
}
