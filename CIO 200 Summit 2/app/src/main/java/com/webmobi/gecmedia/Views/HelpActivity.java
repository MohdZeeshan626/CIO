package com.webmobi.gecmedia.Views;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.webmobi.gecmedia.R;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {

    TextView contactus, appinfo, privacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        contactus = (TextView) findViewById(R.id.contact_txt);
        appinfo = (TextView) findViewById(R.id.appinfo_txt);
        privacy = (TextView) findViewById(R.id.privacy_txt);

//        contactus.setOnClickListener(this);
        appinfo.setOnClickListener(this);
        privacy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
//            case R.id.contact_txt:
//                //navigate to ContactUs
//                i = new Intent(this, ContactUs.class);
//                startActivity(i);
//                break;
            case R.id.appinfo_txt:
                //navigate to AppInfo
                i = new Intent(this, AppInfo.class);
                startActivity(i);
                break;
            case R.id.privacy_txt:
                //navigate to PrivacyPolicy
                i = new Intent(this, PrivacyPolicy.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
