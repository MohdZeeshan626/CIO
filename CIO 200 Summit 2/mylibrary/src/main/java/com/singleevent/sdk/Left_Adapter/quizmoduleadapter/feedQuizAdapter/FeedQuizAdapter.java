package com.singleevent.sdk.Left_Adapter.quizmoduleadapter.feedQuizAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.singleevent.sdk.R;
import com.singleevent.sdk.interfaces.OnRecyclerVoteClick;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.pojo.quizmodulepojo.QuizAnswerPojo;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class FeedQuizAdapter extends RecyclerView.Adapter<FeedQuizAdapter.myViewHolder> {
    Context context;
    List<QuizAnswerPojo> answerPojos;
    OnRecyclerVoteClick submit_answer;
    int parent_position;
    AppDetails appDetails;
    List<String> answers = new ArrayList<>();

    public FeedQuizAdapter(Context context, List<QuizAnswerPojo> answerPojos, OnRecyclerVoteClick submit_answer,int position) {
        this.context = context;
        this.answerPojos = answerPojos;
        this.submit_answer = submit_answer;
        this.parent_position = position;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_recycler_answers_items, parent, false);
        return new FeedQuizAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        appDetails = Paper.book().read("Appdetails");

        holder.options.setText(answerPojos.get(position).getAnswer());
        Log.d("getItemCount", "getItemCount: "+answerPojos.size());
        holder.check_answer.setText(answerPojos.get(position).getAnswer());
        holder.check_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.check_answer.isChecked()) {

                    answers.add(answerPojos.get(position).getAnswer());

                } else {
                    answers.remove(answerPojos.get(position).getAnswer());
                }
               // submit_answer.getCheckedItems(answers,answerPojos.get(position).getQuestion_id(),answerPojos.get(position).getQuiz_id());
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d("getItemCount", "getItemCount: "+answerPojos.size());
        return answerPojos.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView options;
        CheckBox check_answer;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            options = itemView.findViewById(R.id.options);
            check_answer = itemView.findViewById(R.id.check_answer);

        }
    }
}
