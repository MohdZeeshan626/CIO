package com.webmobi.gecmedia.Views;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import io.paperdb.Paper;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.utils.DataBaseStorage;
import com.webmobi.gecmedia.R;

public class ContactUs extends AppCompatActivity implements View.OnClickListener {
    ImageView back_btn;
    EditText feedback;
    TextView send;
    AppDetails appDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        setContentView(R.layout.activity_contac_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        back_btn = (ImageView) findViewById(R.id.contac_back_btn);
        feedback = (EditText) findViewById(R.id.feedback);
        send = (TextView) findViewById(R.id.send_txt);
        back_btn.setOnClickListener(this);
        send.setOnClickListener(this);
        appDetails = Paper.book().read("Appdetails");
        send.setBackground(Util.setdrawable(ContactUs.this, R.drawable.round_selected, Color.parseColor(appDetails.getTheme_color())));

    }

    private void sendReport() {
        String report_text = feedback.getText().toString();

        //sending the report to the email client
        if (isEmpty(report_text)) {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@webmobi.in"});
            email.putExtra(Intent.EXTRA_SUBJECT, "Android Discovery AppNew Report ");
            email.putExtra(Intent.EXTRA_TEXT, report_text);

            //need this to prompts email client only
            email.setType("message/rfc822");

            startActivityForResult(Intent.createChooser(email, "Choose an Email client :"), 12);
        } else {
            Toast.makeText(this, "Please enter valid message", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12)
            finish();

    }

    private boolean isEmpty(String txt) {
        //checking the text is valid or not
        boolean isFeedbackEmpty = false;

        if (txt.equalsIgnoreCase("") || txt.isEmpty() || txt == null)
            isFeedbackEmpty = false;
        else
            isFeedbackEmpty = true;

        return isFeedbackEmpty;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contac_back_btn:
                onBackPressed();
                break;
            case R.id.send_txt:
                //checking the app is online or offline
                if (DataBaseStorage.isInternetConnectivity(this))
                    sendReport();
                else
                    Error_Dialog.show("Please Check Your Internet Connection", this);
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

