package com.jeffcunningham.lv4t_android.util;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.jeffcunningham.lv4t_android.BuildConfig;
import com.jeffcunningham.lv4t_android.R;

import javax.inject.Inject;

/**
 * Created by jeffcunningham on 1/30/18.
 */

public class RemoteConfigUtil {

    private Logger logger;

    private static final String TAG = "RemoteConfigUtil";

    @Inject
    public RemoteConfigUtil(Logger logger) {
        this.logger = logger;
    }

    public FirebaseRemoteConfig initializeFirebaseRemoteConfig() {

        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.REMOTE_CONFIG_DEV_MODE)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_default);

        long cacheExpiration = 2400; // 1 hour in seconds.
        firebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (firebaseRemoteConfig.activateFetched()) {
                    logger.info(TAG, "initializeFirebaseRemoteConfig: activation successful");
                } else {
                    logger.info(TAG, "initializeFirebaseRemoteConfig: activation unsuccessful");
                }
            }
        });

        return firebaseRemoteConfig;

    }


}
