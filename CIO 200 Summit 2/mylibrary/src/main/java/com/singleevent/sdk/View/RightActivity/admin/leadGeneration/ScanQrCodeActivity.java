package com.singleevent.sdk.View.RightActivity.admin.leadGeneration;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;

import io.paperdb.Paper;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by webMOBI on 12/18/2017.
 */

public class ScanQrCodeActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler{
    private Toolbar toolbar;
    private static final int ZBAR_CAMERA_PERMISSION = 12;
    private ZBarScannerView mScannerView;
    private AppDetails appDetails;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);
        appDetails = Paper.book().read("Appdetails");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        mScannerView.setAutoFocus(true);
        contentFrame.addView(mScannerView);

        appDetails = Paper.book().read("Appdetails");



    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(Util.applyFontToMenuItem(this,new SpannableString("QR Scan")));
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        Intent intent = new Intent();
        intent.putExtra("content", result.getContents());
        setResult(RESULT_OK, intent);
        finish();
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
}
