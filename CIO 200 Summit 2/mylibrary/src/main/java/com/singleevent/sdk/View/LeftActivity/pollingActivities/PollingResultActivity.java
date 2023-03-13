package com.singleevent.sdk.View.LeftActivity.pollingActivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.singleevent.sdk.App;
//import com.singleevent.sdk.Left_Adapter.pollingadapters.pollingModuleAdapter.PollingResultMainAdapter;
import com.singleevent.sdk.R;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Polling.PollingModel;
import com.singleevent.sdk.pojo.pollingpojo.PollingResultMainPojo;
import com.singleevent.sdk.pojo.pollingpojo.PollingResultPojo;
import com.singleevent.sdk.service.Health1NetworkController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.paperdb.Paper;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollingResultActivity extends AppCompatActivity {
    RecyclerView poll_result_recycler;
    //PollingResultMainAdapter pollingResultMainAdapter;
    List<PollingModel.Poll> pollList = new ArrayList<>();
    private final String app_id = "4d066a5185b2e7c70c63601091f5ac45a197";

    AppDetails appDetails;
    List<String> user_id_array = new ArrayList<>();
    List<PollingResultMainPojo> resultMainPojos = new ArrayList<>();
    boolean check;
    String user_id;
    String poll_id, questions;
    SharedPreferences sharedPreferencesLogin;
    private Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling_result);
        initViews();
        setPollingResultRecyclerView();
        getPollingData();

        try {
//            mSocket=App.getInstance().getmSocketHealth();
//            mSocket.connect();
//            mSocket.on(io.socket.client.Socket.EVENT_CONNECT, new io.socket.emitter.Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    Log.d("check_socketid", "connected  " + mSocket.id());
//                    mSocket.emit("joinpoll", poll_id);
//                }
//            });
//            if (mSocket.connected()) {
//                Log.d("check_connection", "onCreatehhh: " + mSocket.connected() + "asdf");
//            }
////            mSocket.on(Socket.EVENT_CONNECT, onConnect);
//            mSocket.on("livepoll2021", onpolling);

        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        mSocket.disconnect();
        Log.d("check_connection", "onCreatehhh: " + mSocket.connected() + "asdf");
        super.onDestroy();
    }

    private final Emitter.Listener onpolling = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resultMainPojos.clear();
                    JSONObject jsonObject = (JSONObject) args[0];
                    try {
                        JSONArray array = jsonObject.getJSONArray("votes");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject1 = array.getJSONObject(i);
                            int total_vote = jsonObject1.getInt("total_votes");
                            String votes = jsonObject1.getString("votes");
                            List<PollingResultPojo> resultPojos = new ArrayList<>();
                            try {
                                JSONObject obj = new JSONObject(votes);
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
                            resultMainPojos.add(new PollingResultMainPojo(questions, total_vote, resultPojos));
                        }
                       // pollingResultMainAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("chcel", "run: " + "jjjj" + jsonObject);
                }
            });

        }
    };

    private final Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            JSONObject obj = new JSONObject();
            try {
                obj.put("poll_id", poll_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("chcel", "connected  " + mSocket.id());
            mSocket.emit("joinpoll", poll_id);

//                }
//            });


        }
    };


    private void initViews() {
        poll_result_recycler = findViewById(R.id.poll_result_recycler);
        sharedPreferencesLogin = getSharedPreferences("login", Context.MODE_PRIVATE);
        appDetails = Paper.book().read("Appdetails");

        user_id = Paper.book().read("userId", "");
        poll_id = getIntent().getStringExtra("poll_id");
        questions = getIntent().getStringExtra("question_name");

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
                    resultMainPojos.clear();
                    PollingModel pollingModel = response.body();
                    if (pollingModel.getResponse()) {
                        pollList = pollingModel.getPolls();
                        for (int i = 0; i < pollList.size(); i++) {
                            if (pollList.get(i).getActiveStatus().equals(ActiveStatus)) {
                                if (pollList.get(i).getPollId().equals(poll_id)) {
                                    String answers = pollList.get(i).getVotes();
                                    int total_vote = pollList.get(i).getTotalVotes();
                                    String participants = pollList.get(i).getVotedBy();
                                    check = participants.contains(user_id);
                                    List<PollingResultPojo> resultPojos = new ArrayList<>();
                                    try {
                                        JSONObject obj = new JSONObject(answers);
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
                                    if (check) {
                                        resultMainPojos.add(new PollingResultMainPojo(pollList.get(i).getQuestion(), total_vote, resultPojos));
                                    }
                                    progressDialog.dismiss();
                                   // pollingResultMainAdapter.notifyDataSetChanged();
                                    break;
                                } else {
                                    progressDialog.dismiss();
                                }
                            }
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(PollingResultActivity.this, pollingModel.getResponseString(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(PollingResultActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PollingModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PollingResultActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void setPollingResultRecyclerView() {
        poll_result_recycler.setLayoutManager(new LinearLayoutManager(this));
     //   pollingResultMainAdapter = new PollingResultMainAdapter(this, resultMainPojos);
      //  poll_result_recycler.setAdapter(pollingResultMainAdapter);

    }
}