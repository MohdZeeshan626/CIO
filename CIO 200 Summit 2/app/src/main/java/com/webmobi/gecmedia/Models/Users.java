package com.webmobi.gecmedia.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lenovo on 28-06-2018.
 */

public class Users {
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_length")
    @Expose
    private Integer userLength;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserLength() {
        return userLength;
    }

    public void setUserLength(Integer userLength) {
        this.userLength = userLength;
    }

}
