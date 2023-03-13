package com.singleevent.sdk.View.RightActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Left_Adapter.SolventRecyclerViewAdapter;
import com.singleevent.sdk.View.LeftActivity.AgendaRoot;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.LocalArraylist.Notes;
import com.singleevent.sdk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

/**
 * Created by Admin on 6/16/2017.
 */

public class Note extends AppCompatActivity implements View.OnClickListener, ExportBottomSheetFragment.ClickAction {

    private static final String TAG = Note.class.getCanonicalName();
    AppDetails appDetails;
    ArrayList<Notes> notes;
    SolventRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView tv_no_notes;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    TextView export_txt;
    public static final int FILE_WRITE_PERMISSION = 1;
    boolean file_write_permission = false;
    ExportBottomSheetFragment bottomSheetFragment;
    ProgressDialog pDialog;
    private String userId;
    RelativeLayout nonotes;
    Button mynotebut;
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;
    int pos;
    String stitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_notesview);
        appDetails = Paper.book().read("Appdetails");
        userId = Paper.book().read("userId","");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);
        notes = new ArrayList<>();
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tv_no_notes = (TextView) findViewById(R.id.tv_no_notes);
        export_txt = (TextView) findViewById(R.id.my_notes_export);
        nonotes=(RelativeLayout)findViewById(R.id.nonotes);
        mynotebut=(Button)findViewById(R.id.mynotebut);
        mynotebut.setBackground(Util.setdrawable(Note.this, com.singleevent.sdk.R.drawable.healthpostbut,
                Color.parseColor(appDetails.getTheme_color())));
        mynotebut.setOnClickListener(this);

        recyclerView.setLayoutManager(gaggeredGridLayoutManager);
        mAdapter = new SolventRecyclerViewAdapter(this, notes);
        recyclerView.setAdapter(mAdapter);
        events = Paper.book().read("Appevents");
        e = events.get(0);
        export_txt.setVisibility(View.GONE);

        export_txt.setOnClickListener(this);
        for (int i = 0; i < e.getTabs().length; i++) {
            if (e.getTabs(i).getType().compareTo("agenda") == 0) {
                pos = i;
                stitle=e.getTabs(i).getTitle().toString();
                break;
            }

        }

        if (Paper.book().read("Islogin", false))
        getNotesFromServer();

    }

    private String converlontostring(Long key) {

        SimpleDateFormat myFormat = new SimpleDateFormat("d MMM h:mm a");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(key);
        return myFormat.format(cal.getTime());
    }

    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString("My Notes");
        setTitle(Util.applyFontToMenuItem(this, s));


        HashMap<Integer, Notes> AgendaNotes = Paper.book(appDetails.getAppId()).read("AgendaNote", new HashMap<Integer, Notes>());
        HashMap<Integer, Notes> ExhibitorNotes = Paper.book(appDetails.getAppId()).read("ExhibitorNote", new HashMap<Integer, Notes>());
        HashMap<Integer, Notes> SponsorNotes = Paper.book(appDetails.getAppId()).read("SponsorNote", new HashMap<Integer, Notes>());
        notes.clear();
        AddAgendaNotes(AgendaNotes);
        AddAgendaNotes(ExhibitorNotes);
        AddAgendaNotes(SponsorNotes);

        mAdapter.notifyDataSetChanged();
        if (notes.size() > 0) {
            export_txt.setVisibility(View.GONE);
            nonotes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            export_txt.setVisibility(View.GONE);
            nonotes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }

    }

    private void AddAgendaNotes(HashMap<Integer, Notes> lists) {
        ArrayList<Notes> list = new ArrayList<>(lists.values());
        for (Notes s : list) {
            notes.add(s);
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

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.my_notes_export) {
            //checking storage permission
            if (isStoragePermissionGranted()) {
                //showing bottom sheet
                showBottomSheetDialogFragment();
            } else {
                Toast.makeText(Note.this, "Enable Storage Permission To Access Export", Toast.LENGTH_SHORT).show();

            }
        }
        if(id==R.id.mynotebut){

            Intent i=new Intent(Note.this, AgendaRoot.class);
            i.putExtra("pos", pos);
            i.putExtra("title", stitle);
            startActivity(i);
        }
    }


    public void showBottomSheetDialogFragment() {
        //showing the bottom sheet fragment
        bottomSheetFragment = new ExportBottomSheetFragment();
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    private void writeFile(boolean isFromShare) {
        //exporting notes to file
        //setting the path
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/EventNotes";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdir();
        //creating file
        File file = new File(dir, "mynotes.txt");
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
        String title, time, desc;
        //writing notes to the file
        for (int i = 0; i < notes.size(); i++) {
            title = notes.get(i).getName().toString();
            time = converlontostring(Long.valueOf(notes.get(i).getLast_updated())).toString();
            desc = notes.get(i).getNotes().toString();
            notes_txt.append(title + " " + time + " " + desc);

            try {
                Files.append(title + "\n", file, Charset.defaultCharset());
                Files.append(time + "\n", file, Charset.defaultCharset());
                Files.append(desc + "\n", file, Charset.defaultCharset());
                Files.append("-------------END-------------" + "\n", file, Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //if it is from share it will share file to the mail application
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
        } else
            Toast.makeText(Note.this, "File Downloaded in " + filepath, Toast.LENGTH_LONG).show();

            
    }

    //checking the storage permission
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
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

    private void getNotesFromServer(){

        String tag_string_req = "Login";
        String url = ApiList.NOTES_DETAILS + "appid="+appDetails.getAppId()+"&userid="+userId;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                System.out.println(response);
                Gson gson = new Gson();

                JSONObject jObj = null;
                JSONObject notesdetails = null; //=  jObj.getJSONObject("Notes_Detail");
                try {
                    jObj = new JSONObject(response);
                    notesdetails =  jObj.getJSONObject("Notes_Detail");
                    if (jObj.getBoolean("response")) {
                        // Adding Note of Agenda

                        JSONArray agenda = notesdetails.getJSONArray("agenda");
                         gson = new Gson();
                        HashMap<Integer, Notes> noteslist = new HashMap<>();
                        for (int i = 0; i < agenda.length(); i++) {
                            String eventString = agenda.getJSONObject(i).toString();
                            Notes eobj = gson.fromJson(eventString, Notes.class);
                            noteslist.put(eobj.getId(), eobj);
                        }

                        Paper.book(appDetails.getAppId()).write("AgendaNote", noteslist);

                        // Adding Note of Exhibitor
                        JSONArray exh = notesdetails.getJSONArray("exhibitors");

                        noteslist = new HashMap<>();
                        gson = new Gson();
                        for (int i = 0; i < exh.length(); i++) {
                            String eventString = exh.getJSONObject(i).toString();
                            Notes eobj = gson.fromJson(eventString, Notes.class);
                            noteslist.put(eobj.getId(), eobj);
                        }

                        Paper.book(appDetails.getAppId()).write("ExhibitorNote", noteslist);

                        // Adding Note of Sponsor

                        JSONArray sponsor = notesdetails.getJSONArray("sponsors");

                        noteslist = new HashMap<>();
                        gson = new Gson();
                        for (int i = 0; i < sponsor.length(); i++) {
                            String eventString = sponsor.getJSONObject(i).toString();
                            Notes eobj = gson.fromJson(eventString, Notes.class);
                            noteslist.put(eobj.getId(), eobj);
                        }

                        Paper.book(appDetails.getAppId()).write("SponsorNote", noteslist);


                        HashMap<Integer, Notes> AgendaNotes = Paper.book(appDetails.getAppId()).read("AgendaNote", new HashMap<Integer, Notes>());
                        HashMap<Integer, Notes> ExhibitorNotes = Paper.book(appDetails.getAppId()).read("ExhibitorNote", new HashMap<Integer, Notes>());
                        HashMap<Integer, Notes> SponsorNotes = Paper.book(appDetails.getAppId()).read("SponsorNote", new HashMap<Integer, Notes>());
                        notes.clear();
                        AddAgendaNotes(AgendaNotes);
                        AddAgendaNotes(ExhibitorNotes);
                        AddAgendaNotes(SponsorNotes);

                        mAdapter.notifyDataSetChanged();


                    }
                } catch (JSONException e) {


                }


            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG,error.toString());
            }
        });


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

    }
}
