package com.jeffcunningham.lv4t_android.login;

/**
 * Created by jeffcunningham on 1/24/17.
 * This interface doesn't extend BasePresenter to inherit the start/stop lifecycle behavior, because the LoginPresenter doesn't
 * make any asynch calls that would have to be cancelled.
 */

public interface LoginPresenter {

    void clearSharedPreferencesData();

    void logoutOfTwitter();
}
