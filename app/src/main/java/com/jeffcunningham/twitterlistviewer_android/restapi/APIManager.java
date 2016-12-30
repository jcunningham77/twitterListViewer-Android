package com.jeffcunningham.twitterlistviewer_android.restapi;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jeffcunningham on 12/20/16.
 */

public class APIManager {

    private static final Retrofit retrofit;

    private static final int TIMEOUT_CONNECT = 1000;
    private static final int TIMEOUT_READ = 1000;
    private static final ConnectionPool CONNECTION_POOL = new ConnectionPool();

    static {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONNECT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT_READ,TimeUnit.MILLISECONDS)
                .connectionPool(CONNECTION_POOL)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://twitterlistviewer-215api.rhcloud.com/" )
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static APITransactions apiTransactions = retrofit.create(APITransactions.class);




}
