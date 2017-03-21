package com.jeffcunningham.lv4t_android.list;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeffcunningham.lv4t_android.MainActivity;
import com.jeffcunningham.lv4t_android.R;
import com.jeffcunningham.lv4t_android.events.GetDefaultListSuccessEvent;
import com.jeffcunningham.lv4t_android.events.GetUserLookupSuccessEvent;
import com.jeffcunningham.lv4t_android.events.ViewListEvent;
import com.jeffcunningham.lv4t_android.util.Constants;
import com.jeffcunningham.lv4t_android.util.ImageLoader;
import com.jeffcunningham.lv4t_android.util.Logger;
import com.jeffcunningham.lv4t_android.util.SharedPreferencesRepository;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TwitterListTimeline;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeffcunningham on 1/18/17.
 */

public class TwitterListFragment extends ListFragment {

    String alias;
    String slug;
    String listName;

    @BindView(R.id.tvListName)
    TextView tvListName;
    @BindView(R.id.twitterAvater)
    ImageView imgTwitterAvatar;

    private static final String TAG = "TwitterListFragment";

    @Inject
    Logger logger;

    @Inject
    TwitterListPresenter twitterListPresenter;

    @Inject
    ImageLoader imageLoader;

    @Inject
    SharedPreferencesRepository sharedPreferencesRepository;

    private String avatarImgUrl;
    private String selectedConfiguration;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_twitter_list, container, false);

        this.selectedConfiguration = getString(R.string.selected_configuration);
        ((MainActivity) getActivity()).component().inject(this);

        this.avatarImgUrl = twitterListPresenter.getTwitterAvatarImgUrl();

        //first check if this fragment was launched via manually selecting list event
        if (getArguments()!=null) {
            this.slug = getArguments().getString("slug");
            this.listName = getArguments().getString("listName");
        }

        //if these values weren't manually passed in, let's check shared preferences
        if (StringUtils.isBlank(this.slug)&&StringUtils.isBlank(this.slug)){
            this.slug = sharedPreferencesRepository.getDefaultListSlug();
            this.listName = sharedPreferencesRepository.getDefaultListName();
        }

        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String selectedConfiguration = getString(R.string.selected_configuration);

        this.alias = twitterListPresenter.getTwitterUserName();

        if (selectedConfiguration.equalsIgnoreCase(Constants.LAYOUT)) {

            logger.info(TAG, "onViewCreated: this.slug = " + this.slug + ", this.listName = " + this.listName);
            logger.info(TAG, "onViewCreated: selected configuration = " + selectedConfiguration);

            if (this.avatarImgUrl!=null && !this.avatarImgUrl.equalsIgnoreCase("")){

                imageLoader.loadImageByUrlWithRoundedCorners(this.avatarImgUrl,imgTwitterAvatar);

            } else {
                logger.info(TAG, "onViewCreated: this.avatarImgUrl is null or empty");
            }

            loadListTimeline(this.slug,this.listName);

        } else {
            //since we are in large or landscape configuration, this fragment may be created at the same time
            //as the lists fragment. set placeholder until we get default list from ListsPresenter, or the user selects a
            // list to view
            logger.info(TAG, "onViewCreated: - no slug or listName initialized yet");
            logger.info(TAG, "onViewCreated: selected configuration = " + selectedConfiguration);
            tvListName.setVisibility(View.GONE);
            imgTwitterAvatar.setVisibility(View.GONE);

        }
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ViewListEvent event) {

        logger.info(TAG, "onMessageEvent: ViewListEvent ");
        //Only execute the below logic if we are in layout-large (tablet) or layout-land (landscape) configurations -
        //in regular configuration, the ListsFragment will launch this fragment
        if (!selectedConfiguration.equalsIgnoreCase(Constants.LAYOUT)){
            loadListTimeline(event.getSlug(),event.getListName());
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetDefaultListSuccessEvent event){
        //Only execute the below logic if we are in layout-large (tablet) or layout-land (landscape) configurations -
        //in regular configuration, the ListsFragment will launch this fragment
        //todo - is this the best way to ensure the fragment is attached to an activity?
        if (isAdded()) {
            if (!selectedConfiguration.equalsIgnoreCase(Constants.LAYOUT)) {
                loadListTimeline(event.getDefaultList().getSlug(), event.getDefaultList().getListName());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetUserLookupSuccessEvent event){
        logger.info(TAG, "onMessageEvent(GetUserLookupSuccessEvent event)");
        tvListName.setText("@"+event.getScreenName());
        tvListName.setVisibility(View.VISIBLE);
    }

    private void loadListTimeline(String slug, String listName){

        logger.info(TAG,"loadListTimeline for slug = " + slug + ", listName = " + listName);

        tvListName.setText("@"+this.alias + "/" + listName);
        tvListName.setVisibility(View.VISIBLE);
        final TwitterListTimeline userTimeline = new TwitterListTimeline.Builder()
                .slugWithOwnerScreenName(slug, this.alias)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(userTimeline)
                .build();
        setListAdapter(adapter);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        setListAdapter(null);
    }

}
