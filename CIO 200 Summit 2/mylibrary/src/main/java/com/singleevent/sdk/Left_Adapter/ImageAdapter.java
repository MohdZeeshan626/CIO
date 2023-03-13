package com.singleevent.sdk.Left_Adapter;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.FileDownloader;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.Attachment;
import com.singleevent.sdk.model.AppDetails;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.core.content.FileProvider;
import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;

import static com.singleevent.sdk.Custom_View.Util.scaleBitmap;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    String appid;
    String userid;
    AppDetails appDetails;
    ImageView imageView;
    ImageView gvideoview;

    ArrayList<String> imgurl=new ArrayList<String>();



    public ImageAdapter(Context context, ArrayList<String> imgurl) {
        this.context = context;
        this.imgurl=imgurl;

    }



    @Override
    public int getCount() {
        return imgurl.size();
    }

    @Override
    public Object getItem(int position) {
        return imgurl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bitmap bitmap;
        URL url;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            //  url = new URL(imgurl.get(position));
            //  Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView = new ImageView(context);
            gvideoview=new ImageView((context));
            // imageView.setImageBitmap(bmp);
            // imageView.setImageResource(imageArray[position]);
          //  Picasso.with(context).load(imgurl.get(position)).into(imageView);
            if(imgurl.get(position).contains(".mp4")){
                //bitmap=ThumbnailUtils.createVideoThumbnail(imgurl.get(position), MediaStore.Video.Thumbnails.MICRO_KIND);
                Uri uri = Uri.parse(imgurl.get(position));
               /* Glide.with(context).load(imgurl.get(position))
                        .asBitmap()
                        .placeholder(R.drawable.playvideo)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(new GridView.LayoutParams(250, 250));*/
               /* BitmapPool bitmapPool = Glide.get(context).getBitmapPool();
                int microSecond = 1000000;// 6th second as an example
                VideoBitmapDecoder videoBitmapDecoder = new VideoBitmapDecoder(microSecond);
                FileDescriptorBitmapDecoder fileDescriptorBitmapDecoder = new FileDescriptorBitmapDecoder(videoBitmapDecoder, bitmapPool, DecodeFormat.PREFER_ARGB_8888);
                Glide.with(context)
                        .load(uri)
                        .asBitmap()
                        // Example
                        .videoDecoder(fileDescriptorBitmapDecoder)
                        .into(imageView);*/


            //    new DownloadImage().execute("fileurl", "filename", imgurl.get(position));

           Bitmap bitmap1=retriveVideoFrameFromVideo(imgurl.get(position));
                  ByteArrayOutputStream baos = new ByteArrayOutputStream();
                   bitmap1.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                bitmap = Bitmap.createScaledBitmap(bitmap1, 250, 250, false);
                imageView.setImageBitmap(bitmap);
               //imageView.setImageBitmap(bitmap);

               /// imageView.setScaleType(ImageView.ScaleType.FIT_XY);
             //   imageView.setLayoutParams(new GridView.LayoutP"arams(250, 250));
                /* Glide.with(context.getApplicationContext())
                        .load(R.drawable.vplay)
                        .placeholder(R.drawable.vplay)
                        .error(R.drawable.vplay)
                        .into(gvideoview);
                gvideoview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                gvideoview.setLayoutParams(new GridView.LayoutParams(25, 25));*/

            }
            else{

                Glide.with(context)
                        .load(imgurl.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(new GridView.LayoutParams(250, 250));

            }

            // added for caching purpose by NP

        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return imageView;

    }


    public static Bitmap retriveVideoFrameFromVideo(String videoPath)throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)"+ e.getMessage());
        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }



    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           dialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
           dialog.setMessage("Downloading..");
           dialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String folderpath = strings[2];
            Bitmap bitmap = null;
            MediaMetadataRetriever mediaMetadataRetriever = null;
            try
            {
                mediaMetadataRetriever = new MediaMetadataRetriever();
                if (Build.VERSION.SDK_INT >= 14)
                    mediaMetadataRetriever.setDataSource(folderpath, new HashMap<String, String>());
                else
                    mediaMetadataRetriever.setDataSource(folderpath);
                //   mediaMetadataRetriever.setDataSource(videoPath);
                bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                try {
                    throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)"+ e.getMessage());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
            finally
            {
                if (mediaMetadataRetriever != null)
                {
                    mediaMetadataRetriever.release();
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            dialog.dismiss();

         //   Bitmap bitmap1=retriveVideoFrameFromVideo(imgurl.get(position));
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
              bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            imageView.setImageBitmap(bitmap);


           /* Glide.with(context)
                    .load(bitmapToByte(bitmap))
                    .asBitmap()
                    .override(250, 250).fitCenter()
                    .into(imageView);
*/


        }
    }
    private byte[] bitmapToByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
