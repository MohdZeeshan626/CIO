package com.singleevent.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by webMOBI on 9/13/2017.
 */

public class Like_User implements Parcelable {

    private String appid;
    private int post_id;
    private int like_status;
    private String like_time;
    private String userid;
    private String username;
    private String profile_pic;

    protected Like_User(Parcel in) {
        appid = in.readString();
        post_id = in.readInt();
        like_status = in.readInt();
        like_time = in.readString();
        userid = in.readString();
        username = in.readString();
        profile_pic = in.readString();
    }

    public static final Creator<Like_User> CREATOR = new Creator<Like_User>() {
        @Override
        public Like_User createFromParcel(Parcel in) {
            return new Like_User(in);
        }

        @Override
        public Like_User[] newArray(int size) {
            return new Like_User[size];
        }
    };

    public String getName() {
        return username;
    }


    public String getAppid() {
        return appid;
    }

    public int getPost_id() {
        return post_id;
    }

    public int getLike_status() {
        return like_status;
    }

    public String getLike_time() {
        return like_time;
    }

    public String getUserid() {
        return userid;
    }

    public String getProfile_pic() {
        if (profile_pic == null)
            return "";
        return profile_pic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appid);
        dest.writeInt(post_id);
        dest.writeInt(like_status);
        dest.writeString(like_time);
        dest.writeString(userid);
        dest.writeString(username);
        dest.writeString(profile_pic);
    }
}
