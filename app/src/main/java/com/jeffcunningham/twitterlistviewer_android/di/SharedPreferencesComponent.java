package com.jeffcunningham.twitterlistviewer_android.di;

import com.jeffcunningham.twitterlistviewer_android.lists.ListsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jeffcunningham on 1/19/17.
 */
@Singleton
@Component(modules = {AppModule.class,SharedPreferencesModule.class})
public interface SharedPreferencesComponent {

//    @Subcomponent.Builder
//    interface  Builder extends SubcomponentBuilder<SharedPreferencesComponent>{
//        SharedPreferencesComponent.Builder sharedPreferencesModule(SharedPreferencesModule module);
//    }

    void inject(ListsFragment listsFragment);


}
