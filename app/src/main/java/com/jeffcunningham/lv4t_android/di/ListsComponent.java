package com.jeffcunningham.lv4t_android.di;

import com.jeffcunningham.lv4t_android.list.TwitterListFragment;
import com.jeffcunningham.lv4t_android.lists.ListsActivity;

import dagger.Component;

/**
 * Created by jeffcunningham on 1/22/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ListsModule.class)
public interface ListsComponent {

    void inject(ListsActivity activity);

//    void inject(ListsFragment fragment);

    //in case we are in Tablet view, the TwitterListFragment will need to resolve it's dependencies
    //from the Lists component as it will belong to the Lists activity
    void inject(TwitterListFragment fragment);



}