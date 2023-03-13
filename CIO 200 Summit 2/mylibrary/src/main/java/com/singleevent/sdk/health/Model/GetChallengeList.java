package com.singleevent.sdk.health.Model;

import java.io.Serializable;

public class GetChallengeList implements Serializable {
    String challenge_id;
    String app_id;
    String imageUrl;
    String title;
    String Description;
    String createdBy;
    String challengeType;
    String viewChallenge;
    String challengeStatus;
    Long startDate;
    Long endDate;
    String joinedChallenge;
    int Totaljoineduser;

    public int getTotaljoineduser() {
        return Totaljoineduser;
    }

    public void setTotaljoineduser(int totaljoineduser) {
        Totaljoineduser = totaljoineduser;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(String challengeType) {
        this.challengeType = challengeType;
    }

    public String getViewChallenge() {
        return viewChallenge;
    }

    public void setViewChallenge(String viewChallenge) {
        this.viewChallenge = viewChallenge;
    }

    public String getChallengeStatus() {
        return challengeStatus;
    }

    public void setChallengeStatus(String challengeStatus) {
        this.challengeStatus = challengeStatus;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getJoinedChallenge() {
        return joinedChallenge;
    }

    public void setJoinedChallenge(String joinedChallenge) {
        this.joinedChallenge = joinedChallenge;
    }
}