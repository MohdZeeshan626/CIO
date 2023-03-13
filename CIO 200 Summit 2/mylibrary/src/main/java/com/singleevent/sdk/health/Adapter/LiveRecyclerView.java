package com.singleevent.sdk.health.Adapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.R;
import com.singleevent.sdk.health.Activity.ChallengeGoal;
import com.singleevent.sdk.health.Activity.JoinChallenge;
import com.singleevent.sdk.health.Activity.SeperateChallenge;
import com.singleevent.sdk.health.Model.GetChallengeList;
import com.singleevent.sdk.health.Model.Joinchallenge;
import com.singleevent.sdk.model.AppDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;
public class LiveRecyclerView extends RecyclerView.Adapter<LiveRecyclerView.ViewHolder> {


        private static String TAG = "Steps History";
        private static String TAG1 = "Active History";

        ScrollingPagerIndicator recyclerIndicator;
        RecyclerView recyclerView;
    Drawable image;


        //vars
        private ArrayList<String> cname = new ArrayList<>();
        private  ArrayList<String> cbycreated = new ArrayList<>();
        private  ArrayList<String> cstartdate = new ArrayList<>();
        private  ArrayList<String> cenddate = new ArrayList<>();
        private  ArrayList<String> totalparticipants = new ArrayList<>();
        private  ArrayList<String> backimg = new ArrayList<>();
        private Context mContext;
        int mode;
        ArrayList<GetChallengeList> challengeLists=new ArrayList<>();
    AppDetails appDetails;
    ArrayList<Joinchallenge> joinchallenges=new ArrayList<>();


    public LiveRecyclerView(ArrayList<String> cname, ArrayList<String> cbycreated, ArrayList<String> cstartdate,ArrayList<String> cenddate,
                               ArrayList<String> totalparticipants,ArrayList<String> backimg,Context mContext) {
            this.cname = cname;
            this.cbycreated = cbycreated;
            this.cstartdate = cstartdate;
            this.cenddate = cenddate;
            this.totalparticipants = totalparticipants;
            this.backimg = backimg;
            this.mContext = mContext;
        }
        public LiveRecyclerView(ArrayList<GetChallengeList> challengeLists,Context mContext,int mode) {
            this.challengeLists=challengeLists;
            this.mContext=mContext;
            this.mode=mode;
        }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if ( holder != null )
        if(challengeLists.size()>0 && challengeLists!=null) {
            {
                String[] values;
                List<String> list = new ArrayList<>();

                //manipulate the attached view
                if (challengeLists.get(position).getJoinedChallenge() != null) {
                    values = challengeLists.get(position).getJoinedChallenge().split(",");
                    list = new ArrayList<String>(Arrays.asList(values));

                }
                if (list != null) {
                    if(mode==0 || mode==1) {
                        if (list.contains(Paper.book().read("userId", ""))) {
                            holder.viewchallenge.setText(" View Challenge ");
                        } else {
                            holder.viewchallenge.setText(" +Join ");
                        }
                    }else{
                        if (list.contains(Paper.book().read("userId", ""))) {
                            holder.viewchallenge.setText(" View final scores ");
                        }
                        else{
                            holder.viewchallenge.setText(" +Join ");
                        }
                    }
                }

                // drawables.putAll(Paper.book().read("Challengeimage"));
                holder.c_name.setText(challengeLists.get(position).getTitle());
                holder.cnam.setText(challengeLists.get(position).getDescription());
                holder.leaderboard_txtname1.setText("Created by : " + challengeLists.get(position).getCreatedBy());
                if (challengeLists.get(position).getImageUrl() != null && challengeLists.get(position).getImageUrl().startsWith("https://")) {
                    try {
                        Glide.with(mContext).load(challengeLists.get(position).getImageUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                Drawable drawable = new BitmapDrawable(mContext.getResources(), resource);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                   // holder.r1.setBackground(drawable);
                                    holder.mainimage.setBackground(drawable);
                                }
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else {
                    Glide.with(mContext).load(R.drawable.rounded_bg_white).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Drawable drawable = new BitmapDrawable(mContext.getResources(), resource);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                               //
                                holder.mainimage.setBackground(drawable);
                            }
                        }
                    });
                }


