package com.jeffcunningham.twitterlistviewer_android;

import android.app.Application;
import android.util.Log;

import com.backendless.Backendless;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by jeffcunningham on 12/8/16.
 */

public class BaseApplication extends Application {


    private static final String TWITTER_KEY = BuildConfig.TWITTER_KEY;
    private static final String TWITTER_SECRET = BuildConfig.TWITTER_SECRET;
    private static final String BACKENDLESS_APP_ID = BuildConfig.BACKENDLESS_APP_ID;
    private static final String BACKENDLESS_KEY = BuildConfig.BACKENDLESS_KEY;
    private static final String BACKENDLESS_APP_VERSION = BuildConfig.BACKENDLESS_APP_VERSION;

    private static final String TAG = "BaseApplication";
    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG, "onCreate: TWITTER_KEY" + TWITTER_KEY);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);

        Fabric.with(this, new Twitter(authConfig));

        Backendless.initApp( this, BACKENDLESS_APP_ID, BACKENDLESS_KEY, BACKENDLESS_APP_VERSION);
    }
}
