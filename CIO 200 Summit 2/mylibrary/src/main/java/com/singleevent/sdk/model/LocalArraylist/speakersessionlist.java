package com.singleevent.sdk.model.LocalArraylist;

import java.io.Serializable;

/**
 * Created by webmodi on 4/11/2016.
 */
public class speakersessionlist implements Serializable {

    String date=null;
    String title=null;
    String time=null;
    String topic;
    String des;
    String pdflink;
    String location;

    public int getAgendaId() {
        return agendaId;
    }

    public String getPdflink() {
        if (pdflink == null)
            return "";
        else
            return pdflink;
    }

    public String getDes() {
        return des;
    }

    public String getTopic() {
        if (topic == null) return "";
        else
            return topic;
    }

    int agendaId;

    public String getLocation() {
        return location;
    }

    public speakersessionlist(String date, String title, String time, String topic, String des, String pdflink, String location, int agendaId) {
        this. date = date;
        this.title = title;
        this.time=time;
        this.topic=topic;
        this.des=des;
        this.pdflink=pdflink;
        this.location=location;
        this.agendaId=agendaId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
