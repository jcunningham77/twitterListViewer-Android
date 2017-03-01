package com.jeffcunningham.lv4t_android.login;

import android.os.Build;
import android.webkit.CookieManager;

import com.jeffcunningham.lv4t_android.util.ListsStorage;
import com.jeffcunningham.lv4t_android.util.Logger;
import com.jeffcunningham.lv4t_android.util.SharedPreferencesRepository;
import com.twitter.sdk.android.Twitter;

import javax.inject.Inject;

/**
 * Created by jeffcunningham on 1/24/17.
 */

public class LoginPresenterImpl implements LoginPresenter {


    SharedPreferencesRepository sharedPreferencesRepository;
    Logger logger;
    private static final String TAG = "LoginPresenterImpl";
    private ListsStorage listsStorage;

    @Inject
    public LoginPresenterImpl(SharedPreferencesRepository sharedPreferencesRepository, Logger logger, ListsStorage listsStorage) {
        this.sharedPreferencesRepository = sharedPreferencesRepository;
        this.logger = logger;
        this.listsStorage = listsStorage;
    }

    @Override
    public void clearSharedPreferencesData() {
        logger.info(TAG, "Clearing Shared Preferences");
        sharedPreferencesRepository.clearDefaultListData();

    }

    @Override
    public void logoutOfTwitter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        }
        Twitter.getSessionManager().clearActiveSession();
        Twitter.logOut();
        listsStorage.clearListsCache();

    }
}
