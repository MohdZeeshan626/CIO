package com.singleevent.sdk.View.LeftActivity.askAquestion.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.askAquestion.model.EventQuestionModel;

import java.util.List;

/**
 * Created by webMOBI on 2/13/2018.
 */

public class AskAQueAdapter extends RecyclerView.Adapter<AskAQueAdapter.MyViewHolder> {

    private List<EventQuestionModel> values;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tv_count, tv_question, tv_vote_count;
        ImageView tv_is_vote;
        LinearLayout linearlayout;
      //  static OnCardClickListner onCardClickListner;
        EventQuestionModel user;
        public MyViewHolder(View view) {
            super(view);
            tv_count = (TextView) view.findViewById(R.id.tv_count);
            tv_question = (TextView) view.findViewById(R.id.tv_question);
            tv_vote_count = (TextView) view.findViewById(R.id.tv_vote_count);
            tv_is_vote = (ImageView) view.findViewById(R.id.tv_is_vote);
            linearlayout=(LinearLayout)view.findViewById(R.id.linearlayout);

            linearlayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if ( v.getId() == linearlayout.getId()){
              //  onCardClickListner.OnItemClick( v, user , position );

            }
        }
    }


    public AskAQueAdapter(Context context, List<EventQuestionModel> values ) {
        this.context = context;
        this.values = values;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context)
                .inflate(R.layout.recycler_event_item_ask_ques, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        EventQuestionModel eventQuestionModel = values.get(position);
        holder.linearlayout.setBackground(Util.setdrawable(context,R.drawable.rectanglebackground,
                Color.parseColor("#ffdadada")));
        holder.tv_count.setText(String.valueOf(++position)+". ");
        holder.tv_question.setText(eventQuestionModel.getQuestion());

        holder.tv_vote_count.setText(String.valueOf(eventQuestionModel.getUp_votes()));
       // holder.tv_is_vote.setText(eventQuestionModel.isUser_vote()?"Voted" : " Vote " );
        holder.tv_is_vote.setImageDrawable(eventQuestionModel.isUser_vote()?context.getDrawable(R.drawable.likecolor):context.getDrawable(R.drawable.likegray));
        if (!eventQuestionModel.isUser_vote()){
           // holder.tv_is_vote.setBackground(context.getResources().getDrawable(R.drawable.rectanglebackground));

          //  GradientDrawable bgShape = (GradientDrawable)holder.tv_is_vote.getBackground();
           /// bgShape.setColor( context.getResources().getColor(R.color.m_light_green));
          //  holder.tv_is_vote.setTextColor(Color.WHITE);
            //holder.tv_is_vote.setImageDrawable(context.getDrawable(R.drawable.likecolor);
        }else {
           // holder.tv_is_vote.setBackground(context.getResources().getDrawable(R.drawable.rectanglebackground));
           // GradientDrawable bgShape = (GradientDrawable)holder.tv_is_vote.getBackground();
           // bgShape.setColor(Color.LTGRAY);
           // holder.tv_is_vote.setTextColor(Color.BLACK);
        }



    }

    @Override
    public int getItemCount() {
        return values.size();
    }
   /* public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        MyViewHolder.onCardClickListner = onCardClickListner;
    }

    public interface OnCardClickListner {
        void OnItemLongClicked(View view, EventQuestionModel user, int position);
        void OnItemClick(View view, EventQuestionModel user,int position);
    }*/
}
