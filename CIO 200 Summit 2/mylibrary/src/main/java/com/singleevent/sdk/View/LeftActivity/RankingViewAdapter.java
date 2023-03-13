package com.singleevent.sdk.View.LeftActivity;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.singleevent.sdk.model.LeaderBoard.LeaderBoard;
import com.singleevent.sdk.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class RankingViewAdapter extends RecyclerView.Adapter<RankingViewAdapter.ViewHolder> {

    private static final String TAG = "Testing" ;
    private ArrayList<String> rankimage = new ArrayList<>();
    private ArrayList<String> rank = new ArrayList<>();
    private ArrayList<String> rankname = new ArrayList<>();
    private ArrayList<String> rankscore = new ArrayList<>();
    private Context mContext;
    String temp;
    private ArrayList<LeaderBoard> leadarray=new ArrayList<>();
    ArrayList<LeaderBoard> lead1=new ArrayList<>();


    /*
        public RankingViewAdapter(ArrayList<String>rankimage,ArrayList<String> rank,ArrayList<String> rankname,ArrayList<String> rankscore,Context mContext) {
            this.rank = rank;
            this.rankimage=rankimage;
            this.rankname = rankname;
            this.rankscore = rankscore;
            this.mContext = mContext;

        }
    */
    public RankingViewAdapter(ArrayList<LeaderBoard> leadarray, Context mContext,String temp) {
        this.leadarray = leadarray;
       /* this.rank = rank;
        this.rankname = rankname;mp
        this.rankscore = rankscore;*/
        this.mContext = mContext;
        this.temp=temp;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ranks, parent, false);
//        ViewHolder cholder = new ViewHolder(view);
//        return cholder;
        if(viewType%2==0){
            View view = View.inflate(parent.getContext(), R.layout.demo_lead, null);
            ViewHolder cholder = new ViewHolder(view,viewType);
            return cholder;}
        else{
            View view = View.inflate(parent.getContext(), R.layout.demo_lead1, null);
            ViewHolder cholder = new ViewHolder(view,viewType);
            return cholder;
        }

    }

    /* @Override
     public int getItemViewType(int position) {
         return TimelineView.getTimeLineViewType(position, getItemCount());
     }*/
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

//        holder.imageName.setText(mImageNames.get(position));

        if (position<leadarray.size()-3) {

            if (position % 2 == 0) {
                holder.hash1.setText(String.valueOf("# "+ (4 + position)));
                holder.name3.setText(leadarray.get(3 + position).getFirst_name() + " " + leadarray.get(3 + position).getLast_name());
                holder.score3.setText(String.valueOf(leadarray.get(3 + position).getTotal_points()));


                if (!leadarray.get(3 + position).getProfile_pic().equalsIgnoreCase("")) {
                    Glide.with(mContext)

                            .load(leadarray.get(3 + position).getProfile_pic().equalsIgnoreCase("") ? R.drawable.round_user : leadarray.get(3 + position).getProfile_pic())
                            .asBitmap()
                            .placeholder(R.drawable.round_user)
                            .error(R.drawable.round_user)
                            .into(holder.image_view3);
                }

      /*   holder.hash1.setText(rank.get(position));
         holder.name3.setText(rankname.get(position));
         holder.score3.setText(rankscore.get(position));

         Glide.with(mContext)
                 .load(rankimage.get(position))
                 .asBitmap()
                 .into(holder.image_view3);*/

            } else {
                holder.hash2.setText(String.valueOf("# "+(4 + position)));
                holder.rname3.setText(leadarray.get(3 + position).getFirst_name() + " " + leadarray.get(3 + position).getLast_name());
                holder.rscore3.setText(String.valueOf(leadarray.get(3 + position).getTotal_points()));
                if (!leadarray.get(3 + position).getProfile_pic().equalsIgnoreCase("")) {
                    Glide.with(mContext)

                            .load(leadarray.get(3 + position).getProfile_pic().equalsIgnoreCase("") ? R.drawable.round_user : leadarray.get(3 + position).getProfile_pic())
                            .asBitmap()
                            .placeholder(R.drawable.round_user)
                            .error(R.drawable.round_user)
                            .into(holder.rimage_view3);
                }


         /*if (!leadarray.get(position).getProfile_pic().equalsIgnoreCase("")){
             Glide.with(context.getApplicationContext())
                     .load(values.get(position).getProfile_pic().equalsIgnoreCase("") ? R.drawable.round_user : values.get(position).getProfile_pic())
                     .asBitmap()
                     .placeholder(R.drawable.round_user)
                     .error(R.drawable.round_user)
                     .into(new BitmapImageViewTarget(holder.iv_profile_pic)*/

        /* holder.hash2.setText(rank.get(position));
         holder.rname3.setText(rankname.get(position));
         holder.rscore3.setText(rankscore.get(position));
         Glide.with(mContext)
                 .load(rankimage.get(position))
                 .asBitmap()
                 .into(holder.rimage_view3);*/


            }
        }

    }
    @Override
    public int getItemCount() {
        // lead1=Paper.book().read("templead");
        if(leadarray.size()>0)
            return leadarray.size()-3;
        else
            return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView name3,score3,rname3,rscore3,hash1,hash2;
        CircleImageView rimage_view3,image_view3;
        LeaderBoard user;
        RelativeLayout rootdemo,leftlead,rightlead;

         RankingViewAdapter.OnCardClickListner onCardClickListner;


        public ViewHolder(View itemView,int viewType) {
            super(itemView);

            if(viewType%2==0){
                image_view3 = itemView.findViewById(R.id.image_view3);
                score3 = itemView.findViewById(R.id.score3);
                hash1 = itemView.findViewById(R.id.hash1);
                name3 = itemView.findViewById(R.id.name3);
              image_view3.setOnClickListener(this);
            }
            else{
                rimage_view3 = itemView.findViewById(R.id.rimage_view3);
                rscore3=itemView.findViewById(R.id.rscore3);
                hash2=itemView.findViewById(R.id.hash2);
                rname3=itemView.findViewById(R.id.rname3);
            rimage_view3.setOnClickListener(this);
            }

        }


        @Override
        public void onClick(View v) {
            System.out.println("Item clicked pos " + getAdapterPosition());

            int position = getAdapterPosition();
            int newpos=position+3;
           /* if (v.getId()==authorName.getId()){

            }*/
            if(leadarray.get(3 + position).getUserid().equalsIgnoreCase(Paper.book().read("userId", ""))) {
                try {
                    if (v.getId() == R.id.image_view3) {

                        Intent i = new Intent(mContext, LeadScore.class);
                        i.putExtra("position", newpos);
                        i.putExtra("cat", temp);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(i);

                    }
                    if (v.getId() == R.id.rimage_view3) {

                        Intent i = new Intent(mContext, LeadScore.class);
                        i.putExtra("position", newpos);
                        i.putExtra("cat", temp);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(i);

                    }
                } catch (Exception e) {
                }
            }



        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            onCardClickListner.OnItemLongClicked(v, user, position);
            return true;
        }
    }
    public interface OnCardClickListner {
        void OnItemLongClicked(View view, LeaderBoard user, int position);
        void OnItemClick(View view, LeaderBoard user, int position);
    }

}