                //if (challengeLists.get(position).getJoinedChallenge() != null) {
                    int n = challengeLists.get(position).getTotaljoineduser();//challengeLists.get(position).getJoinedChallenge().split(",").length;
                    holder.leaderboard_txtname.setText(String.valueOf(n) + " " + "Joined");
               /* } else {
                    holder.leaderboard_txtname.setText(0 + " " + "Joined");
                }*/

                try {
                    holder.txt_upcoming_Challenge_dates.setText(gettime(challengeLists.get(position).getStartDate()) + "-" + gettime(challengeLists.get(position).getEndDate()));
                } catch (Exception e) {
                }
                holder.viewchallenge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (holder.viewchallenge.getText().toString().equalsIgnoreCase(" View Challenge ")) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ChallengeList", challengeLists);
                            Intent i = new Intent(mContext, SeperateChallenge.class);
                            i.putExtras(bundle);
                            i.putExtra("challenge_id_pos", holder.getAdapterPosition());
                            //i.putExtra("ChallengeList",challengeLists);
                            mContext.startActivity(i);
                        }
                        else if(holder.viewchallenge.getText().toString().equalsIgnoreCase(" View final scores ")){
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ChallengeList", challengeLists);
                            Intent i = new Intent(mContext, SeperateChallenge.class);
                            i.putExtras(bundle);
                            i.putExtra("challenge_id_pos", holder.getAdapterPosition());
                            mContext.startActivity(i);
                        }
                        else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                            alertDialogBuilder.setTitle(challengeLists.get(holder.getAdapterPosition()).getTitle());
                            alertDialogBuilder.setMessage("Are you sure, You want to join this challenge");

                            alertDialogBuilder.setPositiveButton("JOIN",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            join(holder.getAdapterPosition());
                                            // joinChallengeAPI(getAdapterPosition());
                                            //  Intent in = new Intent(mContext, JoinChallenge.class);
                                            // in.putExtra("position",getAdapterPosition());
                                            //  mContext.startActivity(in);

                                        }
                                    });

                            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //   Toast.makeText(mContext, "You clicked no button", Toast.LENGTH_LONG).show();

                                }
                            });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
//                    Button pbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//                    pbutton.setBackgroundColor(Color.YELLOW);

