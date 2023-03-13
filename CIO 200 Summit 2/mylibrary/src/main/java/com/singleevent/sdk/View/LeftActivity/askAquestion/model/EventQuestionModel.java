package com.singleevent.sdk.View.LeftActivity.askAquestion.model;

import java.io.Serializable;

/**
 * Created by webMOBI on 2/13/2018.
 */

public class EventQuestionModel implements Serializable {

    private String Question;
    private String Answer;
    private int up_votes;
    private boolean user_vote;
    private int question_id;
    private String appid ;
    private String userid ;
    private int agenda_id;

    public boolean isUser_vote() {
        return user_vote;
    }

    public int getUp_votes() {
        return up_votes;
    }

    public void setUp_votes(int up_votes) {
        this.up_votes = up_votes;
    }


    public void setUser_vote(boolean user_vote) {
        this.user_vote = user_vote;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getAgenda_id() {
        return agenda_id;
    }

    public void setAgenda_id(int agenda_id) {
        this.agenda_id = agenda_id;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public int getVote() {
        return up_votes;
    }


}
