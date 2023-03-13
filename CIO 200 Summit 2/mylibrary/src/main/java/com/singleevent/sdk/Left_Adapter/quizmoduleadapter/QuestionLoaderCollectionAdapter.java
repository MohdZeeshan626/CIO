package com.singleevent.sdk.Left_Adapter.quizmoduleadapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.QuizModule.LoadQuizActivity;
import com.singleevent.sdk.View.LeftActivity.QuizModule.QuizResultActivity;
import com.singleevent.sdk.model.quiz.SubmitQuizAnswerModel;
import com.singleevent.sdk.pojo.quizmodulepojo.QuizAnswerSubmittingPojo;
import com.singleevent.sdk.pojo.quizmodulepojo.QuizQuestionPojo;
import com.singleevent.sdk.pojo.quizmodulepojo.QuizSumitAnswerArrayPojo;
import com.singleevent.sdk.service.Health1NetworkController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class QuestionLoaderCollectionAdapter extends RecyclerView.Adapter<QuestionLoaderCollectionAdapter.myViewHolder> {

    QuizAnswerAdapter answerAdapter;
    Context context;
    List<QuizQuestionPojo> questionsPojoList;
    ViewPager2 viewPager2;
    List<QuizSumitAnswerArrayPojo> answerArrays = new ArrayList<>();
    private final String app_id = "4d066a5185b2e7c70c63601091f5ac45a197";

    public QuestionLoaderCollectionAdapter(Context context, List<QuizQuestionPojo> questionsPojoList, ViewPager2 viewPager2) {
        this.context = context;
        this.questionsPojoList = questionsPojoList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mcq_question_row_items, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("login", MODE_PRIVATE);
        String userId = Paper.book().read("userId","");

        holder.textView.setText("Total no of Questions " + questionsPojoList.size());
        holder.questions.setText(questionsPojoList.get(position).getQuestion());
        answerAdapter = new QuizAnswerAdapter(context, questionsPojoList.get(position).getAnswerList());
        holder.answer_recycler.setAdapter(answerAdapter);
        if (position == questionsPojoList.size() - 1) {
            holder.submit_btn.setVisibility(View.VISIBLE);
            holder.next_page.setVisibility(View.GONE);
        } else {
            holder.submit_btn.setVisibility(View.GONE);
            holder.next_page.setVisibility(View.VISIBLE);
        }
        holder.submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("MCQ_Quiz_Answer", MODE_PRIVATE);
                String answer = sharedPreferences.getString("answer", "");
                if (!answer.isEmpty()) {
                    holder.submitAnswer(answer, questionsPojoList.get(position).getQuiz_id(), questionsPojoList.get(position).getQuestion_id(), userId,questionsPojoList.get(position).getQuizName());
                } else {
                    Toast.makeText(context, "select answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("MCQ_Quiz_Answer", MODE_PRIVATE);
                String answer = sharedPreferences.getString("answer", "");
                if (!answer.isEmpty()) {
                    holder.saveAnswer(answer, questionsPojoList.get(position).getQuiz_id(),questionsPojoList.get(position).getQuestion_id(), userId,questionsPojoList.get(position).getQuizName());
                } else {
                    Toast.makeText(context, "select answer", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return questionsPojoList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView textView, questions;
        RecyclerView answer_recycler;
        Button next_page, submit_btn;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            questions = itemView.findViewById(R.id.questions);
            next_page = itemView.findViewById(R.id.next_page);
            submit_btn = itemView.findViewById(R.id.submit_btn);
            answer_recycler = itemView.findViewById(R.id.answer_recycler);
            answer_recycler.setLayoutManager(new LinearLayoutManager(context));

        }

        public void submitAnswer(String answer, String quiz_id, String question_id, String userId, String quizName) {
            clearShearedPref();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat();
            sdf1.applyPattern("yyyy-MM-dd hh:mm:ss");
            String strDate = sdf1.format(cal.getTime());
            Log.d("heyCheckhere", "\n" + strDate);
            answerArrays=new ArrayList<>();
            answerArrays.add(new QuizSumitAnswerArrayPojo(question_id, answer));
            Log.d("heyCheckhere", "\n" + answerArrays.size());
            QuizAnswerSubmittingPojo submittingPojo = new QuizAnswerSubmittingPojo(quiz_id, app_id, userId, strDate, answerArrays);
            ProgressDialog progressDialog=new ProgressDialog(context);
            progressDialog.show();
            Health1NetworkController.getInstance().getService().answerQuiz(submittingPojo).enqueue(new Callback<SubmitQuizAnswerModel>() {
                @Override
                public void onResponse(Call<SubmitQuizAnswerModel> call, Response<SubmitQuizAnswerModel> response) {
                    if(response.code()==400){
                        Intent i=new Intent(context, QuizResultActivity.class);
                        i.putExtra("quiz_id",quiz_id);
                        i.putExtra("quiz_name",quizName);
                        itemView.getContext().startActivity(i);
                        ((LoadQuizActivity)context).finish();
                        Toast.makeText(context, "quiz is Already submitted", Toast.LENGTH_SHORT).show();
                    }
                    if (response.isSuccessful()) {
                        SubmitQuizAnswerModel quizAnswerModel = response.body();
                        if (quizAnswerModel.getResponse()) {
                            clearShearedPref();
                            progressDialog.dismiss();
                            Intent i=new Intent(context, QuizResultActivity.class);
                            i.putExtra("quiz_id",quiz_id);
                            i.putExtra("quiz_name",quizName);
                            i.putExtra("correctAns",quizAnswerModel.getCorrectscore());
                            i.putExtra("incorrectAns",quizAnswerModel.getIncorrectscore());
                            itemView.getContext().startActivity(i);
                            ((LoadQuizActivity)context).finish();
                        } else {
                            progressDialog.dismiss();
                            clearShearedPref();
                            Toast.makeText(context, quizAnswerModel.getResponseString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        clearShearedPref();
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SubmitQuizAnswerModel> call, Throwable t) {
                    progressDialog.dismiss();
                    clearShearedPref();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }

        public void saveAnswer(String answer, String quiz_id, String question_id, String userId, String quizName) {
//            answerArrays=new ArrayList<>();
//            answerArrays.add(new QuizSumitAnswerArrayPojo(question_id, answer));
//            Log.d("heyCheckhere", "\n" + answerArrays.size());
            clearShearedPref();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat();
            sdf1.applyPattern("yyyy-MM-dd hh:mm:ss");
            String strDate = sdf1.format(cal.getTime());
            Log.d("heyCheckhere", "\n" + strDate);
            answerArrays=new ArrayList<>();
            answerArrays.add(new QuizSumitAnswerArrayPojo(question_id, answer));
            Log.d("heyCheckhere", "\n" + answerArrays.size());
            QuizAnswerSubmittingPojo submittingPojo = new QuizAnswerSubmittingPojo(quiz_id, app_id, userId, strDate, answerArrays);

            ProgressDialog progressDialog=new ProgressDialog(context);
            progressDialog.show();
            Health1NetworkController.getInstance().getService().answerQuiz(submittingPojo).enqueue(new Callback<SubmitQuizAnswerModel>() {
                @Override
                public void onResponse(Call<SubmitQuizAnswerModel> call, Response<SubmitQuizAnswerModel> response) {
                    if(response.code()==400){
                        viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, true);
                        Toast.makeText(context, "quiz is Already submitted", Toast.LENGTH_SHORT).show();
                    }
                    if (response.isSuccessful()) {
                        SubmitQuizAnswerModel quizAnswerModel = response.body();
                        if (quizAnswerModel.getResponse()) {
                            progressDialog.dismiss();
                            clearShearedPref();
                            Toast.makeText(context, "submitted successfully", Toast.LENGTH_SHORT).show();

                            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, true);

                        } else {
                            progressDialog.dismiss();
                            clearShearedPref();
                            Toast.makeText(context, quizAnswerModel.getResponseString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        clearShearedPref();
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SubmitQuizAnswerModel> call, Throwable t) {
                    progressDialog.dismiss();
                    clearShearedPref();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }

        private void clearShearedPref() {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MCQ_Quiz_Answer", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }
}
