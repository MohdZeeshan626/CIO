package com.singleevent.sdk.model;

import java.io.Serializable;

public class Likeinfo implements Serializable {
    String userid;
    String username;
    String profile_pic;
    long like_time;

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

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public long getLike_time() {
        return like_time;
    }

    public void setLike_time(long like_time) {
        this.like_time = like_time;
    }
}
