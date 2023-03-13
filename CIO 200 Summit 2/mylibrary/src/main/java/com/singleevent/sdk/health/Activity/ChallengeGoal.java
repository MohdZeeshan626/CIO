package com.singleevent.sdk.health.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.R;
import com.singleevent.sdk.health.Adapter.RecyclerViewAdapter;
import com.singleevent.sdk.health.Adapter.RecylerViewActiveChallengeAdapter;
import com.singleevent.sdk.health.Model.CustomItem;
import com.singleevent.sdk.model.AppDetails;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

public class ChallengeGoal extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mTotalkm = new ArrayList<>();
    private ArrayList<Integer> mPosition = new ArrayList<>();
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    //  Spinner spinner,spinmode;
    TextView next;
    Button button6;
    //RecyclerView activeRecylerView;
    LinearLayout myEvents, myEvents1;
    float ImgWidth, ImgHeight, lwidth, lheight;
    ArrayList<CustomItem> customlist;
    LinearLayout public_privacy, private_privacy;
    TextView privacytext,gp_text,gp_text1;
    AppDetails appDetails;
    ImageView imgpublic1,imgpublic;


    private ArrayList<String> mImage = new ArrayList<>();
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mTotal = new ArrayList<>();
    RecylerViewActiveChallengeAdapter recylerViewActiveChallengeAdapter;
    //TextView txtActive;
    //   LinearLayout linearLayout;
    // Button btn_create,btn_join,btn_active_option;
     ImageView backpress;
     String type="steps";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenge_goal);
        public_privacy = findViewById(R.id.public_privacy);
        private_privacy = findViewById(R.id.private_privacy);
        privacytext = findViewById(R.id.privacytext);
        appDetails = Paper.book().read("Appdetails");
        public_privacy.setOnClickListener(this);
        private_privacy.setOnClickListener(this);
        gp_text=(TextView)findViewById(R.id.gp_text);
        gp_text1=(TextView)findViewById(R.id.gp_text1);
        imgpublic=(ImageView) findViewById(R.id.imgpublic);
        imgpublic1=(ImageView) findViewById(R.id.imgpublic1);
        backpress=(ImageView)findViewById(R.id.backpress);
        backpress.setOnClickListener(this);
       // private_privacy.setBackground(Util.setrounded(Color.GRAY));
       // public_privacy.setBackground(Util.setrounded(Color.GRAY));
         next=(TextView) findViewById(R.id.next);
        next.setBackground(Util.setrounded(Color.parseColor("#7d71d0")));
        button6=(Button) findViewById(R.id.button6);
        button6.setBackground(Util.setrounded(Color.parseColor("#7d71d0")));
        next.setOnClickListener(this);
        button6.setOnClickListener(this);
        public_privacy.setBackground(Util.setrounded(Color.parseColor("#E1E1E1")));
        private_privacy.setBackground(Util.setrounded(Color.parseColor("#E1E1E1")));
        GradientDrawable drawable1 = (GradientDrawable) public_privacy.getBackground();
        drawable1.setStroke (2, Color.DKGRAY);
        GradientDrawable drawable2 = (GradientDrawable) private_privacy.getBackground();
        drawable2.setStroke (2, Color.DKGRAY);

    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.public_privacy) {
            type="steps";
            GradientDrawable drawable = (GradientDrawable) public_privacy.getBackground();
            drawable.setStroke (5, Color.parseColor ("#5952c1")); // Sets the border width and color

            // If you use your own defined color, use drawable.setColor (getResources () getColor (R.color.yellow_color).);
            // Set the fill color

            // drawable.setColor( Color.parseColor("#ff495b"));

            //  public_privacy.setBackground(Util.setrounded(Color.parseColor(appDetails.getTheme_color())));
            gp_text.setTextColor(Color.parseColor("#5952c1"));//(appDetails.getTheme_color()));
          //  imgpublic.setColorFilter(Color.parseColor(appDetails.getTheme_color()));
            gp_text1.setTextColor(Color.parseColor("#203142"));
          //  imgpublic1.setColorFilter(Color.parseColor("#203142"));
          //  private_privacy.setBackground(com.singleevent.sdk.Custom_View.Util.setrounded(Color.WHITE));

            GradientDrawable drawable1 = (GradientDrawable) private_privacy.getBackground();
            drawable1.setStroke (3, Color.DKGRAY);


         //   privacytext.setText("Anyone can see who's in the challenge");
        }
        if (view.getId() == R.id.private_privacy) {
            type="time";
            GradientDrawable drawable = (GradientDrawable) private_privacy.getBackground();
            drawable.setStroke (5, Color.parseColor ("#5952c1"));
            gp_text1.setTextColor(Color.parseColor("#5952c1"));//(appDetails.getTheme_color()));
           // imgpublic1.setColorFilter(Color.parseColor(appDetails.getTheme_color()));
            gp_text.setTextColor(Color.parseColor("#203142"));
         //   imgpublic.setColorFilter(Color.parseColor("#203142"));
            //  private_privacy.setBackground(Util.setrounded(Color.parseColor(appDetails.getTheme_color())));
           // public_privacy.setBackground(Util.setrounded(Color.WHITE));
            GradientDrawable drawable1 = (GradientDrawable) public_privacy.getBackground();

            drawable1.setStroke (3, Color.DKGRAY);
           // privacytext.setText("Only member can see who's in the challenge");
        }

        if (view.getId() == R.id.button6) {
            Intent i=new Intent(ChallengeGoal.this,ChallengeActivity.class);
            startActivity(i);
            finish();

        }
        if (view.getId() == R.id.backpress) {
            Intent i=new Intent(ChallengeGoal.this,ChallengeActivity.class);
            startActivity(i);
            finish();

        }
        if(view.getId()==R.id.next){
            Intent i=new Intent(ChallengeGoal.this,CreateChallengeActivity.class);
            //i.putExtra("type",type);
            startActivity(i);
            //finish();
        }

    /*@Override
    public void onClick(View view) {
        if (user_check.isChecked()) {
            user_check1.setChecked(false);

            groupprivacy="public";
            mode=0;
        }
        if(user_check1.isChecked())
        {
            groupprivacy="private";
            user_check.setChecked(false);
            mode=1;
        }
        if(view.getId()==R.id.btn_savenfinish) {
            if (c_name.getText().length() == 0) {
                Toast.makeText(getApplicationContext(), "Please Enter Your Challenge Name", Toast.LENGTH_LONG).show();
            } else {
                if(mode==0) {
                    Intent in = new Intent(ChallengeDetailsActivity.this, ConfirmationChallengeActivity.class);
                    in.putExtra("cname",c_name.getText().toString());
                    startActivity(in);
                }else{
                    Intent i=new Intent(ChallengeDetailsActivity.this,PrivateChallenge.class);
                    i.putExtra("cname",c_name.getText().toString());
                    i.putExtra("challenge_id","122");
                    startActivity(i);
                }
            }
        }
        }*/

    }
}