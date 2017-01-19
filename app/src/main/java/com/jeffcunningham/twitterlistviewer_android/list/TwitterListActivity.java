package com.jeffcunningham.twitterlistviewer_android.list;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jeffcunningham.twitterlistviewer_android.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TwitterListTimeline;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeffcunningham on 1/7/17.
 */

public class TwitterListActivity extends android.app.ListActivity {

    String slug;
    String alias;

    TwitterSession twitterSession;

    private static final String TAG = "TwitterListActivity";

    @BindView(R.id.tvListName)
    TextView tvListName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_list);
        ButterKnife.bind(this);
        this.slug = getIntent().getStringExtra("slug");


        this.twitterSession = Twitter.getSessionManager().getActiveSession();
        this.alias = twitterSession.getUserName();

        tvListName.setText("@"+this.alias+"/"+ getIntent().getStringExtra("listName"));

        Log.i(TAG, "onCreate: slug = " + slug + ", alias = " + alias);

        final TwitterListTimeline userTimeline = new TwitterListTimeline.Builder()
                .slugWithOwnerScreenName(slug,alias)
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();


        setListAdapter(adapter);

    }


}
