package com.singleevent.sdk.View.LeftActivity.QuizModule;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


//import com.singleevent.sdk.Left_Adapter.quizmoduleadapter.QuizResultMainAdapter;
import com.singleevent.sdk.R;
import com.singleevent.sdk.model.quiz.QuizModel;
import com.singleevent.sdk.pojo.pollingpojo.PollingResultPojo;
import com.singleevent.sdk.pojo.quizmodulepojo.QuizResultMainPojo;
import com.singleevent.sdk.service.Health1NetworkController;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import com.github.nkzawa.emitter.Emitter;
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizResultActivity extends AppCompatActivity {
    TextView correct, incorrect;
    RecyclerView quiz_result_recycler;
    String quiz_id, quiz_name;
   // QuizResultMainAdapter quizResultMainAdapter;
    private final String app_id = "4d066a5185b2e7c70c63601091f5ac45a197";
    List<QuizModel.QuestionArray> questionArrays = new ArrayList<>();
    List<QuizModel.Datum> quizList = new ArrayList<>();
    List<QuizResultMainPojo> resultMainPojos = new ArrayList<>();
    String url = "https://health1.webmobi.in/socket/con";
//    private Socket mSocket;
//    {
//        try {
//            mSocket = IO.socket(url);
//
//        } catch (URISyntaxException e) {
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        initViews();
        setResultRecycler();
        getQuizResultData();

    }

    private void getQuizResultData() {
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
                                        String options = questionArrays.get(j).getVotes();
                                        int total_vote=questionArrays.get(j).getTotalVotes();
                                        List<PollingResultPojo> resultPojos = new ArrayList<>();
                                        try {
                                            JSONObject obj = new JSONObject(options);
                                            Log.d("gggg", "onBindViewHolder: " + obj);
                                            Iterator<String> keysItr = obj.keys();
                                            while (keysItr.hasNext()) {
                                                String key = keysItr.next();
                                                Object value = obj.get(key);
                                                int count = (Integer) value;
                                                Log.d("ggg_key", "onBindViewHolder: " + key + "\n" + count);
                                                resultPojos.add(new PollingResultPojo(key, count, total_vote));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        resultMainPojos.add(new QuizResultMainPojo(questionArrays.get(j).getQuestion(), questionArrays.get(j).getQuestionId(),total_vote, resultPojos));
                                        //quizResultMainAdapter.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                    }
                                }
                            }
                        }
                    } else {
                        progressDialog.dismiss();

                        Toast.makeText(QuizResultActivity.this, quizModel.getResponseString(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    progressDialog.dismiss();

                    Toast.makeText(QuizResultActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<QuizModel> call, Throwable t) {
                progressDialog.dismiss();

                Toast.makeText(QuizResultActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setResultRecycler() {
        quiz_result_recycler.setLayoutManager(new LinearLayoutManager(this));
        //quizResultMainAdapter=new QuizResultMainAdapter(this,resultMainPojos);
        //quiz_result_recycler.setAdapter(quizResultMainAdapter);
    }

    private void initViews() {
        correct = findViewById(R.id.correct_ans);
        incorrect = findViewById(R.id.incorrect_ans);
        quiz_result_recycler = findViewById(R.id.quiz_result_recycler);
        quiz_id = getIntent().getStringExtra("quiz_id");
        quiz_name = getIntent().getStringExtra("quiz_name");
        //        int correctAns = getIntent().getIntExtra("correctAns", 0);
//        int incorrectAns = getIntent().getIntExtra("incorrectAns", 0);
//        correct.setText("correct answers : " + correctAns);
//        incorrect.setText("incorrect answers : " + incorrectAns);
    }

  //  @Override
  //  protected void onDestroy() {
//        mSocket.off();
//        mSocket.disconnect();
       // super.onDestroy();
  //  }
}