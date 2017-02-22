package com.jeffcunningham.lv4t_android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jeffcunningham.lv4t_android.di.annotations.ApplicationContext;

import javax.inject.Inject;

/**
 * Created by jeffcunningham on 1/19/17.
 */

public class SharedPreferencesRepositoryImpl implements SharedPreferencesRepository {

    private SharedPreferences prefs;
    private Logger logger;

    private static final String TAG = "SharedPrefRepImpl";

    @Inject
    public SharedPreferencesRepositoryImpl(@ApplicationContext Context context, Logger logger) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.logger = logger;
    }

    @Override
    public void persistDefaultListData(String slug, String listName) {

        logger.info(TAG, "persistDefaultListData: for slug = " + slug + ", listName = " + listName);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("slug",slug);
        editor.putString("listName",listName);
        editor.commit();

    }

    @Override
    public void clearDefaultListData() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("slug");
        editor.remove("listName");
        editor.commit();
    }

    @Override
    public String getDefaultListSlug() {
        logger.info(TAG, ": getDefaultListSlug(): ");
        return prefs.getString("slug", "");
    }

    @Override
    public String getDefaultListName() {
        logger.info(TAG, ": getDefaultListName(): ");
        return prefs.getString("listName", "");
    }

    @Override
    public void persistTwitterAvatarImgUrl(String twitterAvatarImgUrl) {
        Log.i(TAG, "persistTwitterAvatarImgUrl: " + twitterAvatarImgUrl);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("twitterAvatarImgUrl",twitterAvatarImgUrl);
        editor.commit();
    }

    @Override
    public String getTwitterAvatarImgUrl() {
        return prefs.getString("twitterAvatarImgUrl", "");
    }

    @Override
    public void clearTwitterAvatarImgUrl() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("twitterAvatarImgUrl");
        editor.commit();

    }


}
