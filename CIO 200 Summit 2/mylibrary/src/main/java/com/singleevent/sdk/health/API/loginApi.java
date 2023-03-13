package com.singleevent.sdk.health.API;

import com.singleevent.sdk.health.Model.loginModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface loginApi {

    @FormUrlEncoded
    @POST("multievent_login")
    Call<loginModel> userOtp(
            @Field("event_id") String eventid,
            @Field("email") String email,
            @Field("action") String action,
            @Field("passkey") String passkey,
            @Field("deviceId") String device_id,
            @Field("deviceType") String device_type
    );
    @FormUrlEncoded
    @POST("createchallenge")
    Call<loginModel> userOtp1(
            @Field("challenge_id") String challenge_id,
            @Field("app_id") String app_id,
            @Field("title") String title,
            @Field("imageUrl") String imageUrl,
            @Field("description") String description,
            @Field("createdBy") String createdBy,
            @Field("challengeType") String challengeType,
            @Field("viewChallenge") String viewChallenge,
            @Field("challengeStatus") String challengeStatus,
            @Field("startDate") String startDate,
            @Field("endDate") String endDate
    );
}
