package com.singleevent.sdk.View.LeftActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.singleevent.sdk.Left_Adapter.FeedAdapter;
import com.singleevent.sdk.R;
import com.singleevent.sdk.interfaces.AddLifeCycleCallBackListener;
import com.singleevent.sdk.pojo.FeedPojo;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;

public class FeedActivity extends AppCompatActivity implements AddLifeCycleCallBackListener, View.OnClickListener {

    //    @BindView(R.id.feed_recycler)
    RecyclerView recyclerView;

    FeedAdapter feedAdapter;
    EditText editText_for_post;
    TextView latest, featured, social, news, questions;
    List<FeedPojo> feedPojoList = new ArrayList<>();
    ImageView search_img, search_cancel, post_image;
    SearchView searchView;
    CardView card;
    TextView post;
    Uri imageUri;
    Uri resultUri;
    LinearLayout write, upload;
    String category = "";
    private io.socket.client.Socket mSocket;
    String url = "https://health1.webmobi.in/socket/con";

    {
        try {
            mSocket = IO.socket(url);

        } catch (URISyntaxException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_left);
        initViews();
        setFeedData();
        setFeedPojoList();
        search_img.setOnClickListener(this);
        search_cancel.setOnClickListener(this);
        latest.setOnClickListener(this);
        featured.setOnClickListener(this);
        social.setOnClickListener(this);
        news.setOnClickListener(this);
        questions.setOnClickListener(this);
        write.setOnClickListener(this);
        upload.setOnClickListener(this);
        setLatest();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCategory(newText);
                return false;
            }
        });
        editText_for_post.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                post.setVisibility(View.VISIBLE);
                if (count == 0) {
                    post.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        try {
            mSocket.connect();
            Log.d("check_connection", "onCreate: " + mSocket.connected());
            if(mSocket.connected()){
                Log.d("check_connection", "onCreate: "+mSocket.connected()+"asdf");
            }
        }catch (Exception e){

        }

    }

    private void filterCategory(String query) {
        List<FeedPojo> new_list = new ArrayList<>();
        if (feedPojoList != null) {
            for (int i = 0; i < feedPojoList.size(); i++) {
                FeedPojo mod = feedPojoList.get(i);
                if (mod.getName().toLowerCase().contains(query.toLowerCase())) {
                    new_list.add(mod);
                }
                feedAdapter.filterData(new_list);
            }
        }
    }

    private void filterByTeamFilter(String query) {
        List<FeedPojo> new_list = new ArrayList<>();
        if (feedPojoList != null) {
            for (int i = 0; i < feedPojoList.size(); i++) {
                FeedPojo mod = feedPojoList.get(i);
                if (mod.getCategory().toLowerCase().contains(query.toLowerCase())) {
                    new_list.add(mod);
                }
                feedAdapter.filterData(new_list);
            }
        }
    }

    public void setFeedPojoList() {
        feedPojoList.clear();

        feedPojoList.add(new FeedPojo("Vivek Prajapati", "", "Latest", "text", getResources().getString(R.string.test_paragraph)));
        feedPojoList.add(new FeedPojo("Rohit Rai", "", "Latest", "text", getResources().getString(R.string.test_paragraph)));
        feedPojoList.add(new FeedPojo("Ritika Singh", "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4", "Latest", "video", getResources().getString(R.string.test_paragraph)));
        feedPojoList.add(new FeedPojo("Rohit Singh", "", "Latest", "text", getResources().getString(R.string.test_paragraph)));
        feedPojoList.add(new FeedPojo("Sambeet", "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4", "Latest", "video", getResources().getString(R.string.test_paragraph)));
        feedPojoList.add(new FeedPojo("Ritika Singh", "", "Latest", "text", getResources().getString(R.string.test_paragraph)));
        feedPojoList.add(new FeedPojo("Mannu tanti", "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4", "Latest", "video", getResources().getString(R.string.test_paragraph)));
        feedPojoList.add(new FeedPojo("Mannu tanti", "https://www.youtube.com/watch?v=d04kVUVYlYg", "Latest", "video", getResources().getString(R.string.test_paragraph)));

        feedPojoList.add(new FeedPojo("Angel", "", "featured", "text", getResources().getString(R.string.test_paragraph)));
        feedPojoList.add(new FeedPojo("Dark Angel", "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4", "featured", "video", getResources().getString(R.string.test_paragraph)));
        feedPojoList.add(new FeedPojo("Light Angel", "", "featured", "text", getResources().getString(R.string.test_paragraph)));
        feedPojoList.add(new FeedPojo("Laptop", "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4", "featured", "video", getResources().getString(R.string.test_paragraph)));
        feedPojoList.add(new FeedPojo("Mobile", "", "featured", "text", getResources().getString(R.string.test_paragraph)));

        feedPojoList.add(new FeedPojo("Server_crash", "https://images.unsplash.com/photo-1562577309-4932fdd64cd1?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nnx8c29jaWFsJTIwbWVkaWF8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60", "social", "image", getResources().getString(R.string.test_paragraph)));
        feedPojoList.add(new FeedPojo("Angel", "https://images.unsplash.com/photo-1579869847557-1f67382cc158?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1191&q=80", "social", "image", getResources().getString(R.string.test_paragraph)));
        feedPojoList.add(new FeedPojo("Mobile", "https://images.unsplash.com/photo-1519389950473-47ba0277781c?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80", "social", "image", getResources().getString(R.string.test_paragraph)));
        feedPojoList.add(new FeedPojo("Mobile", "https://images.unsplash.com/photo-1432888622747-4eb9a8efeb07?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1053&q=80", "social", "image", getResources().getString(R.string.test_paragraph)));

        feedAdapter.notifyDataSetChanged();
    }

    public void setFeedData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedAdapter = new FeedAdapter(feedPojoList, this);
        recyclerView.setAdapter(feedAdapter);

    }


    @Override
    public void addLifeCycleCallBack(YouTubePlayerView youTubePlayerView) {
        getLifecycle().addObserver(youTubePlayerView);
    }

    private void initViews() {
//        ActionBar actionBar = getSupportActionBar();
//        getSupportActionBar().setTitle("Feeds");
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
//        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.feed_recycler);
        search_img = findViewById(R.id.search_img);
        searchView = findViewById(R.id.searchView);
        search_cancel = findViewById(R.id.search_cancel);
        card = findViewById(R.id.card);
        questions = findViewById(R.id.questions);
        latest = findViewById(R.id.latest);
        featured = findViewById(R.id.featured);
        social = findViewById(R.id.social);
        news = findViewById(R.id.news);
        write = findViewById(R.id.write);
        upload = findViewById(R.id.upload);
        editText_for_post = findViewById(R.id.editText_for_post);
        post = findViewById(R.id.post);
        post_image = findViewById(R.id.post_image);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.search_img) {
            card.setVisibility(View.VISIBLE);
            searchView.setFocusable(true);
            search_cancel.setVisibility(View.VISIBLE);
            search_img.setVisibility(View.GONE);
        } else if (id == R.id.search_cancel) {
            card.setVisibility(View.GONE);
            search_cancel.setVisibility(View.GONE);
            search_img.setVisibility(View.VISIBLE);
        } else if (id == R.id.latest) {
            setLatest();
        } else if (id == R.id.featured) {
            filterByTeamFilter("featured");
            category = "featured";
            featured.setTextColor(getResources().getColor(R.color.white));
            social.setTextColor(getResources().getColor(R.color.dark_grey));
            news.setTextColor(getResources().getColor(R.color.dark_grey));
            questions.setTextColor(getResources().getColor(R.color.dark_grey));
            latest.setTextColor(getResources().getColor(R.color.dark_grey));
        } else if (id == R.id.social) {
            filterByTeamFilter("social");
            category = "social";
            featured.setTextColor(getResources().getColor(R.color.dark_grey));
            social.setTextColor(getResources().getColor(R.color.white));
            news.setTextColor(getResources().getColor(R.color.dark_grey));
            questions.setTextColor(getResources().getColor(R.color.dark_grey));
            latest.setTextColor(getResources().getColor(R.color.dark_grey));
        } else if (id == R.id.news) {
            filterByTeamFilter("news");
            category = "news";
            featured.setTextColor(getResources().getColor(R.color.dark_grey));
            social.setTextColor(getResources().getColor(R.color.dark_grey));
            news.setTextColor(getResources().getColor(R.color.white));
            questions.setTextColor(getResources().getColor(R.color.dark_grey));
            latest.setTextColor(getResources().getColor(R.color.dark_grey));
        } else if (id == R.id.questions) {
            filterByTeamFilter("questions");
            category = "questions";
            featured.setTextColor(getResources().getColor(R.color.dark_grey));
            social.setTextColor(getResources().getColor(R.color.dark_grey));
            news.setTextColor(getResources().getColor(R.color.dark_grey));
            questions.setTextColor(getResources().getColor(R.color.white));
            latest.setTextColor(getResources().getColor(R.color.dark_grey));
        } else if (id == R.id.write) {
            editText_for_post.setEnabled(true);
            editText_for_post.setFocusableInTouchMode(true);
            editText_for_post.requestFocus();
        } else if (id == R.id.upload) {
            editText_for_post.setEnabled(true);
            editText_for_post.setFocusableInTouchMode(true);
            editText_for_post.requestFocus();
            uploadImageFromSystem();
        }
    }

    private void setLatest() {
        filterByTeamFilter("latest");
        category = "latest";
        featured.setTextColor(getResources().getColor(R.color.dark_grey));
        social.setTextColor(getResources().getColor(R.color.dark_grey));
        news.setTextColor(getResources().getColor(R.color.dark_grey));
        questions.setTextColor(getResources().getColor(R.color.dark_grey));
        latest.setTextColor(getResources().getColor(R.color.white));
    }

    public void createPost(View view) {
        String content = editText_for_post.getText().toString();
        if (!content.isEmpty() && !category.isEmpty() || resultUri != null) {
            if (resultUri != null) {
                feedPojoList.add(new FeedPojo("vivek", resultUri.toString(), category, "image", content));
                feedAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                editText_for_post.getText().clear();
                resultUri = null;
                post_image.setVisibility(View.GONE);
                post.setVisibility(View.GONE);
                return;
            }
            if (content.contains("https://")) {
                feedPojoList.add(new FeedPojo("vivek", "", category, "link", content));
            } else {
                feedPojoList.add(new FeedPojo("vivek", "", category, "image", content));
            }
            feedAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Posted Successfully", Toast.LENGTH_SHORT).show();
            editText_for_post.getText().clear();
            resultUri = null;
            post_image.setVisibility(View.GONE);
            post.setVisibility(View.GONE);


            ;
        } else {
            Toast.makeText(this, "unSuccessful", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageFromSystem() {
        Dexter.withContext(FeedActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
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
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = CropImage.getPickImageResultUri(this, data);
            Log.d("image_uri", "nextActivity: " + imageUri);
            startCrop(imageUri);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                post_image.setVisibility(View.VISIBLE);
                post.setVisibility(View.VISIBLE);
                post_image.setImageURI(result.getUri());
                resultUri = result.getUri();
                Log.d("image_uri", "nextActivity: " + resultUri);

            }
        }

    }

    private void startCrop(Uri imageUri) {
        CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(3, 2).setMultiTouchEnabled(true).start(this);
    }
}