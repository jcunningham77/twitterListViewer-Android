package com.jeffcunningham.twitterlistviewer_android.di;

import com.jeffcunningham.twitterlistviewer_android.lists.ListsPresenter;
import com.jeffcunningham.twitterlistviewer_android.lists.ListsPresenterImpl;

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


}
