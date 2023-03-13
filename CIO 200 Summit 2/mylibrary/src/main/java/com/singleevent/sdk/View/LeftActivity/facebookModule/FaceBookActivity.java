package com.singleevent.sdk.View.LeftActivity.facebookModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.singleevent.sdk.R;

public class FaceBookActivity extends AppCompatActivity {
    private CallbackManager facebookCallbackManager;
    private ShareDialog shareDialog;
    LinearLayout container_layout;
    EditText title, links;
    TextView message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_book);
        title = findViewById(R.id.title);
        links = findViewById(R.id.links);
        container_layout = findViewById(R.id.container_layout);
        message = findViewById(R.id.message);
        FacebookSdk.sdkInitialize(getApplicationContext());
        facebookCallbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences_faceBook_connected = getSharedPreferences("faceBookConnected", MODE_PRIVATE);
        boolean fb_connected = sharedPreferences_faceBook_connected.getBoolean("connected", false);
        if(fb_connected){
            container_layout.setVisibility(View.VISIBLE);
            message.setVisibility(View.GONE);
        }else{
            container_layout.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);

        }
    }

    public void postToFaceBook(View view) {
        if ((title.getText().toString() != null && !title.getText().toString().isEmpty()) &&
                (links.getText().toString() != null && !links.getText().toString().isEmpty())) {

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setQuote(title.getText().toString())
                    .setContentUrl(Uri.parse(links.getText().toString()))
                    .build();
            if (shareDialog.canShow(ShareLinkContent.class)) {
                shareDialog.show(linkContent);
            }
            shareDialog.registerCallback(facebookCallbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(FaceBookActivity.this, "Data send successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(FaceBookActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(FaceBookActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Toast.makeText(FaceBookActivity.this, "all fields are required", Toast.LENGTH_SHORT).show();
        }

    }
}