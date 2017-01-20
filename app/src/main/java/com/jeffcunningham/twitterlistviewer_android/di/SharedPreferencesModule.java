package com.jeffcunningham.twitterlistviewer_android.di;

import android.content.Context;

import com.jeffcunningham.twitterlistviewer_android.util.SharedPreferencesRepository;
import com.jeffcunningham.twitterlistviewer_android.util.SharedPreferencesRepositoryImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jeffcunningham on 1/19/17.
 */
@Module
public class SharedPreferencesModule {

    private Context context;

    public SharedPreferencesModule(Context context){
        this.context = context;
    }

    @Provides
    SharedPreferencesRepository provideSharedPreferences(Context context){
        return new SharedPreferencesRepositoryImpl(context);
    }

}
