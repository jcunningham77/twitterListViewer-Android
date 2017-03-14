package com.jeffcunningham.lv4t_android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.jeffcunningham.lv4t_android.di.ApplicationComponent;
import com.jeffcunningham.lv4t_android.di.ApplicationModule;
import com.jeffcunningham.lv4t_android.di.DaggerApplicationComponent;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by jeffcunningham on 12/8/16.
 */

public class BaseApplication extends Application {

    private static final String TWITTER_KEY = BuildConfig.TWITTER_KEY;
    private static final String TWITTER_SECRET = BuildConfig.TWITTER_SECRET;
    private ApplicationComponent applicationComponent;

    private static final String TAG = "BaseApplication";
    @Override
    public void onCreate() {
        super.onCreate();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
