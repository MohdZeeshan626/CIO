package com.singleevent.sdk.Left_Adapter.quizmoduleadapter.feedQuizAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.singleevent.sdk.R;
import com.singleevent.sdk.pojo.quizmodulepojo.QuizResultMainPojo;

import java.util.List;

public class FeedQuizMainResultAdapterRecycler extends RecyclerView.Adapter<FeedQuizMainResultAdapterRecycler.myViewHolder> {
    Context context;
    List<QuizResultMainPojo> quizResultMainPojos;
    FeedQuizResultAdapter quizResultAdapter;
    RecyclerView recyclerView;
    int id;
    public FeedQuizMainResultAdapterRecycler(Context context, List<QuizResultMainPojo> quizResultMainPojos,RecyclerView recyclerView,int id) {
        this.context = context;
        this.quizResultMainPojos = quizResultMainPojos;
        this.recyclerView = recyclerView;
        this.id=id;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_quiz_main, parent, false);
        return new FeedQuizMainResultAdapterRecycler.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.question_count.setText("Questions " + (position + 1) + "/" + quizResultMainPojos.size());
        holder.progress_for_total_question.setMax(100);
        holder.progress_for_total_question.setProgress(((position + 1) * 100) / quizResultMainPojos.size());
        holder.question.setText(quizResultMainPojos.get(position).getQuestionName());
        Log.d("check_question", "instantiateItem: " + quizResultMainPojos.get(position).getQuestionName());
        quizResultAdapter = new FeedQuizResultAdapter(context, quizResultMainPojos.get(position).getResultPojos());
        holder.answer_recycler.setAdapter(quizResultAdapter);
        holder.next_page.setVisibility(View.VISIBLE);
        holder.prev_page.setVisibility(View.VISIBLE);
        holder.next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView == null) {
                    recyclerView.setId(id);
                }
                recyclerView.scrollToPosition(position+1);

            }
        });
        holder.prev_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView == null) {
                    recyclerView.setId(id);
                }
                recyclerView.scrollToPosition(position-1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return quizResultMainPojos.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        RecyclerView answer_recycler;
        TextView question, question_count, prev_page, next_page;
        ProgressBar progress_for_total_question;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            answer_recycler = itemView.findViewById(R.id.answer_recycler);
            question = itemView.findViewById(R.id.question);
            prev_page = itemView.findViewById(R.id.prev_page);
            next_page = itemView.findViewById(R.id.next_page);
            question_count = itemView.findViewById(R.id.question_count);
            progress_for_total_question = itemView.findViewById(R.id.progress_for_total_question);
            answer_recycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}
