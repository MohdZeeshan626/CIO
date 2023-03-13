package com.singleevent.sdk.pojo.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostReqFeed {
    @SerializedName("appid")
    @Expose
    private String appid;
    @SerializedName("create_time")
    @Expose
    private Long createTime;
    @SerializedName("post_content")
    @Expose
    private String postContent;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("post_name")
    @Expose
    private String postName;
    @SerializedName("userid")
    @Expose
    private String userid;

    public PostReqFeed(String appid, Long createTime, String postContent, String postName, String userid) {
        this.appid = appid;
        this.createTime = createTime;
        this.postContent = postContent;
        this.postName = postName;
        this.userid = userid;
    }

    public PostReqFeed(String appid, Long createTime, String postContent, String groupId, String postName, String userid) {
        this.appid = appid;
        this.createTime = createTime;
        this.postContent = postContent;
        this.groupId = groupId;
        this.postName = postName;
        this.userid = userid;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
