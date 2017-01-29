package com.jeffcunningham.twitterlistviewer_android.util;

/**
 * Created by jeffcunningham on 1/19/17.
 */

public interface SharedPreferencesRepository {

    public void persistDefaultListData(String slug, String listName);

    public void clearDefaultListData();

    public String getDefaultListSlug();

    public String getDefaultListName();

    public void persistTwitterAvatarImgUrl(String twitterAvatarImgUrl);

    public String getTwitterAvatarImgUrl();

    public void clearTwitterAvatarImgUrl();
}
