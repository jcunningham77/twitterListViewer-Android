package com.jeffcunningham.twitterlistviewer_android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.jeffcunningham.twitterlistviewer_android.model.dto.TwitterList;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.ListOwnershipService;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.TwitterApiClientExtension;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.List;

import retrofit2.Call;

/**
 * Created by jeffcunningham on 12/10/16.
 */

public class ListsActivity extends Activity {

    private static final String TAG = "ListsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);



        TwitterSession twitterSession = Twitter.getSessionManager().getActiveSession();
        TwitterApiClientExtension twitterApiClientExtension = new TwitterApiClientExtension(Twitter.getSessionManager().getActiveSession());
        ListOwnershipService listOwnershipService = twitterApiClientExtension.getListOwnershipService();

        Call<List<TwitterList>> listMembership= listOwnershipService.listOwnershipByScreenName(twitterSession.getUserName());

        listMembership.enqueue(new Callback<List<TwitterList>>(){


            @Override
            public void success(Result<List<TwitterList>> result) {

                for (TwitterList twitterList : result.data){
                    Log.i(TAG, "success: twitterList = " + twitterList.getFullName());
                }
            }

            @Override
            public void failure(TwitterException exception) {

                Log.e(TAG, "failure: " + exception.getMessage());
                Log.getStackTraceString(exception);

            }
        });

    }
}
