package com.jeffcunningham.twitterlistviewer_android.lists;

import android.util.Log;

import com.jeffcunningham.twitterlistviewer_android.events.GetListOwnershipByTwitterUserSuccessEvent;
import com.jeffcunningham.twitterlistviewer_android.restapi.APIManager;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.ListOwnershipService;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.TwitterApiClientExtension;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.dto.TwitterList;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;

/**
 * Created by jeffcunningham on 1/19/17.
 */

public class ListsPresenterImpl {

    public ListsPresenterImpl() {

    }

    private APIManager apiManager;

    TwitterSession twitterSession;
    private static final String TAG = "ListsPresenterImpl";


    public void getListMembershipByTwitterUser(){

        // get current Twitter user's list membership
        this.twitterSession = Twitter.getSessionManager().getActiveSession();
        TwitterApiClientExtension twitterApiClientExtension = new TwitterApiClientExtension(Twitter.getSessionManager().getActiveSession());
        ListOwnershipService listOwnershipService = twitterApiClientExtension.getListOwnershipService();

        Call<List<TwitterList>> listMembership= listOwnershipService.listOwnershipByScreenName(twitterSession.getUserName());

        listMembership.enqueue(new Callback<List<TwitterList>>(){


            @Override
            public void success(Result<List<TwitterList>> result) {

                for (TwitterList twitterList : result.data){
                    Log.i(TAG, "success: twitterList = " + twitterList.getFullName());
                }

                EventBus.getDefault().post(new GetListOwnershipByTwitterUserSuccessEvent(result.data));


                //listsAdapter.setTwitterUserId(twitterSession.getUserId());
                //listsAdapter.setTwitterLists(result.data);

                //todo-- execute from ListsFragment
                //getDefaultListId(twitterSession.getUserName());

            }

            @Override
            public void failure(TwitterException exception) {

                Log.e(TAG, "failure: " + exception.getMessage());
                Log.getStackTraceString(exception);

            }
        });

    }

}
