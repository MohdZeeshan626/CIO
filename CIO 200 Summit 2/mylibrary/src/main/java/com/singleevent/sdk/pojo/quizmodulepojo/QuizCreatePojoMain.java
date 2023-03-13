package com.singleevent.sdk.pojo.quizmodulepojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizCreatePojoMain {
    @SerializedName("app_id")
    @Expose
    private String appId;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("customtheme")
    @Expose
    private Boolean customtheme;
    @SerializedName("quizarray")
    @Expose
    private List<QuizCreateQuestionPojo> quizarray = null;
    @SerializedName("quiz_name")
    @Expose
    private String quizName;

    public QuizCreatePojoMain(String appId, String createdBy, Boolean customtheme, List<QuizCreateQuestionPojo> quizarray, String quizName) {
        this.appId = appId;
        this.createdBy = createdBy;
        this.customtheme = customtheme;
        this.quizarray = quizarray;
        this.quizName = quizName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getCustomtheme() {
        return customtheme;
    }

    public void setCustomtheme(Boolean customtheme) {
        this.customtheme = customtheme;
    }

    public List<QuizCreateQuestionPojo> getQuizarray() {
        return quizarray;
    }

    public void setQuizarray(List<QuizCreateQuestionPojo> quizarray) {
        this.quizarray = quizarray;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

}
