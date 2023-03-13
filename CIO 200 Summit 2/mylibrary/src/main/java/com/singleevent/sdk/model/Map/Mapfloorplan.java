package com.singleevent.sdk.model.Map;

import java.io.Serializable;

/**
 * Created by webmodi on 4/1/2016.
 */
public class Mapfloorplan implements Serializable {

    String name;
    String imageUrl;
    String description;
    internalfloorplaces[] places;

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        if (imageUrl == null)
            return "";
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public internalfloorplaces getPlaces(int i) {
        return places[i];
    }

    public int getPlacesSize() {
        if (places == null) return 0;
        else return places.length;
    }

}
