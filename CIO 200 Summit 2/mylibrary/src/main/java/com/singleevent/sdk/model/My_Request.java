package com.singleevent.sdk.model;

import java.io.Serializable;

/**
 * Created by webMOBI on 10/10/2017.
 */

public class My_Request implements Serializable{

    String table_id;
    String appid;
    String userid;
    String username;
    String email;
    String profile_pic;
    String title;
    String description;
    String booking_status;
    long meeting_date;
    long from_time;
    long to_time;


    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_pic() {
        if (profile_pic == null)
            return "";
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public long getMeeting_date() {
        return meeting_date;
    }

    public void setMeeting_date(long meeting_date) {
        this.meeting_date = meeting_date;
    }

    public long getFrom_time() {
        return from_time;
    }

    public void setFrom_time(long from_time) {
        this.from_time = from_time;
    }

    public long getTo_time() {
        return to_time;
    }

    public void setTo_time(long to_time) {
        this.to_time = to_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof My_Request)) return false;

        My_Request that = (My_Request) o;

        return getTable_id().equals(that.getTable_id());

    }

    @Override
    public int hashCode() {
        return getTable_id().hashCode();
    }
}
