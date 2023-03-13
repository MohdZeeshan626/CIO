package com.singleevent.sdk.model.Polling;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VoteModel {
    @SerializedName("response")
    @Expose
    private Boolean response;
    @SerializedName("responseString")
    @Expose
    private String responseString;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
    public class Datum {

        @SerializedName("total_votes")
        @Expose
        private Integer totalVotes;
        @SerializedName("votes")
        @Expose
        private String votes;
        @SerializedName("poll_id")
        @Expose
        private String pollId;
        @SerializedName("app_id")
        @Expose
        private String appId;

        public Integer getTotalVotes() {
            return totalVotes;
        }

        public void setTotalVotes(Integer totalVotes) {
            this.totalVotes = totalVotes;
        }

        public String getVotes() {
            return votes;
        }

        public void setVotes(String votes) {
            this.votes = votes;
        }

        public String getPollId() {
            return pollId;
        }

        public void setPollId(String pollId) {
            this.pollId = pollId;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

    }
}
