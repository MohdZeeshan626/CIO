package com.singleevent.sdk.model.LocalArraylist;

import java.io.Serializable;

/**
 * Created by Admin on 6/21/2017.
 */

public class ChatMSG implements Serializable {


    String message;
    String name;
    String id;
    String profile_pic;
    String create_date;
    String msg_datatype;
    int color;
    int badgecount = 0;
    String recipient_name;
    String recipient_id;
    String message_body;
    public ChatMSG(String message, String name, String id, String profile_pic, String create_date, String msg_datatype, int color, int badgecount) {
        this.message = message;
        this.name = name;
        this.id = id;
        this.profile_pic = profile_pic;
        this.create_date = create_date;
        this.msg_datatype = msg_datatype;
        this.color = color;
        this.badgecount = badgecount;
    }

    public int getBadgecount() {
        return badgecount;
    }

    public void setBadgecount(int badgecount) {
        this.badgecount = badgecount;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getMessage_body() {
        return message_body;
    }

    public String getSender_name() {
        return name;
    }

    public String getSender_id() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getProfile_pic() {
        if (profile_pic == null)
            return "";
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getMsg_datatype() {
        return msg_datatype;
    }

    public void setMsg_datatype(String msg_datatype) {
        this.msg_datatype = msg_datatype;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getRecipient_name() {
        return recipient_name;
    }

    public void setRecipient_name(String recipient_name) {
        this.recipient_name = recipient_name;
    }

    public String getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public void setMessage_body(String message_body) {
        this.message_body = message_body;
    }
}
