package com.jeffcunningham.twitterlistviewer_android.di;

import com.jeffcunningham.twitterlistviewer_android.lists.ListsPresenterImpl;

import dagger.Component;

/**
 * Created by jeffcunningham on 1/19/17.
 */

@Component(modules = {SharedPreferencesModule.class})
public interface AppComponent {

    void inject(ListsPresenterImpl listsPresenter);
    SharedPreferencesComponent sharedPreferencesComponent(SharedPreferencesModule module);


}
