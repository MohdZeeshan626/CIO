package com.singleevent.sdk.model;

/**
 * Created by Fujitsu on 30-08-2017.
 */

public class TimeSlot {


    String id;
    long from_time;
    long to_time;
    boolean status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
