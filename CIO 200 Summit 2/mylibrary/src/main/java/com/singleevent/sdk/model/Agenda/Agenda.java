package com.singleevent.sdk.model.Agenda;


import java.io.Serializable;

public class Agenda implements Serializable {
    long name;
    Agendadetails[] detail;

    public long getName() {
        return name;
    }

    public Agendadetails getDetail(int i) {
        return detail[i];
    }

    public Agendadetails[] getDetail() {
        return detail;
    }

    public int getDetailSize() {
        if (detail == null) return 0;
        else return detail.length;
    }
}

