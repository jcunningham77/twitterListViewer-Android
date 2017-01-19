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
     * Returns a ListOwnershipService that can query the TwitterAPI for lists owned by a given Twitter account
     * @return {@link com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions} to access TwitterApi
     */
    public ListOwnershipService getListOwnershipService() {
        return getService(ListOwnershipService.class);
    }
}
