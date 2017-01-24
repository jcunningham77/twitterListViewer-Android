package com.jeffcunningham.twitterlistviewer_android.lists;

import android.util.Log;

import com.jeffcunningham.twitterlistviewer_android.events.GetDefaultListSuccessEvent;
import com.jeffcunningham.twitterlistviewer_android.events.GetListOwnershipByTwitterUserSuccessEvent;
import com.jeffcunningham.twitterlistviewer_android.restapi.APIManager;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.get.DefaultList;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.ListOwnershipService;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.TwitterApiClientExtension;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.dto.TwitterList;
import com.jeffcunningham.twitterlistviewer_android.util.SharedPreferencesRepository;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by jeffcunningham on 1/19/17.
 */

public class ListsPresenterImpl implements ListsPresenter {

    SharedPreferencesRepository sharedPreferencesRepository;



    @Inject
    public ListsPresenterImpl(SharedPreferencesRepository sharedPreferencesRepository) {



        this.sharedPreferencesRepository = sharedPreferencesRepository;
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

    public void getDefaultListId(){
        String userName = twitterSession.getUserName();
        Call<DefaultList> getDefaultListCall = apiManager.apiTransactions.getDefaultList(userName);


        getDefaultListCall.enqueue(new retrofit2.Callback<DefaultList>() {
            @Override
            public void onResponse(Call<DefaultList> call, Response<DefaultList> response) {
                Log.i(TAG, "onResponse: Retrofit call to Node get default list API succeeded, default list id = " + response.body());

                persistDefaultListDataToSharedPreferences(response.body().getSlug(),response.body().getListName());
                EventBus.getDefault().post(new GetDefaultListSuccessEvent(response.body()));
//                setDefaultListIdForAdapterLists(response.body());
            }

            @Override
            public void onFailure(Call<DefaultList> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);

            }
        });
    }

    private void persistDefaultListDataToSharedPreferences(String slug, String listName){
        Log.i(TAG, "persistDefaultListDataToSharedPreferences: slug = " + slug + ", listName = " + listName);

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("slug",slug);
//        editor.putString("listName",listName);
//        editor.commit();
        sharedPreferencesRepository.persistDefaultListData(slug,listName);


    }

}
