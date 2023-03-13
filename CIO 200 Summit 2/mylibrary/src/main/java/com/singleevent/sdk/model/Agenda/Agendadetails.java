package com.singleevent.sdk.model.Agenda;

import java.io.Serializable;

/**
 * Created by webmodi on 4/1/2016.
 */
public class Agendadetails implements Serializable {
    long name;
    String activity;
    String location;
    String time;
    String topic;
    String description;
    String category;
    int speakerId[] = {};
    int agendaId;
    int hide_agenda;
    String pdflink;
    String attachment_type;
    String attachment_name;
    String attachment_url;
    long fromtime;
    long totime;
    String survey_checkvalue;
    int agenda_registration;



    String stream_link;


    int enable_agora_stream;



    String floor_id;
    LocationDetails location_detail;
    String map_checkvalue;

    public int getAgenda_registration() {
        return agenda_registration;
    }
    public int getHide_agenda() {
        return hide_agenda;
    }

    public void setHide_agenda(int hide_agenda) {
        this.hide_agenda = hide_agenda;
    }

    public LocationDetails getLocation_detail() {
        return location_detail;
    }

    public void setLocation_detail(LocationDetails location_detail) {
        this.location_detail = location_detail;
    }

    public Agendadetails(long name, String activity, String location, String time, String topic, String description, String category, int[] speakerId, int agendaId, String attachment_type, String attachment_name, String attachment_url, long fromtime, long totime,String survey_checkvalue,String map_checkvalue,String floor_id,String stream_link,int enable_agora_stream) {
        this.activity = activity;
        this.location = location;
        this.time = time;
        this.topic = topic;
        this.description = description;
        this.category = category;
        this.speakerId = speakerId;
        this.agendaId = agendaId;
        this.fromtime = fromtime;
        this.totime = totime;
        this.name = name;
        this.attachment_type = attachment_type;
        this.attachment_name = attachment_name;
        this.attachment_url = attachment_url;
        this.survey_checkvalue=survey_checkvalue;
        this.map_checkvalue=map_checkvalue;
        this.floor_id=floor_id;
        this.stream_link=stream_link;
        this.enable_agora_stream=enable_agora_stream;
    }
    public String getStream_link() {
        return stream_link;
    }

    public int getEnable_agora_stream() {
        return enable_agora_stream;
    }

    public String getFloor_id() {
        return floor_id;
    }
    public String getAttachment_type() {
        return attachment_type;
    }

    public String getMap_checkvalue() {
        return map_checkvalue;
    }

    public void setMap_checkvalue(String map_checkvalue) {
        this.map_checkvalue = map_checkvalue;
    }

    public void setAttachment_type(String attachment_type) {
        this.attachment_type = attachment_type;
    }

    public String getAttachment_name() {
        return attachment_name;
    }
    public String getSurvey_checkvalue() {
        return survey_checkvalue;
    }


    public void setAttachment_name(String attachment_name) {
        this.attachment_name = attachment_name;
    }

    public String getAttachment_url() {
        if (attachment_url == null)
            return "";
        return attachment_url;
    }

    public void setAttachment_url(String attachment_url) {
        this.attachment_url = attachment_url;
    }

    public long getFromtime() {
        return fromtime;
    }

    public void setFromtime(long fromtime) {
        this.fromtime = fromtime;
    }

    public long getTotime() {
        return totime;
    }

    public void setTotime(long totime) {
        this.totime = totime;
    }

    public String getPdflink() {
        if (pdflink == null)
            return "";
        else
            return pdflink;
    }

    public String getActivity() {
        if (activity == null)
            return "";
        else
            return activity;
    }

    public String getLocation() {
        if (location == null)
            return "";
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getTopic() {
        if (topic == null) return "";
        else
            return topic;
    }

    public String getDescription() {
        if (description == null)
            return "";
        return description;
    }

    public String getCategory() {
        return category;
    }

    public int[] getSpeakerId() {
        return speakerId;
    }

    public int getAgendaId() {
        return agendaId;
    }

    public long getName() {
        return name;
    }


}
