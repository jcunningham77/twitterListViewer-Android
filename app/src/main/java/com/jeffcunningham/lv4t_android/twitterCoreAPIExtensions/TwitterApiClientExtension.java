package com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

import okhttp3.OkHttpClient;

/**
 * Created by jeffcunningham on 12/12/16.
 */

public class TwitterApiClientExtension extends TwitterApiClient {

    public TwitterApiClientExtension(TwitterSession session, OkHttpClient client) {
        super(session, client);
    }



    /**
     * Returns a TwitterAPIService that can query the TwitterAPI for lists owned by a given Twitter account
     * @return {@link com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions} to access TwitterApi
     */
    public TwitterAPIService getTwitterAPIService() {
        return getService(TwitterAPIService.class);
    }
}
