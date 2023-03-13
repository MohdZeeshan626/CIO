package com.singleevent.sdk.model;

/**
 * Created by Fujitsu on 30-08-2017.
 */

public class Comments {


    String username;
    String profile_pic;
    long comment_time;
    String comment;
    String userid;

    public String getName() {
        return username;
    }

    public String getProfilepic() {
        if (profile_pic == null)
            return "";
        return profile_pic;
    }

    public long getCommentedon() {
        return comment_time;
    }

    public String getCommenttxt() {
        return comment;
    }

    public Comments(String username, String profile_pic, long comment_time, String comment) {
        this.username = username;
        this.profile_pic = profile_pic;
        this.comment_time = comment_time;
        this.comment = comment;
    }
}
