package com.singleevent.sdk;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FeedpostTest {
    FeedPost SUT;
    com.singleevent.sdk.View.RightActivity.FeedPost SUT2;
    List<String> answers = new ArrayList<>();
    List<String> correctAnswer = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        SUT = new FeedPost();
        SUT2=new com.singleevent.sdk.View.RightActivity.FeedPost();
        answers.add("aaa");
        answers.add("bbb");
        answers.add("ccc");
    }

    @Test
    public void checkSelectedAnswerBelongToList() {
        correctAnswer.add("ccc");
        assertTrue(SUT.checkCorrectAnswer(answers, correctAnswer));
    }

    @Test
    public void checkSelectedAnswerNotBelongToList() {
        correctAnswer.add("cccaa");
        assertFalse(SUT.checkCorrectAnswer(answers, correctAnswer));
    }

    @Test
    public void checkSelectAnswerIsHavingEmptyString() {
        correctAnswer.add("");
        assertFalse(SUT.checkCorrectAnswer(answers, correctAnswer));
    }

    @Test
    public void checkSelectAnswerIsNullList() {
        assertFalse(SUT.checkCorrectAnswer(answers, correctAnswer));
    }

    @Test
    public void checkSentanceConatainURl() {
        assertTrue(SUT.checkStringContainUrl("www.gdfdgg.com"));
    }

    @Test
    public void checkSentenceDoNotContainURl() {
        assertFalse(SUT.checkStringContainUrl("how is the Day Today. What is yourView"));
    }

    @Test
    public void checkAPIToActivatePoll() {
        assertTrue(SUT.activePollQuestion("47SX"));

    }

    @Test
    public void checkSuccessStatusForActivePollApi() {
        assertTrue(SUT.activePollQuestionStatus_check("47SX")==200);
    }

    @Test
    public void checkAPIToActivatePollForWrongPollId(){
        assertFalse(SUT.activePollQuestion(""));
    }
    @Test
    public void checkNotFoundStatusForActivePollApi() {
        assertTrue(SUT.activePollQuestionStatus_check("dsfd")==404);
    }

    @Test
    public void checkAPIToActivatePollIfPollIdIsEmpty(){
        assertFalse(SUT.activePollQuestion(null));
    }
    @Test
    public void checkBadRequestStatusForActivePollApi() {
        assertTrue(SUT.activePollQuestionStatus_check(null)==400);
    }




}
