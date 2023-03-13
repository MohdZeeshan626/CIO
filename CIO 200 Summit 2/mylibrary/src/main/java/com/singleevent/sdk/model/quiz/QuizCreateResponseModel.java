package com.singleevent.sdk.model.quiz;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizCreateResponseModel {
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

        @SerializedName("quiz_id")
        @Expose
        private String quizId;
        @SerializedName("app_id")
        @Expose
        private String appId;
        @SerializedName("active_status")
        @Expose
        private String activeStatus;
        @SerializedName("created_by")
        @Expose
        private String createdBy;
        @SerializedName("Questionids")
        @Expose
        private String questionids;
        @SerializedName("no_of_questions")
        @Expose
        private Integer noOfQuestions;
        @SerializedName("user_type")
        @Expose
        private String userType;

        public String getQuizId() {
            return quizId;
        }

        public void setQuizId(String quizId) {
            this.quizId = quizId;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getActiveStatus() {
            return activeStatus;
        }

        public void setActiveStatus(String activeStatus) {
            this.activeStatus = activeStatus;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getQuestionids() {
            return questionids;
        }

        public void setQuestionids(String questionids) {
            this.questionids = questionids;
        }

        public Integer getNoOfQuestions() {
            return noOfQuestions;
        }

        public void setNoOfQuestions(Integer noOfQuestions) {
            this.noOfQuestions = noOfQuestions;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

    }
}
