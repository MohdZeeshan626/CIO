package com.singleevent.sdk.health.Model;

public class LeaderBoardPoints {
    int leaderboard_id;
    String challenge_id;
    String app_id;
    String user_id;
    int time;
    double distance;
    int steps;

    public int getLeaderboard_id() {
        return leaderboard_id;
    }

    public void setLeaderboard_id(int leaderboard_id) {
        this.leaderboard_id = leaderboard_id;
    }

    public String getChallenge_id() {
        return challenge_id;
    }

    public void setChallenge_id(String challenge_id) {
        this.challenge_id = challenge_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
