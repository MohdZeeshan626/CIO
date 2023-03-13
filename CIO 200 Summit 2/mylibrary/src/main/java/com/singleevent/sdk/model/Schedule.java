package com.singleevent.sdk.model;

/**
 * Created by Fujitsu on 30-08-2017.
 */

public class Schedule {


    String id;
    long name;
    int timeslotlength;
    TimeSlot[] timeslots;

    public String getId() {
        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public long getName() {
        return name;
    }


    public TimeSlot[] getTimeslot() {
        return timeslots;
    }

    public TimeSlot getTimeslot(int i) {
        return timeslots[i];
    }

    public int getTimeslotlength() {
        return timeslots.length;
    }
}
