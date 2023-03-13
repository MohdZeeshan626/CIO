package com.singleevent.sdk.pojo.quizmodulepojo;



import com.singleevent.sdk.pojo.pollingpojo.PollingResultPojo;

import java.util.List;

public class QuizResultMainPojo {
    String questionName;
    String question_id;
    int numberOfParticipants;
    List<PollingResultPojo> resultPojos;

    public QuizResultMainPojo(String questionName, String question_id, int numberOfParticipants, List<PollingResultPojo> resultPojos) {
        this.questionName = questionName;
        this.question_id = question_id;
        this.numberOfParticipants = numberOfParticipants;
        this.resultPojos = resultPojos;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public List<PollingResultPojo> getResultPojos() {
        return resultPojos;
    }

    public void setResultPojos(List<PollingResultPojo> resultPojos) {
        this.resultPojos = resultPojos;
    }
}
