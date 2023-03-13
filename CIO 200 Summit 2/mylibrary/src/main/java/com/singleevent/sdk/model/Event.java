package com.singleevent.sdk.model;

import java.io.Serializable;

/**
 * Created by Admin on 5/9/2017.
 */

public class Event implements Serializable {

    private String appid;
    private String app_category;
    private String app_name;
    private String app_title;
    private String app_image;
    private String entry_fee;
    private String app_description;
    private int info_privacy;
    private String accesstype;
    private String start_date;
    private String end_date;
    private String venue;
    private String location;
    private String private_key;
    private double latitude;
    private double longitude;
    private User[] users;
    private int users_length;
    private boolean downloaded = false;
    private String app_url;
    private String app_logo;

    public String getApp_logo() {
        return app_logo;
    }

    public void setApp_logo(String app_logo) {
        this.app_logo = app_logo;
    }


    public String getApp_url() {
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

    public String getAppid() {
        return appid;
    }

    public String getApp_category() {
        return app_category;
    }

    public String getApp_name() {
        return app_name;
    }

    public String getApp_title() {
        return app_title;
    }

    public String getApp_image() {
        if (app_image == null)
            return "";
        return app_image;
    }

    public String getPrivate_key() {
        if (private_key == null)
            return "";
        return private_key;
    }

    public void setPrivate_key(String private_key) {

        this.private_key = private_key;
    }

    public String getEntry_fee() {
        return entry_fee;
    }

    public String getApp_description() {
        if (app_description == null)
            return "";
        return app_description;
    }

    public int getInfo_privacy() {
        return info_privacy;
    }

    public String getAccesstype() {
        return accesstype;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getVenue() {
        return venue;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getUsers_length() {
        return users_length;
    }

    public User getUsers(int i) {
        return users[i];
    }

    public int getusersSize() {
        if (users == null) return 0;
        return users.length;
    }

    public User[] getUsers() {
        return users;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

}

