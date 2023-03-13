package com.singleevent.sdk.health.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class passwordModel {
    @SerializedName("response")
    @Expose
    private boolean response;

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public password_response getResponseString() {
        return responseString;
    }

    public void setResponseString(password_response responseString) {
        this.responseString = responseString;
    }

    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data) {
        this.data = data;
    }

    public passwordModel(boolean response, password_response responseString, ArrayList data) {
        this.response = response;
        this.responseString = responseString;
        this.data = data;
    }

    @SerializedName("responseString")
    @Expose
    private password_response responseString;

    @SerializedName("data")
    @Expose
    private ArrayList data;
}
