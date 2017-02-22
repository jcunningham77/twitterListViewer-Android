package com.jeffcunningham.lv4t_android.di;

import com.jeffcunningham.lv4t_android.list.TwitterListPresenter;
import com.jeffcunningham.lv4t_android.list.TwitterListPresenterImpl;

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
