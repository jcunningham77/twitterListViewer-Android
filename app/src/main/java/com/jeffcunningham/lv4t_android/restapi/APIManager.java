package com.jeffcunningham.lv4t_android.restapi;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.jeffcunningham.lv4t_android.BuildConfig;
import com.jeffcunningham.lv4t_android.R;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.jeffcunningham.lv4t_android.util.Constants.API_URL;

/**
 * Created by jeffcunningham on 12/20/16.
 */

public class APIManager {

    private static final Retrofit retrofit;

    private static final int TIMEOUT_CONNECT = 1000;
    private static final int TIMEOUT_READ = 1000;
    private static final ConnectionPool CONNECTION_POOL = new ConnectionPool();
    private static FirebaseRemoteConfig firebaseRemoteConfig;




    static {


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONNECT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT_READ,TimeUnit.MILLISECONDS)
                .connectionPool(CONNECTION_POOL)
                .addInterceptor(logging)
                .build();


        //todo - use the builder - figure out how to use that from a static context, or another solution
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
        long cacheExpiration = 60; // 1 hour in seconds.
        firebaseRemoteConfig.fetch(cacheExpiration);


        retrofit = new Retrofit.Builder()
                .baseUrl(firebaseRemoteConfig.getString(API_URL))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static APITransactions apiTransactions = retrofit.create(APITransactions.class);




}
