package com.singleevent.sdk.View.RightActivity.admin.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by User on 9/30/2016.
 */

public class EventUser implements Parcelable {

    private String userid;
    private String first_name;
    private String last_name;
    private String company;
    private String designation;
    private String profile_pic;
    private String website;
    private String mobile;
    private String user_blog;
    private String description;
    private String checkin_status;
    private String email;

    public String getBeacon_id() {
        return beacon_id;
    }

    public void setBeacon_id(String beacon_id) {
        this.beacon_id = beacon_id;
    }

    private String beacon_id;

    protected EventUser(Parcel in) {
        userid = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        company = in.readString();
        designation = in.readString();
        profile_pic = in.readString();
        website = in.readString();
        mobile = in.readString();
        user_blog = in.readString();
        description = in.readString();
        checkin_status = in.readString();
        email = in.readString();
        agenda_id = in.createIntArray();
        beacon_id = in.readString();
    }

    public static final Creator<EventUser> CREATOR = new Creator<EventUser>() {
        @Override
        public EventUser createFromParcel(Parcel in) {
            return new EventUser(in);
        }

        @Override
        public EventUser[] newArray(int size) {
            return new EventUser[size];
        }
    };

    public int[] getAgenda_id() {
        return agenda_id;
    }

    public void setAgenda_id(int[] agenda_id) {
        this.agenda_id = agenda_id;
    }

    private int[] agenda_id;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser_blog() {
        return user_blog;
    }

    public void setUser_blog(String user_blog) {
        this.user_blog = user_blog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCheckin_status() {
        return checkin_status;
    }

    public void setCheckin_status(String checkin_status) {
        this.checkin_status = checkin_status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "EventUser{" +
                "userid='" + userid + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name=" + last_name +
                ", email='" + email + '\'' +
                ", company='" + company + '\'' +
                ", designation='" + designation + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                ", website='" + website + '\'' +
                ", mobile='" + mobile + '\'' +
                ", user_blog='" + user_blog + '\'' +
                ", description='" + description + '\'' +
                ", checkin_status='" + checkin_status + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userid);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(company);
        parcel.writeString(designation);
        parcel.writeString(profile_pic);
        parcel.writeString(website);
        parcel.writeString(mobile);
        parcel.writeString(user_blog);
        parcel.writeString(description);
        parcel.writeString(checkin_status);
        parcel.writeString(email);
        parcel.writeIntArray(agenda_id);
        parcel.writeString(beacon_id);
    }
}
