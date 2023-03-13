package com.singleevent.sdk.pojo.pollingpojo;

import java.util.List;

public class PollingResultMainPojo {
    String questionName;
    int numberOfParticipants;
    List<PollingResultPojo> resultPojos;

    public PollingResultMainPojo(String questionName, int numberOfParticipants, List<PollingResultPojo> resultPojos) {
        this.questionName = questionName;
        this.numberOfParticipants = numberOfParticipants;
        this.resultPojos = resultPojos;
    }

    public String getQuestionName() {
        return questionName;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public List<PollingResultPojo> getResultPojos() {
        return resultPojos;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public void setResultPojos(List<PollingResultPojo> resultPojos) {
        this.resultPojos = resultPojos;
    }
}
