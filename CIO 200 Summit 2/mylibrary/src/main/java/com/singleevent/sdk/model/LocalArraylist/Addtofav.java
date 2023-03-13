package com.singleevent.sdk.model.LocalArraylist;


import java.io.Serializable;

public class Addtofav implements Serializable {
    String agendadate;
    String agendaname;
    String description;
    String topic;
    String time;
    String activity;
    String notes;
    int agenda_id;
    String speakerId;

    public String getAgendadate() {
        return agendadate;
    }

    public String getAgendaname() {
        if (time == null) return "";
        return agendaname;
    }

    public String getDescription() {
        return description;
    }

    public String getTopic() {
        if (topic == null) return "";
        else return topic;
    }

    public String getTime() {
        if (time == null) return "";
        return time;
    }

    public String getActivity() {
        if (activity == null)
            return "";
        else
            return activity;

    }

    public String getNotes() {
        return notes;
    }

    public int getagendaid() {
        return agenda_id;
    }

    public String getSpeakerId() {
        return speakerId;
    }


}
