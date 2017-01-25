package com.jeffcunningham.twitterlistviewer_android.list;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.jeffcunningham.twitterlistviewer_android.BaseApplication;
import com.jeffcunningham.twitterlistviewer_android.R;
import com.jeffcunningham.twitterlistviewer_android.di.DaggerListComponent;
import com.jeffcunningham.twitterlistviewer_android.di.ListComponent;
import com.jeffcunningham.twitterlistviewer_android.di.ListModule;
import com.jeffcunningham.twitterlistviewer_android.util.Logger;

import javax.inject.Inject;

/**
 * Created by jeffcunningham on 1/7/17.
 */

public class TwitterListActivity extends android.app.Activity {

    private static final String TAG = "TwitterListActivity";
    private ListComponent component;

    @Inject
    Logger logger;

    ListComponent component() {
        if (component == null) {
            component = DaggerListComponent.builder()
                    .applicationComponent(((BaseApplication) getApplication()).getApplicationComponent())
                    .listModule(new ListModule())
                    .build();
        }
        return component;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        component().inject(this);

        setContentView(R.layout.activity_twitter_list);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        TwitterListFragment twitterListFragment = new TwitterListFragment();
        Bundle bundle = new Bundle();
        logger.info(TAG, "onCreate: getIntent().getStringExtra(\"slug\") = " + getIntent().getStringExtra("slug"));
        bundle.putString("slug",getIntent().getStringExtra("slug"));
        bundle.putString("listName",getIntent().getStringExtra("listName"));
        twitterListFragment.setArguments(bundle);
        ft.add(R.id.fragment_container, twitterListFragment);
        ft.commit();
    }
}
