package com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * Created by jeffcunningham on 12/12/16.
 */

public class TwitterApiClientExtension extends TwitterApiClient {

    public TwitterApiClientExtension(TwitterSession session) {
        super(session);
    }



    /**
     * @return {@link com.twitter.sdk.android.core.services.FavoriteService} to access TwitterApi
     */
    public ListOwnershipService getListOwnershipService() {
        return getService(ListOwnershipService.class);
    }
}
