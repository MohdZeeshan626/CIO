package com.singleevent.sdk.model.Agenda;

import java.io.Serializable;

/**
 * Created by webMOBI on 11/21/2017.
 */

class Floors implements Serializable{

    /*"floor_id": 1,
						"name": "3rd floor",
						"description": "",
						"imageUrl": ""*/

    private int floor_id;
    private String name;
    private String description;
    private String imageUrl;

    public int getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(int floor_id) {
        this.floor_id = floor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
