package com.webmobi.gecmedia.Views;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.webmobi.gecmedia.BuildConfig;
import com.webmobi.gecmedia.R;

import cyd.awesome.material.AwesomeText;

public class AppInfo extends AppCompatActivity {

    AwesomeText close_btn;
    TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        close_btn = (AwesomeText) findViewById(R.id.appinfo_close);
        version = (TextView) findViewById(R.id.version_txt);

        //setting the version name dynamically
        version.setText("Version " + BuildConfig.VERSION_NAME);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}


