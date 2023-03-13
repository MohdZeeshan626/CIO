package com.singleevent.sdk.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.support.v4.os.ResultReceiver;
import android.util.Base64;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;
import com.singleevent.sdk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by webMOBI on 9/12/2017.
 */

public class UploadPost extends IntentService {


    JSONObject header = null;
    ResultReceiver receiver;
    private static final String TAG = "UploadPost";
    private ArrayList<Image> imageselected = new ArrayList<>();
    public static NotificationManager mNotificationManager;
    static NotificationCompat.Builder builder;
    static Context context;
    static int NOTIFICATION_ID = 111;
    Intent intent;
    private RequestQueue queue;

    public UploadPost() {
        super(UploadPost.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        this.intent = intent;

        // broadcast reciver
        receiver = intent.getParcelableExtra("receiver");
        try {
            header = new JSONObject(intent.getStringExtra("header"));
            imageselected = intent.getParcelableArrayListExtra("attachment");
            uploaddata();
            // queue= Volley.newRequestQueue(UploadPost.this);
            //  Demo demo=new Demo();
            // demo.execute();
        } catch (JSONException | IOException | InterruptedException e) {
            e.printStackTrace();
        }


        Log.d(TAG, "Service Stopping!");
        this.stopSelf();


    }

    private void uploaddata() throws JSONException, IOException, InterruptedException {


        FileUploadNotification(getApplicationContext());

        JSONObject main = new JSONObject();
        main.put("post_req", header);

        JSONArray attachmentlist = new JSONArray();
        for (Image images : imageselected) {
            String someFilepath = images.getName();
            String extension = someFilepath.substring(someFilepath.lastIndexOf(".")+1);
            JSONObject attachement = new JSONObject();
            attachement.put("res_name", images.getName());

            attachement.put("res_description", "");
            if(images.getType()!=null){
                try {
                    if (images.getType().contains("video")) {
                       // StringBuffer s = new StringBuffer();//  /storage/emulated/0/DCIM/Camera/VID_20201120_195537.mp4
                       // s = s.append("file://" + images.getPath());//   file:///storage/emulated/0/DCIM/Camera/VID_20201120_195537.mp4file:///storage/emulated/0/DCIM/Camera/VID_20201120_195537.mp4
                       // File imageFile = new File(images.getPath());
                        attachement.put("res_type", /*images.getType()*/"video/"+"mp4");
                        attachement.put("res_blob", Convert_uri(images.getPath()));

                      //  attachement.put("res_blob", (images.getPath()));
                    }
                    else if(images.getType().equalsIgnoreCase("jpg")){
                        attachement.put("res_type", /*images.getType()*/"image/"+extension);
                        attachement.put("res_blob", convert_uri_to_base64(images.getPath()));
                    }
                    else{
                        attachement.put("res_type", /*images.getType()*/"image/"+extension);
                        attachement.put("res_blob", convert_uri_to_base64(images.getPath()));
                    }
                   // attachement.put("res_blob", convert_uri_to_base64(images.getPath()));
                }catch (Exception e){

                }
            }
            else {
             attachement.put("res_type", /*images.getType()*/"image/"+extension);
            attachement.put("res_blob", convert_uri_to_base64(images.getPath()));
            }
            attachement.put("res_width", images.getImageWidth());
            attachement.put("res_height", images.getImageHeight());
            attachmentlist.put(attachement);
            System.out.println(attachmentlist);
        }
        main.put("attachments", attachmentlist);

        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                ApiList.Post_Feed,
                main,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("POST RESPONSE"+response);
                        success_notification();

                        try {
                            if (response.getBoolean("response")) {
                                Bundle b = new Bundle();
                                b.putString("data", response.toString());
                                receiver.send(0, b);
                            }else{
                                Bundle b = new Bundle();
                                b.putString("data", response.toString());
                                receiver.send(1, b);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("PRINT ERROR"+error);
                        failUploadNotification();

                    }
                }
        );

        App.getInstance().addToRequestQueue(jsonObjectRequest, "Feeds");
        //   Thread.sleep(1000);
        //jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(500000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    public void FileUploadNotification(Context context)  {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Uploading Post")
                .setContentText("Please wait...")
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setProgress(100, 0, true)
                .setSound(soundUri)
                .setAutoCancel(false);

        mNotificationManager.notify(NOTIFICATION_ID, builder.build());


    }


