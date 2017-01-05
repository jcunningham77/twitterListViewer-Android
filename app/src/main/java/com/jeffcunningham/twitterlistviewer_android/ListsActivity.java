package com.jeffcunningham.twitterlistviewer_android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jeffcunningham.twitterlistviewer_android.restapi.APIManager;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.DefaultList;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.ListOwnershipService;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.TwitterApiClientExtension;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.dto.TwitterList;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by jeffcunningham on 12/10/16.
 */

public class ListsActivity extends Activity {

    private static final String TAG = "ListsActivity";

//    @BindView(R.id.listsRecyclerView)
    RecyclerView listsRecyclerView;

    private ListsAdapter listsAdapter;
    private RecyclerView.LayoutManager listsLayoutManager;

    private APIManager apiManager;

    TwitterSession twitterSession;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        listsRecyclerView = (RecyclerView)findViewById(R.id.listsRecyclerView);

        listsRecyclerView.setHasFixedSize(true);
        listsLayoutManager = new LinearLayoutManager(this);
        listsRecyclerView.setLayoutManager(listsLayoutManager);
        listsAdapter = new ListsAdapter();
        listsRecyclerView.setAdapter(listsAdapter);

        this.twitterSession = Twitter.getSessionManager().getActiveSession();
        TwitterApiClientExtension twitterApiClientExtension = new TwitterApiClientExtension(Twitter.getSessionManager().getActiveSession());
        ListOwnershipService listOwnershipService = twitterApiClientExtension.getListOwnershipService();

        Call<List<TwitterList>> listMembership= listOwnershipService.listOwnershipByScreenName(twitterSession.getUserName());

        EventBus.getDefault().register(this);

        listMembership.enqueue(new Callback<List<TwitterList>>(){


            @Override
            public void success(Result<List<TwitterList>> result) {

                for (TwitterList twitterList : result.data){
                    Log.i(TAG, "success: twitterList = " + twitterList.getFullName());
                }

                listsAdapter.setTwitterUserId(twitterSession.getUserId());
                listsAdapter.setTwitterLists(result.data);
                getDefaultListId(twitterSession.getUserName());

            }

            @Override
            public void failure(TwitterException exception) {

                Log.e(TAG, "failure: " + exception.getMessage());
                Log.getStackTraceString(exception);

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetDefaultListEvent event) {
        Log.i(TAG, "onClick: ITEM position PRESSED = " + String.valueOf(event.position));
        Log.i(TAG, "onClick: List Name = " + event.slug);
        Log.i(TAG, "onClick: List ID = " + event.listId);
        Log.i(TAG, "onClick: User alias = " + twitterSession.getUserName() );
        persistDefaultListId(twitterSession.getUserName(),event.listId,event.slug);

    }

    private void persistDefaultListId(String alias, long listId, String slug){
        DefaultList defaultListBody = new DefaultList();
        defaultListBody.setAlias(alias);
        defaultListBody.setListId(listId);
        defaultListBody.setSlug(slug);
        Log.i(TAG, "persistDefaultListId: listId = " + listId + " slug = " + slug);
        Call<DefaultList> postDefaultListCall = apiManager.apiTransactions.postDefaultList(defaultListBody);

        postDefaultListCall.enqueue(new retrofit2.Callback<DefaultList>() {
            @Override
            public void onResponse(Call<DefaultList> call, Response<DefaultList> response) {
                Log.i(TAG, "onResponse: Retrofit call to Node post default list succeeded, default list id = " + response.body().getListId());
                Log.i(TAG, "onResponse: Retrofit call to Node post default list succeeded, default list slug = " + response.body().getSlug());
                Log.i(TAG, "onResponse: Retrofit call to Node post default list succeeded, default list alias = " + response.body().getAlias());

                setDefaultListIdForAdapterLists(response.body());
            }

            @Override
            public void onFailure(Call<DefaultList> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);

            }
        });

    }


    private void getDefaultListId(String username) {

        Call<DefaultList> getDefaultListCall = apiManager.apiTransactions.getDefaultList(username);


        getDefaultListCall.enqueue(new retrofit2.Callback<DefaultList>() {
            @Override
            public void onResponse(Call<DefaultList> call, Response<DefaultList> response) {
                Log.i(TAG, "onResponse: Retrofit call to Node get default list API succeeded, default list id = " + response.body());

                setDefaultListIdForAdapterLists(response.body());
            }

            @Override
            public void onFailure(Call<DefaultList> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);

            }
        });


    }

    private void setDefaultListIdForAdapterLists(DefaultList defaultList){
        for (TwitterList twitterList: listsAdapter.getTwitterLists()){
            Log.i(TAG, "setDefaultListIdForAdapterLists: default list id =   " + defaultList.getListId() +  ", this list id = " + twitterList.getId());
            if (defaultList.getListId().longValue()==twitterList.getId().longValue()){
                twitterList.setDefaultList(true);
                Log.i(TAG, "setting this twitterList to default = true ");
            } else {
                twitterList.setDefaultList(false);
            }
            listsAdapter.notifyDataSetChanged();
        };
    }
}
