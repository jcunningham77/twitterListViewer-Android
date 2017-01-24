package com.jeffcunningham.twitterlistviewer_android.lists;

import android.util.Log;

import com.jeffcunningham.twitterlistviewer_android.events.GetDefaultListSuccessEvent;
import com.jeffcunningham.twitterlistviewer_android.events.GetListOwnershipByTwitterUserSuccessEvent;
import com.jeffcunningham.twitterlistviewer_android.restapi.APIManager;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.get.DefaultList;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.post.Data;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.post.PostDefaultList;
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


    @Override
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

    public void persistDefaultListId(String alias, String listId, String slug, String listName){


        PostDefaultList defaultListBody = new PostDefaultList();
        Data defaultListBodyData = new Data();

        defaultListBodyData.setAlias(alias);
        defaultListBodyData.setListId(listId);
        defaultListBodyData.setSlug(slug);
        defaultListBodyData.setListName(listName);
        defaultListBody.setData(defaultListBodyData);
        Log.i(TAG, "persistDefaultListId: listId = " + listId + " slug = " + slug);
        Call<DefaultList> postDefaultListCall = apiManager.apiTransactions.postDefaultList(defaultListBody);

        postDefaultListCall.enqueue(new retrofit2.Callback<DefaultList>() {
            @Override
            public void onResponse(Call<DefaultList> call, Response<DefaultList> response) {
                Log.i(TAG, "onResponse: Retrofit call to Node post default list succeeded, default list id = " + response.body().getListId());
                Log.i(TAG, "onResponse: Retrofit call to Node post default list succeeded, default list slug = " + response.body().getSlug());
                Log.i(TAG, "onResponse: Retrofit call to Node post default list succeeded, default list alias = " + response.body().getAlias());

                persistDefaultListDataToSharedPreferences(response.body().getSlug(),response.body().getListName());
//                setDefaultListIdForAdapterLists(response.body());
                EventBus.getDefault().post(new GetDefaultListSuccessEvent(response.body()));
            }

            @Override
            public void onFailure(Call<DefaultList> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);

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
        sharedPreferencesRepository.persistDefaultListData(slug,listName);
    }

}
