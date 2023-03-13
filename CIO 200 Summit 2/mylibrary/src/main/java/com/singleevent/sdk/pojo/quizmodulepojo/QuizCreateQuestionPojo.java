package com.singleevent.sdk.pojo.quizmodulepojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizCreateQuestionPojo {
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("options")
    @Expose
    private String options;
    @SerializedName("correctanswer")
    @Expose
    private String correctanswer;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

    public QuizCreateQuestionPojo(String question, String options, String correctanswer, Boolean isActive) {
        this.question = question;
        this.options = options;
        this.correctanswer = correctanswer;
        this.isActive = isActive;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getCorrectanswer() {
        return correctanswer;
    }

    public void setCorrectanswer(String correctanswer) {
        this.correctanswer = correctanswer;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
