package com.jeffcunningham.twitterlistviewer_android.list;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;

import javax.inject.Inject;

/**
 * Created by jeffcunningham on 1/24/17.
 */

public class TwitterListPresenterImpl implements TwitterListPresenter {

    private TwitterSession twitterSession;

    @Inject
    public TwitterListPresenterImpl() {

    }

    @Override
    public String getTwitterUserName() {
        twitterSession = Twitter.getSessionManager().getActiveSession();
        return twitterSession.getUserName();
    }
}
