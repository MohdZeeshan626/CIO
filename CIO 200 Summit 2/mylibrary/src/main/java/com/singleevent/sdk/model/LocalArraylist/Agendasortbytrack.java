package com.singleevent.sdk.model.LocalArraylist;



import com.singleevent.sdk.model.Agenda.Agendadetails;

import java.util.ArrayList;

/**
 * Created by webmodi on 4/9/2016.
 */
public class Agendasortbytrack {

    String groupname;
    ArrayList<Agendadetails> details;

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public ArrayList<Agendadetails> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<Agendadetails> details) {
        this.details = details;
    }

}
