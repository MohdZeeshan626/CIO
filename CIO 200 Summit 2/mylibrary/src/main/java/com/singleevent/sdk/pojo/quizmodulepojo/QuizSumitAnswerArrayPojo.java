package com.singleevent.sdk.pojo.quizmodulepojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizSumitAnswerArrayPojo {
    @SerializedName("question_id")
    @Expose
    private String questionId;
    @SerializedName("answer")
    @Expose
    private String answer;

    public QuizSumitAnswerArrayPojo(String questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
