package com.jeffcunningham.twitterlistviewer_android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;

/**
 * Created by jeffcunningham on 1/19/17.
 */

public class SharedPreferencesRepositoryImpl implements SharedPreferencesRepository {

    private SharedPreferences prefs;

    @Inject
    public SharedPreferencesRepositoryImpl(Context context) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void persistDefaultListData(String slug, String listName) {


        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("slug",slug);
        editor.putString("listName",listName);
        editor.commit();

    }
}
