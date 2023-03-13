package com.singleevent.sdk.health.Activity;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.singleevent.sdk.R;

public class MainActivityHealth extends AppCompatActivity {

    Button getStarted;
    TextView Signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_health);
        getStarted = (Button) findViewById(R.id.btn_getstarted);
       // getSupportActionBar().hide();
        Paper.init(this);
        Boolean flag=Paper.book().read("ISLOGINSTATUS",false);
        if(flag)
        {
            Intent intent = new Intent(MainActivityHealth.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else {

            getStarted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(MainActivityHealth.this, LoginwithnumberActivity.class);
                    startActivity(in);
                }
            });
        }
    }
}
