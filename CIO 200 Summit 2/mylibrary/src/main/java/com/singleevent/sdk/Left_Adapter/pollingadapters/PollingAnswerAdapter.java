package com.singleevent.sdk.Left_Adapter.pollingadapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.Feeds;
import com.singleevent.sdk.View.RightActivity.group_feed.GroupFeedView;
import com.singleevent.sdk.feeds_class.OnItemClickListener;
import com.singleevent.sdk.interfaces.OnRecyclerVoteClick;
import com.singleevent.sdk.model.Polling.VoteModel;
import com.singleevent.sdk.pojo.pollingpojo.PollingAnswersPojo;
import com.singleevent.sdk.pojo.pollingpojo.PostVoteForPolling;
import com.singleevent.sdk.service.Health1NetworkController;

import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

public class PollingAnswerAdapter extends RecyclerView.Adapter<PollingAnswerAdapter.myViewHolder> {
    Context context;
    List<PollingAnswersPojo> answersPojoList;
    int parent_position;
    private OnRecyclerVoteClick toVote;

    public PollingAnswerAdapter(Context context, List<PollingAnswersPojo> answersPojoList, OnRecyclerVoteClick toVote, int parent_position) {
        this.context = context;
        this.answersPojoList = answersPojoList;
        this.toVote = toVote;
        this.parent_position = parent_position;
    }

    public PollingAnswerAdapter(Context context, List<PollingAnswersPojo> answersPojoList) {
        this.context = context;
        this.answersPojoList = answersPojoList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.polling_recycler_answers_items, parent, false);
        return new PollingAnswerAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.answers.setText(answersPojoList.get(position).getAnswer());
        holder.options.setText(answersPojoList.get(position).getAnswer());
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toVote != null) {
                   // toVote.votePoll(answersPojoList.get(position).getAnswer(), answersPojoList.get(position).getPoll_id(), parent_position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return answersPojoList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        RadioButton answers;
        RadioGroup radio;
        TextView options;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            answers = itemView.findViewById(R.id.answer);
            options = itemView.findViewById(R.id.options);
            radio = itemView.findViewById(R.id.radio);

        }
    }
}
