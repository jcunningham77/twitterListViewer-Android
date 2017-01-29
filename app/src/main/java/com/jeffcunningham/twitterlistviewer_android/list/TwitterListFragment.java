package com.jeffcunningham.twitterlistviewer_android.list;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeffcunningham.twitterlistviewer_android.R;
import com.jeffcunningham.twitterlistviewer_android.lists.ListsActivity;
import com.jeffcunningham.twitterlistviewer_android.util.Logger;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TwitterListTimeline;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeffcunningham on 1/18/17.
 */

public class TwitterListFragment extends ListFragment {

    String slug;
    String alias;
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

    private String avatarImgUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_twitter_list, container, false);

        //for tablet, this Fragment will belong to ListsActivity
        if (getActivity().getClass().equals(TwitterListActivity.class)){
            ((TwitterListActivity) getActivity()).component().inject(this);
            //we only display the ImgUrl when this fragment is viewed on phone (i.e. launched by TwitterListActivity
            this.avatarImgUrl = twitterListPresenter.getTwitterAvatarImgUrl();

        } else {
            ((ListsActivity) getActivity()).component().inject(this);
        }

        //in tablet view, the arguments will not have been set by TwitterListActivity, so null check this
        if (getArguments()!=null){
            this.slug = getArguments().getString("slug","");
            this.listName = getArguments().getString("listName","");

        }



        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        EventBus.getDefault().register(this);

        String selectedConfiguration = getString(R.string.selected_configuration);


        this.alias = twitterListPresenter.getTwitterUserName();

        //slug and listname are present and were likely passed from TwitterListActivity (phone width)
        if (this.slug!=null&&this.listName!=null) {

            logger.info(TAG, "onViewCreated: this.slug = " + this.slug + ", this.listName = " + this.listName);
            logger.info(TAG, "onViewCreated: selected configuration = " + selectedConfiguration);
            tvListName.setText("@"+this.alias+"/"+ this.listName);
            if (this.avatarImgUrl!=null && !this.avatarImgUrl.equalsIgnoreCase("")){
                Picasso.with(getContext()).load(this.avatarImgUrl).into(imgTwitterAvatar);
            } else {
                logger.info(TAG, "onViewCreated: this.avatarImgUrl is null or empty");
            }

            final TwitterListTimeline userTimeline = new TwitterListTimeline.Builder()
                    .slugWithOwnerScreenName(slug,alias)
                    .build();
            final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                    .setTimeline(userTimeline)
                    .build();
            setListAdapter(adapter);

        } else {
            //slug and listname are not present from TwitterListActivity (table width)
            //set placeholder until we get default list from ListsPresenter, or the user selects a list to view
            logger.info(TAG, "onViewCreated: - no slug or listName initialized yet");
            logger.info(TAG, "onViewCreated: selected configuration = " + selectedConfiguration);
            tvListName.setVisibility(View.GONE);
            imgTwitterAvatar.setVisibility(View.GONE);

        }



    }

    //probably don't want this - since we aren't going to want to display avatar and handle in tablet mode,
    //since you can already see that on the Lists Fragment
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(GetListOwnershipByTwitterUserSuccessEvent event){
//
//        if (event.getTwitterLists().get(0)!=null){
//            Picasso.with(getContext()).load(event.getTwitterLists().get(0).getUser().getProfileImageUrlHttps()).into(imgTwitterAvatar);
//        }
//    }
}
