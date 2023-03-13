package com.singleevent.sdk.model;

/**
 * Created by Admin on 6/22/2017.
 */

public class Categories {

    String category;
    String color_code;

    public String getColor_code() {
        if (color_code == null || color_code.equalsIgnoreCase("")) {

            return "#F814A7";
        }
        else {
            if (color_code.contains("#")) {
                return color_code;
            } else {
                return "#" + color_code;
            }
        }
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public String getCategory() {

        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
