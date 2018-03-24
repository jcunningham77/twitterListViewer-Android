package com.jeffcunningham.lv4t_android.restapi;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.jeffcunningham.lv4t_android.R;
import com.jeffcunningham.lv4t_android.util.Logger;
import com.jeffcunningham.lv4t_android.util.LoggerImpl;

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
    
    private static Logger logger = new LoggerImpl();


    private static final String TAG = "APIManager";


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
        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_default);
        long cacheExpiration = 0; // 1 hour in seconds.
        firebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                logger.info(TAG, "onComplete: fetch completed");
            }
        });
        if (firebaseRemoteConfig.activateFetched()){
            logger.info(TAG,"success activated fetched config, firebaseRemoteConfig.getString(API_URL) = " + firebaseRemoteConfig.getString(API_URL));
        } else {
            logger.info(TAG,"failed activated fetched config");
        }


        retrofit = new Retrofit.Builder()
                .baseUrl(firebaseRemoteConfig.getString(API_URL))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static APITransactions apiTransactions = retrofit.create(APITransactions.class);




}
