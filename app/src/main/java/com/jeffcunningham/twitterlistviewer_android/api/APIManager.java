package com.jeffcunningham.twitterlistviewer_android.api;

import com.jeffcunningham.twitterlistviewer_android.api.dto.BELoginUserRequest;
import com.jeffcunningham.twitterlistviewer_android.api.dto.BELoginUserResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by jeffcunningham on 12/13/16.
 */

public class APIManager {

    static Retrofit backEndlessRetrofit = new Retrofit.Builder()
            .baseUrl("https://api.backendless.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public interface BackEndlessService {
        @POST("/api/login")
        Call<BELoginUserResponse> login(@Body BELoginUserRequest beLoginUserRequest);
    }

    static BackEndlessService backEndlessService = backEndlessRetrofit.create(BackEndlessService.class);

    public static BackEndlessService getBackEndlessService(){
        return backEndlessService;
    }


}
