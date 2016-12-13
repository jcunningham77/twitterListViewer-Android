package com.jeffcunningham.twitterlistviewer_android;

import android.app.Activity;
import android.os.Bundle;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.services.StatusesService;

/**
 * Created by jeffcunningham on 12/10/16.
 */

public class ListsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();

//        final TwitterListTimeline listTimeline = new TwitterListTimeline.Builder()
//                .slugWithOwnerId()
//                .build();
//        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
//                .setTimeline(userTimeline)
//                .build();
//        setListAdapter(adapter);
    }
}
