package com.singleevent.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by webMOBI on 9/13/2017.
 */

public class Comment_User implements Parcelable {

    private String appid;
    private int post_id;
    private int comment;
    private String comment_time;
    private String userid;
    private String first_name;
    private String last_name;
    private String profile_pic;

    protected Comment_User(Parcel in) {
        appid = in.readString();
        post_id = in.readInt();
        comment = in.readInt();
        comment_time = in.readString();
        userid = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        profile_pic = in.readString();
    }

    public static final Creator<Comment_User> CREATOR = new Creator<Comment_User>() {
        @Override
        public Comment_User createFromParcel(Parcel in) {
            return new Comment_User(in);
        }

        @Override
        public Comment_User[] newArray(int size) {
            return new Comment_User[size];
        }
    };

    public String getName() {
        return first_name + " " + last_name;
    }

    public String getAppid() {
        return appid;
    }

    public int getPost_id() {
        return post_id;
    }

    public int getComment() {
        return comment;
    }

    public String getComment_time() {
        return comment_time;
    }

    public String getUserid() {
        return userid;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getProfile_pic() {
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
        dest.writeInt(comment);
        dest.writeString(comment_time);
        dest.writeString(userid);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(profile_pic);
    }
}
