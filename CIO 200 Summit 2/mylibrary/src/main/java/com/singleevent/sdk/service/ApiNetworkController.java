package com.singleevent.sdk.service;

import com.singleevent.sdk.ApiList;
import com.singleevent.sdk.interfaces.NetworkInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiNetworkController {

    NetworkInterface service;
//    String base_url="https://api.webmobi.com/api/event/";
    String base_url= ApiList.dom +"/api/event/";
    public NetworkInterface getService(){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit=new Retrofit.Builder().baseUrl(base_url).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build();
        service=retrofit.create(NetworkInterface.class);
        return service;
    }
    public static ApiNetworkController getInstance(){
        return new ApiNetworkController();
    }

}
