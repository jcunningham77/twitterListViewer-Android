package com.jeffcunningham.twitterlistviewer_android.lists;

/**
 * Created by jeffcunningham on 1/22/17.
 */
public interface ListsPresenter {

    public void getListOwnershipByTwitterUser();

    public void persistDefaultListId(String listId, String slug, String listName);


}
