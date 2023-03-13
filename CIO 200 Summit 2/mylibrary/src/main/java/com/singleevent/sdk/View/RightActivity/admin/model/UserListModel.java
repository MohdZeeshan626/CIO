package com.singleevent.sdk.View.RightActivity.admin.model;

import java.io.Serializable;

/**
 * Created by webMOBI on 12/4/2017.
 */

public class UserListModel implements Serializable{

    private String first_name;
    private String last_name;
    private String event_user_id;
    private String email;

    public String getFirst_name() {
        return first_name;
    }

    public UserListModel(String first_name, String last_name, String event_user_id, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.event_user_id = event_user_id;
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEvent_user_id() {
        return event_user_id;
    }

    public void setEvent_user_id(String event_user_id) {
        this.event_user_id = event_user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
