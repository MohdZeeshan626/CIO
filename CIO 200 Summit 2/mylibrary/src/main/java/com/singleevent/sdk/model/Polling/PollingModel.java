package com.singleevent.sdk.model.Polling;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PollingModel {
    @SerializedName("response")
    @Expose
    private Boolean response;
    @SerializedName("responseString")
    @Expose
    private String responseString;
    @SerializedName("Polls")
    @Expose
    private List<Poll> polls = null;

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public List<Poll> getPolls() {
        return polls;
    }

    public void setPolls(List<Poll> polls) {
        this.polls = polls;
    }

    public class Poll {

        @SerializedName("poll_id")
        @Expose
        private String pollId;
        @SerializedName("question")
        @Expose
        private String question;
        @SerializedName("votes")
        @Expose
        private String votes;
        @SerializedName("correct_answer")
        @Expose
        private String correctAnswer;
        @SerializedName("total_votes")
        @Expose
        private Integer totalVotes;
        @SerializedName("voted_by")
        @Expose
        private String votedBy;
        @SerializedName("created_by")
        @Expose
        private String createdBy;
        @SerializedName("active_status")
        @Expose
        private String activeStatus;
        @SerializedName("lock_status")
        @Expose
        private String lockStatus;
        @SerializedName("archive_status")
        @Expose
        private String archiveStatus;
        @SerializedName("main_color")
        @Expose
        private String mainColor;
        @SerializedName("option_color_1")
        @Expose
        private String optionColor1;
        @SerializedName("option_color_2")
        @Expose
        private String optionColor2;
        @SerializedName("option_color_3")
        @Expose
        private String optionColor3;
        @SerializedName("option_color_4")
        @Expose
        private String optionColor4;
        @SerializedName("option_color_5")
        @Expose
        private String optionColor5;
        @SerializedName("option_color_6")
        @Expose
        private String optionColor6;
        @SerializedName("image_background")
        @Expose
        private String imageBackground;

        public String getPollId() {
            return pollId;
        }

        public void setPollId(String pollId) {
            this.pollId = pollId;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getVotes() {
            return votes;
        }

        public void setVotes(String votes) {
            this.votes = votes;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
        }

        public Integer getTotalVotes() {
            return totalVotes;
        }

        public void setTotalVotes(Integer totalVotes) {
            this.totalVotes = totalVotes;
        }

        public String getVotedBy() {
            return votedBy;
        }

        public void setVotedBy(String votedBy) {
            this.votedBy = votedBy;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getActiveStatus() {
            return activeStatus;
        }

        public void setActiveStatus(String activeStatus) {
            this.activeStatus = activeStatus;
        }

        public String getLockStatus() {
            return lockStatus;
        }

        public void setLockStatus(String lockStatus) {
            this.lockStatus = lockStatus;
        }

        public String getArchiveStatus() {
            return archiveStatus;
        }

        public void setArchiveStatus(String archiveStatus) {
            this.archiveStatus = archiveStatus;
        }

        public String getMainColor() {
            return mainColor;
        }

        public void setMainColor(String mainColor) {
            this.mainColor = mainColor;
        }

        public String getOptionColor1() {
            return optionColor1;
        }

        public void setOptionColor1(String optionColor1) {
            this.optionColor1 = optionColor1;
        }

        public String getOptionColor2() {
            return optionColor2;
        }

        public void setOptionColor2(String optionColor2) {
            this.optionColor2 = optionColor2;
        }

        public String getOptionColor3() {
            return optionColor3;
        }

        public void setOptionColor3(String optionColor3) {
            this.optionColor3 = optionColor3;
        }

        public String getOptionColor4() {
            return optionColor4;
        }

        public void setOptionColor4(String optionColor4) {
            this.optionColor4 = optionColor4;
        }

        public String getOptionColor5() {
            return optionColor5;
        }

        public void setOptionColor5(String optionColor5) {
            this.optionColor5 = optionColor5;
        }

        public String getOptionColor6() {
            return optionColor6;
        }

        public void setOptionColor6(String optionColor6) {
            this.optionColor6 = optionColor6;
        }

        public String getImageBackground() {
            return imageBackground;
        }

        public void setImageBackground(String imageBackground) {
            this.imageBackground = imageBackground;
        }
    }
}
