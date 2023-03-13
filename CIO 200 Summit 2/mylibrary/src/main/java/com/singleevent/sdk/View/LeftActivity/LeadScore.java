package com.singleevent.sdk.View.LeftActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.Custom_View.Letter.Roundeddrawable;
import com.singleevent.sdk.Left_Adapter.Lead_Score_Adapter;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.LeaderBoard.LeaderBoard;
import com.singleevent.sdk.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import io.paperdb.Paper;

public class LeadScore extends AppCompatActivity implements View.OnClickListener,Lead_Score_Adapter.OnCardClickListner {

    AppDetails appDetails;
    RecyclerView scorelist;
    static ArrayList<LeaderBoard> leaderBoard;
    Lead_Score_Adapter lead_score_adapter;
    ArrayList<LeaderBoard> al1;
    TextView lname,ltotalscore;
    ImageView lprofile;
    Toolbar toolbar;
    String cat;
    HashMap<String,String> points;
    static LetterTileProvider tileProvider;
    int pos;
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Paper.init(this);
        appDetails = Paper.book().read("Appdetails");
        setContentView(R.layout.leaddetail);
        scorelist=(RecyclerView)findViewById(R.id.scorelist);
        lname=(TextView)findViewById(R.id.lname);
        ltotalscore=(TextView)findViewById(R.id.ltotalscore);
        lprofile=(ImageView)findViewById(R.id.lprofile);
        al1=new ArrayList<>();
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        toolbar.setOnClickListener(this);
        points=new HashMap<>();
        int total_points,session_checkin_points,
                ask_points,survey_points,polling_points,moodometer_points,login_points,profile_points,other_points
                ,lead_points,comment_points,like_points,post_points;

        if (getIntent().getExtras() == null)
        {}
        else{
            try{
                pos=getIntent().getExtras().getInt("position");
                cat=getIntent().getExtras().getString("cat");

            }catch (Exception e)
            {}}
        String s=cat+"templead";
        al1=Paper.book().read(s);

        try {
            lname.setText(al1.get(pos).getFirst_name()+" "+al1.get(pos).getLast_name());
            int totsc=al1.get(pos).getTotal_points();
            String totpoint=String.valueOf(totsc);
            ltotalscore.setText(totpoint);
            Random r;
            r = new Random();
            DisplayMetrics displayMetrics;
            int color= Color.argb(255, r.nextInt(256), r.nextInt(256), r.nextInt(256));
            final float dpWidth; displayMetrics = getApplication().getResources().getDisplayMetrics();
            dpWidth = displayMetrics.widthPixels * 0.15F;
            RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams) lprofile.getLayoutParams();
            imgParams.width = (int) dpWidth;
            imgParams.height = (int) dpWidth;
          /*  Bitmap letterTile = tileProvider.getLetterTile(user.getName().trim(), "key", (int) dpWidth, (int) dpWidth,color);
            profilepic.setImageDrawable(new Roundeddrawable(letterTile));
*/


            if (URLUtil.isValidUrl(al1.get(pos).getProfile_pic()))
                Glide.with(getApplication().getApplicationContext())

                        .load(al1.get(pos).getProfile_pic())
                        .asBitmap()
                        .placeholder(R.drawable.round_user)
                        .error(R.drawable.round_user)
                        .into(new BitmapImageViewTarget(lprofile) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getApplication().getResources(),
                                        Bitmap.createScaledBitmap(resource, (int) dpWidth, (int) dpWidth, false));
                                drawable.setCircular(true);
                                lprofile.setImageDrawable(drawable);
                            }
                        });
            else{
                Bitmap letterTile = tileProvider.getLetterTile(al1.get(pos).getFirst_name().trim(), "key", (int) dpWidth, (int) dpWidth, color);
                lprofile.setImageDrawable(new Roundeddrawable(letterTile));
            }
            for(int i=0; i<12; i++){
                if(i==0)
                {

                    points.put("Likes",String.valueOf(al1.get(pos).getLike_points()));


                }
                else if(i==1)
                {
                    points.put("SessionCheckin",String.valueOf(al1.get(pos).getSession_checkin_points()));



                }
                else if(i==2)
                {
                    points.put("Moodometer",String.valueOf(al1.get(pos).getMoodometer_points()));


                }
                else if(i==3)
                {
                    points.put("LogIn",String.valueOf(al1.get(pos).getLogin_points()));


                }
                else if(i==4)
                {
                    points.put("Profile",String.valueOf(al1.get(pos).getProfile_points()));


                }
                else if(i==5)

                {
                    points.put("Post",String.valueOf(al1.get(pos).getPost_points()));


                }
                else    if(i==6)
                {
                    points.put("AskQuestion",String.valueOf(al1.get(pos).getAsk_points()));


                }
                else if(i==7)
                {
                    points.put("Survey",String.valueOf(al1.get(pos).getSurvey_points()));

                }
                else if(i==8)
                {
                    points.put("Lead",String.valueOf(al1.get(pos).getLead_points()));



                }
                else   if(i==9)
                {
                    points.put("Comments",String.valueOf(al1.get(pos).getComment_points()));


                }
                else if(i==10)
                {
                    points.put("Polling",String.valueOf(al1.get(pos).getPolling_points()));

                }
                else if(i==11)

                {
                    points.put("Others",String.valueOf(al1.get(pos).getOther_points()));



                }



            }
            lead_score_adapter = new Lead_Score_Adapter(this, points,pos);
            scorelist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                    false));

            scorelist.setAdapter(lead_score_adapter);
            lead_score_adapter.setOnCardClickListner(this);
        }catch(Exception e){}


    }

    @Override
    public void OnItemLongClicked(View view, HashMap<String,String> user, int position) {

    }

    @Override
    public void OnItemClick(View view, HashMap<String,String> user, int position) {
        Intent i;
        //i = new Intent(LikeActivity.this, GroupFeedView.class);
        //   i.putExtra("group_id", user.getGroup_id());
        //  i.putExtra("group_name", user.getGroup_name());
        //   startActivity(i);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.backward) {
            onBackPressed();
        }
         if(view.getId()==R.id.toolbar){
            onBackPressed();
    }



    }
}
