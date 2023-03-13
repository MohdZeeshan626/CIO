package com.singleevent.sdk.model;


import com.singleevent.sdk.model.Agenda.Agendadetails;

import java.util.Comparator;


/**
 * Created by Admin on 5/24/2017.
 */

public class DateEndComparator implements Comparator<Agendadetails> {


    @Override
    public int compare(Agendadetails e1, Agendadetails e2) {
        if (e1.getTotime() > e2.getTotime()) {
            return 1;
        } else {
            return -1;
        }
    }

}
