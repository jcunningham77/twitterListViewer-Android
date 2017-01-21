package com.jeffcunningham.twitterlistviewer_android.lists;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.jeffcunningham.twitterlistviewer_android.R;
import com.jeffcunningham.twitterlistviewer_android.list.TwitterListActivity;

/**
 * Created by jeffcunningham on 12/10/16.
 */

public class ListsActivity extends Activity {

    private static final String TAG = "ListsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        SharedPreferencesComponent component = DaggerSharedPreferencesComponent.builder().sharedPreferencesModule(this.getApplicationContext());


//        AppComponent appComponent = AppComponent;

        //check if there is a default list set, and show that list's tweets via the TwitterListActivity
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String persistedDefaultSlug = preferences.getString("slug", "");
        String persistedDefaultListName = preferences.getString("listName", "");
        if ((null != persistedDefaultSlug) && (!persistedDefaultSlug.equalsIgnoreCase(""))) {
            Log.i(TAG, "onCreate: we have a default list Id, \"" + persistedDefaultSlug + "\" stored - forward to that TwitterListActivity.");
            Intent listIntent = new Intent(ListsActivity.this, TwitterListActivity.class);
            listIntent.putExtra("slug", persistedDefaultSlug);
            listIntent.putExtra("listName", persistedDefaultListName);
            startActivity(listIntent);

        }

        setContentView(R.layout.activity_lists);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ListsFragment listsFragment = new ListsFragment();
        ft.add(R.id.fragment_container, listsFragment);
        ft.commit();


    }

}
