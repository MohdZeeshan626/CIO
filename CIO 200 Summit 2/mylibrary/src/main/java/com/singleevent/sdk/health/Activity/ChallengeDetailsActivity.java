package com.singleevent.sdk.health.Activity;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.ExifInterface;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.MyRequestBase;
import com.singleevent.sdk.View.RightActivity.admin.checkin.EventUsersActivity;
import com.singleevent.sdk.View.RightActivity.admin.model.EventUser;
import com.singleevent.sdk.health.API.loginApi;
import com.singleevent.sdk.health.Model.ChallengeList;
import com.singleevent.sdk.health.Model.Updatesteps;
import com.singleevent.sdk.health.Model.loginModel;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Likeinfo;
import com.singleevent.sdk.model.My_Request;
import com.singleevent.sdk.service.UploadPost;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.singleevent.sdk.health.Activity.PrivateChallenge.CHECK_IN_REQ_TAG;

public class ChallengeDetailsActivity extends AppCompatActivity  implements View.OnClickListener {

    Button btnsave;
    EditText c_name, c_desc;
    int mode;
    LinearLayout public_privacy, private_privacy;
    TextView privacytext,gp_text,gp_text1;
    String groupprivacy;
    AppDetails appDetails;
    ImageView imgpublic1,imgpublic;
    String starttime,endtime;
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private static final int ZBAR_CAMERA_PERMISSION1 = 444;
    int startday;
    ArrayList<ChallengeList> challengeLists=new ArrayList<>();
    ArrayList<Updatesteps> updatesteps=new ArrayList<>();
    private ArrayList<String> HmMins ;
    Uri photoURI;
    String mCurrentPhotoPath;
    TextView textView6;
    ImageView ch_image;
    Bitmap bitmap;
    String type="steps";

    String selectedImagePath;
    boolean isfrom=false;
    String encodedImage;
    private double CellWidth;
    String imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_details);
        btnsave = findViewById(R.id.btn_savenfinish);
        c_name = findViewById(R.id.ed_challengeName);
        c_desc = findViewById(R.id.ed_challengDescrip);
        public_privacy = findViewById(R.id.public_privacy);
        private_privacy = findViewById(R.id.private_privacy);
        privacytext = findViewById(R.id.privacytext);
        appDetails = Paper.book().read("Appdetails");
        public_privacy.setOnClickListener(this);
        private_privacy.setOnClickListener(this);
        gp_text=(TextView)findViewById(R.id.gp_text);
        gp_text1=(TextView)findViewById(R.id.gp_text1);
        imgpublic=(ImageView) findViewById(R.id.imgpublic);
        imgpublic1=(ImageView) findViewById(R.id.imgpublic1);
        ch_image=(ImageView) findViewById(R.id.imageView3);
        textView6=(TextView)findViewById(R.id.textView6);
       // textView6.setBackground(Util.setrounded(Color.LTGRAY));
        HmMins=new ArrayList<>();
        HmMins= Paper.book().read("Weekly_Steps",null);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        CellWidth = (displayMetrics.widthPixels * 0.20F);


        textView6.setOnClickListener(this);
        btnsave.setOnClickListener(this);
     //   btnsave.setBackground(Util.setrounded(Color.parseColor(appDetails.getTheme_color())));

        if(getIntent().getExtras()==null){
            finish();
        }
        try {
            starttime = getIntent().getExtras().getString("selectdate");
            startday = getIntent().getExtras().getInt("selectdays");
            //type=getIntent().getExtras().getString("type");

            String oldDate = starttime;
            System.out.println("Date before Addition: "+oldDate);
            //Specifying date format that matches the given date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            try{
                //Setting the date to the given date
                c.setTime(sdf.parse(oldDate));
            }catch(ParseException e){
                e.printStackTrace();
            }

            //Number of Days to add
            c.add(Calendar.DAY_OF_MONTH, startday);
            //Date after adding the days to the given date
             endtime = sdf.format(c.getTime());
            //Displaying the new Date after addition of Days
        }catch (Exception e){

        }
