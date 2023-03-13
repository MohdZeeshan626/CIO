package com.singleevent.sdk.View.RightActivity.admin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SegmentModel {

    @SerializedName("response")
    @Expose
    private Boolean response;
    @SerializedName("responseString")
    @Expose
    private List<ResponseString> responseString = null;

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public List<ResponseString> getResponseString() {
        return responseString;
    }

    public void setResponseString(List<ResponseString> responseString) {
        this.responseString = responseString;
    }

}


