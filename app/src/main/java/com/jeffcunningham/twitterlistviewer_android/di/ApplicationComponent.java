package com.jeffcunningham.twitterlistviewer_android.di;

import android.app.Application;
import android.content.SharedPreferences;

import com.jeffcunningham.twitterlistviewer_android.BaseApplication;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseApplication baseApplication);
    Application application();

    SharedPreferences sharedPreferences();

}
