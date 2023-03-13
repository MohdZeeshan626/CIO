package com.singleevent.sdk.model.LocalArraylist;

import java.io.Serializable;

/**
 * Created by webmodi on 7/29/2016.
 */
public class AddingUser implements Serializable {

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAppId() {
        return appId;
    }

    public String getProfilepic() {
        if (profilepic == null) return "null";
        return profilepic;
    }

    String userId;
    String name;
    String email;
    String appId;
    String profilepic;

    public String getDesignation() {
        if (designation == null)
            return "";
        return designation;
    }

    public String getCompanyName() {
        if (companyName == null)
            return "";
        return companyName;
    }

    String designation;
    String companyName;

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    long lastMessageTime;
}
