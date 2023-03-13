package com.singleevent.sdk.View.RightActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.singleevent.sdk.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.paperdb.Paper;

public class FeedPostDemo extends AppCompatActivity {

   @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
       setContentView(R.layout.newfeedpost);

    }
}
