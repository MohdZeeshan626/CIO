package com.singleevent.sdk.model.quiz;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitQuizAnswerModel {
    @SerializedName("response")
    @Expose
    private Boolean response;
    @SerializedName("responseString")
    @Expose
    private String responseString;
    @SerializedName("correctscore")
    @Expose
    private Integer correctscore;
    @SerializedName("incorrectscore")
    @Expose
    private Integer incorrectscore;

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public Integer getCorrectscore() {
        return correctscore;
    }

    public void setCorrectscore(Integer correctscore) {
        this.correctscore = correctscore;
    }

    public Integer getIncorrectscore() {
        return incorrectscore;
    }

    public void setIncorrectscore(Integer incorrectscore) {
        this.incorrectscore = incorrectscore;
    }
}
