package com.singleevent.sdk.View.LeftActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.singleevent.sdk.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class SharePost extends AppCompatActivity implements View.OnClickListener {

    Button sharepost;
    String fb = "com.facebook.katana";
    /* Twitter - "com.twitter.android"
    Instagram - "com.instagram.android"
    Pinterest - "com.pinterest"*/
    Bitmap image;
    InputStream input;
    File storagePath;
    Bitmap thumb;
    ProgressDialog pDialog;
    String type;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharepost);
        sharepost = (Button) findViewById(R.id.share);
        sharepost.setOnClickListener(this);
        if (getIntent().getExtras() == null)
            finish();

        String url = getIntent().getExtras().getString("url");
        String title = getIntent().getExtras().getString("title");

        if (!url.equalsIgnoreCase("") && !url.equalsIgnoreCase(null)) {
            try {
                if (url.contains(".mp4")) {
                    type = "video/*";
                    Uri uri = Uri.parse("https://dlt16w76krcyp.cloudfront.net/feeds/videos/feeds_b8357007ed2b820da1ed2adce32494524604_834526403_20210527_181919.mp4_1622119783954.mp4?dt=1622119784137.mp4");
//                    new DataShare(SharePost.this, title).Share(this, uri);
                    new DataShare(SharePost.this, title).shareTextUrl(url,title);
                } else {
                    type = "image/*";
//                    new DataShare(SharePost.this, title).ShareAndLoadImage(url);
                    Uri uri=Uri.parse(url);
//                    new DataShare(SharePost.this, title).Share(this, uri);
                    new DataShare(SharePost.this, title).shareTextUrl(url,title);

                }
            } catch (Exception e) {
                Log.d("check_error_forshare", "onCreate: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            forText(title);
        }

        //img();
    }

    public void img() {
        try {
            URL url = new URL("https://webmobi.s3.amazonaws.com/nativeapps/eventdemofirst/1573651229119_App_logo_new.png");
            input = url.openStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //The sdcard directory e.g. '/sdcard' can be used directly, or
            //more safely abstracted with getExternalStorageDirectory()
            storagePath = Environment.getExternalStorageDirectory();
            OutputStream output = new FileOutputStream(new File(storagePath, "share" + ".png"));


            try {

                byte[] buffer = new byte[1000];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }
            } finally {
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.share) {
            new DataShare(SharePost.this, "ok").ShareAndLoadImage("https://webmobi.s3.amazonaws.com/nativeapps/eventdemofirst/1573651229119_App_logo_new.png");



           /* Intent it = getPackageManager().getLaunchIntentForPackage(fb);
            if (it != null) {
                // The application exists
              *//*  Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app " + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            //   Bitmap b= getBitmapFromURL("https://webmobi.s3.amazonaws.com/nativeapps/eventdemofirst/1573651229119_App_logo_new.png");
*//*
                try {
                    URL url = new URL("https://webmobi.s3.amazonaws.com/nativeapps/eventdemofirst/1573651229119_App_logo_new.png");
                     image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch(IOException e) {
                    System.out.println(e);
                }*//*
              try {
                  {

                      Uri uri = Uri.parse("file:///" + storagePath + "share");


                      Intent intent = new Intent(Intent.ACTION_SEND);
                      intent.setType("image/*");
                      intent.putExtra(Intent.EXTRA_TEXT, "Hey view/download this image");
                      //  String path = MediaStore.Images.Media.insertImage(getContentResolver(), String.valueOf(uri), "", null);
                      // Uri screenshotUri = Uri.parse(path);

                      intent.putExtra(Intent.EXTRA_STREAM, String.valueOf(uri));

                      startActivity(Intent.createChooser(intent, "Share image via..."));
                  }}catch (Exception e)
              {

              }

            } else {
                // The application does not exist
                // Open GooglePlay or use the default system picker
            }*/

        }
    }

    public Bitmap getBitmapFromURL(String src) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                // Ur URL
                String link = src;
                try {
                    URL url = new URL(link);
                    thumb = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    // UI component

                } catch (Exception e) {

                }
            }
        });
        thread.start();
        return thumb;
    }

    public class DataShare {

        private Target loadtarget;
        Context context;
        String DataUrl; // set your message
        String ImageUrl;


        public DataShare(Context context, String dataUrl) {
            this.context = context;
            this.DataUrl = dataUrl;
        }

        public void ShareAndLoadImage(String url) {  //url is your image url

            pDialog = new ProgressDialog(SharePost.this, R.style.MyAlertDialogStyle);
            pDialog.setMessage("Loading ...");
            pDialog.show();
            if (loadtarget == null){
                loadtarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    // do something with the Bitmap
                    try {
                        if (ActivityCompat.checkSelfPermission(SharePost.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SharePost.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(SharePost.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        } else {
                            handleLoadedBitmap(bitmap);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {



                }
                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {



                }

            };
        }
            try {
//                Picasso.with(context).load(url).into(loadtarget);
            } catch (Exception e) {

            }
        }

        public Uri handleLoadedBitmap(Bitmap b) throws IOException {
            // do something here
// file donwload temporary
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            if (null != b) {
                Log.i("Data", "=>" + b);
                file.getParentFile().mkdirs();
                FileOutputStream out = new FileOutputStream(file);
                b.compress(Bitmap.CompressFormat.PNG, 30, out);
                out.close();
                //Uri data = Uri.fromFile(file);
                Uri data = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);

                Share(context, data);

            }
            return Uri.fromFile(file);

        }
        private void shareTextUrl(String url, String title) {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, title);
            share.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(share, "Share link!"));
        }

        public void Share(Context context, Uri bmpUri) {
            try {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            } catch (Exception e) {

            }

            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);

                Log.i("bmpurl", "=>" + bmpUri);
                intent.putExtra(Intent.EXTRA_TEXT, DataUrl);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject"); // set your subject here
                intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                if (type != null) {
                    intent.setType(type);
                } else {
                    intent.setType("image/*");
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                if (!TextUtils.isEmpty(bmpUri + "")) {
                    startActivity(Intent.createChooser(intent, "Share with"));
                }
                finish();
            } catch (Exception e) {
            }

        }

    }

    public void forText(String s) {
        String t = s;


        // The application exists
            /*Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, t);
            startActivity(sendIntent);*/
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, t);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, t);
        startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
        finish();

    }

}
