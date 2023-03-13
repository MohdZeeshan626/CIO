package com.singleevent.sdk.health.API;

import com.singleevent.sdk.health.Model.loginModel;
import com.singleevent.sdk.health.Model.passwordModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PasswordApi {
    @FormUrlEncoded
    @POST("multievent_login")
    Call<passwordModel> passwordOtp(
            @Field("event_id") String eventid,
            @Field("email") String email,
            @Field("action") String action,
            @Field("passkey") String passkey,
            @Field("deviceId") String device_id,
            @Field("deviceType") String device_type
    );
}