package com.jeffcunningham.lv4t_android.list;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.jeffcunningham.lv4t_android.BaseApplication;
import com.jeffcunningham.lv4t_android.R;
import com.jeffcunningham.lv4t_android.di.DaggerListComponent;
import com.jeffcunningham.lv4t_android.di.ListComponent;
import com.jeffcunningham.lv4t_android.di.ListModule;
import com.jeffcunningham.lv4t_android.lists.ListsActivity;
import com.jeffcunningham.lv4t_android.util.Logger;

import javax.inject.Inject;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

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

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        String selectedConfiguration = getString(R.string.selected_configuration);
        logger.info(TAG,"onConfigurationChanged, selected_configuration = " + selectedConfiguration);
        if (selectedConfiguration.equalsIgnoreCase("layout-land")){

            Intent listsIntent = new Intent(this, ListsActivity.class);
            listsIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(listsIntent);
        }
    }
}
