package com.singleevent.sdk.Left_Adapter.quizmoduleadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.singleevent.sdk.R;
import com.singleevent.sdk.pojo.pollingpojo.PollingResultPojo;

import java.util.List;

public class QuizResultAdapter extends RecyclerView.Adapter<QuizResultAdapter.myViewHolder> {
    Context context;
    List<PollingResultPojo> resultPojos;

    public QuizResultAdapter(Context context, List<PollingResultPojo> resultPojos) {
        this.context = context;
        this.resultPojos = resultPojos;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.polling_result_row_items,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        if(resultPojos.get(position).getNumberOfTotalVotes()>0) {
            holder.option_name.setText(resultPojos.get(position).getOptionName());
            holder.number_of_votes.setText(resultPojos.get(position).getNumberOfVotes() + "/" + resultPojos.get(position).getNumberOfTotalVotes());
            float per = (resultPojos.get(position).getNumberOfVotes() * 100) / resultPojos.get(position).getNumberOfTotalVotes();
            holder.percentage.setText(per + "%");
            holder.progress_for_vote.setMax(100);
            holder.progress_for_vote.setProgress((resultPojos.get(position).getNumberOfVotes() * 100) / resultPojos.get(position).getNumberOfTotalVotes());
        }
    }

    @Override
    public int getItemCount() {
        return resultPojos.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView option_name,number_of_votes,percentage;
        ProgressBar progress_for_vote;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            option_name=itemView.findViewById(R.id.option_name);
            number_of_votes=itemView.findViewById(R.id.number_of_votes);
            percentage=itemView.findViewById(R.id.percentage);
            progress_for_vote=itemView.findViewById(R.id.progress_for_vote);
        }
    }
}
