package com.jeffcunningham.twitterlistviewer_android.lists;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.jeffcunningham.twitterlistviewer_android.BaseApplication;
import com.jeffcunningham.twitterlistviewer_android.R;
import com.jeffcunningham.twitterlistviewer_android.di.DaggerListsComponent;
import com.jeffcunningham.twitterlistviewer_android.di.ListsComponent;
import com.jeffcunningham.twitterlistviewer_android.di.ListsModule;
import com.jeffcunningham.twitterlistviewer_android.list.TwitterListActivity;
import com.jeffcunningham.twitterlistviewer_android.list.TwitterListFragment;
import com.jeffcunningham.twitterlistviewer_android.util.Logger;
import com.jeffcunningham.twitterlistviewer_android.util.SharedPreferencesRepository;

import javax.inject.Inject;

/**
 * Created by jeffcunningham on 12/10/16.
 */

public class ListsActivity extends Activity {

    private static final String TAG = "ListsActivity";

    private ListsComponent component;

    @Inject
    SharedPreferencesRepository sharedPreferencesRepository;
    @Inject
    Logger logger;

    public ListsComponent component() {
        if (component == null) {
            component = DaggerListsComponent.builder()
                    .applicationComponent(((BaseApplication) getApplication()).getApplicationComponent())
                    .listsModule(new ListsModule())
                    .build();
        }
        return component;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        component().inject(this);

        String persistedDefaultSlug = sharedPreferencesRepository.getDefaultListSlug();
        String persistedDefaultListName = sharedPreferencesRepository.getDefaultListName();

        String selectedConfiguration = getString(R.string.selected_configuration);
        logger.info(TAG,"onCreate - layout configuration = " + selectedConfiguration);

        setContentView(R.layout.activity_lists);



        //we only forward to TwitterListActivity when running on Phone-sized device
        if (selectedConfiguration.equalsIgnoreCase("layout")) {

            if ((null != persistedDefaultSlug) && (!persistedDefaultSlug.equalsIgnoreCase(""))) {
                logger.info(TAG, "onCreate: we have a default list Id, \"" + persistedDefaultSlug + "\" stored - forward to the TwitterListActivity.");
                Intent listIntent = new Intent(ListsActivity.this, TwitterListActivity.class);
                listIntent.putExtra("slug", persistedDefaultSlug);
                listIntent.putExtra("listName", persistedDefaultListName);
                startActivity(listIntent);

            }

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ListsFragment listsFragment = (ListsFragment) fm.findFragmentByTag("ListsFragment");

            if(listsFragment==null){
                listsFragment = new ListsFragment();
            }
            listsFragment.setRetainInstance(false);

            if(!listsFragment.isAdded()){
                ft.add(R.id.lists_fragment_container, listsFragment, "ListsFragment");
                ft.commit();
            }

        } else {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ListsFragment listsFragment = (ListsFragment) fm.findFragmentByTag("ListsFragment");

            if(listsFragment==null){
                listsFragment = new ListsFragment();
            }
            listsFragment.setRetainInstance(false);

            if(!listsFragment.isAdded()) {
                ft.add(R.id.lists_fragment_container, listsFragment, "ListsFragment");
            }

            TwitterListFragment twitterListFragment = new TwitterListFragment();
            ft.add(R.id.twitter_list_fragment_container,twitterListFragment);

            ft.commit();
            
        }





    }
}
