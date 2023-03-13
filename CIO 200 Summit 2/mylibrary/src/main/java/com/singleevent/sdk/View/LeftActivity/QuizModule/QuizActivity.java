package com.singleevent.sdk.View.LeftActivity.QuizModule;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.singleevent.sdk.Left_Adapter.quizmoduleadapter.QuizAdapter;
import com.singleevent.sdk.R;
import com.singleevent.sdk.model.quiz.QuizModel;
import com.singleevent.sdk.pojo.quizmodulepojo.QuizPojo;
import com.singleevent.sdk.service.Health1NetworkController;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity {
    RecyclerView quiz_recycler;
    QuizAdapter quizAdapter;
    List<QuizPojo> quizPojos=new ArrayList<>();
    private final String app_id = "4d066a5185b2e7c70c63601091f5ac45a197";
    List<QuizModel.Datum> quizData=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        quiz_recycler=findViewById(R.id.quiz_recycler);
        setQuizRecycler();
        getQuizData();
    }

    private void getQuizData() {
        String active="true";
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.show();
        Health1NetworkController.getInstance().getService().getAllQuiz(app_id).enqueue(new Callback<QuizModel>() {
            @Override
            public void onResponse(Call<QuizModel> call, Response<QuizModel> response) {
                if(response.isSuccessful()){
                    QuizModel quizModel=response.body();
                    if(quizModel.getResponse()){
                        quizData=quizModel.getData();
                        for(int i=0;i<quizData.size();i++){
                            if(quizData.get(i).getActiveStatus().equals(active)){
                                quizPojos.add(new QuizPojo(quizData.get(i).getQuizId(),quizData.get(i).getQuizName(),quizData.get(i).getNoOfQuestions(),quizData.get(i).getLockStatus()));
                            }
                            progressDialog.dismiss();
                            quizAdapter.notifyDataSetChanged();
                        }
                    }else{
                        progressDialog.dismiss();
                    }
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(QuizActivity.this,response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<QuizModel> call, Throwable t) {
                progressDialog.dismiss();

                Toast.makeText(QuizActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setQuizRecycler() {
        quiz_recycler.setLayoutManager(new LinearLayoutManager(this));
        quizAdapter=new QuizAdapter(this,quizPojos);
        quiz_recycler.setAdapter(quizAdapter);

    }
}