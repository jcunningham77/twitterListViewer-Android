package com.jeffcunningham.twitterlistviewer_android.di;

import com.jeffcunningham.twitterlistviewer_android.list.TwitterListPresenter;
import com.jeffcunningham.twitterlistviewer_android.list.TwitterListPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jeffcunningham on 1/23/17.
 */
@Module
public class ListModule {

    @Provides
    TwitterListPresenter provideTwitterListPresenter(TwitterListPresenterImpl impl){
        return impl;
    }

}
