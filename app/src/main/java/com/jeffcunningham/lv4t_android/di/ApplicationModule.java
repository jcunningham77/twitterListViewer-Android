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
import com.jeffcunningham.lv4t_android.util.SharedPreferencesRepository;
import com.jeffcunningham.lv4t_android.util.SharedPreferencesRepositoryImpl;

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

    @Provides
    @Singleton
    ListsStorage provideListStorage(ListsStorageImpl impl){
        return impl;
    }


}
