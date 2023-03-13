package com.singleevent.sdk.View.RightActivity.group_feed;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.singleevent.sdk.Custom_View.Util;


import com.singleevent.sdk.View.RightActivity.FeedPost;
import com.singleevent.sdk.gallery_camera.GalleryPicker;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Config;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;

import com.singleevent.sdk.databinding.FeedPostActivityBinding;
import com.singleevent.sdk.model.Polling.CreatePollResponse;
import com.singleevent.sdk.pojo.pollingpojo.CreatePollPojo;
import com.singleevent.sdk.service.Health1NetworkController;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;

import static com.singleevent.sdk.View.RightActivity.FeedPost.getDataColumn;
import static com.singleevent.sdk.View.RightActivity.FeedPost.isDownloadsDocument;
import static com.singleevent.sdk.View.RightActivity.FeedPost.isExternalStorageDocument;
import static com.singleevent.sdk.View.RightActivity.FeedPost.isMediaDocument;


public class GroupUploadPost extends AppCompatActivity implements View.OnClickListener {

    FeedPostActivityBinding binding;
    AppDetails appDetails;
    int width;
    private float dpWidth, Iwidth;
    private TextView txtSearch;
    private ArrayList<Image> imageselected = new ArrayList<>();
    Context context;
    String group_id;
    String vimeo_video_link="";
    private static final int ZBAR_CAMERA_PERMISSION = 1;
    LinearLayout edit_text_list_layout;
    List<String> options_list = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    String post_category = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        context = GroupUploadPost.this;
        binding = DataBindingUtil.setContentView(this, R.layout.feed_post_activity);
        appDetails = Paper.book().read("Appdetails");
        edit_text_list_layout = findViewById(R.id.edit_text_list_layout);
        binding.toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(binding.toolbar);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        dpWidth = displayMetrics.widthPixels;
        width = (int) dpWidth;
        Iwidth = dpWidth * 0.10F;
        if (getIntent().getExtras() == null)
            finish();

        group_id = getIntent().getExtras().getString("group_id");

        setactiveimage(binding.acamera, R.drawable.camera_color);
        setactiveimage(binding.agallery, R.drawable.gallery);
        binding.ecomment.addTextChangedListener(watcher);
        binding.editTextForPost.addTextChangedListener(watcher);
        setactiveimage(binding.selectCategory, R.drawable.ic_drop_down);

