package com.singleevent.sdk.pojo.quizmodulepojo;

import java.util.List;

public class QuizQuestionPojo {
    String question;
    String question_id;
    String quiz_id;
    List<QuizAnswerPojo> answerList;
    boolean answered;
    String quizName;
    public QuizQuestionPojo(String question, String question_id, String quiz_id, List<QuizAnswerPojo> answerList, String quizName) {
        this.question = question;
        this.question_id = question_id;
        this.quiz_id = quiz_id;
        this.answerList = answerList;
        this.quizName=quizName;
    }

    public String getQuestion() {
        return question;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public List<QuizAnswerPojo> getAnswerList() {
        return answerList;
    }

    public boolean isAnswered() {
        return answered;
    }

    public String getQuizName() {
        return quizName;
    }
}
