package com.jeffcunningham.twitterlistviewer_android.lists;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeffcunningham.twitterlistviewer_android.R;
import com.jeffcunningham.twitterlistviewer_android.events.GetDefaultListSuccessEvent;
import com.jeffcunningham.twitterlistviewer_android.events.GetListOwnershipByTwitterUserFailureEvent;
import com.jeffcunningham.twitterlistviewer_android.events.GetListOwnershipByTwitterUserSuccessEvent;
import com.jeffcunningham.twitterlistviewer_android.events.SetDefaultListEvent;
import com.jeffcunningham.twitterlistviewer_android.events.ViewListEvent;
import com.jeffcunningham.twitterlistviewer_android.list.TwitterListActivity;
import com.jeffcunningham.twitterlistviewer_android.lists.ui.ListsAdapter;
import com.jeffcunningham.twitterlistviewer_android.restapi.dto.get.DefaultList;
import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.dto.TwitterList;
import com.jeffcunningham.twitterlistviewer_android.util.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeffcunningham on 1/18/17.
 */

public class ListsFragment extends Fragment {

    private static final String TAG = "ListsFragment";

    @BindView(R.id.listsRecyclerView)
    RecyclerView listsRecyclerView;
    @BindView(R.id.error)
    TextView tvError;


    ListsAdapter listsAdapter;
    private RecyclerView.LayoutManager listsLayoutManager;

    @Inject
    ListsPresenterImpl listsPresenter;
    @Inject
    Logger logger;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        ((ListsActivity) getActivity()).component().inject(this);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvError.setVisibility(View.GONE);
        listsRecyclerView.setHasFixedSize(true);
        listsLayoutManager = new LinearLayoutManager(getActivity());
        listsRecyclerView.setLayoutManager(listsLayoutManager);
        listsAdapter = new ListsAdapter();
        listsRecyclerView.setAdapter(listsAdapter);
        
        EventBus.getDefault().register(this);
        listsPresenter.getListMembershipByTwitterUser();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetDefaultListSuccessEvent event){
        setDefaultListIdForAdapterLists(event.getDefaultList());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetListOwnershipByTwitterUserSuccessEvent event){
        listsAdapter.setTwitterLists(event.getTwitterLists());
        listsPresenter.getDefaultListId();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetListOwnershipByTwitterUserFailureEvent event){
        tvError.setText(getContext().getString(R.string.retrieveListsError));
        tvError.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetDefaultListEvent event) {


        logger.info(TAG, "onClick: ITEM position PRESSED = " + String.valueOf(event.getPosition()));
        logger.info(TAG, "onClick: List Name = " + event.getSlug());
        logger.info(TAG, "onClick: List ID = " + event.getListId());


        listsPresenter.persistDefaultListId(event.getListId(),event.getSlug(),event.getListName());

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ViewListEvent event){
        Intent listIntent = new Intent(getActivity(), TwitterListActivity.class);
        listIntent.putExtra("slug",event.getSlug());
        listIntent.putExtra("listName",event.getListName());
        startActivity(listIntent);
    }



    private void setDefaultListIdForAdapterLists(DefaultList defaultList){
        for (TwitterList twitterList: listsAdapter.getTwitterLists()){
            logger.info(TAG, "setDefaultListIdForAdapterLists: default list id =   " + defaultList.getListId() +  ", this list id = " + twitterList.getIdStr());
            if (defaultList.getListId().equalsIgnoreCase(twitterList.getIdStr())){
                twitterList.setDefaultList(true);
                logger.info(TAG, "setting this twitterList to default = true ");
            } else {
                twitterList.setDefaultList(false);
            }
            listsAdapter.notifyDataSetChanged();
        };
    }
}
