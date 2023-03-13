package com.singleevent.sdk.health.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class loginModel {
    @SerializedName("response")
    @Expose
    private boolean response;

    @SerializedName("responseString")
    @Expose
    private String responseString;

    @SerializedName("data")
    @Expose
    private ArrayList data;

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }

    public loginModel(boolean response, String responseString, ArrayList data) {
        this.response = response;
        this.responseString = responseString;
        this.data = data;
    }
}