////////////////////////Adding for Dialy call
       /* Calendar timeOfDay = Calendar.getInstance();
        timeOfDay.set(Calendar.HOUR_OF_DAY, 5);
        timeOfDay.set(Calendar.MINUTE, 0);
        timeOfDay.set(Calendar.SECOND, 0);

        new DailyRunnerDaemon(timeOfDay, new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    // call whatever your daily task is here
                    UpdateSteps();
                }
                catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(),"An error occurred performing daily housekeeping",Toast.LENGTH_LONG).show();
                }
            }
        }, "daily-housekeeping");*/
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.public_privacy) {
            mode = 0;
            GradientDrawable drawable = (GradientDrawable) public_privacy.getBackground();
            drawable.setStroke (1, Color.parseColor (appDetails.getTheme_color())); // Sets the border width and color
           // If you use your own defined color, use drawable.setColor (getResources () getColor (R.color.yellow_color).);
           // Set the fill color

           // drawable.setColor( Color.parseColor("#ff495b"));

          //  public_privacy.setBackground(Util.setrounded(Color.parseColor(appDetails.getTheme_color())));
            gp_text.setTextColor(Color.parseColor(appDetails.getTheme_color()));
            imgpublic.setColorFilter(Color.parseColor(appDetails.getTheme_color()));
            gp_text1.setTextColor(Color.parseColor("#203142"));
            imgpublic1.setColorFilter(Color.parseColor("#203142"));
            private_privacy.setBackground(Util.setrounded(Color.WHITE));
            GradientDrawable drawable1 = (GradientDrawable) private_privacy.getBackground();
            drawable1.setStroke (1, Color.GRAY);


            privacytext.setText("Anyone can see who's in the challenge");
        }
        if (view.getId() == R.id.private_privacy) {
            mode = 1;
            GradientDrawable drawable = (GradientDrawable) private_privacy.getBackground();
            drawable.setStroke (1, Color.parseColor (appDetails.getTheme_color()));
            gp_text1.setTextColor(Color.parseColor(appDetails.getTheme_color()));
            imgpublic1.setColorFilter(Color.parseColor(appDetails.getTheme_color()));
            gp_text.setTextColor(Color.parseColor("#203142"));
            imgpublic.setColorFilter(Color.parseColor("#203142"));
          //  private_privacy.setBackground(Util.setrounded(Color.parseColor(appDetails.getTheme_color())));
            public_privacy.setBackground(Util.setrounded(Color.WHITE));
            GradientDrawable drawable1 = (GradientDrawable) public_privacy.getBackground();
            drawable1.setStroke (1, Color.GRAY);
            privacytext.setText("Only member can see who's in the challenge");
        }
        if(view.getId()==R.id.textView6){
            onSelectImageClick();
            //checkCameraPermission();
        }

        if (view.getId() == R.id.btn_savenfinish) {
            if (c_name.getText().length() == 0) {

                Toast.makeText(getApplicationContext(), "Please Enter Your Challenge Name", Toast.LENGTH_LONG).show();
            }
            else if(c_desc.getText().length()==0){
                Toast.makeText(getApplicationContext(), "Please Enter Your Challenge Description", Toast.LENGTH_LONG).show();
            }else {
                if (mode == 0) {
                   // login();
                    //SubmitDetails(mode,getAlphaNumericString(8));
                    createChallenge(0);
                    //
                    // UpdateSteps();

                } else {
                    //createChallenge(1);
                    Intent i = new Intent(ChallengeDetailsActivity.this, PrivateChallenge.class);
                    i.putExtra("cname", c_name.getText().toString());
                    i.putExtra("c_des", c_desc.getText().toString());
                    i.putExtra("start_time",starttime);
                    i.putExtra("end_time",endtime);
                    if(imageurl!=null){
                    i.putExtra("imageurl",imageurl);}
                    else{
                        i.putExtra("imageurl","imageurl");
                    }
                   // i.putExtra("type",type);
                    startActivity(i);
                    finish();


                }
            }
        }



    }




    public String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "01234fahg569";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }


  private void uploadbase64(String base) {

      final ProgressDialog dialog = new ProgressDialog(ChallengeDetailsActivity.this, R.style.MyAlertDialogStyle);
      dialog.setMessage("Uploading image ...");
      dialog.show();
      String tag_string_req = "Profile";
      String url ="https://check1.webmobi.in/api/event/uploadmisc";
      StringRequest strReq = new StringRequest(Request.Method.POST,
              url, new Response.Listener<String>() {


          @Override
          public void onResponse(String response) {
              dialog.dismiss();
              try {

                  JSONObject jObj = new JSONObject(response);
                  System.out.println(jObj.getString("responseString"));


                  if (jObj.getBoolean("response")) {
                  //    Error_Dialog.show(response.toString(), ChallengeDetailsActivity.this);
                      Paper.book().write("ProfilePIC", jObj.getString("responseString"));
                      try {
                          imageurl=jObj.getJSONObject("responseString").getString("Location");
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                      //setImages(jObj.getString("responseString"));
                  } else {
                      if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                          Error_Dialog.show("Session Expired, Please Login ", ChallengeDetailsActivity.this);
                      } else
                          Error_Dialog.show(jObj.getString("responseString"), ChallengeDetailsActivity.this);
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
                  Error_Dialog.show("Timeout", ChallengeDetailsActivity.this);
              } else if (VolleyErrorLis.isServerProblem(error)) {
                  Error_Dialog.show(VolleyErrorLis.handleServerError(error, ChallengeDetailsActivity.this), ChallengeDetailsActivity.this);
              } else if (VolleyErrorLis.isNetworkProblem(error)) {
                  Error_Dialog.show("Please Check Your Internet Connection", ChallengeDetailsActivity.this);
              }
          }
      }) {
          @Override
          protected Map<String, String> getParams() {
              Map<String, String> params = new HashMap<String, String>();
              params.put("appUrl",appDetails.getAppUrl());
              params.put("filedata", base);
              params.put("userid",Paper.book().read("userId", ""));
              params.put("file_name","test");
              params.put("contenttype", "jpeg");
              System.out.println(params);
              return params;
          }

          /*@Override
          public Map<String, String> getHeaders() throws AuthFailureError {
              Map<String, String> headers = new HashMap<String, String>();
              headers.put("token", Paper.book().read("token", ""));
              return headers;
          }*/
      };


      App.getInstance().addToRequestQueue(strReq, tag_string_req);
      strReq.setRetryPolicy(new DefaultRetryPolicy(
              500000,
              DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
              DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
      ));


  }

    private void createChallenge(int mode) {
        String reqTag = "create challenge";
        JSONObject jsonObject = new JSONObject();
        try {

          //  jsonObject.put("challenge_id",getAlphaNumericString(8));
            jsonObject.put("app_id",appDetails.getAppId());
            jsonObject.put("title",c_name.getText().toString());
            if(imageurl!=null&& !imageurl.equalsIgnoreCase("")){
            jsonObject.put("imageUrl",imageurl);
            }else{
                jsonObject.put("imageUrl","imageUrl");
            }
            jsonObject.put("description",c_desc.getText().toString());
            jsonObject.put("createdBy",Paper.book().read("username", " ") );
            jsonObject.put("challengeType",type);
            jsonObject.put("viewChallenge","public");
            jsonObject.put("challengeStatus","active");
            jsonObject.put("startDate",starttime);//"2021-01-22");
            jsonObject.put("endDate",endtime);//"2021-01-29");


        } catch (Exception e) {
            e.printStackTrace();
        }
        //new Json post request
        if (true) {
            final ProgressDialog pDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
            pDialog.setMessage("Creating ...");
            pDialog.show();

            JsonObjectRequest jsonObjReq;

            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    "https://api2.webmobi.com/health/createchallenge", jsonObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Gson gson = new Gson();
                            try {
                                JSONObject object = response.getJSONObject("data");
                                challengeLists.clear();
                                    ChallengeList user = gson.fromJson(object.toString(), ChallengeList.class);
                                    challengeLists.add(user);
                                if(mode==0){
                                    Intent i = new Intent(ChallengeDetailsActivity.this, ConfirmationChallengeActivity.class);
                                    i.putExtra("Challengename",user.getTitle().toString());
                                    i.putExtra("startdate",user.getStartDate());
                                    i.putExtra("end_date",user.getEndDate());
                                    startActivity(i);
                                    finish();}
                               /* else{
                                    Intent i = new Intent(ChallengeDetailsActivity.this, PrivateChallenge.class);
                                    i.putExtra("cname", c_name.getText().toString());
                                    i.putExtra("challenge_id", "122");
                                    startActivity(i);
                                    finish();
                                }*/




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // hide the progress dialog
                            pDialog.dismiss();
                            try {
                                if (response.getString("response").equals("true")) {

                                    Toast.makeText(ChallengeDetailsActivity.this, Util.applyFontToMenuItem(ChallengeDetailsActivity.this,
                                            new SpannableString("Challenge created successfully")), Toast.LENGTH_SHORT).show();
                                } else if (response.getString("response").equals("false")) {
                                    if (response.getString("responseString").equals("Insufficient data.")) {

                                    } else {
                                        Toast.makeText(ChallengeDetailsActivity.this, "Backup was not successful", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            } catch (Exception e) {

                            }
                        }

                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                   /* if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }*/

                    // hide the progress dialog
                    pDialog.dismiss();
                    if ("com.android.volley.TimeoutError".equals(error.toString()))
                        Toast.makeText(ChallengeDetailsActivity.this, "Timeout Error", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(ChallengeDetailsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                }
            });
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            jsonObjReq.setShouldCache(false);
            App.getInstance().addToRequestQueue(jsonObjReq, reqTag);

        }
    }

    public static class DailyRunnerDaemon
    {
        private final Runnable dailyTask;
        private final int hour;
        private final int minute;
        private final int second;
        private String runThreadName = "abc";

        public DailyRunnerDaemon(Calendar timeOfDay, Runnable dailyTask, String runThreadName)
        {
            this.dailyTask = dailyTask;
            this.hour = timeOfDay.get(Calendar.HOUR_OF_DAY);
            this.minute = timeOfDay.get(Calendar.MINUTE);
            this.second = timeOfDay.get(Calendar.SECOND);
            this.runThreadName = runThreadName;
        }

        public void start()
        {
            startTimer();
        }

        private void startTimer() {

        }

        {
            new Timer(runThreadName, true).schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    dailyTask.run();
                    startTimer();
                }
            }, getNextRunTime());
        }


        private Date getNextRunTime()
        {
            Calendar startTime = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, hour);
            startTime.set(Calendar.MINUTE, minute);
            startTime.set(Calendar.SECOND, second);
            startTime.set(Calendar.MILLISECOND, 0);

            if(startTime.before(now) || startTime.equals(now))
            {
                startTime.add(Calendar.DATE, 1);
            }

            return startTime.getTime();
        }
    }

    public void selectimage(View view){
        checkCameraPermission();
    }
    private void checkCameraPermission() {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // only for marshmallow and newer versions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
            } else {
                //captureImage(true);
                onSelectImageClick();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            }
            else{
                //captureImage(true);
                onSelectImageClick();
            }
        } else {
          //  captureImage(true);.
            onSelectImageClick();
        }

    }

    public void onSelectImageClick() {

        Rect square6 = new Rect();
        square6.set(0, 0, 0, 0);

        CropImage.activity(null)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setRequestedSize((int) CellWidth, (int) CellWidth)
                .setMinCropWindowSize((int) CellWidth, (int) CellWidth)
                .start(this);
    }
    private void captureImage(boolean iscamera) {
        dispatchTakePictureIntent();
    }
    private void dispatchTakePictureIntent() {

        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your challenge picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = null;

                        pictureActionIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(
                                pictureActionIntent,
                                60);

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {


                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            // Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File
                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                        "com.webmobi.eventsapp.provider",
                                        photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                                startActivityForResult(takePictureIntent, 50);
                            }
                        }

                    }
                });
        myAlertDialog.show();

       /* Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.webmobi.eventsapp.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, 50);
            }
        }*/
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imagePath = mCurrentPhotoPath;
         bitmap=null;
         selectedImagePath=null;
        switch (requestCode) {

            case 50:

                if (resultCode == Activity.RESULT_OK) {
                    setPic(imagePath);
                    isfrom=false;


                } break;
            case 203:
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        final InputStream imageStream;
                        try {
                            imageStream = getContentResolver().openInputStream(result.getUri());
                            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                             encodedImage = encodeImage(selectedImage);
                            if (selectedImagePath != null) {
                                textView6.setText("Edit Image");
                            }
                            ch_image.setImageBitmap(selectedImage);
                            uploadbase64(encodedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case 60:
                if (data != null) {

                    Uri selectedImage = data.getData();
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImage, filePath,
                            null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    selectedImagePath = c.getString(columnIndex);
                    c.close();

                    if (selectedImagePath != null) {
                        textView6.setText("Edit Image");
                    }

                    bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
                    // preview image
                    bitmap = Bitmap.createScaledBitmap(bitmap, 160, 130, false);
                   // CropImage.ActivityResult result = CropImage.getActivityResult(data);

                    final InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap selectedImage1 = BitmapFactory.decodeStream(imageStream);
                         encodedImage = encodeImage(selectedImage1);
                       // updateprofilepic(encodedImage);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }



                    ch_image.setImageBitmap(bitmap);
                    isfrom=true;

                } else {
                    Toast.makeText(getApplicationContext(), "Cancelled",
                            Toast.LENGTH_SHORT).show();
                }


                break;

        }


    }




    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
    private void setPic(String imagePath) {
        // Get the dimensions of the View

        int targetW = ch_image.getWidth();
        int targetH = ch_image.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        try {


            bmOptions.inJustDecodeBounds = false;
            // bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            ch_image.setImageBitmap(bitmap);
            if (bitmap != null) {
                textView6.setText("Edit Image");
            }
            ///////////////
           /* Uri imageUri = Uri.parse(mCurrentPhotoPath);
            Bitmap rotatedBitmap;
            int rotateImage = getCameraPhotoOrientation(ChallengeDetailsActivity.this, imageUri, mCurrentPhotoPath);
            Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            Matrix matrix = new Matrix();
            matrix.postRotate(rotateImage);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap1, bitmap1.getWidth(), bitmap1.getHeight(), true);
            rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            ch_image.setImageBitmap(bitmap);
            rotatedBitmap.recycle();
            rotatedBitmap = null;*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);
            String s=imageFile.getPath();
            String s1=s.substring(6);
            ExifInterface exif = new ExifInterface(s);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }
    private String convert_uri_to_base64(String path) {

        Bitmap bm = BitmapFactory.decodeFile(path);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
//        return baos.toByteArray();


        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bm.recycle();
            bm = null;
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        } else {
            Uri imageUri = Uri.parse(path);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                bitmap.recycle();
                bitmap = null;
                byte[] b = baos.toByteArray();
                return Base64.encodeToString(b, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }


}

