package com.singleevent.sdk.model.Polling;


import java.io.Serializable;

public class Report implements Serializable {

    private Pollresult[] pollresult;

    private String polling_id;
    private String total_count;

    String question;
    String[] answer;
    String type;
    String detail;
    int id;

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }

    public Report(Pollresult[] pollresult, int id, String question, String[] answer, String type, String detail, Boolean isAnsSubmitted) {
        this.pollresult = pollresult;
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.type = type;
        this.detail = detail;
        this.isAnsSubmitted = isAnsSubmitted;
    }


    public Report(Pollresult[] pollresult, int id, String question, String[] answer, String type, String detail, Boolean isAnsSubmitted,String total_count,String polling_id) {
        this.pollresult = pollresult;
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.type = type;
        this.detail = detail;
        this.isAnsSubmitted = isAnsSubmitted;
        this.total_count = total_count;
        this.polling_id = polling_id;
    }

    public Boolean getAnsSubmitted() {

        return isAnsSubmitted;
    }

    public void setAnsSubmitted(Boolean ansSubmitted) {
        isAnsSubmitted = ansSubmitted;
    }

    Boolean isAnsSubmitted;

    public String[] getAnswer() {
        return answer;
    }

    public void setAnswer(String[] answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getDetail() {
        return detail;
    }

    public String getType() {
        return type;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pollresult[] getPollresult ()
    {
        return pollresult;
    }

    public void setPollresult (Pollresult[] pollresult)
    {
        this.pollresult = pollresult;
    }

    public String getPolling_id ()
    {
        return polling_id;
    }

    public void setPolling_id (String polling_id)
    {
        this.polling_id = polling_id;
    }
}
