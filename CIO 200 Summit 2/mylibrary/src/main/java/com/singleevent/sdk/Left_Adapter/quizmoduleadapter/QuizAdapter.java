package com.singleevent.sdk.Left_Adapter.quizmoduleadapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.QuizModule.LoadQuizActivity;
import com.singleevent.sdk.pojo.quizmodulepojo.QuizPojo;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.myViewHolder> {
    Context context;
    List<QuizPojo> quizPojoList;

    public QuizAdapter(Context context, List<QuizPojo> quizPojoList) {
        this.context = context;
        this.quizPojoList = quizPojoList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_main_row_items, parent, false);
        return new myViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        String unlock="false";
        holder.question_count.setText("No of Questions : " + quizPojoList.get(position).getQuestion_count());
        holder.quiz_name.setText(quizPojoList.get(position).getQuiz_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quizPojoList.get(position).getLock_status().equals(unlock))
                {
                    Intent i=new Intent(context, LoadQuizActivity.class);
                    i.putExtra("quiz_id",quizPojoList.get(position).getQuiz_id());
                    holder.itemView.getContext().startActivity(i);
//                    Toast.makeText(context,"can go forward",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,"This Quiz is locked", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizPojoList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView question_count, quiz_name;
        ImageView next;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            question_count = itemView.findViewById(R.id.question_count);
            quiz_name = itemView.findViewById(R.id.quiz_name);
            next = itemView.findViewById(R.id.next);

        }
    }
}
