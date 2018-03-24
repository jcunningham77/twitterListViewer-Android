package com.jeffcunningham.lv4t_android.util;

import javax.inject.Inject;

/**
 * Created by jeffcunningham on 3/24/18.
 */

public class AppSettingsStorage implements Storage<AppSettings> {

    private AppSettings appSettings;
    private Logger logger;

    private static final String TAG = "AppSettingsStorage";

    @Inject
    public AppSettingsStorage(Logger logger) {
        this.logger = logger;
        logger.info(TAG, "AppSettingsStorage: constructor called");
    }

    @Override
    public void update(AppSettings value) {
        logger.info(TAG,"updatingAppSettings:" + value.toString());
        this.appSettings = value;

    }

    @Override
    public AppSettings retrieve() {
        logger.info(TAG, "retrieve: AppSetttings = " + appSettings.toString());
        return appSettings;
    }
}
