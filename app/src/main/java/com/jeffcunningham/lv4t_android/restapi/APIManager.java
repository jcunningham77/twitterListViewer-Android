package com.jeffcunningham.lv4t_android.restapi;

import com.jeffcunningham.lv4t_android.util.AppSettings;
import com.jeffcunningham.lv4t_android.util.Storage;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jeffcunningham on 12/20/16.
 */

public class APIManager {


    private static final int TIMEOUT_CONNECT = 1000;
    private static final int TIMEOUT_READ = 1000;
    private static final ConnectionPool CONNECTION_POOL = new ConnectionPool();
    private static final String TAG = "APIManager";

    //field injected by dagger
    Storage<AppSettings> appSettingsStorage;

    private final Retrofit retrofit;
    public APITransactions apiTransactions;

    @Inject
    public APIManager(Storage<AppSettings> appSettingsStorage){
        this.appSettingsStorage = appSettingsStorage;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONNECT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT_READ,TimeUnit.MILLISECONDS)
                .connectionPool(CONNECTION_POOL)
                .addInterceptor(logging)
                .build();

        String API_URL = appSettingsStorage.retrieve().API_URL;

        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.apiTransactions = retrofit.create(APITransactions.class);

    }

}
