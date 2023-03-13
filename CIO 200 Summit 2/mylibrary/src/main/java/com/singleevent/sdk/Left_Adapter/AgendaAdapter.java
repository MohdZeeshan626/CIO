package com.singleevent.sdk.Left_Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.App;
import com.singleevent.sdk.Custom_View.Error_Dialog;
import com.singleevent.sdk.Custom_View.VolleyErrorLis;
import com.singleevent.sdk.model.Agenda.Agendadetails;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.AgendaDetails;
import com.singleevent.sdk.View.LeftActivity.AgoraClass;
import com.singleevent.sdk.View.LeftActivity.Streamlink;
import com.singleevent.sdk.zoommeet.main.java.us.zoom.sdksample.ui.InitAuthSDKActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cyd.awesome.material.AwesomeText;
import io.paperdb.Paper;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder> {
    static final int VIEW_TYPE_ENABLE = 0;
    static final int VIEW_TYPE_DISABLE = 1;
    private static Context context1;
    private List<Agendadetails> agendaDetailList;
    AppDetails appDetails;
    public String userid;
    public Agendadetails agendadetails;
    static HashMap<String, Integer> categorylist = new HashMap<>();
    static long clickeddate;
    static String filterby;
    static List<Integer> list;
    private static String token;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AgendaAdapter(Context context, List<Agendadetails> myDataset, HashMap<String,Integer> categorylist, long clickeddate, String filterby ) {
        this.agendaDetailList = myDataset;
        AgendaAdapter.context1 = context;
        Paper.init(context);
        this.clickeddate = clickeddate;
        this.categorylist.putAll(categorylist);
        this.token = token;
        this.filterby = filterby;

        userid =Paper.book().read("userId", "");
        appDetails = Paper.book().read("Appdetails", null);
        list = Paper.book(appDetails.getAppId()).read("SCH", new ArrayList<Integer>() {
        });

    }



    //after search update list
    public void updateList(List<Agendadetails> list) {

        agendaDetailList = list;
        this.list = Paper.book(appDetails.getAppId()).read("SCH",new ArrayList<Integer>() {
        });
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(context1)
                .inflate(R.layout.s_agenda_view, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        final Agendadetails detail = agendaDetailList.get(position);

        // - replace the contents of the view with that element
        //swiprefreshmenu
        holder.setItem(detail, position, agendaDetailList);


        holder.linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putLong("Date", clickeddate);
                args.putSerializable("Agendaview", detail);
                args.putSerializable("category_colorList",categorylist);
                Intent i = new Intent(context1, AgendaDetails.class);
                i.putExtras(args);
                i.setAction("com.agenda");
                context1.startActivity(i);


            }
        });



    }

    boolean swipeEnableByViewType(int viewType) {
        if (viewType == VIEW_TYPE_ENABLE)
            return true;
        else
            return viewType != VIEW_TYPE_DISABLE;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return agendaDetailList.size();
    }
    public void addtosch(String msg, final String mark) {

        // converting arraylist to jsonarray

        Gson gson = new GsonBuilder().create();
        final JsonArray myCustomArray = gson.toJsonTree(list).getAsJsonArray();


        final ProgressDialog dialog = new ProgressDialog((Activity)context1,R.style.MyAlertDialogStyle);
        dialog.setMessage(msg);
        dialog.show();
        String tag_string_req = "Schdule";
        String url = ApiList.Schdule;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {

                    JSONObject jObj = new JSONObject(response);


                    if (jObj.getBoolean("response")) {
                        Error_Dialog.show(jObj.getString("responseString"), (Activity)context1);
                        notifyDataSetChanged();
                    } else {
                        if (jObj.getString("responseString").equalsIgnoreCase("Invalid token")) {
                            Intent i = new Intent();
                            i.putExtra("keyMessage", "Session Expired, Please Login ");
                            i.putExtra("keyAlert", "Session Expired");
                            i.setClassName(context1.getPackageName(), context1.getPackageName()+".SessionExpired");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context1.startActivity(i);

                        } else
                            Error_Dialog.show(jObj.getString("responseString"), (Activity)context1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();

                Paper.book().write("Sync", true);
                if (error instanceof TimeoutError) {
                    Error_Dialog.show("Timeout", (Activity)context1);
                } else if (VolleyErrorLis.isServerProblem(error)) {
                    Error_Dialog.show(VolleyErrorLis.handleServerError(error, context1), (Activity)context1);
                } else if (VolleyErrorLis.isNetworkProblem(error)) {
                    Error_Dialog.show("Please Check Your Internet Connection", (Activity)context1);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", Paper.book().read("userId",""));
                params.put("action", mark);
                params.put("schedules", myCustomArray.toString());
                params.put("appid", appDetails.getAppId());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("token", token );
                return headers;
            }
        };


        strReq.setShouldCache(false);
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        Context context;
        AppDetails appDetails;
        static OnCardClickListner onCardClickListner;

        LinearLayout linearlayout;
        TextView fromtime;
        TextView totime ;
        AwesomeText dots,isch;
        ImageView broadcast,youtubevideo;
        String id,pwd;
        TextView title,theatrename;
        TextView venue;
        RelativeLayout loc;
        private Agendadetails agendadetails;
        boolean b;

        ViewHolder(View v) {
            super(v);
            appDetails = Paper.book().read("Appdetails");


            theatrename = (TextView) v.findViewById(R.id.theatrename);
            fromtime = (TextView) v.findViewById(R.id.fromtime);
            totime = (TextView) v.findViewById(R.id.totime);
            dots = (AwesomeText) v.findViewById(R.id.dots);
            isch = (AwesomeText) v.findViewById(R.id.isch);
            title = (TextView) v.findViewById(R.id.title);
            venue = (TextView) v.findViewById(R.id.venue);
            loc = (RelativeLayout) v.findViewById(R.id.loc);
            linearlayout = (LinearLayout) v.findViewById(R.id.linearlayout);
            broadcast=(ImageView)v.findViewById(R.id.broadcast);
            youtubevideo=(ImageView)v.findViewById(R.id.youtubevideo);
            v.setOnClickListener(this);
            broadcast.setOnClickListener(this);

            isch.setOnClickListener(this);
        }


        public void setItem(final Agendadetails agendadetails, int position, List<Agendadetails> userList) {

            this.agendadetails = agendadetails;


            String s[]=new String[20];
            s=appDetails.getDisable_items();

            if(s.length>0&& s!=null) {
                for (int i = 0; i < s.length; i++) {
                    if (!s[i].equalsIgnoreCase("agora")) {
                        broadcast.setImageResource(R.drawable.broadcast);
                        broadcast.setVisibility(View.VISIBLE);
                    }
                    else{
                        broadcast.setImageResource(R.drawable.broadcast);
                        broadcast.setVisibility(View.GONE);
                    }
                }
            }
            else{
                broadcast.setImageResource(R.drawable.broadcast);
                broadcast.setVisibility(View.VISIBLE);
            }
            if(!agendadetails.getStream_link().equalsIgnoreCase(""))
            {
                String st=agendadetails.getStream_link();
                b=st.contains("zoom");
                if(b) {
                    try {
                        youtubevideo.setImageResource(R.drawable.zm_ic_setting_zoom);
                        youtubevideo.setVisibility(View.VISIBLE);
                        broadcast.setVisibility(View.GONE);
                        youtubevideo.setOnClickListener(this);

                    }catch (Exception e)
                    {

                    }

                }
                else{
                    youtubevideo.setImageResource(R.drawable.video_pause);
                    youtubevideo.setVisibility(View.VISIBLE);
                    broadcast.setVisibility(View.GONE);
                    youtubevideo.setOnClickListener(this);
                }

            }
            else{
                youtubevideo.setVisibility(View.GONE);
            }

            if(s.length>0 && s!=null) {
                for(int i=0;i<s.length;i++) {
                    if (s[i].contains("sessionstream")) {
                        broadcast.setEnabled(false);
                        youtubevideo.setEnabled(false);
                        break;
                    }
                }
            }


            //new logic
            if (filterby.equals("time")) {
                if (position == 0) {
                    theatrename.setVisibility(View.VISIBLE);
                    theatrename.setText(converlontostring(agendadetails.getFromtime()));

                } else if (!String.valueOf(agendadetails.getFromtime()).
                        equalsIgnoreCase(String.valueOf(userList.get(position - 1).getFromtime()))) {

                    theatrename.setVisibility(View.VISIBLE);
                    theatrename.setText(converlontostring(agendadetails.getFromtime()));
                } else {
                    theatrename.setVisibility(View.GONE);

                }
            }else if (filterby.equals("category")){
                if (position == 0){

                    theatrename.setVisibility(View.VISIBLE);
                    theatrename.setText(agendadetails.getCategory());

                }else if (!String.valueOf(agendadetails.getCategory()).equalsIgnoreCase(
                        String.valueOf(userList.get(position - 1).getCategory()))){

                    theatrename.setVisibility(View.VISIBLE);
                    theatrename.setText(agendadetails.getCategory());
                }else {

                    theatrename.setVisibility(View.GONE);
                }

            }else {
                //coming updates by filter
            }

            fromtime.setText(converlontostring(agendadetails.getFromtime()));
            totime.setText(converlontostring(agendadetails.getTotime()));
            title.setText(agendadetails.getTopic());

            try{
                dots.setTextColor(categorylist.get(agendadetails.getCategory()));
            }catch (Exception e ){
                e.printStackTrace();
            }


            if (!agendadetails.getLocation().equalsIgnoreCase("")) {
                venue.setText(agendadetails.getLocation());
                loc.setVisibility(View.VISIBLE);
            } else {
                loc.setVisibility(View.GONE);
            }


            if (!agendadetails.getLocation().equalsIgnoreCase("")) {
                venue.setText(agendadetails.getLocation());
                loc.setVisibility(View.VISIBLE);
            } else {
                loc.setVisibility(View.GONE);
            }


            if (list.contains(agendadetails.getAgendaId())) {
                isch.setTextColor(Color.parseColor(appDetails.getTheme_color()));

            } else {
                isch.setTextColor(context1.getResources().getColor(R.color.light_gray));
            }


        }




        @Override
        public void onClick(View v) {
            System.out.println("Item clicked pos " + getAdapterPosition());

            int position = getAdapterPosition();
           /* if (v.getId()==isch.getId()){


            }*/
            if(v.getId()==broadcast.getId())
            {
              /* Intent i=new Intent(context1, CallActivity.class);
               context1.startActivity(i);*/
                Intent intent = null;
                try {
                   /* intent = new Intent(context1, AgoraClass.class);
                    intent.putExtra("agendaId",agendadetails.getAgendaId());
                    intent.putExtra("token",token);
                    context1.startActivity(intent);
*/
                    Bundle args = new Bundle();
                    args.putInt("agendaId", agendadetails.getAgendaId());
                    args.putString("token", token);
                    args.putSerializable("AgendaList",agendadetails);
                    System.out.println("A Details"+args);
                    Intent i = new Intent(context1, AgoraClass.class);
                    i.putExtras(args);
                    //i.setAction("com.agenda");
                    context1.startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if(v.getId()==youtubevideo.getId())
            {
              /* Intent i=new Intent(context1, CallActivity.class);
               context1.startActivity(i);*/
                Intent intent = null;
                try {

                    if(b) {
                        String st=agendadetails.getStream_link();
                        Pattern pattern = Pattern.compile(Pattern.quote("j/") + "(.*?)" + Pattern.quote("?"), Pattern.DOTALL);
                        Matcher matcher = pattern.matcher(st);
                        //  Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                        //  Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
                        while (matcher.find()) {
                            // System.out.println(matcher.group(1));
                            id = matcher.group(1);
                            // System.out.println(h);
                        }
                        if(id==null)
                        {
                            int index1=st.lastIndexOf('/');
                            String ssss=st.substring(index1,st.length());
                            id=st.substring(st.lastIndexOf("/") + 1);
                            System.out.println(ssss+ "ID is"+id);
                        }

                        pwd = st.substring(st.lastIndexOf("=") + 1);

                        Intent i = new Intent(context1, InitAuthSDKActivity.class);
                        i.putExtra("id",id);
                        i.putExtra("pwd",pwd);

                        AgendaAdapter.context1.startActivity(i);
                    }
                    else{
                        Bundle args = new Bundle();

                        args.putInt("agendaId",agendadetails.getAgendaId());
                        args.putString("token",token);
                        args.putString("stream_url",agendadetails.getStream_link());
                        args.putString("title",agendadetails.getTopic());
                        args.putSerializable("AgendaList",agendadetails);
                        intent = new Intent(AgendaAdapter.context1, Streamlink.class);
                        intent.putExtras(args);
                        AgendaAdapter.context1.startActivity(intent);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else
                onCardClickListner.OnItemClick(v,agendadetails, position);


        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            onCardClickListner.OnItemLongClicked(v, agendadetails, position);
            return true;
        }
        private String converlontostring(Long key) {

            SimpleDateFormat myFormat = new SimpleDateFormat("hh:mm a");
            myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            myFormat.format(key);
            return myFormat.format(key);
        }
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        ViewHolder.onCardClickListner = onCardClickListner;
    }

    public interface OnCardClickListner {
        void OnItemLongClicked(View view, Agendadetails user, int position);

        void OnItemClick(View view, Agendadetails user, int position);
    }
}
