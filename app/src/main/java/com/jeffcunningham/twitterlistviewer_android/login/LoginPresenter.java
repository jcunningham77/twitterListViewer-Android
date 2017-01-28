package com.jeffcunningham.twitterlistviewer_android.login;

/**
 * Created by jeffcunningham on 1/24/17.
 */

public interface LoginPresenter {

    void clearSharedPreferencesData();

    void logoutOfTwitter();
}
