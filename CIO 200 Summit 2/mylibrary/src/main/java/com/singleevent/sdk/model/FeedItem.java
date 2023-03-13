package com.singleevent.sdk.model;

public class FeedItem {
    private String postId;
    private String name;
    private String status;
    private String image;
    private String profilepic;
    private String postTime;
    private String url;
    int likes;

    public boolean isMyLike() {
        return myLike;
    }

    boolean myLike = false;

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public void setMyLike(boolean myLike) {
        this.myLike = myLike;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    private int comments;

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComment() {
        return comments;
    }

    public void setComment(int comments) {
        this.comments = comments;
    }


    public FeedItem() {
    }

    public FeedItem(String postId, String name, String image, String status,
                    String profilepic, String postTime, String url, int likes, int comments, boolean myLike) {
        super();
        this.postId = postId;
        this.name = name;
        this.image = image;
        this.status = status;
        this.profilepic = profilepic;
        this.postTime = postTime;
        this.url = url;
        this.likes = likes;
        this.comments = comments;
        this.myLike = myLike;
    }

    public String getId() {
        return postId;
    }

    public void setId(String postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImge() {
        if (image.equalsIgnoreCase(""))
            return null;
        return image;
    }

    public void setImge(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePic() {
        if (profilepic == null)
            return "";
        return profilepic;
    }

    public void setProfilePic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getTimeStamp() {
        return postTime;
    }

    public void setTimeStamp(String postTime) {
        this.postTime = postTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
