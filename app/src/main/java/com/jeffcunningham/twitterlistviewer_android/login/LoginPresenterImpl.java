package com.jeffcunningham.twitterlistviewer_android.login;

import com.jeffcunningham.twitterlistviewer_android.util.Logger;
import com.jeffcunningham.twitterlistviewer_android.util.SharedPreferencesRepository;

import javax.inject.Inject;

/**
 * Created by jeffcunningham on 1/24/17.
 */

public class LoginPresenterImpl implements LoginPresenter {


    SharedPreferencesRepository sharedPreferencesRepository;
    Logger logger;
    private static final String TAG = "LoginPresenterImpl";

    @Inject
    public LoginPresenterImpl(SharedPreferencesRepository sharedPreferencesRepository, Logger logger) {
        this.sharedPreferencesRepository = sharedPreferencesRepository;
        this.logger = logger;
    }

    @Override
    public void clearSharedPreferencesData() {
        logger.info(TAG, "Clearing Shared Preferences");
        sharedPreferencesRepository.clearDefaultListData();

    }
}
