package com.singleevent.sdk.agora.openvcall.model;

import java.io.Serializable;

public class GroupInfo implements Serializable {
    String group_name;
    String group_id;
    String status;

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
