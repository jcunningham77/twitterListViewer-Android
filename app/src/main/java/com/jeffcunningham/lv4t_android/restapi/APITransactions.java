package com.jeffcunningham.lv4t_android.restapi;

import com.jeffcunningham.lv4t_android.restapi.dto.get.DefaultList;
import com.jeffcunningham.lv4t_android.restapi.dto.post.PostDefaultList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by jeffcunningham on 12/29/16.
 */

public interface APITransactions {

    @GET("api/default-list/{alias}")

    Call<DefaultList> getDefaultList(@Path("alias")String alias);

    @POST("api/default-list")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Call<DefaultList> postDefaultList(@Body PostDefaultList defaultList);
}
