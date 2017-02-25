package com.jeffcunningham.lv4t_android.lists;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.jeffcunningham.lv4t_android.BaseApplication;
import com.jeffcunningham.lv4t_android.R;
import com.jeffcunningham.lv4t_android.di.DaggerListsComponent;
import com.jeffcunningham.lv4t_android.di.ListsComponent;
import com.jeffcunningham.lv4t_android.di.ListsModule;
import com.jeffcunningham.lv4t_android.list.TwitterListActivity;
import com.jeffcunningham.lv4t_android.list.TwitterListFragment;
import com.jeffcunningham.lv4t_android.util.Logger;
import com.jeffcunningham.lv4t_android.util.SharedPreferencesRepository;

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

            //double check that the fragment doesn't already exist
            //this activity may have been launched from the TwitterListActivity's
            //onConfigurationStateChange method - which means, ListActivity.onCreate, the ListFragment may already exist
            //try to find it by Tag, and only add if not already added.
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ListsFragment listsFragment = (ListsFragment) fm.findFragmentByTag("ListsFragment");
            if(listsFragment==null){
                listsFragment = new ListsFragment();
            }
            listsFragment.setRetainInstance(false);

            if(!listsFragment.isAdded()){
//                ft.add(R.id.lists_fragment_container, listsFragment, "ListsFragment");
                ft.replace(R.id.lists_fragment_container, listsFragment, "ListsFragment");
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
//                ft.add(R.id.lists_fragment_container, listsFragment, "ListsFragment");
                ft.replace(R.id.lists_fragment_container, listsFragment, "ListsFragment");
            }

            TwitterListFragment twitterListFragment = new TwitterListFragment();
//            ft.add(R.id.twitter_list_fragment_container,twitterListFragment);
            ft.replace(R.id.twitter_list_fragment_container,twitterListFragment,"TwitterListFragment");

            ft.commit();

        }





    }
}
