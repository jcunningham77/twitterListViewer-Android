package com.jeffcunningham.twitterlistviewer_android.restapi;

import com.jeffcunningham.twitterlistviewer_android.restapi.dto.DefaultList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by jeffcunningham on 12/29/16.
 */

public interface APITransactions {

    @GET("api/default-list/{alias}")
    Call<DefaultList> getDefaultList(@Path("alias")String alias);

    @POST("api/default-list")
    Call<DefaultList> postDefaultList(@Body DefaultList defaultList);
}