//                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(mContext);
//                    materialAlertDialogBuilder.setTitle(mChallengename.get(getAdapterPosition()));
//                    materialAlertDialogBuilder.setMessage("Are you sure you wantto join this challenge?");
//                    materialAlertDialogBuilder.show();


                        }

               /* Intent in = new Intent(mContext, ChallengeGoal.class);
                mContext.startActivity(in);*/
                        //Intent in = new Intent(mContext, JoinChallengeDetailsActivity.class);
                        // mContext.startActivity(in);
                    }
                });
            }
        }else{
            holder.maincard.setVisibility(View.GONE);
            holder.noitem.setVisibility(View.VISIBLE);
        }
        else //view is either non-existant or detached waiting to be reattached
            notifyItemChanged( position );
    }

    public  Drawable getBitmapFromURL(String src,int position) {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(challengeLists.get(position).getImageUrl());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image  = new BitmapDrawable(mContext.getResources(), bitmap);

            }
        });
        thread.start();
        return image;
    }

    @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.runningview, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }




        public class ViewHolder extends RecyclerView.ViewHolder{

            Button btnCreateChallege;

            RelativeLayout parentLayout;
            TextView c_name,leaderboard_txtname1,leaderboard_txtname,txt_upcoming_Challenge_dates,cnam,noitem;
            RelativeLayout r1;
            LinearLayout maincard;
            Button viewchallenge;
            ImageView leaderboard_image1,leaderboard_image,mainimage;
            public ViewHolder(View itemView) {
                super(itemView);
                c_name = itemView.findViewById(R.id.cname);
                cnam = itemView.findViewById(R.id.cnam);
                leaderboard_txtname1 = itemView.findViewById(R.id.leaderboard_txtname1);
                leaderboard_txtname = itemView.findViewById(R.id.leaderboard_txtname);
                txt_upcoming_Challenge_dates =itemView.findViewById(R.id.txt_upcoming_Challenge_dates);
                mainimage=itemView.findViewById(R.id.mainimage);
                r1 = itemView.findViewById(R.id.r1);
                leaderboard_image1=itemView.findViewById(R.id.leaderboard_image1);
                leaderboard_image=itemView.findViewById(R.id.leaderboard_image);
                viewchallenge = itemView.findViewById(R.id.viewchallenge);
                noitem=itemView.findViewById(R.id.noitem);
                maincard=itemView.findViewById(R.id.maincard);
                viewchallenge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }



                });
            }

        }
    public String gettime(long mill){
        String dateString;

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(mill);
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String days = myFormat.format(cal.getTime());
        int  year = cal.get(Calendar.YEAR);
        int   month = cal.get(Calendar.MONTH);
        int  day = cal.get(Calendar.DAY_OF_MONTH);
        String mo=getmonth(month);
        String temp=mo+" "+day;
        return temp;
    }
    public String getmonth(int n){
        ArrayList<String> monthArray=new ArrayList<>();
        monthArray.add("Jan");
        monthArray.add("Feb");
        monthArray.add("Mar");
        monthArray.add("Apr");
        monthArray.add("May");
        monthArray.add("Jun");
        monthArray.add("Jul");
        monthArray.add("Aug");
        monthArray.add("Sep");
        monthArray.add("Oct");
        monthArray.add("Nov");
        monthArray.add("Dec");
        return monthArray.get(n);
    }

    @Override
    public int getItemCount() {
        return challengeLists.size();
    }

    private void join(int pos) {
        appDetails = Paper.book().read("Appdetails");
        final ProgressDialog dialog = new ProgressDialog(mContext, R.style.MyAlertDialogStyle);
        dialog.setMessage("joining challenge ...");
        dialog.show();
        String tag_string_req = "Profile";
        String url ="https://api2.webmobi.com/health/joinchallenge";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    System.out.println(response);
                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Gson gson = new Gson();
                        try {

                            JSONObject object = jObj.getJSONObject("data");
                            joinchallenges.clear();
                            Joinchallenge user = gson.fromJson(object.toString(), Joinchallenge.class);
                            joinchallenges.add(user);

                            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(mContext);
                            mAlertDialogBuilder.setIcon(R.drawable.ic_check_circle_black_24dp);
                            mAlertDialogBuilder.setTitle("Sucessfully joined challenge!");
                            JoinChallenge joinChallenge=new JoinChallenge();
                            mAlertDialogBuilder.setMessage("This challenge will now appear in your dashboard.");
                            mAlertDialogBuilder.setNegativeButton("Go to Dashboard",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("ChallengeList", challengeLists);
                                    Intent i = new Intent(mContext, SeperateChallenge.class);
                                    i.putExtras(bundle);
                                    i.putExtra("challenge_id_pos", pos);
                                    mContext.startActivity(i);

//                                                                      Intent i=new Intent(mContext,SeperateChallenge.class);
//                                    i.putExtra("challenge_id_pos",pos);
//                                    i.putExtra("ChallengeList",challengeLists);
//                                    mContext.startActivity(i);

                                }
                            });
                            AlertDialog mAlertDialog = mAlertDialogBuilder.create();
                            mAlertDialog.show();
                            // Toast.makeText(mContext,"You clicked join button",Toast.LENGTH_LONG).show();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            //  Error_Dialog.show("Session Expired, Please Login ", ChallengeDetailsActivity.this);
                        } else{

                        }
                        //  Error_Dialog.show(jObj.getString("responseString"), ChallengeDetailsActivity.this);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                if (error instanceof TimeoutError) {
                    // Error_Dialog.show("Timeout", ChallengeDetailsActivity.this);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    //  Error_Dialog.show(VolleyErrorLis.handleServerError(error, ChallengeDetailsActivity.this), ChallengeDetailsActivity.this);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    //  Error_Dialog.show("Please Check Your Internet Connection", ChallengeDetailsActivity.this);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("app_id", appDetails.getAppId());
                params.put("challenge_id",challengeLists.get(pos).getChallenge_id());
                params.put("user_id", Paper.book().read("userId", ""));
                System.out.println(params);
                return params;
            }

        };


        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }

    }


