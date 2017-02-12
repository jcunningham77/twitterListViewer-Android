package com.jeffcunningham.twitterlistviewer_android.lists;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.jeffcunningham.twitterlistviewer_android.util.ImageLoader;
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
    @BindView(R.id.twitterAlias)
    TextView tvTwitterAlias;
    @BindView(R.id.twitterAvater)
    ImageView imgTwitterAvatar;



    ListsAdapter listsAdapter;
    private RecyclerView.LayoutManager listsLayoutManager;
    private String avatarImgUrl;

    @Inject
    ListsPresenterImpl listsPresenter;
    @Inject
    Logger logger;
    @Inject
    ImageLoader imageLoader;

    private String selectedConfiguration;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lists, container, false);
        ((ListsActivity) getActivity()).component().inject(this);
        ButterKnife.bind(this,view);
        this.selectedConfiguration = getString(R.string.selected_configuration);
        logger.info(TAG, "onCreateView: selectedConfiguration =" + selectedConfiguration);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvError.setVisibility(View.GONE);
        //handle header for wide screens
        if (this.selectedConfiguration.equalsIgnoreCase("layout-land")||this.selectedConfiguration.equalsIgnoreCase("layout-large")){
            tvTwitterAlias.setVisibility(View.GONE);
        }


        listsRecyclerView.setHasFixedSize(true);
        listsLayoutManager = new LinearLayoutManager(getActivity());
        listsRecyclerView.setLayoutManager(listsLayoutManager);
        listsAdapter = new ListsAdapter();
        listsRecyclerView.setAdapter(listsAdapter);
        
        EventBus.getDefault().register(this);
        listsPresenter.getListOwnershipByTwitterUser();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetDefaultListSuccessEvent event){
        setDefaultListIdForAdapterLists(event.getDefaultList());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetListOwnershipByTwitterUserSuccessEvent event){
        logger.info(TAG, "onMessageEvent(GetListOwnershipByTwitterUserSuccessEvent event)");
        listsAdapter.setTwitterLists(event.getTwitterLists());
        if (event.getTwitterLists().get(0)!=null){

            this.avatarImgUrl = event.getTwitterLists().get(0).getUser().getProfileImageUrlHttps();
            imageLoader.loadImageByUrlWithRoundedCorners(this.avatarImgUrl,imgTwitterAvatar);
            tvTwitterAlias.setText("@" + event.getTwitterLists().get(0).getUser().getScreenName());

        }
        listsPresenter.getDefaultListId();


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetListOwnershipByTwitterUserFailureEvent event){
        tvError.setText(getActivity().getApplicationContext().getString(R.string.retrieveListsError));
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

        //only launch TwitterListActivity if we are in layout (phone) configuration
        //in tablet config (layout-large), TwitterListFragment is also listening to this event and
        //can refresh itself
        logger.info(TAG,"onMessageEvent - ViewListEvent, this.selectedConfiguration: " + this.selectedConfiguration);
        if (this.selectedConfiguration.equalsIgnoreCase("layout")){
            Intent listIntent = new Intent(getActivity(), TwitterListActivity.class);
            listIntent.putExtra("slug",event.getSlug());
            listIntent.putExtra("listName",event.getListName());
            listIntent.putExtra("twitterAvatarImageUrl",this.avatarImgUrl);
            startActivity(listIntent);
        }


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

    @Override
    public void onDestroy(){
        super.onDestroy();
        logger.info(TAG, "onDestroy: ");
        listsRecyclerView = null;
    }
}
