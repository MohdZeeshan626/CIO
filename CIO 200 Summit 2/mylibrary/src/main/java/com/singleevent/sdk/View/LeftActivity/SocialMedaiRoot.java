package com.singleevent.sdk.View.LeftActivity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Left_Adapter.SocialMediaAdapter;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Events;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.R;

import java.util.ArrayList;

import io.paperdb.Paper;

/**
 * Created by Admin on 5/31/2017.
 */

public class SocialMedaiRoot extends AppCompatActivity {

    AppDetails appDetails;
    int pos;
    private String title;
    private ArrayList<Events> events = new ArrayList<Events>();
    Events e;
    ArrayList<Items> medialists = new ArrayList<>();
    ListView listview;
    LinearLayout lsp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.s_speakerroot);
        appDetails = Paper.book().read("Appdetails");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        lsp=(LinearLayout)findViewById(R.id.lsp);
        lsp.setVisibility(View.GONE);
        setSupportActionBar(toolbar);


        // getting the data from previous activity

        if (getIntent().getExtras() == null)
            finish();

        pos = getIntent().getExtras().getInt("pos");
        title = getIntent().getExtras().getString("title");
        events = Paper.book().read("Appevents");
        e = events.get(0);

        medialists.clear();
        for (int j = 0; j < e.getTabs(pos).getItemsSize(); j++) {
            medialists.add(e.getTabs(pos).getItems(j));
        }

        listview = (ListView) findViewById(R.id.speakerlist);
        listview.setAdapter(new SocialMediaAdapter(SocialMedaiRoot.this, medialists));


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


}
