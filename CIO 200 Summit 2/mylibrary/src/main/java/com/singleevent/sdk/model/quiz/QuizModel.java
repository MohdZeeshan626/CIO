package com.singleevent.sdk.model.quiz;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizModel {

        @SerializedName("response")
        @Expose
        private Boolean response;
        @SerializedName("responseString")
        @Expose
        private String responseString;
        @SerializedName("data")
        @Expose
        private List<Datum> data = null;

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

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }

    public class Datum {

        @SerializedName("quiz_id")
        @Expose
        private String quizId;
        @SerializedName("app_id")
        @Expose
        private String appId;
        @SerializedName("quiz_name")
        @Expose
        private String quizName;
        @SerializedName("created_by")
        @Expose
        private String createdBy;
        @SerializedName("no_of_questions")
        @Expose
        private Integer noOfQuestions;
        @SerializedName("lock_status")
        @Expose
        private String lockStatus;
        @SerializedName("active_status")
        @Expose
        private String activeStatus;
        @SerializedName("quiz_data")
        @Expose
        private String quizData;
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
        private Object imageBackground;
        @SerializedName("question_array")
        @Expose
        private List<QuestionArray> questionArray = null;

        public String getQuizId() {
            return quizId;
        }

        public void setQuizId(String quizId) {
            this.quizId = quizId;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getQuizName() {
            return quizName;
        }

        public void setQuizName(String quizName) {
            this.quizName = quizName;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public Integer getNoOfQuestions() {
            return noOfQuestions;
        }

        public void setNoOfQuestions(Integer noOfQuestions) {
            this.noOfQuestions = noOfQuestions;
        }

        public String getLockStatus() {
            return lockStatus;
        }

        public void setLockStatus(String lockStatus) {
            this.lockStatus = lockStatus;
        }

        public String getActiveStatus() {
            return activeStatus;
        }

        public void setActiveStatus(String activeStatus) {
            this.activeStatus = activeStatus;
        }

        public String getQuizData() {
            return quizData;
        }

        public void setQuizData(String quizData) {
            this.quizData = quizData;
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

        public Object getImageBackground() {
            return imageBackground;
        }

        public void setImageBackground(Object imageBackground) {
            this.imageBackground = imageBackground;
        }

        public List<QuestionArray> getQuestionArray() {
            return questionArray;
        }

        public void setQuestionArray(List<QuestionArray> questionArray) {
            this.questionArray = questionArray;
        }

    }

    public class QuestionArray {

        @SerializedName("question_id")
        @Expose
        private String questionId;
        @SerializedName("question")
        @Expose
        private String question;
        @SerializedName("options")
        @Expose
        private String options;
        @SerializedName("correctanswer")
        @Expose
        private String correctanswer;
        @SerializedName("isActive")
        @Expose
        private Integer isActive;
        @SerializedName("votes")
        @Expose
        private String votes;
        @SerializedName("total_votes")
        @Expose
        private Integer totalVotes;
        @SerializedName("activated_at")
        @Expose
        private Object activatedAt;
        @SerializedName("quiz_id")
        @Expose
        private String quizId;

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
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

        public String getCorrectanswer() {
            return correctanswer;
        }

        public void setCorrectanswer(String correctanswer) {
            this.correctanswer = correctanswer;
        }

        public Integer getIsActive() {
            return isActive;
        }

        public void setIsActive(Integer isActive) {
            this.isActive = isActive;
        }

        public String getVotes() {
            return votes;
        }

        public void setVotes(String votes) {
            this.votes = votes;
        }

        public Integer getTotalVotes() {
            return totalVotes;
        }

        public void setTotalVotes(Integer totalVotes) {
            this.totalVotes = totalVotes;
        }

        public Object getActivatedAt() {
            return activatedAt;
        }

        public void setActivatedAt(Object activatedAt) {
            this.activatedAt = activatedAt;
        }

        public String getQuizId() {
            return quizId;
        }

        public void setQuizId(String quizId) {
            this.quizId = quizId;
        }

    }
}
