package com.singleevent.sdk.health.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class password_response {
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("tokenExpirationUtc")
    @Expose
    private String tokenexpire;

    @SerializedName("userId")
    @Expose
    private String userid;

    @SerializedName("username")
    @Expose
    private String username;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenexpire() {
        return tokenexpire;
    }

    public void setTokenexpire(String tokenexpire) {
        this.tokenexpire = tokenexpire;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public password_response(String token, String tokenexpire, String userid, String username, String user_type) {
        this.token = token;
        this.tokenexpire = tokenexpire;
        this.userid = userid;
        this.username = username;
        this.user_type = user_type;
    }

    @SerializedName("userType")
    @Expose
    private String user_type;
}
