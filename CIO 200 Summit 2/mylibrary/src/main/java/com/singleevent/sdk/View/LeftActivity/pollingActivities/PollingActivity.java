package com.singleevent.sdk.View.LeftActivity.pollingActivities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.singleevent.sdk.Left_Adapter.pollingadapters.pollingModuleAdapter.PollingAdapter;
import com.singleevent.sdk.Left_Adapter.pollingadapters.pollingModuleAdapter.PollingAdapterMainList;
import com.singleevent.sdk.R;
import com.singleevent.sdk.model.AppDetails;
import com.singleevent.sdk.model.Polling.PollingModel;
import com.singleevent.sdk.pojo.pollingpojo.PollingAnswersPojo;
import com.singleevent.sdk.pojo.pollingpojo.PollingQuestionsPojo;
import com.singleevent.sdk.service.Health1NetworkController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollingActivity extends AppCompatActivity {
    RecyclerView polling_view;
//        PollingAdapter pollingAdapter;
    PollingAdapterMainList pollingAdapter;
    List<PollingModel.Poll> pollList = new ArrayList<>();
    List<PollingQuestionsPojo> questionsPojos = new ArrayList<>();
    List<String> user_id_array = new ArrayList<>();
    private final String app_id = "4d066a5185b2e7c70c63601091f5ac45a197";
    boolean check;
    String user_id;
    SharedPreferences sharedPreferencesLogin;
    AppDetails appDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling);
        polling_view = findViewById(R.id.polling_view);
        appDetails = Paper.book().read("Appdetails");

        //sharedPreferencesLogin = getSharedPreferences("login", Context.MODE_PRIVATE);
        user_id = Paper.book().read("userId", "");
        setPollingRecyclerView();
        getPollingData();

    }

    private void getPollingData() {
        String ActiveStatus = "true";
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        Health1NetworkController.getInstance().getService().getPolls(app_id).enqueue(new Callback<PollingModel>() {
            @Override
            public void onResponse(Call<PollingModel> call, Response<PollingModel> response) {
                if (response.isSuccessful()) {
                    questionsPojos.clear();
                    PollingModel pollingModel = response.body();
                    if (pollingModel.getResponse()) {
                        pollList = pollingModel.getPolls();
                        for (int i = 0; i < pollList.size(); i++) {
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
                                        answersPojos.add(new PollingAnswersPojo(key));
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
                                pollingAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(PollingActivity.this, pollingModel.getResponseString(), Toast.LENGTH_SHORT).show();

                    }
                } else {
                    progressDialog.dismiss();

                    Toast.makeText(PollingActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PollingModel> call, Throwable t) {
                progressDialog.dismiss();

                Toast.makeText(PollingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onBackPressed() {
        clearSharedPref();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        clearSharedPref();
        super.onDestroy();
    }

    private void setPollingRecyclerView() {
        polling_view.setLayoutManager(new LinearLayoutManager(this));
        pollingAdapter = new PollingAdapterMainList(questionsPojos, this);
        polling_view.setAdapter(pollingAdapter);
    }

    private void clearSharedPref() {
        SharedPreferences sharedPreferences = getSharedPreferences("poll", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}