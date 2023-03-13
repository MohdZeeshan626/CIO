package com.singleevent.sdk.interfaces;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public interface OnRecyclerVoteClick {
    void votePoll(String answer,String poll_id, int position);
    void answerQuiz(String answer,String question_id,String quiz_id, int position);

    void getCheckedItems(List<String> value, String question_id,String Quiz_id);
    //    void nextAnswer(int value);
    void nextAnswer(String answers, String question_id, String quiz_id, int value, RecyclerView recyclerView, int id, int parent_pos);

}
