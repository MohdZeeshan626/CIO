package com.singleevent.sdk.agora.openvcall.model;

        import java.io.Serializable;

public class GroupChat implements Serializable {



    //String name;
    private String username;
    String message_body;


    String sender_id;


    String profile_pic;
    String create_date;
    String recipient_group_id;
    int color;
    private boolean incomingMessage;
    private boolean section;
    String To_UserID;
    String To_UserName;
    String From_UserID;
    String From_UserName;
    String appid;
    String appname;
    String chat_type;
    String TimeinMilli;
    String msg;
    String msg_datatype;
    String multievent_flag;
    String eventid;



    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
    public String getTo_UserID() {
        return To_UserID;
    }

    public void setTo_UserID(String to_UserID) {
        To_UserID = to_UserID;
    }

    public String getTo_UserName() {
        return To_UserName;
    }

    public void setTo_UserName(String to_UserName) {
        To_UserName = to_UserName;
    }

    public String getFrom_UserID() {
        return From_UserID;
    }

    public void setFrom_UserID(String from_UserID) {
        From_UserID = from_UserID;
    }

    public String getFrom_UserName() {
        return From_UserName;
    }

    public void setFrom_UserName(String from_UserName) {
        From_UserName = from_UserName;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getChat_type() {
        return chat_type;
    }

    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }

    public String getTimeinMilli() {
        return TimeinMilli;
    }

    public void setTimeinMilli(String timeinMilli) {
        TimeinMilli = timeinMilli;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg_datatype() {
        return msg_datatype;
    }

    public void setMsg_datatype(String msg_datatype) {
        this.msg_datatype = msg_datatype;
    }

    public String getMultievent_flag() {
        return multievent_flag;
    }

    public void setMultievent_flag(String multievent_flag) {
        this.multievent_flag = multievent_flag;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public boolean isIncomingMessage() {
        return incomingMessage;
    }

    public void setIncomingMessage(boolean incomingMessage) {
        this.incomingMessage = incomingMessage;
    }

    public boolean isSection() {
        return section;
    }

    public void setSection(boolean section) {
        this.section = section;
    }
    //int badgecount = 0;

    public GroupChat() {
        super();
    }


   /* public GroupChat(String message_body, String sender_id, String create_date, String recipient_group_id, int color) {
        this.message_body =message_body;
        this.sender_id = sender_id;
        this.create_date = create_date;
        this.recipient_group_id = recipient_group_id;
        this.color=color;

    }*/

    public String getMessage_body() {
        return message_body;
    }
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getSender_id() {
        return sender_id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public String getRecipient_group_id() {
        return recipient_group_id;
    }

    public void setMessage_body(String message_body) {
        this.message_body = message_body;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public void setRecipient_group_id(String recipient_group_id) {
        this.recipient_group_id = recipient_group_id;
    }
}
