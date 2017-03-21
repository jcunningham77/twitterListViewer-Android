package com.jeffcunningham.lv4t_android.lists;

import com.crashlytics.android.Crashlytics;
import com.jeffcunningham.lv4t_android.events.GetDefaultListSuccessEvent;
import com.jeffcunningham.lv4t_android.events.GetListOwnershipByTwitterUserFailureEvent;
import com.jeffcunningham.lv4t_android.events.GetListOwnershipByTwitterUserSuccessEvent;
import com.jeffcunningham.lv4t_android.events.GetUserLookupSuccessEvent;
import com.jeffcunningham.lv4t_android.events.NoDefaultListPersistedEvent;
import com.jeffcunningham.lv4t_android.events.NoListOwnershipByTwitterUserEvent;
import com.jeffcunningham.lv4t_android.restapi.APIManager;
import com.jeffcunningham.lv4t_android.restapi.dto.get.DefaultList;
import com.jeffcunningham.lv4t_android.restapi.dto.post.Data;
import com.jeffcunningham.lv4t_android.restapi.dto.post.PostDefaultList;
import com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions.TwitterAPIService;
import com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions.TwitterApiClientExtension;
import com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions.dto.list.TwitterList;
import com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions.dto.user.User;
import com.jeffcunningham.lv4t_android.util.ListsCache;
import com.jeffcunningham.lv4t_android.util.ListsStorage;
import com.jeffcunningham.lv4t_android.util.Logger;
import com.jeffcunningham.lv4t_android.util.SharedPreferencesRepository;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.util.List;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
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
    private ListsStorage listsStorage;
    private OkHttpClient client;

    //these dependencies are not injected by Dagger
    private APIManager apiManager;
    private TwitterSession twitterSession;
    Call<List<TwitterList>> listMembership;
    Call<List<User>> userLookup;
    Call<DefaultList> postDefaultListCall;
    Call<DefaultList> getDefaultListCall;



    @Inject
    public ListsPresenterImpl(SharedPreferencesRepository sharedPreferencesRepository, Logger logger, ListsStorage listsStorage, OkHttpClient client) {

        this.sharedPreferencesRepository = sharedPreferencesRepository;
        this.logger = logger;
        this.listsStorage = listsStorage;
        this.client = client;
    }

    @Override
    public void start(){

        if(Twitter.getSessionManager().getActiveSession()!=null) {
            logger.info(TAG,"start: twitter session is not null");
            getListOwnershipByTwitterUser();
        } else {
            logger.info(TAG,"start: twitter session is null");
        }

    }

    @Override
    public void stop(){
        if(listMembership!=null){
            logger.info(TAG,"stop: cancelling listMembership call");
            listMembership.cancel();
        }
        if (postDefaultListCall!=null){
            logger.info(TAG,"stop: cancelling postDefaultListCall call");
            postDefaultListCall.cancel();
        }
        if (getDefaultListCall!=null){
            logger.info(TAG,"stop: cancelling getDefaultListCall call");
            getDefaultListCall.cancel();
        }
        if (userLookup!=null){
            logger.info(TAG,"stop: cancelling getDefaultListCall call");
            userLookup.cancel();
        }


    }

    @Override
    public void getListOwnershipByTwitterUser(){

        // get current Twitter user's list membership
        this.twitterSession = Twitter.getSessionManager().getActiveSession();

        TwitterApiClientExtension twitterApiClientExtension = new TwitterApiClientExtension(Twitter.getSessionManager().getActiveSession(), client);
        final TwitterAPIService twitterAPIService = twitterApiClientExtension.getTwitterAPIService();
        logger.info(TAG,"getListOwnershipByTwitterUser: for alias = " + this.twitterSession.getUserName());

        //todo extract the below to a use case
        //todo cache at retrofit layer
        if ((listsStorage.retrieveListsCache()!=null)&&(Minutes.minutesBetween(listsStorage.retrieveListsCache().getDepositTimestamp(),new DateTime()).getMinutes()<15)){
            logger.info(TAG,"getListOwnershipByTwitterUser, twitter list cache is younger than 15 minutes, use it");
            EventBus.getDefault().post(new GetListOwnershipByTwitterUserSuccessEvent(listsStorage.retrieveListsCache().getTwitterLists()));

        } else {

            listMembership= twitterAPIService.listOwnershipByScreenName(twitterSession.getUserName());
            listMembership.enqueue(new Callback<List<TwitterList>>(){

                @Override
                public void success(Result<List<TwitterList>> result) {

                    //check rate limit
                    String requestsRemaining;
                    requestsRemaining = result.response.headers().get("x-rate-limit-remaining");
                    logger.info(TAG,"getListOwnershipByTwitterUser, success = requestsRemaining = " + requestsRemaining);

                    for (TwitterList twitterList : result.data){
                        logger.info(TAG, "success: twitterList = " + twitterList.getFullName());
                    }

                    if (result.data.size()>0){
                        String twitterAvatarImgUrl = ((List<TwitterList>)result.data).get(0).getUser().getProfileImageUrlHttps();
                        sharedPreferencesRepository.persistTwitterAvatarImgUrl(twitterAvatarImgUrl);

                        listsStorage.clearListsCache();
                        ListsCache listsCache = new ListsCache();
                        listsCache.setTwitterLists(result.data);
                        listsCache.setDepositTimestamp(new DateTime());
                        //in case there was old list data in there
                        listsStorage.persistListsCache(listsCache);
                        EventBus.getDefault().post(new GetListOwnershipByTwitterUserSuccessEvent(result.data));
                    } else {//no list data returned from Twitter
                        //try to at least get the user data to display their avatar and handle
                        logger.info(TAG, "listMembership success, but no list data, execute user lookup: ");
                        userLookup = twitterAPIService.userLookupByScreenName(twitterSession.getUserName());
                        userLookup.enqueue(new Callback<List<User>>() {
                            @Override
                            public void success(Result<List<User>> result) {
                                logger.info(TAG, "userLookup success");
                                String twitterAvatarImgUrl = ((List<User>)result.data).get(0).getProfileImageUrlHttps();
                                sharedPreferencesRepository.persistTwitterAvatarImgUrl(twitterAvatarImgUrl);

                                listsStorage.clearListsCache();
                                EventBus.getDefault().post(new GetUserLookupSuccessEvent(twitterAvatarImgUrl,twitterSession.getUserName()));

                            }

                            @Override
                            public void failure(TwitterException exception) {
                                logger.info(TAG, "userLookup failure");
                                logger.error(TAG, "failure: " + exception.getMessage(), exception);



                            }
                        });

                        EventBus.getDefault().post(new NoListOwnershipByTwitterUserEvent());
                    }

                }

                @Override
                public void failure(TwitterException exception) {

                    logger.error(TAG, "failure: " + exception.getMessage());
                    logger.getStackTraceString(exception);
                    EventBus.getDefault().post(new GetListOwnershipByTwitterUserFailureEvent());

                }
            });
        }
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
        postDefaultListCall = apiManager.apiTransactions.postDefaultList(defaultListBody);

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
        getDefaultListCall = apiManager.apiTransactions.getDefaultList(userName);

        getDefaultListCall.enqueue(new retrofit2.Callback<DefaultList>() {
            @Override
            public void onResponse(Call<DefaultList> call, Response<DefaultList> response) {

                logger.info(TAG, "onResponse: Retrofit call to Node get default list API succeeded, default list id = " + response.body());

                if (response.body()!=null){
                    persistDefaultListDataToSharedPreferences(response.body().getSlug(),response.body().getListName());
                    EventBus.getDefault().post(new GetDefaultListSuccessEvent(response.body()));
                } else {
                    logger.info(TAG,"onResponse: Retrofit call to Node get default list API succeeded, but there is no default list data.");
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
