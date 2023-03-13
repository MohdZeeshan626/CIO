package com.singleevent.sdk.Left_Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.singleevent.sdk.Custom_View.Letter.LetterTileProvider;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.RightActivity.Group_feed;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.HashMap;

import io.paperdb.Paper;

public class Lead_Score_Adapter extends RecyclerView.Adapter<Lead_Score_Adapter.ViewHolder> {

    private static Context context1;
    private HashMap<String,String> eventUserList;
    AppDetails appDetails;
    static LetterTileProvider tileProvider;
    public String userid;
    int pos;
    // Provide a suitable constructor (depends on the kind of dataset)
    public Lead_Score_Adapter(Context context, HashMap<String,String> hm, int pos) {
        eventUserList = hm;
        Lead_Score_Adapter.context1 = context;
        this.pos=pos;
        Paper.init(context);
//        userid = DataBaseStorage.decrypt(Paper.book().read("userId",""));
        userid = Paper.book().read("userId", "");
        appDetails = Paper.book().read("Appdetails",null);
    }

    //after search update list
    public void updateList(HashMap<String,String> list){

        eventUserList = list;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Lead_Score_Adapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(context1)
                .inflate(R.layout.leadscorelist, parent, false);

        return new Lead_Score_Adapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final Lead_Score_Adapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        final HashMap<String,String> user = eventUserList;
     //   Paper.book().write(UserList)

        // - replace the contents of the view with that element
        holder.setItem(user,position);

        //new logic
      /*  if (position==0){
            holder.alpha_header.setVisibility(View.VISIBLE);
            holder.alpha_header.setText(user.getGroup_name().substring(0,1));
        }else if (!user.getGroup_name().substring(0,1).equalsIgnoreCase(eventUserList.get(position-1).getGroup_name().substring(0,1))){
            holder.alpha_header.setVisibility(View.VISIBLE);
            holder.alpha_header.setText(user.getGroup_name().substring(0,1));
        }else {
            holder.alpha_header.setVisibility(View.GONE);

        }*/
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(eventUserList.size()>0)
        return 12;
        else return 0;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        Context context;
        int count;
        HashMap<String ,String> user;
        int pos;
        TextView authorName,alpha_header,points,book_title;
       int total_points,session_checkin_points,
                ask_points,survey_points,polling_points,moodometer_points,login_points,profile_points,other_points
                ,lead_points,comment_points,like_points,post_points;
        // Button invite;
        // TextView tvDesignation;

        Button confirm,cancel;

        AppDetails appDetails;
        static Lead_Score_Adapter.OnCardClickListner onCardClickListner;
        SwipeHorizontalMenuLayout sml;


        ViewHolder(View v) {
            super(v);
            appDetails = Paper.book().read("Appdetails");


            //  alpha_header = (TextView)v.findViewById(R.id.alpha_header);
            authorName = (TextView) v.findViewById(R.id.book_title);
            points=(TextView)v.findViewById(R.id.points);


            sml = (SwipeHorizontalMenuLayout) v.findViewById(R.id.sml);

            confirm=(Button)v.findViewById(R.id.confirm);
            cancel=(Button)v.findViewById(R.id.cancel);
            // invite.setOnClickListener(this);



            v.setOnClickListener(this);
            sml.setOnClickListener(this);
            //confirm.setOnClickListener(this);
           // cancel.setOnClickListener(this);

        }

        void setItem(HashMap<String,String> user,int pos) {
            tileProvider = new LetterTileProvider(context1);
            this.user = user;

            if(pos==0)
            {
                points.setText(user.get("Likes")+" pts");
                authorName.setText("Likes");


            }
            else if(pos==1)
            {   points.setText(user.get("SessionCheckin")+" pts");
                authorName.setText("Session Checkin");



            }
            else if(pos==2)
            {
                points.setText(user.get("Moodometer")+" pts");
                authorName.setText("Moodometer");

            }
            else if(pos==3)
            {points.setText(user.get("LogIn")+" pts");
                authorName.setText("LogIn");

            }
            else if(pos==4)
            {
                points.setText(user.get("Profile")+" pts");
                authorName.setText("Profile");

            }
            else if(pos==5)

            {
                points.setText(user.get("Post")+" pts");
                authorName.setText("Post");

            }
            else    if(pos==6)
            {
                points.setText(user.get("AskQuestion")+" pts");
                authorName.setText("Ask Question");

            }
            else if(pos==7)
            {
                points.setText(user.get("Survey")+" pts");

                authorName.setText("Survey");

            }
            else if(pos==8)
            {
                points.setText(user.get("Lead")+" pts");
                authorName.setText("Lead");

            }
            else   if(pos==9)
            {
                points.setText(user.get("Comments")+" pts");
                authorName.setText("Comments");

            }
            else if(pos==10)
            {points.setText(user.get("Polling")+" pts");
                authorName.setText("Polling ");

            }
            else if(pos==11)

            {
                points.setText(user.get("Others")+" pts");
                authorName.setText("Others");

            }



          // setPoints();



        }


        public  void setPoints(){
            for(int i=count; i<12; i++){
                if(i==0)
                {
                    count=0;
                    points.setText((String.valueOf(like_points)));
                    authorName.setText("Likes");


                }
               else if(i==1)
                {  count=1;
                    authorName.setText("Session Checkin");
                    points.setText(String.valueOf(session_checkin_points));


                }
               else if(i==2)
                {  count=2;
                    points.setText((String.valueOf(moodometer_points)));
                    authorName.setText("Moodometer");

                }
               else if(i==3)
                {count=3;
                    points.setText((String.valueOf(login_points)));

                     authorName.setText("LogIn");

                                }
               else if(i==4)
                {
                    count=4;
                    points.setText((String.valueOf(profile_points)));
                    authorName.setText("Profile");
                    break;
                }
               else if(i==5)

                {
                    count=5;
                    points.setText((String.valueOf(post_points)));
                    authorName.setText("Post");
                    break;
                }
             else    if(i==6)
                {
                    count=6;
                    points.setText((String.valueOf(ask_points)));
                    authorName.setText("Ask Question");
                    break;
                }
                else if(i==7)
                {
                    count=7;
                    points.setText((String.valueOf(survey_points)));

                    authorName.setText("Survey");
                    break;
                }
               else if(i==8)
                {
                    count=8;
                    points.setText((String.valueOf(lead_points)));
                    authorName.setText("Lead");
                    break;
                }
              else   if(i==9)
                {
                    count=9;
                    points.setText((String.valueOf(comment_points)));
                    authorName.setText("Comments");
                    break;
                }
               else if(i==10)
                {count=10;
                    points.setText((String.valueOf(polling_points)));
                    authorName.setText("Polling ");
                    break;
                }
               else if(i==11)

                {
                    count=11;
                    points.setText((String.valueOf(other_points)));
                    authorName.setText("Others");
                    break;
                }



            }
        }
        @Override
        public void onClick(View v) {
            System.out.println("Item clicked pos " + getAdapterPosition());
            Group_feed gf=new Group_feed();
            int position = getAdapterPosition();


            //



        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            onCardClickListner.OnItemLongClicked(v, user, position);
            return true;
        }
    }

    public void setOnCardClickListner(Lead_Score_Adapter.OnCardClickListner onCardClickListner) {
        Lead_Score_Adapter.ViewHolder.onCardClickListner = onCardClickListner;
    }

    public interface OnCardClickListner {
        void OnItemLongClicked(View view, HashMap<String,String> user, int position);
        void OnItemClick(View view, HashMap<String,String> user, int position);
    }
}