        binding.acamera.setOnClickListener(this);
        binding.agallery.setOnClickListener(this);
        binding.selectCategory.setOnClickListener(this);
        binding.addOptions.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Add Post");
        setTitle(Util.applyFontToMenuItem(this, s));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post, menu);

        /** Get the action view of the menu item whose id is search */
        View v = (View) menu.findItem(R.id.post).getActionView();

        /** Get the edit text from the action view */
        txtSearch = (TextView) v.findViewById(R.id.filterapply);
        txtSearch.setEnabled(false);
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (post_category.equals("social")) {
                    hideKeybaord(view);
                    uploadthepost();
                }
                else if (post_category.equals("polling")) {
                    hideKeybaord(view);
                    getPollOptions();
                    if ((binding.editTextForPost.getText().toString() != null && !binding.editTextForPost.getText().toString().isEmpty()) &&
                            (binding.correctAns.getText().toString() != null && !binding.correctAns.getText().toString().isEmpty())) {
                        if (options_list.size() > 1) {
                            List<String> correctOption = new ArrayList<>();
                            correctOption.add(binding.correctAns.getText().toString().trim());
                            if (checkCorrectAnswer(options_list, correctOption)) {
                                createPollForFeed(binding.editTextForPost.getText().toString().trim(), correctOption.toString(), options_list.toString());
                            } else {
                                Toast.makeText(GroupUploadPost.this, "correct answer should match atLest one option", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(GroupUploadPost.this, "Give atLeast Two options", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (binding.editTextForPost.getText().toString() == null || binding.editTextForPost.getText().toString().isEmpty()) {
                            binding.editTextForPost.setError("field can not be Empty");
                            binding.editTextForPost.requestFocus();
                        } else if (binding.correctAns.getText().toString() == null || binding.correctAns.getText().toString().isEmpty()) {
                            binding.correctAns.setError("field can not be Empty");
                            binding.correctAns.requestFocus();
                        }
                    }
                }

            }
        });
        return true;
    }

    private boolean checkCorrectAnswer(List<String> options_list, List<String> correctOption) {

        for (int i = 0; i < options_list.size(); i++) {
            if (correctOption.get(0).equals(options_list.get(i))) {
                Log.d("create_poll", "checkCorrectAnswer: "+correctOption.get(0)+"\n"+options_list.get(i));
                return true;
            }
        }
        return false;
    }

    private void createPollForFeed(String question, String correct_answer, String options) {
        String user_id = Paper.book().read("userId", "");
        String votes = options.replaceAll("\\s", ""); // using built in method
        Log.d("create_poll_polls", "createPollForFeed: "+votes);
        Log.d("create_poll", "createPollForFeed: \n" + user_id + "\n" + appDetails.getAppId() + "\n" + question + "\n" + votes + "\n" + correct_answer);
        CreatePollPojo pollPojo=new CreatePollPojo(appDetails.getAppId(),question,votes,correct_answer,"false","false","false","true","red",user_id);

        Health1NetworkController.getInstance().getService().createPoll(pollPojo).enqueue(new Callback<CreatePollResponse>() {
            @Override
            public void onResponse(Call<CreatePollResponse> call, retrofit2.Response<CreatePollResponse> response) {
                CreatePollResponse createPollResponse=response.body();
                if(createPollResponse.getResponse()){
                    Log.d("error_while_poll", "success: "+createPollResponse.getResponse());

                    activePollQuestion(createPollResponse.getData().getPollId(),createPollResponse.getData().getQuestion());

                }else{
                    Log.d("error_while_poll", "else: "+createPollResponse.getResponse());

                }

            }
            @Override
            public void onFailure(Call<CreatePollResponse> call, Throwable t) {
                Log.d("error_while_poll", "onFailure: "+t.getMessage());
            }
        });

    }

    private void activePollQuestion(String pollId, String question) {

        Health1NetworkController.getInstance().getService().changePollActiveStatus(appDetails.getAppId(),pollId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().toString());
                    boolean status = jsonObject.getBoolean("response");
                    if(status){
//                       String post_content=question+" {"+pollId+"}";
                        uploadPostForPoll(question,pollId);

                    }else{
                        String responseString=jsonObject.getString("responseString");
                        Toast.makeText(GroupUploadPost.this,responseString,Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }
    private void uploadPostForPoll(String question,String pollid){
        try {
            JSONObject header = new JSONObject();
            header.put("appid", appDetails.getAppId());
            header.put("userid", Paper.book().read("userId", ""));
            header.put("post_name", "polling{"+pollid+"}");
            header.put("post_content", question);
            header.put("create_time", System.currentTimeMillis());
            header.put("group_id",group_id);


            final String requestdata = header.toString();

            Intent intent = new Intent();
            intent.putExtra("header", requestdata);
            intent.putParcelableArrayListExtra("attachment", imageselected);
            setResult(RESULT_OK, intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
    private void uploadthepost() {

        if(binding.editTextForPost.getText().toString().contains("https://vimeo.com/")){
            String url=binding.editTextForPost.getText().toString();
            url = url.replace("https://vimeo.com/", "https://player.vimeo.com/video/");
            url += "/config";
            getUrl(url);
        }else{
           uploadPostData();
        }

    }

    private void getUrl(String url) {
        StringRequest str = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject req=jsonObject.getJSONObject("request");
                    JSONObject files=req.getJSONObject("files");
                    JSONArray progressive=files.getJSONArray("progressive");
                    JSONObject array1=progressive.getJSONObject(0);
                    String v_url=array1.getString("url");
                    uploadData(v_url);
                    Log.d("check_v_url", "onResponse: "+v_url);
                }catch (JSONException e){
//                    Toast.makeText(GroupUploadPost.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.d("vimeo_check", "onResponse: "+e.getMessage());
                    e.printStackTrace();
                    uploadPostData();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                uploadPostData();

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(str);
    }

    private void uploadPostData(){
        try {
            JSONObject header = new JSONObject();
            header.put("appid", appDetails.getAppId());
            header.put("userid", Paper.book().read("userId", ""));
            header.put("post_name", "");
            header.put("post_content", binding.editTextForPost.getText().toString());
            header.put("create_time", System.currentTimeMillis());
            header.put("group_id",group_id);

            final String requestdata = header.toString();

            Intent intent = new Intent();
            intent.putExtra("header", requestdata);
            intent.putParcelableArrayListExtra("attachment", imageselected);
            setResult(RESULT_OK, intent);
            finish();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void uploadData(String v_url) {
        try {
            JSONObject header = new JSONObject();
            header.put("appid", appDetails.getAppId());
            header.put("userid", Paper.book().read("userId", ""));
            header.put("post_name", "");
            header.put("post_content", v_url);
            header.put("create_time", System.currentTimeMillis());
            header.put("group_id",group_id);
            final String requestdata = header.toString();
            Intent intent = new Intent();
            intent.putExtra("header", requestdata);
            intent.putParcelableArrayListExtra("attachment", imageselected);
            setResult(RESULT_OK, intent);
            finish();


        } catch (JSONException e) {
            e.printStackTrace();
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


    private void setactiveimage(ImageView view, int camera_color) {

        Glide.with(getApplicationContext())

                .load(camera_color)
                .asBitmap()
                .placeholder(camera_color)
                .error(camera_color)
                .into(new BitmapImageViewTarget(view) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        view.setImageBitmap(Util.scaleBitmap(resource, (int) Iwidth, (int) Iwidth));
                    }
                });
    }


    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            binding.pmsg.setText(charSequence);
        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (editable.length() == 0) {
                txtSearch.setEnabled(false);

            } else {
                txtSearch.setEnabled(true);

            }

        }
    };

    @Override
    public void onClick(View v) {

        int i = v.getId();

        if (i == R.id.acamera) {
            if(checkCameraPermission()) {



                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                        context, R.style.MyAlertDialogStyle);
                myAlertDialog.setTitle("Choose your Option");
                myAlertDialog.setMessage("upload Picture or Video");

                myAlertDialog.setPositiveButton("Record Video",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (isStoragePermissionGranted()) {
                                    Intent pictureActionIntent = null;

                                    pictureActionIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                    pictureActionIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
                                    startActivityForResult(
                                            pictureActionIntent,
                                            1);
                                }

                            }
                        });

                myAlertDialog.setNegativeButton("Picture",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                PickGallery_or_Camera(true);



/* Intent intent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                File f = new File(android.os.Environment
                                        .getExternalStorageDirectory(), "temp.jpg");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(f));

                                startActivityForResult(intent,
                                        1);*/


                            }
                        });
                myAlertDialog.show();
                //   onSelectImageClick();



            }
            else{
                //PickGallery_or_Camera(true);
                // onSelectImageClick();
            }
            //  PickGallery_or_Camera(true);
            // onSelectImageClick();

            //dispatchTakePictureIntent();
        }
        else if (i == R.id.agallery) {
//          if(feedenable) {
//
//              /*Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//              pickIntent.setType("image/* video/*");
//              startActivityForResult(pickIntent, 10);*/
//              PickGallery_or_Camera(false);
//          }else{
//              PickGallery_or_Camera(false);
//          }
            // PickGallery_or_Camera(false);
            //onSelectImageClick();
            //   launchGalleryIntent();
            chooseImageFromGallery();

        }

        else if (i == R.id.select_category) {
            bottomSheetDialog = new BottomSheetDialog(GroupUploadPost.this, R.style.BottomSheetTheme);
            View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_layout_feeds,
                    (ViewGroup) findViewById(R.id.bottom_sheet));
            sheetView.findViewById(R.id.polls).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(GroupUploadPost.this, "selected polls", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                    post_category = "polling";
                    CategorySelected(post_category);

                }
            });
            sheetView.findViewById(R.id.social).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(GroupUploadPost.this, "selected socials", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                    post_category = "social";
                    CategorySelected(post_category);
                }
            });
            bottomSheetDialog.setContentView(sheetView);
            bottomSheetDialog.show();

        } else if (i == R.id.add_options) {
            addEditText();
        }

    }
    private void addEditText() {
        View optionsEditText = getLayoutInflater().inflate(R.layout.row_add_edittext_dynamically, null, false);
        EditText editText = optionsEditText.findViewById(R.id.options);
        ImageView cancel = optionsEditText.findViewById(R.id.clear_option);
        edit_text_list_layout.addView(optionsEditText);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEditTextView(optionsEditText);
            }
        });

    }

    private void removeEditTextView(View optionsEditText) {
        edit_text_list_layout.removeView(optionsEditText);
    }

    private void getPollOptions() {
        options_list.clear();
        for (int i = 0; i < edit_text_list_layout.getChildCount(); i++) {
            View optionsEditText = edit_text_list_layout.getChildAt(i);
            EditText editText = optionsEditText.findViewById(R.id.options);
            if (editText.getText().toString() != null && !editText.getText().toString().isEmpty()) {
                options_list.add(editText.getText().toString());
                Log.d("options_list", "getPollOptions: " + options_list.toString());
            }
        }
    }

    private void CategorySelected(String category) {
        if (category.equals("polling")) {
            binding.ecomment.setVisibility(View.GONE);
            binding.attachmentimg.setVisibility(View.GONE);
            binding.pollingCard.setVisibility(View.VISIBLE);
            binding.categoryName.setText(category);
        } else if (category.equals("social")) {
            binding.ecomment.setVisibility(View.GONE);
            binding.attachmentimg.setVisibility(View.VISIBLE);
            binding.pollingCard.setVisibility(View.GONE);
            binding.categoryName.setText(category);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            imageselected = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);

            if (imageselected.size() > 0) {
               binding.attachmentimg.setItemList(imageselected);
            }

        }
        if (resultCode == RESULT_OK && requestCode == 1) {
           /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
            VideoView videoView = new VideoView(this);
            videoView.setVideoURI(data.getData());
            videoView.start();
            builder.setView(videoView).show();

*/

            try {
                Uri selectedMediaUri = data.getData();
                if (selectedMediaUri.toString().contains("png")) {
                    //handle image
                    // imageselected = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
                    Uri selectedImageUri = data.getData();
                    String imagepath = getPath(this, selectedImageUri);
                    File imageFile = new File(imagepath);
                    Image image = new Image(0, imageFile.getName(), imagepath, 100, 100, "jpg");
                    imageselected.add(image);

                    if (imageselected.size() > 0) {
                        binding.attachmentimg.setItemList(imageselected);


                    }
                } else {
                    Uri selectedImageUri = data.getData();
                    //  InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                    ///  Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    //   encodedImage = encodeImage(selectedImage);




                    String imagepath = getPath(this, selectedImageUri);
                    File imageFile = new File(imagepath);

                    // Uri imageUri = Uri.parse(imageFile.getAbsolutePath());//Uri.parse(imageFile.getPath());
                    // String s=ConvertToString(imageUri);
                    String imagepath1 = getPath(this, selectedImageUri);
                    Image image = new Image(0,imageFile.getName(), imagepath1, 100, 100, "video");
                    imageselected.add(image);
                    //  Videoselected.add(imagepath);
                    if (imageselected.size() > 0) {
                        binding.attachmentimg.setItemList(imageselected);
                    }
                    //
                    //  String s=ConvertToString(imageUri);
                    // String path=uploadbase64(s,imageFile.getName());

                }
            }catch (Exception e){

            }
        }
        if(requestCode==10 &&data!=null)
        {
            Uri selectedMediaUri = data.getData();
            if (selectedMediaUri.toString().contains("png")) {
                //handle image
                try {
                    imageselected = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);

                    if (imageselected.size() > 0) {
                        binding.attachmentimg.setItemList(imageselected);


                    }
                }catch (Exception e){

                }

            } else  if (selectedMediaUri.toString().contains("mp4")) {
                //imageselected = data.getParcelableArrayListExtra(Config.EXTRA_CONFIG);
                try {
                    Uri selectedImageUri = data.getData();

                    String imagepath = getPath(this, selectedImageUri);
                    File imageFile = new File(imagepath);
                    Image image = new Image(0, imageFile.getName(), imagepath, 100, 100, "video");
                    imageselected.add(image);
                    //  Videoselected.add(imagepath);
                    if (imageselected.size() > 0) {
                        binding.attachmentimg.setItemList(imageselected);
                    }

                }catch (Exception e){

                }
            }

        }
        if (requestCode == 121) {
            if (resultCode == Activity.RESULT_OK) {

                try{  Uri imageUri = data.getData();
                    String imagepath = getPath(this,imageUri);
                    File imageFile = new File(imagepath);
                    Image image = new Image(0,imageFile.getName(),imagepath,100,100,"jpg");
                    imageselected.add(image);
                    //  Videoselected.add(imagepath);
                    if (imageselected.size() > 0) {
                        binding.attachmentimg.setItemList(imageselected);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private void PickGallery_or_Camera(boolean isgallery) {

        GalleryPicker.with((AppCompatActivity) context)
                .setCameraMode(isgallery)
                .setMultipleMode(false)
                .setSelectedImages(imageselected)
                .setMaxSize(15)
                .setImageTitle("Gallery")
                .setToolbarColor(Color.parseColor(appDetails.getTheme_color()))
                .start();
    }
    private boolean checkCameraPermission() {

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // only for marshmallow and newer versions
            if (ContextCompat.checkSelfPermission(GroupUploadPost.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(GroupUploadPost.this,
                        new String[]{
                                Manifest.permission.CAMERA}, ZBAR_CAMERA_PERMISSION);
            } else {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted");
                return true;
            } else {

//                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    private void chooseImageFromGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, 121);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


}

