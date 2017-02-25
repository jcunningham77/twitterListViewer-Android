package com.jeffcunningham.lv4t_android.lists;

/**
 * Created by jeffcunningham on 1/22/17.
 */
public interface ListsPresenter {

    void getListOwnershipByTwitterUser();

    void persistDefaultListId(String listId, String slug, String listName);


}
