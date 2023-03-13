package com.webmobi.gecmedia.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lenovo on 28-06-2018.
 */

public class GetEventsModel {
    @SerializedName("response")
    @Expose
    private Boolean response;
    @SerializedName("user_exist")
    @Expose
    private Boolean userExist;
    @SerializedName("users")
    @Expose
    private Users users;
    @SerializedName("events")
    @Expose
//    private EventDetail eventDetail;
    private List<EventDetail> eventDetail = null;

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public Boolean getUserExist() {
        return userExist;
    }

    public void setUserExist(Boolean userExist) {
        this.userExist = userExist;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public List<EventDetail> getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(List<EventDetail> eventDetail) {
        this.eventDetail = eventDetail;
    }

   /* public EventDetail getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(EventDetail eventDetail) {
        this.eventDetail = eventDetail;
    }*/
}
