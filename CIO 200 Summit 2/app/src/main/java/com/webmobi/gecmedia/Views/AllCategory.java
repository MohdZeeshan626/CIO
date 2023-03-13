package com.webmobi.gecmedia.Views;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.singleevent.sdk.Custom_View.Util;
import com.webmobi.gecmedia.R;
import com.webmobi.gecmedia.Views.Adapter.CategoryListAdapter;

public class AllCategory extends AppCompatActivity {

    String[] cat_name = {"Trade Show and Events", "Schools", "Community Centers", "Film Festivals", "Museums", "Personal Events", "Associations", "Airports", "National Park"};
    Integer[] cat_img = {R.drawable.trade_ic, R.drawable.schools_ic, R.drawable.community_ic, R.drawable.filmfest_ic, R.drawable.museums_ic, R.drawable.personal_ic, R.drawable.association_ic, R.drawable.airport_ic, R.drawable.park_ic};
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.category_list);
        CategoryListAdapter categoryListAdapter = new CategoryListAdapter(R.layout.category_list_row, cat_name, cat_img, this);
        listView.setAdapter(categoryListAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent cat = new Intent(getApplicationContext(), EventCategory.class);
                cat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                cat.putExtra("categoryname", cat_name[i]);
                startActivity(cat);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SpannableString s = new SpannableString("Categories");
        setTitle(Util.applyFontToMenuItem(this, s));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
