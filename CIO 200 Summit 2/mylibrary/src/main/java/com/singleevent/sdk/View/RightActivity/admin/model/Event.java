package com.singleevent.sdk.View.RightActivity.admin.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 9/29/2016.
 */

public class Event implements Parcelable {

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
    private String appUrl;
    private String appId;
    private String eventName;
    private String eventDate;
    private String start_date;
    private String end_date;
    private String location;
    private String imageUrl;

   /* public Event(AppData appData) {
        appUrl = appData.getApp_url();
        appId = appData.getAppid();
        eventName = appData.getApp_name();
        eventDate = appData.converlontostring(appData.getStart_date(),appData.getEnd_date());
        location = appData.getLocation();
        imageUrl = appData.getApp_image();
    }
*/
    protected Event(Parcel in) {
        appUrl = in.readString();
        appId = in.readString();
        eventName = in.readString();
        eventDate = in.readString();
        location = in.readString();
        imageUrl = in.readString();
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appUrl);
        dest.writeString(appId);
        dest.writeString(eventName);
        dest.writeString(eventDate);
        dest.writeString(location);
        dest.writeString(imageUrl);
    }
}
