package com.singleevent.sdk.View.LeftActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;

import com.singleevent.sdk.Left_Adapter.SwipeAdapters;
import android.widget.ImageButton;

import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import android.widget.Toast;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Left_Adapter.ViewPagerFixed;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.ExportBottomSheetFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import io.paperdb.Paper;

public class FullScreenActivity extends AppCompatActivity  implements View.OnClickListener,ExportBottomSheetFragment.ClickAction  {

    ViewPager imageView;
    ArrayList<String> imgurl =new ArrayList<>();
    Toolbar toolbar;
    private String title="Photo Gallery";
    AppDetails appDetails;
    Button imagedownload;
    ViewPagerFixed vp;
    InputStream input;
    File storagePath;
    ImageButton right, left;
    ExportBottomSheetFragment bottomSheetFragment;
    int current = 0, n = 0;
    int position;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.activity_full_screen);
        vp=(ViewPagerFixed) findViewById(R.id.image_view);
        appDetails = Paper.book().read("Appdetails");
        imgurl = Paper.book().read("galleryurl");
        toolbar = (Toolbar) findViewById(R.id.toolbarf);
        imagedownload=(Button)findViewById(R.id.imagedownload);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        toolbar.setTitle(Util.applyFontToMenuItem(FullScreenActivity.this,
                new SpannableString(title)));      //  setSupportActionBar(toolbar);
        toolbar.setOnClickListener(this);
        Intent i = getIntent();
         position = i.getExtras().getInt("id");
         imagedownload.setOnClickListener(this);


/*
         SwipeAdapters adaptr = new SwipeAdapters(this,imgurl,position);
        vp.getCurrentItem();
        adaptr.getItemPosition(position);
        System.out.println("Full Screen of getposition"+position);
        vp.setAdapter(adaptr);*/
        //   ImageAdapter imageAdapter=new ImageAdapter(this, imgurl);
        // imageView.setImageResource(imageAdapter.imageArray[position]);
        n = imgurl.size();
        // URL url = null;
        try {
           /* url = new URL(imgurl.get(position));
            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(url
                        .openConnection().getInputStream());*/
            //   Picasso.with(this).load(imgurl.get(position)).into(imageView);
         /* Glide.with(this)
                .load(imgurl.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
            current = position;*/
            final  SwipeAdapters adaptr = new SwipeAdapters(this,imgurl,position);
            vp.setAdapter(adaptr);
            vp.setCurrentItem(position);
            // System.out.println("Full Screen of getposition"+position);
        } /*catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/

        // imageView.setImageBitmap(bmp);
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
/*
    private String getByteArrayFromImageURL(String url) {

        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            InputStream is = ucon.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
        return null;
    }
*/

    //
/*
    private void writeFile(boolean isFromShare) {

        Picasso.with(this)
                .load(imgurl.get(position))
                .into(new Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                              try {
                                  String root = Environment.getExternalStorageDirectory().toString();
                                  File myDir = new File(root + "/App Gallery");

                                  if (!myDir.exists()) {
                                      myDir.mkdirs();
                                  }

                                  String name = new Date().toString() + ".jpg";
                                  myDir = new File(myDir, name);
                                  FileOutputStream out = new FileOutputStream(myDir);
                                  bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                  out.flush();
                                  out.close();
                              } catch(Exception e){
                                  // some action
                              }
                          }

                          @Override
                          public void onBitmapFailed(Drawable errorDrawable) {
                          }

                          @Override
                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                          }
                      }
                );

    }
*/
    private void writeFile(boolean isFromShare) {
        //exporting notes to file
        //setting the path
       /* String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyImage";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdir();
        //creating file
        File file = new File(dir, "myimage.jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String filepath = file.getPath();

        StringBuilder notes_txt = new StringBuilder();
        String title, time, desc;*/
        //writing notes to the file
        if (!isFromShare) {

            try {
                URL url = new URL(imgurl.get(position));
                input = url.openStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //The sdcard directory e.g. '/sdcard' can be used directly, or
                //more safely abstracted with getExternalStorageDirectory()
                 storagePath = Environment.getExternalStorageDirectory();
                OutputStream output = new FileOutputStream(new File(storagePath, "myapp" + position + ".png"));
                count++;


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

            Toast.makeText(FullScreenActivity.this, "File Downloaded in " + storagePath.getAbsolutePath()+"myapp"+position+".jpg", Toast.LENGTH_LONG).show();




    /*    //if it is from share it will share file to the mail application
        if (isFromShare) {

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse( "file://"+filepath));
            intent.putExtra(Intent.EXTRA_TEXT, "");
            intent.setData(Uri.parse("mailto:"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


            File file1 = new File(filepath);
            //checking file is existing in the path and can able to read
            if (!file1.exists() || !file1.canRead()) {
                return;
            }
            //creating uri
            Uri uri = Uri.fromFile(file);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(intent);
        } */
        }else
            Toast.makeText(FullScreenActivity.this, "File Downloaded in " + storagePath, Toast.LENGTH_LONG).show();


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

    public void showBottomSheetDialogFragment() {
        //showing the bottom sheet fragment
        bottomSheetFragment = new ExportBottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public void onDownloadClick() {
        bottomSheetFragment.dismiss();
        //create the file
        writeFile(false);
    }

    @Override
    public void onShareClick() {
        bottomSheetFragment.dismiss();
        //create and share the file
        writeFile(true);

    }

    @Override
    public void onCancel() {
        //closing the bottomsheet
        bottomSheetFragment.dismiss();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbarf) {
            onBackPressed();
        }
        else if(v.getId()==R.id.imagedownload){

            //checking storage permission
            if (isStoragePermissionGranted()) {
                //showing bottom sheet
                showBottomSheetDialogFragment();
            } else {
                Toast.makeText(FullScreenActivity.this, "Enable Storage Permission To Access Image", Toast.LENGTH_SHORT).show();

            }
        }
       /* if (v.getId() == R.id.rightmode) {
            if (current != n - 1) {
                Picasso.with(this).load(imgurl.get(++current)).into(imageView);
            }
        }

        if (v.getId() == R.id.leftmode) {
            if (current != 0) {
                Picasso.with(this).load(imgurl.get(--current)).into(imageView);
            }
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString(title);
        setTitle(Util.applyFontToMenuItem(this, s));
        //tv_app_version_name.setText("Version : " + DataBaseStorage.getAppVersionName(this));

    }

    public void onClickListener(View v) {



    }
}
