package com.singleevent.sdk.Left_Adapter.pollingadapters.pollingModuleAdapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.singleevent.sdk.Left_Adapter.pollingadapters.PollingAnswerAdapter;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.pollingActivities.PollingActivity;
import com.singleevent.sdk.View.LeftActivity.pollingActivities.PollingResultActivity;
import com.singleevent.sdk.model.Polling.VoteModel;
import com.singleevent.sdk.pojo.pollingpojo.PollingQuestionsPojo;
import com.singleevent.sdk.pojo.pollingpojo.PostVoteForPolling;
import com.singleevent.sdk.service.Health1NetworkController;

import java.net.URISyntaxException;
import java.util.List;

import io.paperdb.Paper;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class PollingAdapter extends RecyclerView.Adapter<PollingAdapter.myViewHolder> {
    Context context;
    List<PollingQuestionsPojo> questionsPojoList;
    PollingAnswerAdapter pollingAnswerAdapter;
    String userId;
    String url = "https://health1.webmobi.in/socket/con";
    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(url);

        } catch (URISyntaxException e) {
        }
    }
    public PollingAdapter(Context context, List<PollingQuestionsPojo> questionsPojoList) {
        this.context = context;
        this.questionsPojoList = questionsPojoList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.polling_recycler_question_items, parent, false);
        return new PollingAdapter.myViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setIsRecyclable(false);

        if (questionsPojoList.get(position).isAnswered()) {
            holder.layout_poll.setVisibility(View.GONE);
            holder.layout_answered.setVisibility(View.VISIBLE);
            holder.done_questions.setText(questionsPojoList.get(position).getQuestions());
            holder.see_result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSocket.connect();
                    mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.d("chcel", "connected  " + mSocket.id());
                            mSocket.emit("joinpoll", questionsPojoList.get(position).getPoll_id());
                        }
                    });
                    Intent i = new Intent(context, PollingResultActivity.class);
                    i.putExtra("question_name",questionsPojoList.get(position).getQuestions());
                    i.putExtra("poll_id", questionsPojoList.get(position).getPoll_id());
                    holder.itemView.getContext().startActivity(i);



                }
            });

        }
        else if (!questionsPojoList.get(position).isAnswered()) {
            holder.layout_poll.setVisibility(View.VISIBLE);
            holder.layout_answered.setVisibility(View.GONE);
            holder.questions.setText(questionsPojoList.get(position).getQuestions());
            pollingAnswerAdapter = new PollingAnswerAdapter(context, questionsPojoList.get(position).getAnswersArray());
            holder.answer_recycler.setAdapter(pollingAnswerAdapter);
            userId = Paper.book().read("userId","");
            holder.vote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("poll", MODE_PRIVATE);
                    String vote = sharedPreferences.getString("vote", "");
                    if (!vote.isEmpty()) {
                        holder.ApiToVote(vote, userId, questionsPojoList.get(position).getPoll_id(),questionsPojoList.get(position).getQuestions());
                    } else {
                        Toast.makeText(context, "select any one to poll", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return questionsPojoList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView questions,done_questions, see_result;
        Button vote;
        RecyclerView answer_recycler;
        LinearLayout layout_poll, layout_answered;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            questions = itemView.findViewById(R.id.questions);
            vote = itemView.findViewById(R.id.vote);
            done_questions = itemView.findViewById(R.id.done_questions);
            see_result = itemView.findViewById(R.id.see_result);
            layout_answered = itemView.findViewById(R.id.layout_answered);
            layout_poll = itemView.findViewById(R.id.layout_poll);
            answer_recycler = itemView.findViewById(R.id.answer_recycler);
            answer_recycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }

        public void ApiToVote(String vote, String userId, String poll_id, String questions) {
            mSocket.connect();
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("chcel", "connected  " + mSocket.id());
                    mSocket.emit("joinpoll", poll_id);
                }
            });
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.show();
            PostVoteForPolling voteForPolling = new PostVoteForPolling(userId, poll_id, vote);
            Health1NetworkController.getInstance().getService().vote(voteForPolling).enqueue(new Callback<VoteModel>() {
                @Override
                public void onResponse(Call<VoteModel> call, Response<VoteModel> response) {
                    if (response.isSuccessful()) {
                        VoteModel voteModel = response.body();
                        if (voteModel.getResponse()) {
                            Toast.makeText(context, voteModel.getResponseString(), Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = context.getSharedPreferences("poll", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            progressDialog.dismiss();
                            Intent i = new Intent(context, PollingResultActivity.class);
                            i.putExtra("question_name",questions);
                            i.putExtra("poll_id", poll_id);
                            itemView.getContext().startActivity(i);
                            ((PollingActivity) context).finish();
                        }
                    } else {
                        Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<VoteModel> call, Throwable t) {
                    t.getStackTrace();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        }
        private void clearSharedPref(){
            SharedPreferences sharedPreferences = context.getSharedPreferences("poll", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }

    }
}
