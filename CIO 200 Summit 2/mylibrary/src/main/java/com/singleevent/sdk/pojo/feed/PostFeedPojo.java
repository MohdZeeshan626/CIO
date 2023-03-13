package com.singleevent.sdk.pojo.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostFeedPojo {
    @SerializedName("attachments")
    @Expose
    private List<Object> attachments = null;
    @SerializedName("post_req")
    @Expose
    private PostReqFeed postReq;

    public PostFeedPojo(List<Object> attachments, PostReqFeed postReq) {
        this.attachments = attachments;
        this.postReq = postReq;
    }

    public List<Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }

    public PostReqFeed getPostReq() {
        return postReq;
    }

    public void setPostReq(PostReqFeed postReq) {
        this.postReq = postReq;
    }


}

