package com.singleevent.sdk.View.LeftActivity.linkedInModule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.singleevent.sdk.R;
import com.singleevent.sdk.model.AppDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class LinkedInPostsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    String body_text, body_link, body_image;
    private String url1_image, asset;

    EditText post_text;
    ImageView post_photo;
    ImageView picked_image;
    TextView post_to_linked_in;
    String image_blob = "";
    private Uri photouri;
    private Bitmap bitmap;
    private byte[] imgByte;
    Toolbar toolbar;
    AppDetails appDetails;
    int pos;
    String hashtag = "", default_msg = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_in_posts);
        initViews();
        sharedPreferences = getSharedPreferences("LinkedIn", MODE_PRIVATE);
//        sharedPreferences = getSharedPreferences("LinkedIn_Authenticated", MODE_PRIVATE);

        post_to_linked_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((post_text.getText().toString().startsWith("wwww.") ||
                        post_text.getText().toString().startsWith("https://"))
                        && post_text.getText().toString().contains(".")) && image_blob.isEmpty()) {
                    try {
                        postLinkToLinkedIn();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (!image_blob.isEmpty()) {
                   ProgressDialog progressDialog=new ProgressDialog(LinkedInPostsActivity.this);
                   progressDialog.show();
                   try {
                        postImageToLinkedIn();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                } else {
                    if (post_text.getText().toString() != null && !post_text.getText().toString().isEmpty()) {
                        try {
                            postTextToLinkedIn();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        post_text.setError("Field Can't be Empty");
                        post_text.requestFocus();
                    }
                }

            }
        });

    }

    public void pickImage(View view) {
        Dexter.withContext(LinkedInPostsActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "select image file"), 1);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            photouri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photouri);
                convertToString();
                picked_image.setImageURI(photouri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("fffffffffff", photouri.toString());
        }

    }

    private String convertToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imgByte = byteArrayOutputStream.toByteArray();
        image_blob=Base64.encodeToString(imgByte, Base64.DEFAULT);
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private void postImageToLinkedIn() throws JSONException {
        step1();
    }

    private void initViews() {
        appDetails = Paper.book().read("Appdetails");
        pos = getIntent().getExtras().getInt("pos");
        hashtag = getIntent().getStringExtra("hashtag");
        default_msg = getIntent().getStringExtra("default_msg");
        toolbar=findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        post_text = findViewById(R.id.post_text);
        post_photo = findViewById(R.id.post_photo);
        post_to_linked_in = findViewById(R.id.post_to_linked_in);
        picked_image = findViewById(R.id.picked_image);
        post_text.setText(default_msg);

    }

    public void back(View view) {
        onBackPressed();
        Intent i =new Intent(LinkedInPostsActivity.this,LinkedInMainActivity.class);
        i.putExtra("pos", pos);
        startActivity(i);
        finish();

    }

    private void postTextToLinkedIn() throws JSONException {
        if (!post_text.getText().toString().isEmpty()) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.show();
            String id=sharedPreferences.getString("id","");
            String token=sharedPreferences.getString("token","");
            body_text = "{\"author\": \"urn:li:person:" + id + "\"," +  // person URN
                    "\"lifecycleState\": \"PUBLISHED\"," +
                    "\"specificContent\": " +
                    "{\"com.linkedin.ugc.ShareContent\":" +
                    "{\"shareCommentary\": " +
                    "{\"text\": \"" + post_text.getText().toString()  + "\"}," +  //adding text
                    "\"shareMediaCategory\": \"NONE\"}}," +
                    "\"visibility\": {\"com.linkedin.ugc.MemberNetworkVisibility\": \"PUBLIC\"}}";

            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://api.linkedin.com/v2/ugcPosts", new JSONObject(body_text),
                    new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            progressDialog.dismiss();
                            Toast.makeText(LinkedInPostsActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                            Intent i =new Intent(LinkedInPostsActivity.this,LinkedInMainActivity.class);
                            i.putExtra("pos", pos);
                            startActivity(i);
                            finish();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.d("fffffffff", error.toString());
                    Toast.makeText(LinkedInPostsActivity.this, "Posting Content failed " + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Authorization", "Bearer " + token);
                    return map;
                }
            };
            queue.add(request);
        } else {
            post_text.setError("Field Can't be Empty");
            post_text.requestFocus();
        }
    }

    private void postLinkToLinkedIn() throws JSONException {
        if (!post_text.getText().toString().isEmpty()) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.show();
            body_link = "{\"author\": \"urn:li:person:" + sharedPreferences.getString("id", "") + "\"," +
                    "    \"lifecycleState\": \"PUBLISHED\"," +
                    "    \"specificContent\": {" +
                    "        \"com.linkedin.ugc.ShareContent\": {" +
                    "            \"shareCommentary\": {" +
                    "                \"text\": \"" + "" + "\"" +
                    "            }," +
                    "            \"shareMediaCategory\": \"ARTICLE\"," +
                    "            \"media\": [" +
                    "                {" +
                    "                    \"status\": \"READY\"," +
                    "                    \"description\": {" +
                    "                        \"text\": \"" + "" + "\"" +
                    "                    }," +
                    "                    \"originalUrl\": \"" + post_text.getText().toString() + "\"," +
                    "                    \"title\": {" +
                    "                        \"text\": \"" + "" + "\"}}]}}," +
                    "    \"visibility\": {\"com.linkedin.ugc.MemberNetworkVisibility\": \"PUBLIC\"}}";

            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://api.linkedin.com/v2/ugcPosts", new JSONObject(body_link),
                    new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            progressDialog.dismiss();
                            Toast.makeText(LinkedInPostsActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                            Intent i =new Intent(LinkedInPostsActivity.this,LinkedInMainActivity.class);
                            i.putExtra("pos", pos);
                            startActivity(i);
                            finish();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();

                    Log.d("fffffffff", error.toString());
                    Toast.makeText(LinkedInPostsActivity.this, "Posting Content failed " + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Authorization", "Bearer " + sharedPreferences.getString("token", ""));
                    return map;
                }
            };
            queue.add(request);

        } else {
            post_text.setError("Field Can't be Empty");
            post_text.requestFocus();

        }
    }

    public void step1() throws JSONException {


        body_image = "{\"registerUploadRequest\": " +
                "{\"recipes\": [\"urn:li:digitalmediaRecipe:feedshare-image\"]," +
                "\"owner\": \"urn:li:person:" + sharedPreferences.getString("id", "") + "\"," +
                "\"serviceRelationships\": " +
                "[{\"relationshipType\": \"OWNER\"," +
                "\"identifier\": \"urn:li:userGeneratedContent\"}]}}";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://api.linkedin.com/v2/assets?action=registerUpload", new JSONObject(body_image), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d("fffffff",response.toString());
                try {
                    url1_image = response.getJSONObject("value").getJSONObject("uploadMechanism")
                            .getJSONObject("com.linkedin.digitalmedia.uploading.MediaUploadHttpRequest")
                            .get("uploadUrl").toString();
                    asset = response.getJSONObject("value").get("asset").toString();
                    Log.d("fffffffffff", url1_image);
                    Log.d("fffffffffff", asset);
                    step2();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LinkedInPostsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", "Bearer " + sharedPreferences.getString("token", ""));
                return map;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void step2() throws IOException, JSONException {
        new Step2Back().execute();
        Log.d("fffffffffff", "transfered");
    }
    public void step3() throws JSONException {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.show();

        body_image = "{" +
                "    \"author\": \"urn:li:person:" + sharedPreferences.getString("id", "") + "\"," +
                "    \"lifecycleState\": \"PUBLISHED\"," +
                "    \"specificContent\": {" +
                "        \"com.linkedin.ugc.ShareContent\": {" +
                "            \"shareCommentary\": {" +
                "                \"text\": \"" + post_text.getText().toString()   + " \"" +
                "            }," +
                "            \"shareMediaCategory\": \"IMAGE\"," +
                "            \"media\": [" +
                "                {" +
                "                    \"status\": \"READY\"," +
                "                    \"description\": {" +
                "                        \"text\": \"" + "" + "\"" +
                "                    }," +
                "                    \"media\": \"" + asset + "\"," +
                "                    \"title\": {" +
                "                        \"text\": \"" + "" + "\"" +
                "                    }" +
                "                }" +
                "            ]" +
                "        }" +
                "    }," +
                "    \"visibility\": {" +
                "        \"com.linkedin.ugc.MemberNetworkVisibility\": \"PUBLIC\"" +
                "    }" +
                "}";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://api.linkedin.com/v2/ugcPosts", new JSONObject(body_image),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        progressDialog.dismiss();
                        Toast.makeText(LinkedInPostsActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                        Intent i =new Intent(LinkedInPostsActivity.this,LinkedInMainActivity.class);
                        i.putExtra("pos", pos);
                        startActivity(i);
                        finish();
                        Log.d("fffffff", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("fffffffff", error.toString());
                Toast.makeText(LinkedInPostsActivity.this, "Posting Content failed " + error, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Authorization", "Bearer " + sharedPreferences.getString("token", ""));
                return map;
            }
        };

        queue.add(request);
    }

    public class Step2Back extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {

            File file = new File(FileUtils.getRealPathFromURI_API19(LinkedInPostsActivity.this,photouri));

            Log.d("ffffffffff",file.getAbsolutePath());

            OkHttpClient client1 = new OkHttpClient().newBuilder().build();
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image",file.getName(),RequestBody.create(MediaType.parse("image/jpeg"),file))
                    .build();

            MediaType mediaType = MediaType.parse("image/jpeg");
            RequestBody body1 = RequestBody.create(mediaType, imgByte);

            okhttp3.Request request1 = new okhttp3.Request.Builder()
                    .url(url1_image)
                    .addHeader("Authorization", "Bearer " + sharedPreferences.getString("token",""))
                    .addHeader("X-Restli-Protocol-Version","2.0.0")
                    .addHeader("Content-Type", "image/jpg")
                    .method("POST",body1)
                    .build();

            try {
                okhttp3.Response response1 = client1.newCall(request1).execute();
                Log.d("hhhhhhhhhhhh",response1.toString());
                newStep();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }


    public void newStep ()
    {
        String str[] = asset.split(":",4);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, "https://api.linkedin.com/v2/assets/" + str[3],
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                        // Toast.makeText(LinkedInPostPhoto.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                        Log.d("fffffff",response.toString());
                        try {
                            step3();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("fffffffff",error.toString());
                Toast.makeText(LinkedInPostsActivity.this, "Posting Content failed " + error, Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
                map.put("Authorization","Bearer " + sharedPreferences.getString("token",""));
                return map;
            }
        };
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i =new Intent(LinkedInPostsActivity.this,LinkedInMainActivity.class);
        i.putExtra("pos", pos);
        startActivity(i);
        finish();
    }
}