package com.singleevent.sdk.View.LeftActivity.QuizModule;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import com.singleevent.sdk.Left_Adapter.quizmoduleadapter.QuestionLoaderCollectionAdapter;
import com.singleevent.sdk.R;
import com.singleevent.sdk.model.quiz.QuizModel;
import com.singleevent.sdk.pojo.quizmodulepojo.QuizAnswerPojo;
import com.singleevent.sdk.pojo.quizmodulepojo.QuizQuestionPojo;
import com.singleevent.sdk.service.Health1NetworkController;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadQuizActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    QuestionLoaderCollectionAdapter questionLoaderAdapter;
    String quiz_id;
    private final String app_id = "4d066a5185b2e7c70c63601091f5ac45a197";
    List<QuizModel.QuestionArray> questionArrays = new ArrayList<>();
    List<QuizModel.Datum> quizList = new ArrayList<>();
    List<QuizQuestionPojo> quizQuestionPojos = new ArrayList<>();
    List<String> options_array = new ArrayList<>();
    String url = "https://health1.webmobi.in/socket/con";
//    private Socket mSocket;
//    {
//        try {
//         //   mSocket = IO.socket(url);
//
//        } catch (URISyntaxException e) {
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_quiz);
        viewPager2 = findViewById(R.id.view_pager);
        quiz_id = getIntent().getStringExtra("quiz_id");
        setMcqViewPager();
        getAllMcqData();
//        mSocket.connect();
//        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.d("quiz_connection_check", "connected  " + mSocket.id());
//                mSocket.emit("joinquiz", quiz_id);
//            }
//        });

    }

    private void getAllMcqData() {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.show();
        Health1NetworkController.getInstance().getService().getAllQuiz(app_id).enqueue(new Callback<QuizModel>() {
            @Override
            public void onResponse(Call<QuizModel> call, Response<QuizModel> response) {
                if (response.isSuccessful()) {
                    QuizModel quizModel = response.body();
                    if (quizModel.getResponse()) {
                        quizList = quizModel.getData();
                        for (int i = 0; i < quizList.size(); i++) {
                            if (quizList.get(i).getQuizId().equals(quiz_id)) {
                                questionArrays = quizList.get(i).getQuestionArray();
                                for (int j = 0; j < questionArrays.size(); j++) {
                                    if(questionArrays.get(j).getIsActive()==1) {
                                        String options = questionArrays.get(j).getOptions();
                                        Log.d("see_options_", "onResponse: \n" + options);
                                        options_array = Arrays.asList(options.split(","));
                                        List<QuizAnswerPojo> answerPojos = new ArrayList<>();
                                        for (int k = 0; k < options_array.size(); k++) {
                                            answerPojos.add(new QuizAnswerPojo(options_array.get(k)));
                                        }
                                        quizQuestionPojos.add(new QuizQuestionPojo(questionArrays.get(j).getQuestion(),
                                                questionArrays.get(j).getQuestionId(),quiz_id,
                                                answerPojos,quizList.get(i).getQuizName()));
                                        questionLoaderAdapter.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                    }
                                }
                            }
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(LoadQuizActivity.this, quizModel.getResponseString(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    progressDialog.dismiss();

                    Toast.makeText(LoadQuizActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<QuizModel> call, Throwable t) {
                progressDialog.dismiss();

                Toast.makeText(LoadQuizActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        mSocket.disconnect();
        clearSharedPref();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        clearSharedPref();
        super.onDestroy();
    }

    private void setMcqViewPager() {
        questionLoaderAdapter = new QuestionLoaderCollectionAdapter(this,quizQuestionPojos,viewPager2);
        viewPager2.setAdapter(questionLoaderAdapter);
        viewPager2.setUserInputEnabled(false);
    }

    public void nextPage(View view) {
        viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, true);
    }

    private void clearSharedPref(){
        SharedPreferences sharedPreferences =getSharedPreferences("MCQ_Quiz_Answer", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}