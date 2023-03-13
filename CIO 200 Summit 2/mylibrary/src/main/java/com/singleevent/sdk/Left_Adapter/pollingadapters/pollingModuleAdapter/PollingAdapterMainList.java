package com.singleevent.sdk.Left_Adapter.pollingadapters.pollingModuleAdapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.singleevent.sdk.Left_Adapter.pollingadapters.PollingAnswerAdapter;
import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.pollingActivities.LoadPollActivity;
import com.singleevent.sdk.View.LeftActivity.pollingActivities.PollingResultActivity;
import com.singleevent.sdk.pojo.pollingpojo.PollingQuestionsPojo;

import java.util.List;

import io.paperdb.Paper;

public class PollingAdapterMainList extends RecyclerView.Adapter<PollingAdapterMainList.myViewHolder> {
    List<PollingQuestionsPojo> questionsPojoList;
    Context context;

    public PollingAdapterMainList(List<PollingQuestionsPojo> questionsPojoList, Context context) {
        this.questionsPojoList = questionsPojoList;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.polling_recycler_question_items,parent,false);
        return new PollingAdapterMainList.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setIsRecyclable(false);

        if (questionsPojoList.get(position).isAnswered()) {
            holder.quiz_card.setVisibility(View.GONE);
            holder.layout_answered.setVisibility(View.VISIBLE);
            holder.done_questions.setText(questionsPojoList.get(position).getQuestions());
            holder.see_result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, PollingResultActivity.class);
                    i.putExtra("question_name",questionsPojoList.get(position).getQuestions());
                    i.putExtra("poll_id", questionsPojoList.get(position).getPoll_id());
                    holder.itemView.getContext().startActivity(i);

                }
            });

        }
        else if (!questionsPojoList.get(position).isAnswered()) {
            holder.quiz_card.setVisibility(View.VISIBLE);
            holder.layout_answered.setVisibility(View.GONE);
            holder.quiz_name.setText(questionsPojoList.get(position).getQuestions());
            holder.quiz_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, LoadPollActivity.class);
                    i.putExtra("question_name",questionsPojoList.get(position).getQuestions());
                    i.putExtra("poll_id", questionsPojoList.get(position).getPoll_id());
                    holder.itemView.getContext().startActivity(i);
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return questionsPojoList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView quiz_name,done_questions, see_result;
        CardView quiz_card;
        CardView layout_answered;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            done_questions = itemView.findViewById(R.id.done_questions);
            see_result = itemView.findViewById(R.id.see_result);
            quiz_name = itemView.findViewById(R.id.quiz_name);
           // quiz_card = itemView.findViewById(R.id.quiz_card);
            layout_answered = itemView.findViewById(R.id.layout_answered);
        }
    }
}
