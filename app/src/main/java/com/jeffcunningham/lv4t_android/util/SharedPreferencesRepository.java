package com.jeffcunningham.lv4t_android.util;

/**
 * Created by jeffcunningham on 1/19/17.
 */

public interface SharedPreferencesRepository {

    void persistDefaultListData(String slug, String listName);

    void clearDefaultListData();

    String getDefaultListSlug();

    String getDefaultListName();

    void persistTwitterAvatarImgUrl(String twitterAvatarImgUrl);

    String getTwitterAvatarImgUrl();

    void clearTwitterAvatarImgUrl();
}
