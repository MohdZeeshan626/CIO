package com.singleevent.sdk.model.LeaderBoard;

import java.io.Serializable;

/**
 * Created by webMOBI on 1/5/2018.
 */

public class LeaderBoard implements Serializable {

    private String first_name,last_name,email,userid,appid,app_type,user_type,admin_flag,permission_level
            ,checkin_status,leads,profile_pic;

    private int checkin_points;
    private int total_points,session_checkin_count,session_checkin_points,
            ask_count,ask_points,survey_count,survey_points,polling_count,polling_points,moodometer_count,moodometer_points,login_points,profile_points,other_points;



    private int attendee_option;
    private int login_count;
    private int post_count;
    private int post_points;
    private int like_count;
    private int like_points;
    private int comment_count;
    private int comment_points;
    private int lead_count;
    private int lead_points;

    public int getSession_checkin_count() {
        return session_checkin_count;
    }

    public void setSession_checkin_count(int session_checkin_count) {
        this.session_checkin_count = session_checkin_count;
    }

    public int getSession_checkin_points() {
        return session_checkin_points;
    }

    public void setSession_checkin_points(int session_checkin_points) {
        this.session_checkin_points = session_checkin_points;
    }

    public int getAsk_count() {
        return ask_count;
    }

    public void setAsk_count(int ask_count) {
        this.ask_count = ask_count;
    }

    public int getAsk_points() {
        return ask_points;
    }

    public void setAsk_points(int ask_points) {
        this.ask_points = ask_points;
    }

    public int getSurvey_count() {
        return survey_count;
    }

    public void setSurvey_count(int survey_count) {
        this.survey_count = survey_count;
    }

    public int getSurvey_points() {
        return survey_points;
    }

    public void setSurvey_points(int survey_points) {
        this.survey_points = survey_points;
    }

    public int getPolling_count() {
        return polling_count;
    }

    public void setPolling_count(int polling_count) {
        this.polling_count = polling_count;
    }

    public int getPolling_points() {
        return polling_points;
    }

    public void setPolling_points(int polling_points) {
        this.polling_points = polling_points;
    }

    public int getMoodometer_count() {
        return moodometer_count;
    }

    public void setMoodometer_count(int moodometer_count) {
        this.moodometer_count = moodometer_count;
    }

    public int getMoodometer_points() {
        return moodometer_points;
    }

    public void setMoodometer_points(int moodometer_points) {
        this.moodometer_points = moodometer_points;
    }

    public int getLogin_points() {
        return login_points;
    }

    public void setLogin_points(int login_points) {
        this.login_points = login_points;
    }

    public int getProfile_points() {
        return profile_points;
    }

    public void setProfile_points(int profile_points) {
        this.profile_points = profile_points;
    }

    public int getOther_points() {
        return other_points;
    }

    public void setOther_points(int other_points) {
        this.other_points = other_points;
    }

    public int getCheckin_points() {
        return checkin_points;
    }

    public void setCheckin_points(int checkin_points) {
        this.checkin_points = checkin_points;
    }


    //private int[] schedules,exhibitor_favorites,sponsor_favorites;



    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getApp_type() {
        return app_type;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getAdmin_flag() {
        return admin_flag;
    }

    public void setAdmin_flag(String admin_flag) {
        this.admin_flag = admin_flag;
    }

    public String getPermission_level() {
        return permission_level;
    }

    public void setPermission_level(String permission_level) {
        this.permission_level = permission_level;
    }

    public String getCheckin_status() {
        return checkin_status;
    }

    public void setCheckin_status(String checkin_status) {
        this.checkin_status = checkin_status;
    }

    public String getLeads() {
        return leads;
    }

    public void setLeads(String leads) {
        this.leads = leads;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public int getAttendee_option() {
        return attendee_option;
    }

    public void setAttendee_option(int attendee_option) {
        this.attendee_option = attendee_option;
    }

    public int getLogin_count() {
        return login_count;
    }

    public void setLogin_count(int login_count) {
        this.login_count = login_count;
    }

    public int getPost_count() {
        return post_count;
    }

    public void setPost_count(int post_count) {
        this.post_count = post_count;
    }

    public int getPost_points() {
        return post_points;
    }

    public void setPost_points(int post_points) {
        this.post_points = post_points;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getLike_points() {
        return like_points;
    }

    public void setLike_points(int like_points) {
        this.like_points = like_points;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getComment_points() {
        return comment_points;
    }

    public void setComment_points(int comment_points) {
        this.comment_points = comment_points;
    }

    public int getLead_count() {
        return lead_count;
    }

    public void setLead_count(int lead_count) {
        this.lead_count = lead_count;
    }

    public int getLead_points() {
        return lead_points;
    }

    public void setLead_points(int lead_points) {
        this.lead_points = lead_points;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }



}//class end
 /*"user_app_table_id": "54c08fc0b45c1307c3e0eb36eac8590d79f0",
		"userid": "286fdb6d21f5d1a4f5cf729eb7841240",
		"appid": "4e4c4a3633dd9d86f7a9cf19029d4ff7681d",
		"app_type": "dashboard",
		"user_type": "eventuser",
		"admin_flag": "attendee&exhibitor",
		"attendee_option": 1,
		"permission_level": null,
		"checkin_status": "checkedin",
		"login_count": 0,
		"leads": null,
		"schedules": "[11,5,9]",
		"exhibitor_favorites": "[5, 7, 3]",
		"sponsor_favorites": "[]",
		"first_name": "Priyanka",
		"last_name": "a",
		"email": "priyanka@mendios.com",
		"profile_pic": "",
		"post_count": 43,
		"post_points": 344,
		"like_count": 11,
		"like_points": 22,
		"comment_count": 0,
		"comment_points": 0,
		"lead_count": 0,
		"lead_points": 0,
		"total_points": 366*/