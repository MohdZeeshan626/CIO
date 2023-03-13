package com.singleevent.sdk.model;

/**
 * Created by User on 5/16/2016.
 */
public class SpeakerFilter extends Items {

    public SpeakerFilter(String name, String description, String image, int speakerId, int agendaId[], String details,String speaker_document_url,int speaker_document_hide
            ,String speaker_document_name) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.speakerId = speakerId;
        this.agendaId = agendaId;
        this.details = details;
        this.speaker_document_url=speaker_document_url;
        this.speaker_document_hide=speaker_document_hide;
        this.speaker_document_name=speaker_document_name;
    }
}
