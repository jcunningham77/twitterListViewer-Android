package com.jeffcunningham.twitterlistviewer_android.di;

import android.app.Application;

import com.jeffcunningham.twitterlistviewer_android.BaseApplication;
import com.jeffcunningham.twitterlistviewer_android.util.Logger;
import com.jeffcunningham.twitterlistviewer_android.util.SharedPreferencesRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseApplication baseApplication);
    Application application();

    //seems like we need these available to use the @Inject at field level
    SharedPreferencesRepository sharedPreferencesRepository();
    Logger logger();

}