package com.jeffcunningham.twitterlistviewer_android.lists;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeffcunningham.twitterlistviewer_android.R;
import com.jeffcunningham.twitterlistviewer_android.events.GetListOwnershipByTwitterUserSuccessEvent;
import com.jeffcunningham.twitterlistviewer_android.events.SetDefaultListEvent;
import com.jeffcunningham.twitterlistviewer_android.events.ViewListEvent;
import com.jeffcunningham.twitterlistviewer_android.list.TwitterListActivity;
import com.jeffcunningham.twitterlistviewer_android.restapi.APIManager;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.get.DefaultList;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.post.Data;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.post.PostDefaultList;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.dto.TwitterList;
import com.twitter.sdk.android.core.TwitterSession;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jeffcunningham on 1/18/17.
 */

public class ListsFragment extends Fragment {

    @BindView(R.id.listsRecyclerView)
    RecyclerView listsRecyclerView;
    ListsAdapter listsAdapter;
    private RecyclerView.LayoutManager listsLayoutManager;
    ListsPresenterImpl listsPresenter;

    private APIManager apiManager;

    TwitterSession twitterSession;

    private static final String TAG = "ListsFragment";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listsRecyclerView.setHasFixedSize(true);
        listsLayoutManager = new LinearLayoutManager(getActivity());
        listsRecyclerView.setLayoutManager(listsLayoutManager);
        listsAdapter = new ListsAdapter();
        listsRecyclerView.setAdapter(listsAdapter);
        //--todo inject via Dagger
        listsPresenter = new ListsPresenterImpl();

        EventBus.getDefault().register(this);

        listsPresenter.getListMembershipByTwitterUser();

//        // get current Twitter user's list membership
//        this.twitterSession = Twitter.getSessionManager().getActiveSession();
//        TwitterApiClientExtension twitterApiClientExtension = new TwitterApiClientExtension(Twitter.getSessionManager().getActiveSession());
//        ListOwnershipService listOwnershipService = twitterApiClientExtension.getListOwnershipService();
//
//        Call<List<TwitterList>> listMembership= listOwnershipService.listOwnershipByScreenName(twitterSession.getUserName());
//
//        listMembership.enqueue(new Callback<List<TwitterList>>(){
//
//
//            @Override
//            public void success(Result<List<TwitterList>> result) {
//
//                for (TwitterList twitterList : result.data){
//                    Log.i(TAG, "success: twitterList = " + twitterList.getFullName());
//                }
//
//                listsAdapter.setTwitterUserId(twitterSession.getUserId());
//                listsAdapter.setTwitterLists(result.data);
//                getDefaultListId(twitterSession.getUserName());
//
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//
//                Log.e(TAG, "failure: " + exception.getMessage());
//                Log.getStackTraceString(exception);
//
//            }
//        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetListOwnershipByTwitterUserSuccessEvent event){
        listsAdapter.setTwitterLists(event.getTwitterLists());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetDefaultListEvent event) {
        Log.i(TAG, "onClick: ITEM position PRESSED = " + String.valueOf(event.getPosition()));
        Log.i(TAG, "onClick: List Name = " + event.getSlug());
        Log.i(TAG, "onClick: List ID = " + event.getListId());
        Log.i(TAG, "onClick: User alias = " + twitterSession.getUserName() );
        persistDefaultListId(twitterSession.getUserName(),event.getListId(),event.getSlug(),event.getListName());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ViewListEvent event){
        Intent listIntent = new Intent(getActivity(), TwitterListActivity.class);
        listIntent.putExtra("slug",event.getSlug());
        listIntent.putExtra("listName",event.getListName());
        startActivity(listIntent);
    }


    private void persistDefaultListId(String alias, String listId, String slug, String listName){
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
                setDefaultListIdForAdapterLists(response.body());
            }

            @Override
            public void onFailure(Call<DefaultList> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);

            }
        });

    }

    private void persistDefaultListDataToSharedPreferences(String slug, String listName){
        Log.i(TAG, "persistDefaultListDataToSharedPreferences: slug = " + slug + ", listName = " + listName);
        SharedPreferences settings = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("slug",slug);
        editor.putString("listName",listName);
        editor.commit();

    }


    private void getDefaultListId(String username) {

        Call<DefaultList> getDefaultListCall = apiManager.apiTransactions.getDefaultList(username);


        getDefaultListCall.enqueue(new retrofit2.Callback<DefaultList>() {
            @Override
            public void onResponse(Call<DefaultList> call, Response<DefaultList> response) {
                Log.i(TAG, "onResponse: Retrofit call to Node get default list API succeeded, default list id = " + response.body());

                persistDefaultListDataToSharedPreferences(response.body().getSlug(),response.body().getListName());
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
