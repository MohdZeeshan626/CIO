package com.singleevent.sdk.pojo;

public class FeedPojo {
    String name;
    String link;
    String category;
    String contentType;
    String writtenContent;

    public FeedPojo(String name, String link, String category, String contentType, String writtenContent) {
        this.name = name;
        this.link = link;
        this.category = category;
        this.contentType = contentType;
        this.writtenContent = writtenContent;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getCategory() {
        return category;
    }

    public String getContentType() {
        return contentType;
    }

    public String getWrittenContent() {
        return writtenContent;
    }
}
