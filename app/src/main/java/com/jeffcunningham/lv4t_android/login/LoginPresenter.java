package com.jeffcunningham.lv4t_android.login;

/**
 * Created by jeffcunningham on 1/24/17.
 */

public interface LoginPresenter {

    void clearSharedPreferencesData();

    void logoutOfTwitter();
}
