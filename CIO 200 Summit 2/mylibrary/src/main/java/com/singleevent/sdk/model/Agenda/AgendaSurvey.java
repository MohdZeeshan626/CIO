package com.singleevent.sdk.model.Agenda;

/**
 * Created by webmodi on 4/1/2016.
 */
public class AgendaSurvey {
    String type;
    String question;
    String id;
    surveyanswer[] answers;

    public String getType() {
        return type;
    }

    public String getQuestion() {
        return question;
    }

    public String getId() {
        return id;
    }

    public surveyanswer getAnswers(int i) {
        return answers[i];
    }

    public int getAnswersSize() {
        if (answers == null) return 0;
        else return answers.length;
    }

}
