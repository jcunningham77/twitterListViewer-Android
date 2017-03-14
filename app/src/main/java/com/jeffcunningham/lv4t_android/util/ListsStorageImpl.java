package com.jeffcunningham.lv4t_android.util;

import javax.inject.Inject;

/**
 * Created by jeffcunningham on 2/27/17.
 */

public class ListsStorageImpl implements ListsStorage {

    private Logger logger;
    private ListsCache listsCache;

    private static final String TAG = "ListsStorageImpl";

    @Inject
    public ListsStorageImpl(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void persistListsCache(ListsCache listsCache) {
        logger.info(TAG, "persistListsCache: ");
        this.listsCache = listsCache;
    }

    @Override
    public ListsCache retrieveListsCache() {
        logger.info(TAG, "retrieveListsCache: ");
        return this.listsCache;
    }

    @Override
    public void clearListsCache() {
        logger.info(TAG, "clearListsCache: ");
        this.listsCache = null;
    }

}
