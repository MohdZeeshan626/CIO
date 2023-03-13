package com.singleevent.sdk.View.LeftActivity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;
import android.widget.ListView;


import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Left_Adapter.PollingAdapter;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.R;

import java.util.ArrayList;

import io.paperdb.Paper;
import java.util.List;

/**
 * Created by Admin on 5/31/2017.
 */

public class PollingRoot extends AppCompatActivity {
    AppDetails appDetails;
    int pos,polltypeid;
    String title,polltype;
    private ArrayList<Events> events = new ArrayList<Events>();
    List<Agendadetails> ad;
    Events e;
    ArrayList<Items> lists = new ArrayList<>();
    ArrayList<Items> lists1 = new ArrayList<>();
    ListView listview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.act_pollingroot);
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);


        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        pos = getIntent().getExtras().getInt("pos");
        title = getIntent().getExtras().getString("title");
        polltype = getIntent().getExtras().getString("polltype");
        polltypeid=getIntent().getExtras().getInt("polltypeid");

        events = Paper.book().read("Appevents");
        e = events.get(0);

        lists.clear();
        lists1.clear();

        for (int j = 0; j < e.getTabs(pos).getItemsSize(); j++) {

            if (e.getTabs(pos).getItems(j).getPoll_type().contains("global")&&e.getTabs(pos).getItems(j).getHide_poll()==0 ) {
                lists.add(e.getTabs(pos).getItems(j));}
            if (e.getTabs(pos).getItems(j).getPoll_type().contains("agenda")&&e.getTabs(pos).getItems(j).getHide_poll()==0) {
                lists1.add(e.getTabs(pos).getItems(j));}

        }

        listview = (ListView) findViewById(R.id.speakerlist);
        /*initializing polling adapter*/
        if(polltype.equals("global")){
            lists1.clear();
            listview.setAdapter(new PollingAdapter(PollingRoot.this, lists, pos, title,polltype,polltypeid));}
        else{
            lists.clear();
            listview.setAdapter(new PollingAdapter(PollingRoot.this, lists1, pos, title,polltype,polltypeid));}}


    public static boolean isAgenaIdAvailable(int n){

        return true;
    }




    @Override
    protected void onResume() {
        super.onResume();

        SpannableString s = new SpannableString(title);
        /*setting title name of this activity*/
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
}
