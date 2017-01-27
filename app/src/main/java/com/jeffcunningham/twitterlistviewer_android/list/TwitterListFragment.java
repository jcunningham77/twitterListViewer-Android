package com.jeffcunningham.twitterlistviewer_android.list;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeffcunningham.twitterlistviewer_android.R;
import com.jeffcunningham.twitterlistviewer_android.util.Logger;
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

    private static final String TAG = "TwitterListFragment";

    @Inject
    Logger logger;

    @Inject
    TwitterListPresenter twitterListPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_twitter_list, container, false);
        ((TwitterListActivity) getActivity()).component().inject(this);

        this.slug = getArguments().getString("slug","");
        this.listName = getArguments().getString("listName","");

        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.alias = twitterListPresenter.getTwitterUserName();

        tvListName.setText("@"+this.alias+"/"+ this.listName);


        logger.info(TAG, "onViewCreated: this.slug = " + this.slug + ", this.alias = " + this.alias);

        final TwitterListTimeline userTimeline = new TwitterListTimeline.Builder()
                .slugWithOwnerScreenName(slug,alias)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(userTimeline)
                .build();


        setListAdapter(adapter);
    }
}
