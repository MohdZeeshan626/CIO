package com.singleevent.sdk.model.LocalArraylist;

/**
 * Created by Admin on 6/13/2017.
 */

public class Rating {


    double rating;
    int type_id;

    public Rating() {
    }

    public Rating(double rating, int type_id) {
        this.rating = rating;
        this.type_id = type_id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }


}
