package com.singleevent.sdk.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by webMOBI on 4/11/2018.
 */

public class EventIDObj implements Serializable{
    ArrayList<Long> uriList;

    public EventIDObj(ArrayList<Long> uriList) {
        this.uriList = uriList;
    }

    public ArrayList<Long> getUriList() {
        return uriList;
    }

    public void setUriList(ArrayList<Long> uriList) {
        this.uriList = uriList;
    }
}
