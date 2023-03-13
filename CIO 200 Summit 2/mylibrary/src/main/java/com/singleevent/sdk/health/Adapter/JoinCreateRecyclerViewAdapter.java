package com.singleevent.sdk.health.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;


import com.google.gson.Gson;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.View.LeftActivity.AgendaDetails;
import com.singleevent.sdk.View.LeftActivity.SurveyRoot;
import com.singleevent.sdk.health.Activity.ChallengeActivity;
import com.singleevent.sdk.health.Activity.ChallengeDetailsActivity;
import com.singleevent.sdk.health.Activity.JoinChallenge;
import com.singleevent.sdk.health.Activity.JoinChallengeDetailsActivity;
import com.singleevent.sdk.R;
import com.singleevent.sdk.health.Activity.SeperateChallenge;
import com.singleevent.sdk.health.Fragment.ActiveFragment;
import com.singleevent.sdk.health.Model.ChallengeList;
import com.singleevent.sdk.health.Model.GetChallengeList;
import com.singleevent.sdk.health.Model.Joinchallenge;
import com.singleevent.sdk.health.Model.Updatesteps;
import com.singleevent.sdk.model.AppDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import io.paperdb.Paper;

public class JoinCreateRecyclerViewAdapter extends RecyclerView.Adapter<JoinCreateRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";


    private ArrayList<String>  mDate = new ArrayList<>();
    private ArrayList<String> mChallengename = new ArrayList<>();
    private ArrayList<String> mChallengenameId = new ArrayList<>();
    private ArrayList<String> mParticipants = new ArrayList<>();
    private ArrayList<Integer> join = new ArrayList<>();
    AppDetails appDetails;
    ArrayList<Joinchallenge> joinchallenges=new ArrayList<>();
    ArrayList<GetChallengeList> getChallengeLists=new ArrayList<>();





    private Context mContext;

    public JoinCreateRecyclerViewAdapter(ArrayList<String> mDate, ArrayList<String> mChallengename, ArrayList<String> mParticipants,ArrayList<Integer> join,ArrayList<String> mChallengenameId, Context mContext) {
        this.mDate = mDate;
        this.mChallengename = mChallengename;
        this.mParticipants = mParticipants;
        this.mContext = mContext;
        this.join=join;
        this.mChallengenameId=mChallengenameId;

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public JoinCreateRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_join_create_layout, parent, false);
        JoinCreateRecyclerViewAdapter.ViewHolder holder = new JoinCreateRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(JoinCreateRecyclerViewAdapter.ViewHolder holder, final int position) {



        holder.txtDate.setText(mDate.get(position));
        holder.txtChallengeName.setText(mChallengename.get(position));
        holder.txtParticipants.setText(mParticipants.get(position));
        if(join.get(position)==1){
            holder.btnCreateChallege.setText(" View Challenge ");
        }
        else{
            holder.btnCreateChallege.setText("+Join");
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent in = new Intent(mContext, JoinChallengeDetailsActivity.class);
              //  mContext.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        Button btnCreateChallege;

        RelativeLayout parentLayout;
        TextView  txtDate,txtChallengeName,txtParticipants;
        public ViewHolder(View itemView) {
            super(itemView);
           txtDate = itemView.findViewById(R.id.join_create_txtDate);
           txtChallengeName = itemView.findViewById(R.id.join_create_txt_challenge_name);
           txtParticipants = itemView.findViewById(R.id.join_create_txtParticipants);
            parentLayout = itemView.findViewById(R.id.parentlayout_join_create);
            btnCreateChallege = itemView.findViewById(R.id.joinbtn_create_challenge);
            btnCreateChallege.setBackground(Util.setrounded(Color.parseColor("#ff8c80f8")));
            getChallengeLists=Paper.book().read("challengelist",null);

            btnCreateChallege.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnCreateChallege.getText().toString().equalsIgnoreCase(" View Challenge ")) {

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("ChallengeList", getChallengeLists);
                        Intent i = new Intent(mContext, SeperateChallenge.class);
                        i.putExtras(bundle);
                        i.putExtra("challenge_id_pos", getAdapterPosition());
                        mContext.startActivity(i);

//                        Intent i=new Intent(mContext,SeperateChallenge.class);
//
//                        i.putExtra("challenge_id_pos",getAdapterPosition());
//                        i.putExtra("ChallengeList",getChallengeLists);
                      //  mContext.startActivity(i);
                    }
                    else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder.setTitle(mChallengename.get(getAdapterPosition()));
                        alertDialogBuilder.setMessage("Are you sure, You want to join this challenge");

                        alertDialogBuilder.setPositiveButton("JOIN",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        join(getAdapterPosition());
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
                }



        });
    }

        }

        //////////////////////////
