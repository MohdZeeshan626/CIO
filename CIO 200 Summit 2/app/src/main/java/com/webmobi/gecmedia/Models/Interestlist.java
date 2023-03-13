package com.webmobi.gecmedia.Models;


import java.io.Serializable;

public class Interestlist implements Serializable {

    String name = null;
    boolean selected = false;

    public Interestlist(String name, boolean selected) {
        super();
        this.name = name;
        this.selected = selected;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}

