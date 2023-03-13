package com.singleevent.sdk.model;

import java.io.Serializable;

/**
 * Created by Admin on 6/28/2017.
 */

public class Notifications implements Serializable {


    String type;
    String title;
    String message;
    long notification_time;
    String rich_message;

    public String getRich_message() {
        return rich_message;
    }

    public long getNotification_time() {
        return notification_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}


