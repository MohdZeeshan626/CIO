package com.singleevent.sdk.model;

/**
 * Created by Admin on 11/21/2016.
 */
public class speakerssocialmedia {

    public String getType() {
        return type;
    }

    public String getLink() {
        return link;
    }

    String type;
    String link;

    public speakerssocialmedia(String type, String link) {
        this.type = type;
        this.link = link;
    }
}
