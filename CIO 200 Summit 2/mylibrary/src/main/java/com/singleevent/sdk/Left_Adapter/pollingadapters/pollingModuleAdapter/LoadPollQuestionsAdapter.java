package com.singleevent.sdk.Left_Adapter.pollingadapters.pollingModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.singleevent.sdk.Left_Adapter.pollingadapters.PollingAnswerAdapter;
import com.singleevent.sdk.Left_Adapter.quizmoduleadapter.QuestionLoaderCollectionAdapter;
import com.singleevent.sdk.R;
import com.singleevent.sdk.pojo.pollingpojo.PollingQuestionsPojo;

import java.util.List;

public class LoadPollQuestionsAdapter extends RecyclerView.Adapter<LoadPollQuestionsAdapter.myViewHolder> {
    Context context;
    List<PollingQuestionsPojo> questionsPojoList;
    PollAnswerAdapter pollAnswerAdapter;

    public LoadPollQuestionsAdapter(Context context, List<PollingQuestionsPojo> questionsPojoList) {
        this.context = context;
        this.questionsPojoList = questionsPojoList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_question_row_items, parent, false);
        return new LoadPollQuestionsAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.questions.setText(questionsPojoList.get(position).getQuestions());
        pollAnswerAdapter=new PollAnswerAdapter(context,questionsPojoList.get(position).getAnswersArray(),questionsPojoList.get(position).getQuestions());
        holder.answer_recycler.setAdapter(pollAnswerAdapter);
    }

    @Override
    public int getItemCount() {
        return questionsPojoList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView questions;
        RecyclerView answer_recycler;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            questions=itemView.findViewById(R.id.questions);
            answer_recycler=itemView.findViewById(R.id.answer_recycler);
            answer_recycler.setLayoutManager(new LinearLayoutManager(context));

        }
    }
}
