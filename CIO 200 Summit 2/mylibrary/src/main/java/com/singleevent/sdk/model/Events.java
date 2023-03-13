package com.singleevent.sdk.model;


public class Events {

    String id;
    String text;
    String tagline;
    String date;
    Tab[] tabs;

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTagline() {
        return tagline;
    }

    public String getDate() {
        return date;
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
