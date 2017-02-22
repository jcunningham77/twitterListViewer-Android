package com.jeffcunningham.twitterlistviewer_android.lists;

import com.crashlytics.android.Crashlytics;
import com.jeffcunningham.twitterlistviewer_android.events.GetDefaultListSuccessEvent;
import com.jeffcunningham.twitterlistviewer_android.events.GetListOwnershipByTwitterUserFailureEvent;
import com.jeffcunningham.twitterlistviewer_android.events.GetListOwnershipByTwitterUserSuccessEvent;
import com.jeffcunningham.twitterlistviewer_android.events.NoDefaultListPersistedEvent;
import com.jeffcunningham.twitterlistviewer_android.restapi.APIManager;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.get.DefaultList;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.post.Data;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.post.PostDefaultList;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.ListOwnershipService;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.TwitterApiClientExtension;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.dto.TwitterList;
import com.jeffcunningham.twitterlistviewer_android.util.Logger;
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

    private static final String TAG = "ListsPresenterImpl";

    //dependencies injected by Dagger via constructor
    private SharedPreferencesRepository sharedPreferencesRepository;
    private Logger logger;

    //these dependencies are not injected by Dagger
    private APIManager apiManager;
    private TwitterSession twitterSession;



    @Inject
    public ListsPresenterImpl(SharedPreferencesRepository sharedPreferencesRepository, Logger logger) {

        this.sharedPreferencesRepository = sharedPreferencesRepository;
        this.logger = logger;
    }

    @Override
    public void getListOwnershipByTwitterUser(){

        // get current Twitter user's list membership
        this.twitterSession = Twitter.getSessionManager().getActiveSession();
        TwitterApiClientExtension twitterApiClientExtension = new TwitterApiClientExtension(Twitter.getSessionManager().getActiveSession());
        ListOwnershipService listOwnershipService = twitterApiClientExtension.getListOwnershipService();
        logger.info(TAG,"getListOwnershipByTwitterUser: for alias = " + this.twitterSession.getUserName());

        Call<List<TwitterList>> listMembership= listOwnershipService.listOwnershipByScreenName(twitterSession.getUserName());

        listMembership.enqueue(new Callback<List<TwitterList>>(){

            @Override
            public void success(Result<List<TwitterList>> result) {

                for (TwitterList twitterList : result.data){
                    logger.info(TAG, "success: twitterList = " + twitterList.getFullName());
                }

                String twitterAvatarImgUrl = ((List<TwitterList>)result.data).get(0).getUser().getProfileImageUrlHttps();
                sharedPreferencesRepository.persistTwitterAvatarImgUrl(twitterAvatarImgUrl);
                EventBus.getDefault().post(new GetListOwnershipByTwitterUserSuccessEvent(result.data));


            }

            @Override
            public void failure(TwitterException exception) {

                logger.error(TAG, "failure: " + exception.getMessage());
                logger.getStackTraceString(exception);
                EventBus.getDefault().post(new GetListOwnershipByTwitterUserFailureEvent());

            }
        });

    }

    public void persistDefaultListId(String listId, String slug, String listName){

        String alias = "";
        this.twitterSession = Twitter.getSessionManager().getActiveSession();
        alias = twitterSession.getUserName();
        Crashlytics.setUserName(alias);

        PostDefaultList defaultListBody = new PostDefaultList();
        Data defaultListBodyData = new Data();

        defaultListBodyData.setAlias(alias);
        defaultListBodyData.setListId(listId);
        defaultListBodyData.setSlug(slug);
        defaultListBodyData.setListName(listName);
        defaultListBody.setData(defaultListBodyData);
        logger.info(TAG, "persistDefaultListId: listId = " + listId + " slug = " + slug);
        Call<DefaultList> postDefaultListCall = apiManager.apiTransactions.postDefaultList(defaultListBody);

        postDefaultListCall.enqueue(new retrofit2.Callback<DefaultList>() {
            @Override
            public void onResponse(Call<DefaultList> call, Response<DefaultList> response) {
                logger.info(TAG, "onResponse: Retrofit call to Node post default list succeeded, default list id = " + response.body().getListId());
                logger.info(TAG, "onResponse: Retrofit call to Node post default list succeeded, default list slug = " + response.body().getSlug());
                logger.info(TAG, "onResponse: Retrofit call to Node post default list succeeded, default list alias = " + response.body().getAlias());

                persistDefaultListDataToSharedPreferences(response.body().getSlug(),response.body().getListName());
                EventBus.getDefault().post(new GetDefaultListSuccessEvent(response.body()));
            }

            @Override
            public void onFailure(Call<DefaultList> call, Throwable t) {
                logger.error(TAG, "onFailure: " + t.getMessage(), t);

            }
        });

    }

    public void getDefaultListId(){


        String userName = twitterSession.getUserName();
        Crashlytics.setUserName(userName);
        logger.info(TAG, "getDefaultListId: for " + userName);
        Call<DefaultList> getDefaultListCall = apiManager.apiTransactions.getDefaultList(userName);

        getDefaultListCall.enqueue(new retrofit2.Callback<DefaultList>() {
            @Override
            public void onResponse(Call<DefaultList> call, Response<DefaultList> response) {

                logger.info(TAG, "onResponse: Retrofit call to Node get default list API succeeded, default list id = " + response.body());


                if (response.body()!=null){
                    persistDefaultListDataToSharedPreferences(response.body().getSlug(),response.body().getListName());
                    EventBus.getDefault().post(new GetDefaultListSuccessEvent(response.body()));
                } else {
                    logger.info(TAG,"onResponse: Retrofit call to Node get default list API succeeded, but there is no default list data.");
                    //todo handle when new user doesn't have a default list
                    EventBus.getDefault().post(new NoDefaultListPersistedEvent());
                }


            }

            @Override
            public void onFailure(Call<DefaultList> call, Throwable t) {
                logger.error(TAG, "onFailure: " + t.getMessage(), t);

            }
        });
    }

    private void persistDefaultListDataToSharedPreferences(String slug, String listName){
        logger.info(TAG, "persistDefaultListDataToSharedPreferences: slug = " + slug + ", listName = " + listName);
        sharedPreferencesRepository.persistDefaultListData(slug,listName);
    }




}
