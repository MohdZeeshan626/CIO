package com.webmobi.gecmedia.Models;

import java.io.Serializable;

public class MultiEvent  implements Serializable{
    String event_name;
    String multi_event_logo;
    String color_code;
    String event_id;
    String app_image;

    public String getApp_image() {
        if(app_image==null)
        return "";
        return app_image;
    }

    public void setApp_image(String app_image) {
        this.app_image = app_image;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getMulti_event_logo() {
        return multi_event_logo;
    }

    public void setMulti_event_logo(String multi_event_logo) {
        this.multi_event_logo = multi_event_logo;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }
    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }
}
