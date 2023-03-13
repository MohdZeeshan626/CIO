package com.singleevent.sdk.View.LeftActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.FileDownloader;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.PDF_Details;
import com.singleevent.sdk.model.PDFurl;
import com.singleevent.sdk.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;
import io.paperdb.Paper;

/**
 * Created by Admin on 5/31/2017.
 */

public class Attachment extends AppCompatActivity {

    AppDetails appDetails;
    int pos;
    private String title;
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;
    ArrayList<PDFurl> medialists = new ArrayList<>();
    LinearLayout listview;
    private double ImgWidth, padwidth;
    File pdfFile;
    View clicked;
    private static final int PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_attachmentroot);
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);


        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        pos = getIntent().getExtras().getInt("pos");
        title = getIntent().getExtras().getString("title");
        events = Paper.book().read("Appevents");
        e = events.get(0);

        medialists.clear();
        for (int j = 0; j < e.getTabs(pos).getpdfSize(); j++) {
            medialists.add(e.getTabs(pos).getPdfurl(j));
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ImgWidth = (displayMetrics.widthPixels) * 0.20;
        padwidth = ImgWidth * 0.50;

        listview = (LinearLayout) findViewById(R.id.contains);

        File folder = new File(Environment.getExternalStorageDirectory(), appDetails.getAppUrl());
        if (folder.exists())
            folder.mkdir();


        if (ActivityCompat.checkSelfPermission(Attachment.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Attachment.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            settingattachmentview();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    settingattachmentview();
                else
                    Error_Dialog.show("Please Enable the Permission To View and Download Docs", Attachment.this);

                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void settingattachmentview() {

        listview.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < medialists.size(); i++) {
            final PDFurl pdfurl = medialists.get(i);
            View child = inflater.inflate(R.layout.s_agendagroup, null);
            final TextView tvTitle = (TextView) child.findViewById(R.id.theatrename);
            tvTitle.setPadding((int) 30, 8, 8, 8);
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            LinearLayout screenlist = (LinearLayout) child.findViewById(R.id.screenlist);
            if(pdfurl.getName()!=null && !pdfurl.getName().equalsIgnoreCase("")){
            tvTitle.setText(pdfurl.getName());
            }
            else{
                tvTitle.setVisibility(View.GONE);
            }
            tvTitle.setTypeface(Util.boldtypeface(Attachment.this));
            List<PDF_Details> details = Arrays.asList(pdfurl.getdetails());
            for (int j = 0; j < details.size(); j++) {
                final PDF_Details item = details.get(j);
                final View child1 = inflater.inflate(R.layout.s_attachmentsitems, null);
                TextView txtmedianame = (TextView) child1.findViewById(R.id.medianame);
                TextView txtmediaurl = (TextView) child1.findViewById(R.id.mediaurl);
                ImageView logo = (ImageView) child1.findViewById(R.id.logo);
                final ImageView download = (ImageView) child1.findViewById(R.id.idownload);
                txtmedianame.setText(item.getAttachment_name());
                txtmedianame.setTypeface(Util.regulartypeface(Attachment.this));
                txtmediaurl.setTypeface(Util.regulartypeface(Attachment.this));

                txtmediaurl.setVisibility(View.GONE);
                // logo.setTextColor(Color.parseColor(appDetails.getTheme_color()));
                //  download.setTextColor(Color.parseColor(appDetails.getTheme_color()));
                pdfFile = new File(Environment.getExternalStorageDirectory() + File.separator + appDetails.getAppUrl() + File.separator + item.getAttachment_name() + ".pdf");  // -> filename = maven.pdf
                if (pdfFile.exists())
                    download.setImageResource(R.drawable.doctick);
                else
                    download.setImageResource(R.drawable.docdownload);


                child1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pdfFile = new File(Environment.getExternalStorageDirectory() + File.separator + appDetails.getAppUrl() + File.separator + item.getAttachment_name() + ".pdf");  // -> filename = maven.pdf
                        clicked = child1;
                        if (!pdfFile.exists())
                            download(item.getAttachment_name(), item.getAttachment_url(), pdfurl.getName());
                        else {
                            try {
                                //   Uri path = FileProvider.getUriForFile(Attachment.this, "com.webmobi.empengagement2020" + ".provider", pdfFile);
                                Uri path = Uri.parse(item.getAttachment_url());

//                            Uri path = Uri.fromFile(pdfFile);
                                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                                pdfIntent.setDataAndType(path, "application/pdf");
                                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(pdfIntent, PackageManager.MATCH_DEFAULT_ONLY);
                                for (ResolveInfo resolveInfo : resInfoList) {
                                    String packageName = resolveInfo.activityInfo.packageName;
                                    grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                }


                                try {
                                    startActivity(pdfIntent);
                                } catch (ActivityNotFoundException e) {

                                    Error_Dialog.show("No Application available to view PDF", Attachment.this);

                                }
                            }catch (Exception e){

                            }
                        }


                    }
                });

                screenlist.addView(child1);
            }
            listview.addView(child);
            listview.setPadding(0,15,0,15);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString(title);
        setTitle(Util.applyFontToMenuItem(this, s));

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

    public void download(String filename, String fileurl, String folderurl) {
        new DownloadFile().execute(fileurl, filename, folderurl);

    }

    private class DownloadFile extends AsyncTask<String, Void, ArrayList<String>> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Attachment.this,R.style.MyAlertDialogStyle);
            dialog.setMessage("Downloading..");
            dialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String folderpath = strings[2];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(Environment.getExternalStorageDirectory(), appDetails.getAppUrl());
            folder.mkdir();


            File pdfFile = new File(folder, fileName + ".pdf");

            try {
                if (!pdfFile.exists()) {
                    pdfFile.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            ArrayList<String> items = new ArrayList<>();
            items.add(fileName);
            items.add(folderpath);
            items.add(fileUrl);

            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            dialog.dismiss();

            ImageView download = (ImageView) clicked.findViewById(R.id.idownload);
            download.setImageResource(R.drawable.doctick);
            //  download.setTextColor(Color.parseColor(appDetails.getTheme_color()));///storage/emulated/0/engagement2020/Sony Guide.pdf
            // download.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_CHECK);////storage/emulated/0/engagement2020/Positive Work Culture.pdf

            File pdfFile = new File(Environment.getExternalStorageDirectory() + File.separator + appDetails.getAppUrl() + File.separator + s.get(0) + ".pdf");  // -> filename = maven.pdf

            try {
                // Uri path = FileProvider.getUriForFile(Attachment.this, getApplicationContext().getPackageName() + ".provider", pdfFile);


//                            Uri path = Uri.fromFile(pdfFile);
                Uri path = Uri.parse(s.get(2));
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "application/pdf");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                ///



                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(pdfIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, path, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }


                try {
                    startActivity(pdfIntent);
                } catch (ActivityNotFoundException e) {

                    Error_Dialog.show("No Application available to view PDF", Attachment.this);

                }
            }catch (Exception e){

            }
        }
    }

}
