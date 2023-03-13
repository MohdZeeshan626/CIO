package com.singleevent.sdk.model.Polling;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatePollResponse {
    @SerializedName("response")
    @Expose
    private Boolean response;
    @SerializedName("responseString")
    @Expose
    private String responseString;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public class Data {

        @SerializedName("poll_id")
        @Expose
        private String pollId;
        @SerializedName("question")
        @Expose
        private String question;
        @SerializedName("created_by")
        @Expose
        private String createdBy;
        @SerializedName("options")
        @Expose
        private String options;
        @SerializedName("votes")
        @Expose
        private String votes;
        @SerializedName("correct_answer")
        @Expose
        private String correctAnswer;
        @SerializedName("app_id")
        @Expose
        private String appId;
        @SerializedName("require_auth")
        @Expose
        private String requireAuth;
        @SerializedName("voting_restriction")
        @Expose
        private String votingRestriction;
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("active_status")
        @Expose
        private String activeStatus;

        public String getPollId() {
            return pollId;
        }

        public void setPollId(String pollId) {
            this.pollId = pollId;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getOptions() {
            return options;
        }

        public void setOptions(String options) {
            this.options = options;
        }

        public String getVotes() {
            return votes;
        }

        public void setVotes(String votes) {
            this.votes = votes;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getRequireAuth() {
            return requireAuth;
        }

        public void setRequireAuth(String requireAuth) {
            this.requireAuth = requireAuth;
        }

        public String getVotingRestriction() {
            return votingRestriction;
        }

        public void setVotingRestriction(String votingRestriction) {
            this.votingRestriction = votingRestriction;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getActiveStatus() {
            return activeStatus;
        }

        public void setActiveStatus(String activeStatus) {
            this.activeStatus = activeStatus;
        }

    }
}