//        private void joinChallengeAPI(int position) {
//
//            String dateTime = String.valueOf(System.currentTimeMillis());
//            final ProgressDialog dialog = new ProgressDialog(mContext, R.style.MyAlertDialogStyle);
//            dialog.setMessage("Joining challenge");
//            dialog.show();
//            String tag_string_req = "joinchallenge";
//            String url = "https://api1.webmobi.com/health/joinchallenge";
//            StringRequest strReq = new StringRequest(Request.Method.POST,
//                    url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Toast.makeText(mContext,response,Toast.LENGTH_LONG).show();
//                    dialog.dismiss();
//                    try {
//                        System.out.println(response);
//                        JSONObject jObj = new JSONObject(response);
//                        if (jObj.getBoolean("response")) {
//
//                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
//                            alertDialogBuilder.setTitle(mChallengename.get(position));
//                            alertDialogBuilder.setPositiveButton("Go to Dashboard",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface arg0, int arg1) {
//
//                                            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(mContext);
//                                            mAlertDialogBuilder.setIcon(R.drawable.ic_check_circle_black_24dp);
//                                            mAlertDialogBuilder.setTitle("Sucessfully joined challenge!");
//                                            JoinChallenge joinChallenge=new JoinChallenge();
//                                            mAlertDialogBuilder.setMessage("This challenge will now appear in your dashboard.");
//                                            mAlertDialogBuilder.setNegativeButton("Go to Dashboard",new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    Intent in = new Intent(mContext, SeperateChallenge.class);
//                                                    mContext.startActivity(in);
//
//                                                }
//                                            });
//
//                                            AlertDialog mAlertDialog = mAlertDialogBuilder.create();
//                                            mAlertDialog.show();
//                                            Toast.makeText(mContext,"You clicked join button",Toast.LENGTH_LONG).show();
//                                        }
//                                    });
//
//                            alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Toast.makeText(mContext,"You clicked no button",Toast.LENGTH_LONG).show();
//
//                                }
//                            });
//
//                            AlertDialog alertDialog = alertDialogBuilder.create();
//                            alertDialog.show();
//                        } else {
//
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    dialog.dismiss();
//                    if (error instanceof TimeoutError) {
//                     //   Error_Dialog.show("Timeout", mContext);
//                        Toast.makeText(mContext,"Timeout",Toast.LENGTH_LONG).show();
//                    } else if (VolleyErrorLis.isServerProblem(error)) {
//
//                        Toast.makeText(mContext,error.toString(),Toast.LENGTH_LONG).show();
//                    } else if (VolleyErrorLis.isNetworkProblem(error)) {
//
//                        Toast.makeText(mContext,"Please Check Your Internet Connection",Toast.LENGTH_LONG).show();
//                    }
//
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("app_id", appDetails.getAppId());
//                    params.put("challenge_id","ege");
//                    params.put("user_id", Paper.book().read("userId", ""));
//                    return params;
//                }
//
//
//            };
//
//
//            strReq.setShouldCache(false);
//            App.getInstance().addToRequestQueue(strReq, tag_string_req);
//            strReq.setRetryPolicy(new DefaultRetryPolicy(
//                    500000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//            ));
//
//        }
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
//                                    Intent i=new Intent(mContext,SeperateChallenge.class);
//                                    i.putExtra("challenge_id_pos",pos);
//                                    i.putExtra("ChallengeList",getChallengeLists);
//                                    mContext.startActivity(i);

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("ChallengeList", getChallengeLists);
                                    Intent i = new Intent(mContext, SeperateChallenge.class);
                                    i.putExtras(bundle);
                                    i.putExtra("challenge_id_pos", pos);
                                    mContext.startActivity(i);

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
                params.put("challenge_id",mChallengenameId.get(pos));
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
