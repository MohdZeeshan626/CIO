package com.singleevent.sdk.Left_Adapter.quizmoduleadapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.singleevent.sdk.R;
import com.singleevent.sdk.pojo.quizmodulepojo.QuizAnswerPojo;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class QuizAnswerAdapter extends RecyclerView.Adapter<QuizAnswerAdapter.myViewHolder> {
    Context context;
    List<QuizAnswerPojo> answerPojos;
    private RadioButton lastCheckedRB = null;

    public QuizAnswerAdapter(Context context, List<QuizAnswerPojo> answerPojos) {
        this.context = context;
        this.answerPojos = answerPojos;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.mcq_answer_row_items,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.answer.setText(answerPojos.get(position).getAnswer());

        holder.answer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (lastCheckedRB != null) {
                    if (lastCheckedRB!=holder.answer){
                        lastCheckedRB.setChecked(false);
                    }
                }
                if (holder.answer.isChecked()) {
                    //store the clicked radiobutton
                    lastCheckedRB = holder.answer;
                }else{
                    lastCheckedRB=null;
                }
            }
        });

        holder.answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences("MCQ_Quiz_Answer", MODE_PRIVATE).edit();
                editor.putString("answer", answerPojos.get(position).getAnswer());
                editor.apply();
                Toast.makeText(context, answerPojos.get(position).getAnswer(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return answerPojos.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        RadioButton answer;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            answer=itemView.findViewById(R.id.answer);

        }
    }
}
