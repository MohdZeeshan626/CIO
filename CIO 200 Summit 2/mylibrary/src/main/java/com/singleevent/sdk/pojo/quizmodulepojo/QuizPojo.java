package com.singleevent.sdk.pojo.quizmodulepojo;

public class QuizPojo {
    String quiz_id;
    String quiz_name;
    int question_count;
    String lock_status;

    public QuizPojo(String quiz_id, String quiz_name, int question_count, String lock_status) {
        this.quiz_id = quiz_id;
        this.quiz_name = quiz_name;
        this.question_count = question_count;
        this.lock_status = lock_status;
    }

    public String getQuiz_id() {
        return quiz_id;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public int getQuestion_count() {
        return question_count;
    }

    public String getLock_status() {
        return lock_status;
    }
}
