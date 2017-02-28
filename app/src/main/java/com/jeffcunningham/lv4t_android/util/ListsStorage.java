package com.jeffcunningham.lv4t_android.util;

/**
 * Created by jeffcunningham on 2/27/17.
 */

public interface ListsStorage {

    void persistListsCache(ListsCache listsCache);

    ListsCache retrieveListsCache();

    void clearListsCache();

}
