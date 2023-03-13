package com.singleevent.sdk.View.RightActivity.admin.adminSurvey.model;

import java.io.Serializable;

public class AdminSurveyModel implements Serializable {

    /*"appid": "ad86ab4c3ea3afade500bea97f2ec07f847f",
	"adminsurvey_flag": "1",
	"default_id": null,
	"detail": "no",
	"type": "multiple",
	"reg_id": 1,
	"user_type": "default",
	"question": "How are you?",
	"answer": "[\"Fine\",\"Not fine\"]",
	"adminsurvey_type": "attendee"*/
    private String appid;
    private String adminsurvey_flag;
    private String detail;
    private String type;
    private int reg_id;
    private String user_type;
    private String question;
    private String[] answer;
    private String adminsurvey_type;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAdminsurvey_flag() {
        return adminsurvey_flag;
    }

    public void setAdminsurvey_flag(String adminsurvey_flag) {
        this.adminsurvey_flag = adminsurvey_flag;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getReg_id() {
        return reg_id;
    }

    public void setReg_id(int reg_id) {
        this.reg_id = reg_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswer() {
        return answer;

    }

    public int answerLength() {
        return answer.length;
    }

    public void setAnswer(String[] answer) {
        this.answer = answer;
    }

    public String getAdminsurvey_type() {
        return adminsurvey_type;
    }

    public void setAdminsurvey_type(String adminsurvey_type) {
        this.adminsurvey_type = adminsurvey_type;
    }
}
