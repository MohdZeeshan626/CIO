package com.webmobi.gecmedia.Models;

/**
 * Created by Admin on 1/19/2017.
 */

public class Events_Wishlist {


    String id;
    String details;

    public Events_Wishlist(String id, String details) {
        this.id = id;
        this.details = details;
    }

    public Events_Wishlist() {

    }

    public String getId() {
        return id;
    }

    public String getDetails() {
        return details;
    }

    public void setId(String id) {

        this.id = id;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