    private void success_notification() {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (builder != null) {
            builder.setContentTitle("Successfully Posted")
                    .setContentText("")
                    .setProgress(0, 0, false)
                    .setSmallIcon(android.R.drawable.stat_sys_upload_done)
                    .setSound(soundUri)
                    .setOngoing(false);

            mNotificationManager.notify(NOTIFICATION_ID, builder.build());

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    deleteNotification();
                }
            }, 1000);
        } else {
            mNotificationManager.cancel(NOTIFICATION_ID);
        }

    }

    public void failUploadNotification() {
        Log.e("downloadsize", "failed notification...");

        PendingIntent pendingIntent = PendingIntent.getService(UploadPost.this, 0, intent, 0);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.act_custom_notification);
        contentView.setImageViewResource(R.id.small_icon, R.drawable.ic_uploadfailed);

        contentView.setOnClickPendingIntent(R.id.retry, pendingIntent);

        if (builder != null) {
            builder.setContentText("")
                    .setProgress(0, 0, false)
                    .setSmallIcon(R.drawable.ic_uploadfailed)
                    .setOngoing(false)
                    .setCustomContentView(contentView);

            mNotificationManager.notify(NOTIFICATION_ID, builder.build());

        } else {
            try{ mNotificationManager.cancel(NOTIFICATION_ID);}catch (Exception e){

            }
        }
    }


    public static void deleteNotification() {
        mNotificationManager.cancel(NOTIFICATION_ID);
        builder = null;
    }

    private String convert_uri_to_base64(String path) {

        Bitmap bm = BitmapFactory.decodeFile(path);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
//        return baos.toByteArray();


        if(bm!=null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            bm.recycle();
            bm = null;
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        }else{
            Uri imageUri = Uri.parse(path);
            Bitmap rotatedBitmap;
            try {
                int rotateImage = getCameraPhotoOrientation(UploadPost.this, imageUri, path);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                Matrix matrix = new Matrix();
                matrix.postRotate(rotateImage);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                rotatedBitmap.recycle();
                rotatedBitmap = null;
                byte[] b = baos.toByteArray();
                return Base64.encodeToString(b, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    //////////////////////////
    private String Convert_uri(String path) throws IOException {

        Uri imageUri = Uri.parse(path);
        return ConvertToString(imageUri);
        //Bitmap bm = BitmapFactory.decodeFile(path.getPath());
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
//        return baos.toByteArray();
        //  StringBuffer s=new StringBuffer();//  /storage/emulated/0/DCIM/Camera/VID_20201120_195537.mp4
        //  s=s.append("file://"+);//   file:///storage/emulated/0/DCIM/Camera/VID_20201120_195537.mp4file:///storage/emulated/0/DCIM/Camera/VID_20201120_195537.mp4
        // File imageFile = new File(s.toString());

     /*  InputStream inputStream = null;//You can get an inputStream using any IO API
       inputStream = new FileInputStream("file://"+path);
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       byte[] buffer = new byte[8192];
       int bytesRead;
       ByteArrayOutputStream output = new ByteArrayOutputStream();
       byte[] b = baos.toByteArray();
       Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
       try {
           while ((bytesRead = inputStream.read(buffer)) != -1) {
               output64.write(buffer, 0, bytesRead);
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
       output64.close();
*/
        //return output.toString();

    }

    public String ConvertToString(Uri uri){
        String  uriString = uri.toString();
        String s="file://"+uriString;
        Uri uri1=Uri.parse(s);
        Log.d("data", "onActivityResult: uri"+uri1);
        //            myFile = new File(uriString);
        //            ret = myFile.getAbsolutePath();
        //Fpath.setText(ret);
        try {
            InputStream in = getContentResolver().openInputStream(uri1);
            byte[] bytes=getBytes(in);
            Log.d("data", "onActivityResult: bytes size="+bytes.length);
            Log.d("data", "onActivityResult: Base64string="+Base64.encodeToString(bytes,Base64.DEFAULT));
            String ansValue = Base64.encodeToString(bytes,Base64.DEFAULT);
            // String Document=Base64.encodeToString(bytes,Base64.DEFAULT);
            return ansValue;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.d("error", "onActivityResult: " + e.toString());
        }
        return  null;
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {


        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 60*1024*1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);
            String s=imageFile.getPath();
            String s1=s.substring(6);
            ExifInterface exif = new ExifInterface(s1);
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

}



