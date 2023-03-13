package com.singleevent.sdk.model.Polling;

/**
 * Created by Admin on 10/20/2016.
 */
public class polling {

    String question;
    String[] answer;
    String type;
    String detail;
    int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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


}
