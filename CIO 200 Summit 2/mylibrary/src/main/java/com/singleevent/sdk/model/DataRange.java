package com.singleevent.sdk.model;

/**
 * Created by Admin on 5/23/2017.
 */

public class DataRange {

    String date;
    boolean isviewclickable;
    long dateinmillis;

    public DataRange(String date, boolean isviewclickable, long dateinmillis) {
        this.date = date;
        this.isviewclickable = isviewclickable;
        this.dateinmillis = dateinmillis;
    }

    public long getDateinmillis() {
        return dateinmillis;
    }

    public void setDateinmillis(long dateinmillis) {
        this.dateinmillis = dateinmillis;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isviewclickable() {
        return isviewclickable;
    }

    public void setIsviewclickable(boolean isviewclickable) {
        this.isviewclickable = isviewclickable;
    }
}

