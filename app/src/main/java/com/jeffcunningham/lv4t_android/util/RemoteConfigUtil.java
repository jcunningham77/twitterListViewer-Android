package com.jeffcunningham.lv4t_android.util;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.jeffcunningham.lv4t_android.BuildConfig;
import com.jeffcunningham.lv4t_android.R;

import javax.inject.Inject;

/**
 * Created by jeffcunningham on 1/30/18.
 */

public class RemoteConfigUtil {

    @Inject
    public RemoteConfigUtil() {
    }

    public FirebaseRemoteConfig initializeFirebaseRemoteConfig(){



        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);

        firebaseRemoteConfig.setDefaults(R.xml.remote_config_default);

        long cacheExpiration = 60; // 1 hour in seconds.
        firebaseRemoteConfig.fetch(cacheExpiration);

        return firebaseRemoteConfig;

    }


}
