package com.singleevent.sdk.model.Agenda;

import com.singleevent.sdk.model.Map.Mapfloorplan;

import java.io.Serializable;

/**
 * Created by webMOBI on 10/31/2017.
 */

public class LocationDetails implements Serializable {

    private String type;
    private String sub_type;
    private String iconCls;
    private String title;
    private String caption;
    private int position;
    private String mod_display;
    private String checkvalue;
    private String description;
    private String venue;
    private String city;
    private String lat;
    Mapfloorplan[] floors = null;
    public Mapfloorplan getFloors(int i) {
        return floors[i];
    }

    public Mapfloorplan[] getFloors() {
        return floors;
    }

    public int getFloorsSize() {
        if (floors == null) return 0;
        else return floors.length;
    }

    String lng;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getMod_display() {
        return mod_display;
    }

    public void setMod_display(String mod_display) {
        this.mod_display = mod_display;
    }

    public String getCheckvalue() {
        return checkvalue;
    }

    public void setCheckvalue(String checkvalue) {
        this.checkvalue = checkvalue;
    }

   /* public String[] getFloors() {
        return floors;
    }

    public void setFloors(String[] floors) {
        this.floors = floors;
    }*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
