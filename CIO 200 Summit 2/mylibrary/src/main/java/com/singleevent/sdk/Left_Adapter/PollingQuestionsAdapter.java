package com.singleevent.sdk.Left_Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Items;
import com.singleevent.sdk.model.Polling.Report;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.LivePollingDetail;
import com.singleevent.sdk.View.LeftActivity.PollingItems;

import java.util.ArrayList;

import io.paperdb.Paper;

/*public class PollingQuestionsAdapter extends RecyclerView.ViewHolder<> {
}*/

public class PollingQuestionsAdapter extends RecyclerView.Adapter<PollingQuestionsAdapter.ViewHolder> {

    ArrayList<Report> n = new ArrayList<>();
    Context context;
    private int lastPosition = -1;
    int tabpos,feedbkTypePos,polltypeid;
    private ArrayList<Items> eventsToDisplay1 = new ArrayList<>();
    String title,pollingName,polltype;
    public int po=0;


    AppDetails appDetails;

    public PollingQuestionsAdapter(Context context, ArrayList<Report> n, int tabpos,
                                   String title,int feedbkTypePos,String pollingName,String polltype ,int polltypeid) {
        this.n = n;
        this.context = context;
        this.tabpos = tabpos;
        this.title = title;
        this.feedbkTypePos=feedbkTypePos;
        this.pollingName = pollingName;
        this.polltype=polltype;
        this.polltypeid=polltypeid;
        Paper.init(context);
        appDetails = Paper.book().read("Appdetails");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView  questions;
        AppCompatButton btn_ans;
        RelativeLayout rl_alreadyAnsrd;
        TextView btn_see_results, tv_answerdpoll;


        public ViewHolder(View view) {
            super(view);
            questions = (TextView) view.findViewById(R.id.questions);
            btn_ans = (AppCompatButton) view.findViewById(R.id.btn_ans);
            rl_alreadyAnsrd = (RelativeLayout) view.findViewById(R.id.rl_alreadyAnsrd);
            btn_see_results = (TextView) view.findViewById(R.id.btn_see_results);
            tv_answerdpoll = (TextView) view.findViewById(R.id.tv_answerdpoll);



        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_list_polling_ques, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Report report = n.get(position);


        //new logic
        if (position==0 ){
            if (report.getAnsSubmitted()) {
                holder.tv_answerdpoll.setVisibility(View.VISIBLE);
                holder.rl_alreadyAnsrd.setVisibility(View.VISIBLE);
                holder.btn_ans.setVisibility(View.GONE);
            }else {
                holder.tv_answerdpoll.setVisibility(View.GONE);
                holder.rl_alreadyAnsrd.setVisibility(View.GONE);
                holder.btn_ans.setVisibility(View.VISIBLE);

            }
        }else if ( report.getAnsSubmitted()!=(n.get(position).getAnsSubmitted())){
            holder.tv_answerdpoll.setVisibility(View.VISIBLE);
            holder.rl_alreadyAnsrd.setVisibility(View.VISIBLE);
            holder.btn_ans.setVisibility(View.GONE);
        }else{

            if (report.getAnsSubmitted()) {
                holder.tv_answerdpoll.setVisibility(View.GONE);
                holder.rl_alreadyAnsrd.setVisibility(View.VISIBLE);
                holder.btn_ans.setVisibility(View.GONE);
            }else {
                holder.tv_answerdpoll.setVisibility(View.GONE);
                holder.rl_alreadyAnsrd.setVisibility(View.GONE);
                holder.btn_ans.setVisibility(View.VISIBLE);
            }

        }



        holder.questions.setText(report.getQuestion());
        holder.btn_ans.setText("Answer poll");
        holder.btn_ans.setBackgroundColor(Color.parseColor(appDetails.getTheme_color()));
        ShapeDrawable shapedrawable = new ShapeDrawable();
        shapedrawable.setShape(new RectShape());
        shapedrawable.getPaint().setColor(Color.parseColor(appDetails.getTheme_color()));
        shapedrawable.getPaint().setStrokeWidth(5f);
        shapedrawable.getPaint().setStyle(Paint.Style.STROKE);

        holder.btn_see_results.setBackground(shapedrawable);

        holder.btn_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle arg = new Bundle();
                arg.putInt("Tabpos", tabpos);
                arg.putInt("Itempos", feedbkTypePos);
                arg.putString("pollName", pollingName);
                arg.putInt("QuestionPosition",position);
                arg.putString("title", title);
                arg.putString("Question",n.get(position).getQuestion());
                arg.putString("polltype",polltype);
                arg.putInt("polltypeid",polltypeid);
                arg.putSerializable("ReportList",n);
                Intent i = new Intent(context, PollingItems.class);
                i.putExtras(arg);
                context.startActivity(i);
            }
        });

        holder.btn_see_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle arg = new Bundle();
                arg.putInt("Tabpos", tabpos);
                arg.putInt("Itempos", feedbkTypePos);
                arg.putString("pollName", pollingName);
                arg.putInt("QuestionPosition",position);
                arg.putString("title", title);
                arg.putSerializable("ReportList",n);
                Intent i = new Intent(context, LivePollingDetail.class);
                i.putExtras(arg);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return n.size();
    }
}
