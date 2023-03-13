package com.singleevent.sdk.pojo.quizmodulepojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizAnswerSubmittingPojo {
    @SerializedName("quiz_id")
    @Expose
    private String quizId;
    @SerializedName("app_id")
    @Expose
    private String appId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("qn_activation_time")
    @Expose
    private String qnActivationTime;
    @SerializedName("answer_array")
    @Expose
    private List<QuizSumitAnswerArrayPojo> answerArray;

    public QuizAnswerSubmittingPojo(String quizId, String appId, String userId, String qnActivationTime, List<QuizSumitAnswerArrayPojo> answerArray) {
        this.quizId = quizId;
        this.appId = appId;
        this.userId = userId;
        this.qnActivationTime = qnActivationTime;
        this.answerArray = answerArray;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQnActivationTime() {
        return qnActivationTime;
    }

    public void setQnActivationTime(String qnActivationTime) {
        this.qnActivationTime = qnActivationTime;
    }

    public List<QuizSumitAnswerArrayPojo> getAnswerArray() {
        return answerArray;
    }

    public void setAnswerArray(List<QuizSumitAnswerArrayPojo> answerArray) {
        this.answerArray = answerArray;
    }

}
