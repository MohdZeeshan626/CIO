package com.singleevent.sdk.View.RightActivity.admin.printsettings;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;

import io.paperdb.Paper;

public class PrintSettingsActivity extends AppCompatActivity {
    Toolbar toolbar;
    AppDetails appDetails;
    Switch print_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(getApplicationContext());
        setContentView(R.layout.activity_print_settings);
        appDetails = Paper.book().read("Appdetails");
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        setSupportActionBar(toolbar);

        print_switch = (Switch) findViewById(R.id.print_switch);
        //reading switch status from local storage
        boolean isEnabled = Paper.book(appDetails.getAppId()).read("print_status", false);
        print_switch.setChecked(isEnabled);
        //setting switch color
        setSwitchColor(isEnabled);
        //once the switch status change this will be triggered
        print_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //storing switch status locally
                Paper.book(appDetails.getAppId()).write("print_status", print_switch.isChecked());
                setSwitchColor(print_switch.isChecked());
            }
        });
    }

    private void setSwitchColor(boolean isEnabled) {
        //setting the switch color according to the theme
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            print_switch.getThumbDrawable().setColorFilter(isEnabled ? Color.parseColor(appDetails.getTheme_color()) : Color.GRAY, PorterDuff.Mode.MULTIPLY);
            print_switch.getTrackDrawable().setColorFilter(isEnabled ? Color.parseColor(appDetails.getTheme_color()) : Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setting title
        SpannableString s = new SpannableString("Print Settings");
        setTitle(Util.applyFontToMenuItem(this, s));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;

    }
}
