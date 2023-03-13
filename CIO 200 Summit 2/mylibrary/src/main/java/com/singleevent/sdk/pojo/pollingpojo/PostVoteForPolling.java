package com.singleevent.sdk.pojo.pollingpojo;

public class PostVoteForPolling {
    String user_id;
    String poll_id;
    String option;

    public PostVoteForPolling(String user_id, String poll_id, String option) {
        this.user_id = user_id;
        this.poll_id = poll_id;
        this.option = option;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPoll_id() {
        return poll_id;
    }

    public String getOption() {
        return option;
    }
}
