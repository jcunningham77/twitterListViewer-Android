package com.jeffcunningham.twitterlistviewer_android.di;

import android.app.Application;

import com.jeffcunningham.twitterlistviewer_android.BaseApplication;
import com.jeffcunningham.twitterlistviewer_android.util.SharedPreferencesRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseApplication baseApplication);
    Application application();

//    SharedPreferences sharedPreferences();

    SharedPreferencesRepository sharedPreferencesRepository();

}
