package com.jeffcunningham.lv4t_android.util;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.jeffcunningham.lv4t_android.R;

import javax.inject.Inject;

import static com.jeffcunningham.lv4t_android.util.Constants.API_URL;

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

    public FirebaseRemoteConfig initializeFirebaseRemoteConfig(){



        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);

        firebaseRemoteConfig.setDefaults(R.xml.remote_config_default);

        long cacheExpiration = 0; // 1 hour in seconds.

        logger.info(TAG, "before fetch: firebaseRemoteConfig.getString(API_URL) = " + firebaseRemoteConfig.getString(API_URL) );
        firebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                logger.info(TAG, "onComplete: fetch completed");
                logger.info(TAG, "after fetch, before activation: firebaseRemoteConfig.getString(API_URL) = " + firebaseRemoteConfig.getString(API_URL) );
                if(firebaseRemoteConfig.activateFetched()){
                    logger.info(TAG, "initializeFirebaseRemoteConfig: activation successful");
                    logger.info(TAG, "after fetch, after activation: firebaseRemoteConfig.getString(API_URL) = " + firebaseRemoteConfig.getString(API_URL) );
                } else {
                    logger.info(TAG, "initializeFirebaseRemoteConfig: activation unsuccessful");
                }
            }
        });


        return firebaseRemoteConfig;

    }


}
