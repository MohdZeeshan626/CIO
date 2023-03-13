package com.singleevent.sdk.View.RightActivity.admin.leadGeneration;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;

import io.paperdb.Paper;

/**
 * Created by webMOBI on 12/14/2017.
 */

public class AddLeadManuallyActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    AppDetails appDetails;
    private Button btn_save,btn_cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lead_manually);
        Paper.init(this);
        appDetails = Paper.book().read("Appdetails");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_save =(Button) findViewById(R.id.btn_save);



        setSupportActionBar(toolbar);
        //setting background color of button and
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));


        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);


        if(Build.VERSION.SDK_INT < 21){
            btn_save.setBackgroundColor(Color.parseColor(String.valueOf(R.color.green_transparent)));
            btn_cancel.setBackgroundColor(Color.parseColor(String.valueOf(R.color.green_transparent)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(Util.applyFontToMenuItem(AddLeadManuallyActivity.this,
                new SpannableString("Add Lead Manually")));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;

    }

    @Override
    public void onClick(View v) {

    }
}
