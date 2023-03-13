package com.singleevent.sdk.Left_Adapter.quizmoduleadapter.feedQuizAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.singleevent.sdk.R;
import com.singleevent.sdk.interfaces.OnRecyclerVoteClick;
import com.singleevent.sdk.pojo.quizmodulepojo.QuizQuestionPojo;

import java.util.List;

public class FeedQuizMainAdapterRecycler extends RecyclerView.Adapter<FeedQuizMainAdapterRecycler.myViewHolder> {
    FeedQuizAdapter quizAdapter;
    Context context;
    List<QuizQuestionPojo> questionsPojoList;
    OnRecyclerVoteClick onRecyclerVoteClick;
    int parent_pos;
    RecyclerView recyclerView;
    int id;
    public FeedQuizMainAdapterRecycler(Context context, List<QuizQuestionPojo> questionsPojoList, OnRecyclerVoteClick onRecyclerVoteClick, int parent_pos, RecyclerView recyclerView,int id) {
        this.context = context;
        this.questionsPojoList = questionsPojoList;
        this.onRecyclerVoteClick = onRecyclerVoteClick;
        this.parent_pos = parent_pos;
        this.recyclerView = recyclerView;
        this.id=id;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_quiz_main, parent, false);
        return new FeedQuizMainAdapterRecycler.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.question_count.setText("Questions " + (position + 1) + "/" + questionsPojoList.size());
        holder.progress_for_total_question.setMax(100);
        holder.progress_for_total_question.setProgress(((position + 1) * 100) / questionsPojoList.size());
        holder.question.setText(questionsPojoList.get(position).getQuestion());
        Log.d("check_question", "instantiateItem: " + questionsPojoList.get(position).getQuestion());
        quizAdapter = new FeedQuizAdapter(context, questionsPojoList.get(position).getAnswerList(), onRecyclerVoteClick, parent_pos);
        holder.answer_recycler.setAdapter(quizAdapter);
        if (position == (questionsPojoList.size() - 1)) {
            holder.submit.setVisibility(View.VISIBLE);
            holder.next_page.setVisibility(View.GONE);
        } else {
            holder.next_page.setVisibility(View.VISIBLE);
            holder.submit.setVisibility(View.GONE);

        }

        holder.next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecyclerVoteClick.nextAnswer("",questionsPojoList.get(position).getQuestion_id(),questionsPojoList.get(position).getQuiz_id(),1,recyclerView,id,position);
            }
        });
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecyclerVoteClick.nextAnswer("",questionsPojoList.get(position).getQuestion_id(),questionsPojoList.get(position).getQuiz_id(),2,recyclerView,id,parent_pos);

                Log.d("question_id", "onClick: "+questionsPojoList.get(position).getQuestion_id()+"\nquiz id :"+questionsPojoList.get(position).getQuiz_id());

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
        return questionsPojoList.size();
    }


    public class myViewHolder extends RecyclerView.ViewHolder {
        RecyclerView answer_recycler;
        TextView question, question_count, prev_page, next_page,submit;
        ProgressBar progress_for_total_question;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            answer_recycler = itemView.findViewById(R.id.answer_recycler);
            question = itemView.findViewById(R.id.question);
            prev_page = itemView.findViewById(R.id.prev_page);
            next_page = itemView.findViewById(R.id.next_page);
            submit = itemView.findViewById(R.id.submit);
            question_count = itemView.findViewById(R.id.question_count);
            progress_for_total_question = itemView.findViewById(R.id.progress_for_total_question);
            answer_recycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()));


        }
    }
}
