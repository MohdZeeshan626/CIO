package com.singleevent.sdk.View.LeftActivity.pollingActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import com.singleevent.sdk.App;
import com.singleevent.sdk.Left_Adapter.pollingadapters.pollingModuleAdapter.LoadPollQuestionsAdapter;
import com.singleevent.sdk.R;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Polling.PollingModel;
import com.singleevent.sdk.pojo.pollingpojo.PollingAnswersPojo;
import com.singleevent.sdk.pojo.pollingpojo.PollingQuestionsPojo;
import com.singleevent.sdk.service.Health1NetworkController;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadPollActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    String poll_id="";
    String user_id;
    LoadPollQuestionsAdapter questionsAdapter;
    private final String app_id = "4d066a5185b2e7c70c63601091f5ac45a197";
    List<PollingModel.Poll> pollList = new ArrayList<>();
    List<PollingQuestionsPojo> questionsPojos = new ArrayList<>();
    boolean check;
    AppDetails appDetails;
    private Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_poll);
        initViews();
        setpollViewPager();
        try {
           // mSocket = App.getInstance().getmSocketHealth();
//            mSocket.connect();
//            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    Log.d("chcel", "connected  " + mSocket.id());
//                    mSocket.emit("joinpoll", poll_id);
//                }
//            });
        }catch (Exception e){}
        getPollingData();

    }

    private void setpollViewPager() {
        questionsAdapter=new LoadPollQuestionsAdapter(this,questionsPojos);
        viewPager2.setAdapter(questionsAdapter);
        viewPager2.setUserInputEnabled(false);

    }

    private void getPollingData() {
        String ActiveStatus = "true";
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
//        appDetails.getAppId()
        Health1NetworkController.getInstance().getService().getPolls(app_id).enqueue(new Callback<PollingModel>() {
            @Override
            public void onResponse(Call<PollingModel> call, Response<PollingModel> response) {
                if (response.isSuccessful()) {
                    questionsPojos.clear();
                    PollingModel pollingModel = response.body();
                    if (pollingModel.getResponse()) {
                        pollList = pollingModel.getPolls();
                        for (int i = 0; i < pollList.size(); i++) {
                            if(pollList.get(i).getPollId().equals(poll_id)) {
                                if (pollList.get(i).getActiveStatus().equals(ActiveStatus)) {
                                    String answers = pollList.get(i).getVotes();
                                    String participants = pollList.get(i).getVotedBy();
                                    check = participants.contains(user_id);

                                    List<PollingAnswersPojo> answersPojos = new ArrayList<>();
                                    try {
                                        JSONObject obj = new JSONObject(answers);
                                        Log.d("gggg", "onBindViewHolder: " + obj);
                                        Iterator<String> keysItr = obj.keys();
                                        while (keysItr.hasNext()) {
                                            String key = keysItr.next();
                                            //answersPojos.add(new PollingAnswersPojo(key,poll_id));
                                            Log.d("ggg_key", "onBindViewHolder: " + key);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (!check) {
                                        questionsPojos.add(new PollingQuestionsPojo(pollList.get(i).getQuestion(), answersPojos, pollList.get(i).getPollId(), false));
                                    } else if (check) {
                                        questionsPojos.add(new PollingQuestionsPojo(pollList.get(i).getQuestion(), answersPojos, pollList.get(i).getPollId(), true));
                                    }

                                    progressDialog.dismiss();
                                    questionsAdapter.notifyDataSetChanged();
                                }
                                break;
                            }
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoadPollActivity.this, pollingModel.getResponseString(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    progressDialog.dismiss();

                    Toast.makeText(LoadPollActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PollingModel> call, Throwable t) {
                progressDialog.dismiss();

                Toast.makeText(LoadPollActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void initViews() {
        poll_id=getIntent().getStringExtra("poll_id");
        user_id = Paper.book().read("userId", "");
        appDetails = Paper.book().read("Appdetails");
        viewPager2 = findViewById(R.id.view_pager);

    }
}