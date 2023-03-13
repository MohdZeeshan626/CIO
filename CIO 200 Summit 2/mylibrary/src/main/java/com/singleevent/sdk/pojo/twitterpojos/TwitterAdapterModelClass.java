package com.singleevent.sdk.pojo.twitterpojos;

public class TwitterAdapterModelClass {

    private String tweet_id;
    private String name;
    private String username;
    private String tweet;
    private String profile_pic_url;
    private String time;
    private int likes_count;
    private int retweet_count;


    public TwitterAdapterModelClass(String tweet_id, String name, String username, String tweet, String profile_pic_url, String time, int likes_count, int retweet_count) {
        this.tweet_id = tweet_id;
        this.name = name;
        this.username = username;
        this.tweet = tweet;
        this.profile_pic_url = profile_pic_url;
        this.time = time;
        this.likes_count = likes_count;
        this.retweet_count = retweet_count;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
    }

    public String getTweet_id() {
        return tweet_id;
    }

    public void setTweet_id(String tweet_id) {
        this.tweet_id = tweet_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
