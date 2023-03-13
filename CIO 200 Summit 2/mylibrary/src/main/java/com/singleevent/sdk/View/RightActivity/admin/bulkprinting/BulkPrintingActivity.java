package com.singleevent.sdk.View.RightActivity.admin.bulkprinting;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.View.RightActivity.admin.SendNotification;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.admin.adminSurvey.UsersModel;
import com.singleevent.sdk.View.RightActivity.admin.bulkprinting.adapter.BulkPrintingUserAdapter;
import com.singleevent.sdk.utils.DataBaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.paperdb.Paper;

public class BulkPrintingActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    AppDetails appDetails;
    CheckBox select_all_chk;
    Spinner cat_spinner;
    RecyclerView userlist_rv;
    List<UsersModel> userview;
    List<UsersModel> selectedUserList;
    TextView noitems, print_btn;
    String token=null, theme_color;
    BulkPrintingUserAdapter bulkPrintingUserAdapter;
    ArrayList<String> spinner_text;
    public static final int FILE_WRITE_PERMISSION = 1;
    boolean file_write_permission = false, isOnLine = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(getApplicationContext());
        setContentView(R.layout.activity_bulk_printing);
        appDetails = Paper.book().read("Appdetails");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        token = Paper.book().read("token", "");
        theme_color = appDetails.getTheme_color();
        /*checking token is null or not if not null calling getusr method*/
        if (token!=null)
        getuser();

        //setting resources
        setResources();

        //file permissions
        fileWritePermission(false);

        //to select the all the users
        select_all_chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    bulkPrintingUserAdapter = new BulkPrintingUserAdapter(BulkPrintingActivity.this, userview, theme_color, true);
                    userlist_rv.setAdapter(bulkPrintingUserAdapter);

                } else {
                    bulkPrintingUserAdapter = new BulkPrintingUserAdapter(BulkPrintingActivity.this, userview, theme_color, false);
                    userlist_rv.setAdapter(bulkPrintingUserAdapter);
                }
            }
        });


        bulkPrintingUserAdapter = new BulkPrintingUserAdapter(BulkPrintingActivity.this, userview, theme_color, false);
        userlist_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        //setting adapter
        userlist_rv.setAdapter(bulkPrintingUserAdapter);
    }

    private void setspinner() {
        //setting spinner values
        setSpinnerValues();
        setListValues(cat_spinner.getId());

        cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                select_all_chk.setChecked(false);
                setListValues(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //setting resources
    private void setResources() {
        userview = new ArrayList<>();
        select_all_chk = (CheckBox) findViewById(R.id.select_all_chk);
        cat_spinner = (Spinner) findViewById(R.id.cat_spinner);
        userlist_rv = (RecyclerView) findViewById(R.id.bp_ussrlist);
        noitems = (TextView) findViewById(R.id.bp_noitems);
        print_btn = (TextView) findViewById(R.id.print_btn);
        spinner_text = new ArrayList<>();

      //  print_btn.setBackgroundColor(Color.parseColor(theme_color));
        print_btn.setBackground(Util.setdrawable(BulkPrintingActivity.this, R.drawable.healthpostbut, Color.parseColor(appDetails.getTheme_color())));


        print_btn.setOnClickListener(this);


        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {Color.parseColor(theme_color), Color.GRAY};
        CompoundButtonCompat.setButtonTintList(select_all_chk, new ColorStateList(states, colors));
       // cat_spinner.setBackground(Util.setdrawable(BulkPrintingActivity.this, R.drawable.healthpostbut, (Color.WHITE)));

    }

    private void setListValues(int i) {
        //filtering users according to the category
        if (i == 0) {
            separateList("all");
        } else if (i == 1) {
            separateList("exhibitor");
        } else if (i == 2) {
            separateList("visitor");
        } else if (i == 3) {
            separateList("media");
        } else {
            separateList("other");
        }

    }

    private void separateList(String category) {
        //filtering users according to the lists
        List<UsersModel> temp_user_list = new ArrayList<>();

        for (int i = 0; i < userview.size(); i++) {
            switch (category) {
                case "all":
                    temp_user_list.add(userview.get(i));
                    break;
                case "other":
                    if (userview.get(i).getAdmin_flag().equalsIgnoreCase("none"))
                        temp_user_list.add(userview.get(i));
                    break;
                case "exhibitor":
                    if (userview.get(i).getAdmin_flag().equalsIgnoreCase("exhibitor"))
                        temp_user_list.add(userview.get(i));
                    break;
                case "visitor":
                    if (userview.get(i).getAdmin_flag().equalsIgnoreCase("visitor"))
                        temp_user_list.add(userview.get(i));
                    break;
                case "media":
                    if (userview.get(i).getAdmin_flag().equalsIgnoreCase("media"))
                        temp_user_list.add(userview.get(i));
                    break;
                /*default:
                    if (userview.get(i).getAdmin_flag().equalsIgnoreCase("none"))
                        temp_user_list.add(userview.get(i));*/
            }

        }
        sendUserToAdapter(temp_user_list);
    }

    private void sendUserToAdapter(List<UsersModel> temp_user_list) {
        //setting users lists to the adapter
        if (temp_user_list.size() > 0) {
            bulkPrintingUserAdapter = new BulkPrintingUserAdapter(BulkPrintingActivity.this, temp_user_list, theme_color, false);
            userlist_rv.setAdapter(bulkPrintingUserAdapter);
            showview(true);
        } else
            showview(false);

    }

    private void setSpinnerValues() {
        //setting spinner values
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, spinner_text
        );

        spinner_text.add("All");
        spinner_text.add("Exhibitor");
        spinner_text.add("Visitor");
        spinner_text.add("Media");
        spinner_text.add("Other");
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        //setting the spinner adapter
        cat_spinner.setAdapter(spinnerArrayAdapter);
    }


    private void getuser() {
        //api call to get the users lists
        final ProgressDialog dialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        dialog.setMessage("Loading the User");
        dialog.show();
        String tag_string_req = "getuser";
        String url = ApiList.Users + appDetails.getAppId() + "&admin_flag=attendee";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
               /* tv_checkInternet.setVisibility(View.GONE);
                if (swiperefresh.isRefreshing()) {
                    swiperefresh.setRefreshing(false);
                }*/
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        parseuser(jObj.getJSONObject("responseString").getJSONArray("users"));
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Error_Dialog.show("Session is Expired Please Login", BulkPrintingActivity.this);


                        } else
                            Error_Dialog.show(jObj.getString("responseString"), BulkPrintingActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Error Msg " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
               /* if (swiperefresh.isRefreshing()) {
                    swiperefresh.setRefreshing(false);
                }*/

                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", BulkPrintingActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, BulkPrintingActivity.this), BulkPrintingActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    // Error_Dialog.show("Please Check Your Internet Connection", FeedBackActivity.this);
                    if (!DataBaseStorage.isInternetConnectivity(BulkPrintingActivity.this)) {
//                        tv_checkInternet.setVisibility(View.VISIBLE);
                        userview = Paper.book(appDetails.getAppId()).read("BulkPirntUsers", new ArrayList<UsersModel>());
                        bulkPrintingUserAdapter = new BulkPrintingUserAdapter(BulkPrintingActivity.this, userview, theme_color, false);
                        userlist_rv.setAdapter(bulkPrintingUserAdapter);
                        setspinner();
                    }
                }

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("token", token);
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

    private void parseuser(JSONArray responseString) {

        try {
            userview.clear();
            Gson gson = new Gson();

            Random r = new Random();

            for (int i = 0; i < responseString.length(); i++) {
                String eventString = responseString.getJSONObject(i).toString();
                UsersModel obj = gson.fromJson(eventString, UsersModel.class);
                obj.setColor(Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256)));

                if (!Paper.book().read("userId", "").equals(obj.getUserid()))
                    userview.add(obj);
            }

            /*Shorting based in alphabet*/
            Collections.sort(userview, new Comparator<UsersModel>() {
                @Override
                public int compare(UsersModel usersModel, UsersModel t1) {
                    return usersModel.getFirst_name().compareToIgnoreCase(t1.getFirst_name());
                }


            });

            Paper.book(appDetails.getAppId()).write("BulkPirntUsers", userview);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        setspinner();

        if (userview.size() > 0) {
            bulkPrintingUserAdapter.notifyDataSetChanged();
        } else
            showview(false);


    }

    private void showview(boolean flag) {
        //enabling users list view and hiding
        if (flag) {
            userlist_rv.setVisibility(View.VISIBLE);
            noitems.setVisibility(View.GONE);
        } else {
            userlist_rv.setVisibility(View.GONE);
            noitems.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //setting title
        SpannableString s = new SpannableString("Bulk Printing");
        setTitle(Util.applyFontToMenuItem(this, s));
        isOnLine = DataBaseStorage.isInternetConnectivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;

    }

    //creating pdf file for printout
    private void generatePDF(boolean isOnline, List<UsersModel> selectedUserList) {

        //path
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDF";

        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();

        //creating file
        File file = new File(dir, "bulkdemo.pdf");
        String file_path = file.getPath();
        System.out.println("PDF Path : " + file_path);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Creating PDF", Toast.LENGTH_SHORT).show();

        }

        // Location to save
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, fOut);
            document.open();
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Webmobi");
            document.addCreator("Webmobi");
            //adding data to the pdf file
            addContents(document, selectedUserList, isOnline);
            document.close();
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }


        //printing the file
        printPDF(file_path);

    }

    private void addContents(Document document, List<UsersModel> selectedUserList, boolean isOnline) {
        for (int i = 0; i < selectedUserList.size(); i++) {

            //user name
            String user_name = selectedUserList.get(i).getFirst_name() + " " + selectedUserList.get(i).getLast_name();
            //user email
            String user_mail = selectedUserList.get(0).getEmail();

            // Document Settings
            BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            float mHeadingFontSize = 18.0f;
            float mValueFontSize = 12.0f;
            /**
             * How to USE FONT....
             */
            BaseFont urName = null;
            //setting the font
            try {
                urName = BaseFont.createFont("assets/fonts/Roboto-Regular.otf", "UTF-8", BaseFont.EMBEDDED);
            } catch (DocumentException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            //adding name
            Font nameFont = new Font(urName, mHeadingFontSize, Font.NORMAL, BaseColor.BLACK);
            // Creating Chunk
            Chunk nameChunk = new Chunk(user_name, nameFont);
            // Creating Paragraph to add...
            Paragraph nameParagraph = new Paragraph(nameChunk);
            // Setting Alignment for Heading
            nameParagraph.setAlignment(Element.ALIGN_CENTER);
            // Finally Adding that Chunk
            try {
                document.add(new Paragraph("\n\n\n\n"));

                document.add(nameParagraph);
            } catch (DocumentException e1) {
                e1.printStackTrace();
            }


            //adding email id
            Font mailFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mailChunk = new Chunk(user_mail, mailFont);
            Paragraph mailParagraph = new Paragraph(mailChunk);
            mailParagraph.setAlignment(Element.ALIGN_CENTER);
            try {
                document.add(mailParagraph);
            } catch (DocumentException e1) {
                e1.printStackTrace();
            }
            if (isOnline) {
                Bitmap bmp = generateQRCode(selectedUserList.get(i).getUserid());
                Image image = null;

                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    try {
                        image = Image.getInstance(stream.toByteArray());
                    } catch (BadElementException e) {
                        e.printStackTrace();
                    }
                    image.setAlignment(Image.MIDDLE);
                    image.scaleAbsolute(80, 80);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                try {
                    document.add(image);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
            //creating new page in the document
            document.newPage();

        }
    }

    private Bitmap generateQRCode(String userid) {
        //generating qr code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = multiFormatWriter.encode(userid, BarcodeFormat.QR_CODE, 150, 150);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

        return bitmap;
    }
    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {

        boolean found = true;

        try {

            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {

            found = false;
        }

        return found;
    }

    private void printPDF(String path) {
        //printing the pdf file
        String uri_path = "file://" + path;
        Intent i = new Intent(Intent.ACTION_VIEW);
        if(isPackageInstalled("com.dynamixsoftware.printershare", getPackageManager())) {
            i.setPackage("com.dynamixsoftware.printershare");
        }
        i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        i.setDataAndType(Uri.parse(uri_path), "application/pdf");
        try {
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            createalertdialog("Print Module Not Available", "No print module found to complete the print job. Contact support@webmobi.com for more information.");
        }
    }

    private void fileWritePermission(boolean isFromPrint) {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(BulkPrintingActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(BulkPrintingActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        FILE_WRITE_PERMISSION);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(BulkPrintingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                String title = "Unable to Print";
                String msg = "Currently your storage permission is not enabled so unable to take the print.";
                if (isFromPrint)
                    createalertdialog(title, msg);
                else
                    createalertdialog(title, msg);

            } else {
                ActivityCompat.requestPermissions(BulkPrintingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        FILE_WRITE_PERMISSION);

            }
        } else {
            file_write_permission = true;


        }
    }
    public  boolean isStoragePermissionGranted() {
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
        }
        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
               // Log.v(TAG,"Permission is granted2");
                return true;
            } else {

              //  Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted2");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
//            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
    private void createalertdialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);

        builder.setTitle(title);
        builder.setMessage(msg).setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        TextView messageView = (TextView)alert.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER_HORIZONTAL);
        messageView.setGravity(Gravity.CENTER_VERTICAL);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.print_btn) {
            if (bulkPrintingUserAdapter != null) {
                selectedUserList = bulkPrintingUserAdapter.getSelectedUserList();
                if (selectedUserList.size() > 0) {
                    if (file_write_permission)
                        generatePDF(isOnLine, selectedUserList);
                    else if( isWriteStoragePermissionGranted())
                        //fileWritePermission(true);
                        generatePDF(isOnLine, selectedUserList);
                    else
                        fileWritePermission(true);

                    for (int i = 0; i < selectedUserList.size(); i++)
                        System.out.println("Selected User List : " + selectedUserList.get(i).getFirst_name());
                }
            }


        }
    }
}
