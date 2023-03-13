package com.singleevent.sdk.pojo.pollingpojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatePollPojo {
    @SerializedName("app_id")
    @Expose
    private String appId;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("options")
    @Expose
    private String options;
    @SerializedName("correct_ans")
    @Expose
    private String correctAns;
    @SerializedName("require_auth")
    @Expose
    private String requireAuth;
    @SerializedName("customtheme")
    @Expose
    private String customtheme;
    @SerializedName("voting_restriction")
    @Expose
    private String votingRestriction;
    @SerializedName("option_add_status")
    @Expose
    private String optionAddStatus;
    @SerializedName("main_color")
    @Expose
    private String mainColor;
    @SerializedName("created_by")
    @Expose
    private String createdBy;

    public CreatePollPojo(String appId, String question, String options, String correctAns, String requireAuth, String customtheme, String votingRestriction, String optionAddStatus, String mainColor, String createdBy) {
        this.appId = appId;
        this.question = question;
        this.options = options;
        this.correctAns = correctAns;
        this.requireAuth = requireAuth;
        this.customtheme = customtheme;
        this.votingRestriction = votingRestriction;
        this.optionAddStatus = optionAddStatus;
        this.mainColor = mainColor;
        this.createdBy = createdBy;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(String correctAns) {
        this.correctAns = correctAns;
    }

    public String getRequireAuth() {
        return requireAuth;
    }

    public void setRequireAuth(String requireAuth) {
        this.requireAuth = requireAuth;
    }

    public String getCustomtheme() {
        return customtheme;
    }

    public void setCustomtheme(String customtheme) {
        this.customtheme = customtheme;
    }

    public String getVotingRestriction() {
        return votingRestriction;
    }

    public void setVotingRestriction(String votingRestriction) {
        this.votingRestriction = votingRestriction;
    }

    public String getOptionAddStatus() {
        return optionAddStatus;
    }

    public void setOptionAddStatus(String optionAddStatus) {
        this.optionAddStatus = optionAddStatus;
    }

    public String getMainColor() {
        return mainColor;
    }

    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
