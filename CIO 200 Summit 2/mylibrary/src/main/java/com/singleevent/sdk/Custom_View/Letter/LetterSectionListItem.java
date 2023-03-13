package com.singleevent.sdk.Custom_View.Letter;

public abstract class LetterSectionListItem {

    private String sortString;

    public abstract String getUniqueId();

    public abstract void calculateSortString();

    public String getSortString() {
        return sortString;
    }

    public void setSortString(String s) {
        sortString = s;
    }

}
