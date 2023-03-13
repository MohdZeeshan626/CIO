package com.singleevent.sdk.pojo.pollingpojo;

import java.util.List;

public class PollingQuestionsPojo {
    String questions;
    List<PollingAnswersPojo> answersArray;
    String poll_id;
    boolean answered;
    public PollingQuestionsPojo(String questions,List<PollingAnswersPojo> answersArray, String poll_id, boolean answered) {
        this.questions = questions;
        this.answersArray = answersArray;
        this.poll_id = poll_id;
        this.answered=answered;
    }

    public boolean isAnswered() {
        return answered;
    }

    public String getQuestions() {
        return questions;
    }

    public List<PollingAnswersPojo> getAnswersArray() {
        return answersArray;
    }

    public String getPoll_id() {
        return poll_id;
    }
}
