package com.singleevent.sdk.health.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

public class Challengemode extends AppCompatActivity  implements View.OnClickListener {
    TextView select;
    Button cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challengemode);
        cancel=findViewById(R.id.button6);
        select=(TextView)findViewById(R.id.select);
        select.setOnClickListener(this);
        cancel.setOnClickListener(this);
        cancel.setBackground(Util.setrounded(Color.parseColor("#d0d2d4")));
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button6){
            onBackPressed();
        }
        if(view.getId()==R.id.select){
            Intent i=new Intent (Challengemode.this,HomeActivity.class);
            startActivity(i);
        }
    }
}
  