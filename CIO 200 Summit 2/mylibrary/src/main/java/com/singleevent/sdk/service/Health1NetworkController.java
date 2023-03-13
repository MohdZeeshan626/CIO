package com.singleevent.sdk.service;

import com.singleevent.sdk.interfaces.NetworkInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Health1NetworkController {
    NetworkInterface service;
    String base_url="https://health1.webmobi.in/health/";
    public NetworkInterface getService(){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit=new Retrofit.Builder().baseUrl(base_url).client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).build();
        service=retrofit.create(NetworkInterface.class);
        return service;
    }
    public static Health1NetworkController getInstance(){
        return new Health1NetworkController();
    }
}
