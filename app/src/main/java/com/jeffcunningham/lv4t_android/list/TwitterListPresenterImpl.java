package com.jeffcunningham.lv4t_android.list;

import com.jeffcunningham.lv4t_android.util.SharedPreferencesRepository;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;

import javax.inject.Inject;

/**
 * Created by jeffcunningham on 1/24/17.
 */

public class TwitterListPresenterImpl implements TwitterListPresenter {

    private TwitterSession twitterSession;
    private SharedPreferencesRepository sharedPreferencesRepository;

    @Inject
    public TwitterListPresenterImpl(SharedPreferencesRepository sharedPreferencesRepository) {
        this.sharedPreferencesRepository = sharedPreferencesRepository;

    }

    @Override
    public String getTwitterUserName() {
        twitterSession = Twitter.getSessionManager().getActiveSession();
        return twitterSession.getUserName();
    }

    @Override
    public String getTwitterAvatarImgUrl() {
        return sharedPreferencesRepository.getTwitterAvatarImgUrl();
    }
}
