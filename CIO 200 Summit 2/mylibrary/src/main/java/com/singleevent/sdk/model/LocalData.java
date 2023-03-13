package com.singleevent.sdk.model;

/**
 * Created by Admin on 8/10/2017.
 */

public class LocalData {


    String title;
    String des;
    long date;
    long fromtime;
    long totime;
    User user;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getFromtime() {
        return fromtime;
    }

    public void setFromtime(long fromtime) {
        this.fromtime = fromtime;
    }

    public long getTotime() {
        return totime;
    }

    public void setTotime(long totime) {
        this.totime = totime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
