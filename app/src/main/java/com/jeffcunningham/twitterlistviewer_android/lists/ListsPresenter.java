package com.jeffcunningham.twitterlistviewer_android.lists;

/**
 * Created by jeffcunningham on 1/22/17.
 */
public interface ListsPresenter {

    public void getListMembershipByTwitterUser();

    public void persistDefaultListId(String alias, String listId, String slug, String listName);


}
