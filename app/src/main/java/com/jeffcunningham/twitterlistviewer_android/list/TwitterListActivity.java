package com.jeffcunningham.twitterlistviewer_android.list;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.jeffcunningham.twitterlistviewer_android.R;

/**
 * Created by jeffcunningham on 1/7/17.
 */

public class TwitterListActivity extends android.app.Activity {

    private static final String TAG = "TwitterListActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_list);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        TwitterListFragment twitterListFragment = new TwitterListFragment();
        Bundle bundle = new Bundle();
        Log.i(TAG, "onCreate: getIntent().getStringExtra(\"slug\") = " + getIntent().getStringExtra("slug"));
        bundle.putString("slug",getIntent().getStringExtra("slug"));
        bundle.putString("listName",getIntent().getStringExtra("listName"));
        twitterListFragment.setArguments(bundle);
        ft.add(R.id.fragment_container, twitterListFragment);
        ft.commit();
    }
}
