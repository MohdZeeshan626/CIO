package com.singleevent.sdk;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.singleevent.sdk.service.Health1NetworkController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Response;

public class FeedPost {
    List<String> check = new ArrayList<>();


    public boolean checkCorrectAnswer(List<String> options_list, List<String> correctOption) {
        if (correctOption.size() > 0) {
            for (int i = 0; i < options_list.size(); i++) {
                if (correctOption.get(0).equals(options_list.get(i))) {
                    return true;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    public boolean checkStringContainUrl(String content) {
        String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(content);//replace with string to compare
        return m.find();
    }

    public boolean activePollQuestion(String pollId) {

        try {
            Response<JsonObject> response = Health1NetworkController.getInstance().getService().changePollActiveStatus("4d066a5185b2e7c70c63601091f5ac45a197", pollId).execute();
            if (response.code() == 200) {
                JSONObject jsonObject = new JSONObject(response.body().toString());
                return jsonObject.getBoolean("response");
            }else {
                return false;
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }

    }

    public int activePollQuestionStatus_check(String pollId) {
        try {
            Response<JsonObject> response = Health1NetworkController.getInstance().getService().changePollActiveStatus("4d066a5185b2e7c70c63601091f5ac45a197", pollId).execute();
           return response.code();


        } catch (IOException e) {
            e.printStackTrace();
           return 0;
        }

    }


}
