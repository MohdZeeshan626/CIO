package com.singleevent.sdk.View.RightActivity.admin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseString {

    @SerializedName("segment_id")
    @Expose
    private String segmentId;
    @SerializedName("appid")
    @Expose
    private String appid;
    @SerializedName("segment_name")
    @Expose
    private String segmentName;
    @SerializedName("segment_users")
    @Expose
    private String segmentUsers;

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName = segmentName;
    }

    public String getSegmentUsers() {
        return segmentUsers;
    }

    public void setSegmentUsers(String segmentUsers) {
        this.segmentUsers = segmentUsers;
    }

}


