package com.singleevent.sdk.Left_Adapter.pollingadapters.pollingModuleAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.singleevent.sdk.Left_Adapter.pollingadapters.PollingAnswerAdapter;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.Feeds;
import com.singleevent.sdk.View.LeftActivity.pollingActivities.LoadPollActivity;
import com.singleevent.sdk.View.LeftActivity.pollingActivities.PollingResultActivity;
import com.singleevent.sdk.model.Polling.PollingModel;
import com.singleevent.sdk.model.Polling.VoteModel;
import com.singleevent.sdk.pojo.pollingpojo.PollingAnswersPojo;
import com.singleevent.sdk.pojo.pollingpojo.PostVoteForPolling;
import com.singleevent.sdk.service.Health1NetworkController;

import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;

public class PollAnswerAdapter extends RecyclerView.Adapter<PollAnswerAdapter.myViewHolder> {
    Context context;
    List<PollingAnswersPojo> answersPojoList;
    String question;

    public PollAnswerAdapter(Context context, List<PollingAnswersPojo> answersPojoList, String question) {
        this.context = context;
        this.answersPojoList = answersPojoList;
        this.question = question;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.polling_recycler_answers_items,parent,false);
        return new PollAnswerAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.options.setText(answersPojoList.get(position).getAnswer());
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"clicked + "+position,Toast.LENGTH_SHORT).show();
               // holder.answerPoll(answersPojoList.get(position).getAnswer(), answersPojoList.get(position).getPoll_id(),question);

            }
        });
    }

    @Override
    public int getItemCount() {
        return answersPojoList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView options;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            options = itemView.findViewById(R.id.options);

        }

        public void answerPoll(String answer, String poll_id, String question) {
            String userId = Paper.book().read("userId","");
            Log.d("answerPoll_test", "answerPoll: "+answer +"\n"+poll_id+"\n"+question);
            PostVoteForPolling voteForPolling = new PostVoteForPolling(userId, poll_id, answer);
            Health1NetworkController.getInstance().getService().vote(voteForPolling).enqueue(new Callback<VoteModel>() {
                @Override
                public void onResponse(Call<VoteModel> call, retrofit2.Response<VoteModel> response) {
                    if (response.code() == 400) {
                        Toast.makeText(context, "You have chosen option which is not present in the list.", Toast.LENGTH_SHORT).show();
                    }
                    if (response.isSuccessful()) {
                        VoteModel voteModel = response.body();
                        if (voteModel.getResponse()) {
                            Intent i = new Intent(context, PollingResultActivity.class);
                            i.putExtra("poll_id",poll_id);
                            i.putExtra("question_name",question);
                            itemView.getContext().startActivity(i);
                            ((LoadPollActivity)context).finish();
                        } else {
                            Toast.makeText(context,voteModel.getResponseString(),Toast.LENGTH_SHORT).show();
                        }
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<VoteModel> call, Throwable t) {
                    t.getStackTrace();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}
