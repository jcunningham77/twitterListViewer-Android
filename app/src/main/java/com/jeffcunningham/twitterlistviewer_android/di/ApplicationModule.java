package com.jeffcunningham.twitterlistviewer_android.di;

import android.app.Application;
import android.content.Context;

import com.jeffcunningham.twitterlistviewer_android.di.annotations.ApplicationContext;
import com.jeffcunningham.twitterlistviewer_android.util.ImageLoader;
import com.jeffcunningham.twitterlistviewer_android.util.ImageLoaderImpl;
import com.jeffcunningham.twitterlistviewer_android.util.Logger;
import com.jeffcunningham.twitterlistviewer_android.util.LoggerImpl;
import com.jeffcunningham.twitterlistviewer_android.util.SharedPreferencesRepository;
import com.jeffcunningham.twitterlistviewer_android.util.SharedPreferencesRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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



    //will not be supplying TwitterSession via Dagger-injected constructors
    //Probably easier to get current session from presenter methods in case user
    //switches Twitter accounts
//    @Provides
//    TwitterSession provideTwitterSession(){
//        return Twitter.getSessionManager().getActiveSession();
//    };

}
