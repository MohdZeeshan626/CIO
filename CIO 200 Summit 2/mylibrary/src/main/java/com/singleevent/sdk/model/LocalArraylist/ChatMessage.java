package com.singleevent.sdk.model.LocalArraylist;

public class ChatMessage {

    private String username;
    private String header;
    private String message;
    private String date;
    private String id;
    private String UserID;
    private String appid;
    private String appname;
    private String msgtype;
    private boolean incomingMessage;
    private boolean section;

    public ChatMessage() {
        super();
    }

    public ChatMessage(String message) {
        super();
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
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

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public boolean isSection() {
        return section;
    }

    public void setSection(boolean section) {
        this.section = section;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIncomingMessage() {
        return incomingMessage;
    }

    public void setIncomingMessage(boolean incomingMessage) {
        this.incomingMessage = incomingMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSystemMessage() {
        return getUsername() == null;
    }

}

