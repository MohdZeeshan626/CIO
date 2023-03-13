package com.singleevent.sdk.model.LocalArraylist;

/**
 * Created by Admin on 10/19/2016.
 */
public class Slot {

    String attendee;
    String time;
    String day;
    String name;

    public String getSubject() {
        return subject;
    }

    public String getVenue() {
        return venue;
    }

    String subject;
    String venue;

    public String getRequested() {
        return requested;
    }

    String requested;

    public String getName() {
        return name;
    }


    public String getAttendee() {
        return attendee;
    }

    public String getTime() {
        return time;
    }

    public String getDay() {
        return day;
    }

    public Slot(String attendee, String time, String day) {
        this.attendee = attendee;
        this.time = time;
        this.day = day;
    }


}
