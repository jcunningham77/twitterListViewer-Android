package com.jeffcunningham.lv4t_android.di;

import com.jeffcunningham.lv4t_android.list.TwitterListPresenter;
import com.jeffcunningham.lv4t_android.list.TwitterListPresenterImpl;
import com.jeffcunningham.lv4t_android.lists.ListsPresenter;
import com.jeffcunningham.lv4t_android.lists.ListsPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jeffcunningham on 1/22/17.
 */
@Module
public class ListsModule {

    @Provides
    ListsPresenter provideListsPresenter(ListsPresenterImpl impl){
        return impl;
    }


    //in case we are in Tablet view, the TwitterListFragment will need to resolve it's dependencies
    //from the Lists component as it will belong to the Lists activity
    @Provides
    TwitterListPresenter provideTwitterListPresenter(TwitterListPresenterImpl impl){
        return impl;
    }


}