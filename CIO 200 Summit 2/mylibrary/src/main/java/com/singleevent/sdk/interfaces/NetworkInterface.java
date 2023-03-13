package com.singleevent.sdk.interfaces;


import com.google.gson.JsonObject;
import com.singleevent.sdk.model.Polling.CreatePollResponse;
import com.singleevent.sdk.model.Polling.PollingModel;
import com.singleevent.sdk.model.Polling.VoteModel;
import com.singleevent.sdk.model.quiz.QuizModel;
import com.singleevent.sdk.model.quiz.SubmitQuizAnswerModel;
import com.singleevent.sdk.pojo.pollingpojo.CreatePollPojo;
import com.singleevent.sdk.pojo.pollingpojo.PostVoteForPolling;
import com.singleevent.sdk.pojo.pollingpojo.PostingVotePojo;
import com.singleevent.sdk.pojo.quizmodulepojo.QuizAnswerSubmittingPojo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NetworkInterface {


    @GET("getpolls")
    Call<PollingModel> getPolls(@Query("app_id") String appid);

    @POST("vote")
    Call<VoteModel> vote(@Body PostVoteForPolling postVoteForPolling);


    @GET("getallquiz")
    Call<QuizModel> getAllQuiz(@Query("app_id") String app_id);

    @POST("answerquiz")
    Call<SubmitQuizAnswerModel> answerQuiz(@Body QuizAnswerSubmittingPojo quizAnswerSubmittingPojo);

    @POST("createpoll")
    Call<CreatePollResponse> createPoll(@Body CreatePollPojo createPollPojo);

    @GET("changeactivestatus")
    Call<JsonObject> changePollActiveStatus(@Query("app_id") String app_id,@Query("poll_id")String poll_id);
    //----------------------health API END---------------------------------------//


}
