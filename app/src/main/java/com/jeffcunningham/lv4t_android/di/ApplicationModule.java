package com.jeffcunningham.lv4t_android.di;

import android.app.Application;
import android.content.Context;

import com.jeffcunningham.lv4t_android.di.annotations.ApplicationContext;
import com.jeffcunningham.lv4t_android.util.ImageLoader;
import com.jeffcunningham.lv4t_android.util.ImageLoaderImpl;
import com.jeffcunningham.lv4t_android.util.ListsStorage;
import com.jeffcunningham.lv4t_android.util.ListsStorageImpl;
import com.jeffcunningham.lv4t_android.util.Logger;
import com.jeffcunningham.lv4t_android.util.LoggerImpl;
import com.jeffcunningham.lv4t_android.util.RemoteConfigUtil;
import com.jeffcunningham.lv4t_android.util.SharedPreferencesRepository;
import com.jeffcunningham.lv4t_android.util.SharedPreferencesRepositoryImpl;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by jeffcunningham on 1/22/17.
 */
@Module
public class ApplicationModule {

    private final Application application;
    private Context context;

    public ApplicationModule(Application application) {
        this.application = application;
        this.context = application.getApplicationContext();
    }

    /**
     * Expose the application to the graph.
     */
    @Provides
    @Singleton
    Application application() {
        return application;
    }

    @Provides
    @ApplicationContext
    public Context provideApplicationContext(){
        return context;
    }

    @Provides
    @Singleton
    RemoteConfigUtil provideRemoteConfigBuilder(RemoteConfigUtil impl){
        return impl;
    }


    @Provides
    @Singleton
    SharedPreferencesRepository providesSharedPreferencesRepository(SharedPreferencesRepositoryImpl impl){
        return impl;
    }

    @Provides
    Logger provideLogger(LoggerImpl impl){
        return impl;
    }

    @Provides
    ImageLoader provideImageLoader(ImageLoaderImpl impl){
        return impl;
    }

    @Provides
    @Singleton
    ListsStorage provideListStorage(ListsStorageImpl impl){
        return impl;
    }

    @Provides
    OkHttpClient provideHttpClient(){
        final int TIMEOUT_CONNECT = 1000;
        final int TIMEOUT_READ = 1000;
        final ConnectionPool CONNECTION_POOL = new ConnectionPool();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONNECT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT_READ,TimeUnit.MILLISECONDS)
                .connectionPool(CONNECTION_POOL)
                .addInterceptor(logging)
                .build();
        return client;
    }


}
