package com.webmobi.gecmedia;

import com.singleevent.sdk.model.Tab;

public class NavigationPojo {
    private String type;
    private String title;
    private String mod_display;
    private String sub_type;
    Tab[] tabs;
    public NavigationPojo(String type, String title, String mod_display, String sub_type) {
        this.type = type;
        this.title = title;
        this.mod_display = mod_display;
        this.sub_type = sub_type;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMod_display() {
        return mod_display;
    }

    public void setMod_display(String mod_display) {
        this.mod_display = mod_display;
    }

    public Tab getTabs(int i) {
        return tabs[i];
    }

    public int getTabsSize() {
        if (tabs == null) return 0;
        return tabs.length;
    }

    public Tab[] getTabs() {
        return tabs;
    }

}
