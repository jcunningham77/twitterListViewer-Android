package com.jeffcunningham.twitterlistviewer_android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jeffcunningham.twitterlistviewer_android.events.SetDefaultListEvent;
import com.jeffcunningham.twitterlistviewer_android.events.ViewListEvent;
import com.jeffcunningham.twitterlistviewer_android.restapi.APIManager;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.DefaultList;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.post.Data;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.post.PostDefaultList;
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

        //check if there is a default list set, and show that list's tweets via the TwitterListActivity
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String persistedDefaultSlug = preferences.getString("slug","");
        if ((null!=persistedDefaultSlug)&&(!persistedDefaultSlug.equalsIgnoreCase(""))){
            Log.i(TAG, "onCreate: we have a default list Id, \"" + persistedDefaultSlug + "\" stored - forward to that TwitterListActivity.");
            Intent listIntent = new Intent(ListsActivity.this, TwitterListActivity.class);
            listIntent.putExtra("slug",persistedDefaultSlug);
            startActivity(listIntent);
            
        }


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
        Log.i(TAG, "onClick: ITEM position PRESSED = " + String.valueOf(event.getPosition()));
        Log.i(TAG, "onClick: List Name = " + event.getSlug());
        Log.i(TAG, "onClick: List ID = " + event.getListId());
        Log.i(TAG, "onClick: User alias = " + twitterSession.getUserName() );
        persistDefaultListId(twitterSession.getUserName(),event.getListId(),event.getSlug());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ViewListEvent event){
        Intent listIntent = new Intent(ListsActivity.this, TwitterListActivity.class);
        listIntent.putExtra("slug",event.getSlug());
        startActivity(listIntent);
    }


    private void persistDefaultListId(String alias, String listId, String slug){
        PostDefaultList defaultListBody = new PostDefaultList();
        Data defaultListBodyData = new Data();

        defaultListBodyData.setAlias(alias);
        defaultListBodyData.setListId(listId);
        defaultListBodyData.setSlug(slug);
        defaultListBody.setData(defaultListBodyData);
        Log.i(TAG, "persistDefaultListId: listId = " + listId + " slug = " + slug);
        Call<DefaultList> postDefaultListCall = apiManager.apiTransactions.postDefaultList(defaultListBody);

        postDefaultListCall.enqueue(new retrofit2.Callback<DefaultList>() {
            @Override
            public void onResponse(Call<DefaultList> call, Response<DefaultList> response) {
                Log.i(TAG, "onResponse: Retrofit call to Node post default list succeeded, default list id = " + response.body().getListId());
                Log.i(TAG, "onResponse: Retrofit call to Node post default list succeeded, default list slug = " + response.body().getSlug());
                Log.i(TAG, "onResponse: Retrofit call to Node post default list succeeded, default list alias = " + response.body().getAlias());

                persistDefaultListSlugToSharedPreferences(response.body().getSlug());
                setDefaultListIdForAdapterLists(response.body());
            }

            @Override
            public void onFailure(Call<DefaultList> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);

            }
        });

    }

    private void persistDefaultListSlugToSharedPreferences(String slug){
        Log.i(TAG, "persistDefaultListSlugToSharedPreferences: slug = " + slug);
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("slug",slug);
        editor.commit();

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
            if (defaultList.getListId().equalsIgnoreCase(twitterList.getIdStr())){
                twitterList.setDefaultList(true);
                Log.i(TAG, "setting this twitterList to default = true ");
            } else {
                twitterList.setDefaultList(false);
            }
            listsAdapter.notifyDataSetChanged();
        };
    }
}
