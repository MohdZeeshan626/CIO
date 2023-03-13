package com.singleevent.sdk;

import com.singleevent.sdk.gallery_camera.Gallery_Model.Image;

import java.util.ArrayList;
import java.util.List;

public class FeedTestPojo {
    private List<ImagePojo> attachments;
    private String title;
    private String post_content;


    public FeedTestPojo(String title, String post_content, List<ImagePojo> attachments) {
        this.title = title;
        this.post_content = post_content;
        this.attachments = attachments;
    }

    public List<ImagePojo> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<ImagePojo> attachments) {
        this.attachments = attachments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }
}
