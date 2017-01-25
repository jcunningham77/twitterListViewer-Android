package com.jeffcunningham.twitterlistviewer_android.di;

import com.jeffcunningham.twitterlistviewer_android.lists.ListsActivity;
import com.jeffcunningham.twitterlistviewer_android.lists.ui.ListsFragment;

import dagger.Component;

/**
 * Created by jeffcunningham on 1/22/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ListsModule.class)
public interface ListsComponent {

    void inject(ListsActivity activity);

    void inject(ListsFragment fragment);



}
