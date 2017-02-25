package com.jeffcunningham.lv4t_android.lists;

import com.jeffcunningham.lv4t_android.BasePresenter;

/**
 * Created by jeffcunningham on 1/22/17.
 */
public interface ListsPresenter extends BasePresenter {

    void getListOwnershipByTwitterUser();

    void persistDefaultListId(String listId, String slug, String listName);


}
