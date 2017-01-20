package com.jeffcunningham.twitterlistviewer_android.util;

/**
 * Created by jeffcunningham on 1/19/17.
 */

public interface SharedPreferencesRepository {

    public void persistDefaultListData(String slug, String listName);
}
