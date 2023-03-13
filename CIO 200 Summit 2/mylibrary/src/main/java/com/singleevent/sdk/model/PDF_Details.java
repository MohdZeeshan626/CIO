package com.singleevent.sdk.model;

/**
 * Created by Admin on 5/31/2017.
 */

public class PDF_Details {


    String attachment_id;
    String attachment_type;
    String attachment_name;
    String attachment_category;

    public String getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(String attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getAttachment_type() {
        return attachment_type;
    }

    public void setAttachment_type(String attachment_type) {
        this.attachment_type = attachment_type;
    }

    public String getAttachment_name() {
        return attachment_name;
    }

    public void setAttachment_name(String attachment_name) {
        this.attachment_name = attachment_name;
    }

    public String getAttachment_category() {
        return attachment_category;
    }

    public void setAttachment_category(String attachment_category) {
        this.attachment_category = attachment_category;
    }

    public String getAttachment_url() {
        return attachment_url;
    }

    public void setAttachment_url(String attachment_url) {
        this.attachment_url = attachment_url;
    }

    String attachment_url;

}
