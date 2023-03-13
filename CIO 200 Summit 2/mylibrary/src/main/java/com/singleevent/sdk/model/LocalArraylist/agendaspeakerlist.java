package com.singleevent.sdk.model.LocalArraylist;

import java.io.Serializable;

/**
 * Created by webmodi on 4/6/2016.
 */
public class agendaspeakerlist implements Serializable {
    public int getSpeakerid() {
        return speakerid;
    }

    public void setSpeakerid(int speakerid) {
        this.speakerid = speakerid;
    }

    int speakerid;
    String name;
    String prof;
    String image;
    String details;
    String linkedin;
    String facebook;
    String speaker_document_url;;
    int speaker_document_hide;
    String speaker_document_name;
    int agendaid[] = {};

    public String getLinkedin() {
        return linkedin;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getSpeaker_document_url() {
        return speaker_document_url;
    }

    public int getSpeaker_document_hide() {
        return speaker_document_hide;
    }

    public void setSpeaker_document_hide(int speaker_document_hide) {
        this.speaker_document_hide = speaker_document_hide;
    }

    public String getSpeaker_document_name() {
        return speaker_document_name;
    }

    public void setSpeaker_document_name(String speaker_document_name) {
        this.speaker_document_name = speaker_document_name;
    }

    public void setSpeaker_document_url(String speaker_document_url) {
        this.speaker_document_url = speaker_document_url;
    }

    public agendaspeakerlist(int speakerid, String name, String prof, String image, String details, String facebook, String linkedin, int agendaid[],
                             String speaker_document_url,int speaker_document_hide,String speaker_document_name) {
        this.linkedin = linkedin;
        this.speaker_document_url=speaker_document_url;
        this.speaker_document_hide=speaker_document_hide;
        this.speaker_document_name=speaker_document_name;
        this.facebook = facebook;
        this.name = name;
        this.prof = prof;
        this.details = details;
        this.image = image;
        this.speakerid = speakerid;
        this.agendaid=agendaid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    public String getImage() {
        if (image == null)
            return "";
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    public int[] getagendaid() {
        return agendaid;
    }
}
