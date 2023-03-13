package com.singleevent.sdk.model.LocalArraylist;

/**
 * Created by webmodi on 4/21/2016.
 */
public class weatherweek {

    String temps;
    String imageurl;
    String days;

    public weatherweek(String temps, String imageurl, String days) {
        this.temps = temps;
        this.imageurl = imageurl;
        this.days = days;
    }

    public String getTemps() {
        return temps;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getDays() {
        return days;
    }
}
